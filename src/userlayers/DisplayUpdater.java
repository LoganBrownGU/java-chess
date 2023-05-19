package userlayers;

import board.Board;
import entities.*;
import gui.GUIRenderer;
import gui.GUITexture;
import main.Coordinate;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import pieces.Pawn;
import pieces.Piece;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;
import toolbox.Maths;
import toolbox.MousePicker;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayUpdater implements Runnable {

    private final int spacing;
    private final Board board;
    private final Camera camera;
    private final Light light;
    private final HashMap<Piece, Entity> entities = new HashMap<>();
    private final HashMap<Coordinate, Entity> squares = new HashMap<>();
    private final HashMap<Coordinate, Entity> highlights = new HashMap<>();
    private TexturedModel highlightModel;
    private final Loader loader;
    private final G3DUserLayer parent;
    private Piece selectedPiece = null;
    private boolean selectingPiece = false;
    private Coordinate selectedSquare = null;
    private boolean selectingSquare = false;
    private MousePicker mousePicker;
    private MasterRenderer renderer;
    private GUIRenderer guiRenderer;
    ArrayList<GUITexture> guis = new ArrayList<>();

    private void updatePieces() {
        int[] directions = new int[board.getPlayers().size()];
        for (Piece piece : board.getPieces()) {
            int idx = board.getPlayers().indexOf(piece.getPlayer());
            if (piece instanceof Pawn && directions[idx] == 0)
                directions[idx] = ((Pawn) piece).direction;
        }

        for (Piece piece : board.getPieces()) {
            int playerIdx = board.getPlayers().indexOf(piece.getPlayer());
            if (entities.get(piece) == null) {
                // load
                try {
                    TexturedModel model = new TexturedModel(OBJLoader.loadObjModel("assets/default_models/" + piece.representation + ".obj", loader),
                            new ModelTexture(loader.loadTexture("assets/default_textures/" + piece.getPlayer().representation + ".png")));

                    Vector3f pos = new Vector3f(piece.getPosition().x * spacing, 0, piece.getPosition().y * spacing);
                    Entity entity = new Entity(model, pos, 0, 90 * directions[playerIdx], 0, 1, new SpherePicker(pos, 1));
                    entities.put(piece, entity);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                // set new position
                entities.get(piece).setPosition(new Vector3f(piece.getPosition().x * spacing, 0, piece.getPosition().y * spacing));
            }
        }
    }

    private void updateHighlights() {
        highlights.clear();

        if (selectedPiece == null) return;

        for (Coordinate move : selectedPiece.possibleMoves()) {
            Entity entity = new Entity(highlightModel, new Vector3f(move.x * spacing, 1, move.y * spacing), 180, 0, 0, 1, null);

            highlights.put(move, entity);
        }
    }

    private void init() {
        DisplayManager.createDisplay("Chess", 1280, 720, true, false);
        renderer = new MasterRenderer("assets/shaders", camera);
        mousePicker = new MousePicker(renderer.getProjectionMatrix(), camera);
        renderer.disableFog();
        camera.setPosition(new Vector3f((float) board.maxX * spacing * .5f, 40, (float) board.maxY * spacing * .5f));

        float aspect = (float) Display.getWidth() / Display.getHeight();
        float guiSize = .03f;
        guis.add(new GUITexture(loader.loadTexture("assets/crosshair.png"), new Vector2f(0, 0), new Vector2f(guiSize, guiSize * aspect)));

        guiRenderer = new GUIRenderer(loader);

        highlightModel = new TexturedModel(OBJLoader.loadObjModel("assets/default_models/pn.obj", loader), new ModelTexture(loader.loadTexture("assets/default_textures/b.png")));
    }

    private void initBoard() {
        int squareSize = 2;
        ModelTexture black = new ModelTexture(loader.loadTexture("assets/default_textures/b.png"));
        ModelTexture white = new ModelTexture(loader.loadTexture("assets/default_textures/w.png"));
        RawModel model = OBJLoader.loadObjModel("assets/default_models/board_square.obj", loader);

        for (int i = 0; i < board.maxX; i++) {
            for (int j = 0; j < board.maxY; j++) {
                ModelTexture tex;
                if (i % 2 == 0) tex = (j % 2 == 0 ? white : black);
                else tex = (j % 2 != 0 ? white : black);

                TexturedModel txModel = new TexturedModel(model, tex);
                Vector3f pos = new Vector3f(i*squareSize, 0, j*squareSize);
                squares.put(new Coordinate(i, j), new Entity(txModel, pos, 0, 0, 0, squareSize, new SpherePicker(pos, (float) squareSize / 2)));
            }
        }
    }

    private void processEntities() {
        for (Piece piece : board.getPieces()) {
            Entity entity = entities.get(piece);
            if (entity == null) continue;

            renderer.processEntity(entity);
        }
    }

    private void processBoard() {
        for (Entity entity : squares.values())
            if (entity != null) renderer.processEntity(entity);
    }

    private void processHighlights() {
        for (Entity entity : highlights.values())
            if (entity != null) renderer.processEntity(entity);
    }

    private void checkMouse() {
        if (Mouse.isButtonDown(0)) {
            mousePicker.update();

            if (selectingPiece) {
                for (Piece piece : board.getPieces()) {
                    Entity entity = entities.get(piece);
                    if (entity.getPicker().isIntersecting(mousePicker.getCurrentRay(), camera.getPosition())) {
                        selectedPiece = piece;
                        synchronized (this) {
                            this.notify();
                        }
                        break;
                    }
                }
            }

            if (selectingSquare) {
                for (Coordinate coord : squares.keySet()) {
                    Entity entity = squares.get(coord);
                    if (entity.getPicker().isIntersecting(mousePicker.getCurrentRay(), camera.getPosition())) {
                        selectedSquare = coord;
                        synchronized (this) {
                            this.notify();
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        init();

        int count = 0;
        while (Display.isCreated()) {
            long start = System.nanoTime();

            camera.move(renderer.getProjectionMatrix(), Maths.createViewMatrix(camera));
            updatePieces();
            updateHighlights();

            if (squares.isEmpty()) initBoard();
            processEntities();
            processBoard();
            processHighlights();

            checkMouse();

            try {
                renderer.render(light, camera);
                guiRenderer.render(guis);
                DisplayManager.updateDisplay();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            if (count++ == 60) {
                long end = System.nanoTime() - start;
                Display.setTitle("Chess " + Math.round(1_000_000_000d / end));
                count = 0;
            }
        }


        renderer.cleanUp();
        guiRenderer.cleanUp();
        loader.cleanUp();
        System.out.println("end");
        parent.endGame();
    }

    public DisplayUpdater(Board board, int spacing, G3DUserLayer parent) {
        this.board = board;
        this.spacing = spacing;

        loader = new Loader();
        light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));
        camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(90, 0, 0), 45);
        this.parent = parent;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void clearSelected() {
        this.selectedPiece = null;
    }

    public Coordinate getSelectedSquare() {
        return selectedSquare;
    }

    public void clearSelectedSquare() {
        this.selectedSquare = null;
    }

    public void setSelectingPiece(boolean selectingPiece) {
        this.selectingPiece = selectingPiece;
    }

    public void setSelectingSquare(boolean selectingSquare) {
        this.selectingSquare = selectingSquare;
    }
}
