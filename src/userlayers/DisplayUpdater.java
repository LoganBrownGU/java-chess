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
import org.lwjgl.util.vector.Vector3f;
import pieces.Pawn;
import pieces.Piece;
import players.Player;
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
    private final Light sun;
    private final ArrayList<Light> highlightLights = new ArrayList<>();
    private final HashMap<Piece, Entity> entities = new HashMap<>();
    private final HashMap<Coordinate, Entity> squares = new HashMap<>();
    private final HashMap<Coordinate, Entity> highlights = new HashMap<>();
    private final HashMap<Player, Piece> takenPieces = new HashMap<>();
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
    private final boolean[] mouseButtonsReleased = new boolean[3];
    private Coordinate checkmarkLocation = null;
    private TexturedModel checkmarkModel;

    private void updateMouseButtons() {
        for (int i = 0; i < mouseButtonsReleased.length; i++)
            mouseButtonsReleased[i] = Mouse.next() && !Mouse.getEventButtonState() && Mouse.getEventButton() == i;
    }

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

    private void updateHighlights(ArrayList<Coordinate> moves) {
        highlights.clear();
        highlightLights.clear();

        for (Coordinate move : moves) {
            Vector3f pos = new Vector3f(move.x * spacing, 0, move.y * spacing);
            Entity entity = new Entity(highlightModel, pos, 0, 0, 0, 0.8f, null);
            Light light = new Light(pos, new Vector3f(.5f, .5f, 0), true);
            highlights.put(move, entity);
            highlightLights.add(light);
        }
    }

    private void init() {
        DisplayManager.createDisplay("Chess", 1280, 720, true, false);
        renderer = new MasterRenderer("assets/shaders", "assets/default_textures/skyboxes/paris_low_res", camera);
        mousePicker = new MousePicker(renderer.getProjectionMatrix(), camera);
        renderer.disableFog();
        camera.setPosition(new Vector3f((float) (board.maxX - 1) * .5f * spacing, 40, -(float) (board.maxY - 1) * 1.5f * spacing));
        camera.setRotation(new Vector3f(55, -180, 0));

        guiRenderer = new GUIRenderer(loader);

        highlightModel = new TexturedModel(OBJLoader.loadObjModel("assets/default_models/highlight.obj", loader), new ModelTexture(loader.loadTexture("assets/default_textures/y.png")));
        checkmarkModel = new TexturedModel(OBJLoader.loadObjModel("assets/default_models/checkmark.obj", loader), new ModelTexture(loader.loadTexture("assets/default_textures/y.png")));
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
                Vector3f pos = new Vector3f(i * squareSize, 0, j * squareSize);
                Vector3f min = new Vector3f(pos.x - (float) squareSize / 2, 0, pos.z - (float) squareSize / 2);
                Vector3f max = new Vector3f(min.x + squareSize, 0, min.z + squareSize);
                squares.put(new Coordinate(i, j), new Entity(txModel, pos, 0, 0, 0, squareSize, new AABBPicker(min, max)));
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
    private void processCheckmark() {
        if (checkmarkLocation == null) return;

        Vector3f pos = new Vector3f(checkmarkLocation.x * spacing, spacing * 3f, checkmarkLocation.y * spacing);
        Vector3f vectorToCamera = Vector3f.sub(pos, camera.getPosition(), null);
        double rotY = Math.atan2(vectorToCamera.x, vectorToCamera.z) + Math.PI;

        Entity checkmark = new Entity(checkmarkModel, pos, 0, (float) Math.toDegrees(rotY), 0, .3f, null);
        renderer.processEntity(checkmark);
    }

    private void checkMouse() {
        for (Piece piece : board.getPieces()) {
            Entity entity = entities.get(piece);
            if (entity.getPicker().isIntersecting(mousePicker.getCurrentRay(), camera.getPosition())) {
                if (mouseButtonsReleased[0] && selectingPiece) {
                    selectedPiece = piece;
                    synchronized (this) { this.notify(); }
                    break;
                } else if (!selectingSquare)
                    updateHighlights(piece.possibleMoves());
            }
        }

        if (mouseButtonsReleased[0] && selectingSquare) {
            for (Coordinate coord : squares.keySet()) {
                Entity entity = squares.get(coord);

                if (entity.getPicker().isIntersecting(mousePicker.getCurrentRay(), camera.getPosition()) && selectedPiece.possibleMoves().contains(coord)) {
                    selectedSquare = coord;
                    synchronized (this) { this.notify(); }
                    return;
                }
            }

            selectedSquare = null;
            highlights.clear();
            synchronized (this) { this.notify(); }
        }
    }

    @Override
    public void run() {
        init();

        int count = 0;
        while (!Display.isCloseRequested()) {
            long start = System.nanoTime();

            camera.move(renderer.getProjectionMatrix(), Maths.createViewMatrix(camera));
            updatePieces();
            updateMouseButtons();

            if (squares.isEmpty()) initBoard();
            processEntities();
            processBoard();
            processHighlights();
            processCheckmark();

            checkMouse();

            try {
                ArrayList<Light> lights = new ArrayList<>(highlightLights);
                lights.add(sun);
                renderer.render(lights, camera);
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
        Display.destroy();
        parent.endGame();
    }

    public DisplayUpdater(Board board, int spacing, G3DUserLayer parent) {
        this.board = board;
        this.spacing = spacing;

        loader = new Loader();
        sun = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1), false);
        camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(90, 0, 0), 70);
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

    public void setCheckmarkLocation(Coordinate checkmarkLocation) {
        this.checkmarkLocation = checkmarkLocation;
    }

    public void clearCheckmarkLocation() {
        this.checkmarkLocation = null;
    }
}
