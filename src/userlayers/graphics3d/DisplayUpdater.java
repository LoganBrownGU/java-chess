package userlayers.graphics3d;

import board.Board;
import entities.*;
import fontRendering.TextMaster;
import gui.GUIMaster;
import gui.GUIRenderer;
import gui.GUITexture;
import gui.TextField;
import main.Coordinate;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import pieces.Pawn;
import pieces.Piece;
import players.Player;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;
import toolbox.Colours;
import toolbox.Maths;
import toolbox.MousePicker;
import userlayers.G3DUserLayer;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayUpdater implements Runnable {

    private final int spacing;
    private final Board board;
    private final Camera camera;
    private final Light sun;
    private final ArrayList<Light> highlightLights = new ArrayList<>();
    private final HashMap<Player, ArrayList<GUITexture>> takenPieces = new HashMap<>();
    private final HashMap<Piece, Entity> entities = new HashMap<>();
    private final HashMap<Coordinate, Entity> squares = new HashMap<>();
    private Entity border;
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
    private final boolean[] mouseButtonsReleased = new boolean[3];
    private Coordinate checkmarkLocation = null;
    private TexturedModel checkmarkModel;
    private Piece takenPiece = null;
    private Player winner = null;

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
                            new ModelTexture(loader.loadTexture("assets/default_textures/" + piece.getPlayer().representation + ".png"), false));

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
            float brightness = 2.3f / moves.size();
            Vector3f pos = new Vector3f(move.x * spacing, 0, move.y * spacing);
            Entity entity = new Entity(highlightModel, pos, 0, 0, 0, 0.4f, null);
            Light light = new Light(pos, new Vector3f(brightness, brightness, 0), true);
            highlights.put(move, entity);
            highlightLights.add(light);
        }
    }

    private void init() {
        DisplayManager.createDisplay("Chess", 1280, 720, false);
        renderer = new MasterRenderer("assets/shaders", "assets/default_textures/skyboxes/sea", camera);
        mousePicker = new MousePicker(renderer.getProjectionMatrix(), camera);
        renderer.disableFog();
        camera.setPosition(new Vector3f((float) (board.maxX - 1) * .5f * spacing, 20, -(float) (board.maxY - 1) * .6f * spacing));
        camera.setRotation(new Vector3f(55, -180, 0));

        guiRenderer = new GUIRenderer(loader, "assets/shaders/guiVertexShader.glsl");

        TextMaster.init(loader, "assets/shaders/fontVertex.glsl", "assets/shaders/fontFragment.glsl");
        GUIMaster.setFont(loader, "assets/fonts/arial");
        TextField tField = new TextField(Colours.DARK_GREY, Colours.WHITE, new Vector2f((float) Display.getWidth() / 10, Display.getHeight() / 2), new Vector2f((float) Display.getWidth() / 5, Display.getHeight()), "", 0);
        tField.add();
        tField = new TextField(Colours.WHITE, Colours.DARK_GREY, new Vector2f(Display.getWidth() - Display.getWidth() / 10, Display.getHeight() / 2), new Vector2f((float) Display.getWidth() / 5, Display.getHeight()), "", 0);
        tField.add();
        tField = new TextField(Colours.DARK_GREY, Colours.WHITE, new Vector2f(Display.getWidth() / 10, Display.getHeight() / 20), new Vector2f((float) Display.getWidth() / 5, Display.getHeight() / 10), "Pieces taken by black", 0);
        tField.add();
        tField = new TextField(Colours.WHITE, Colours.DARK_GREY, new Vector2f(Display.getWidth() - Display.getWidth() / 10, Display.getHeight() / 20), new Vector2f((float) Display.getWidth() / 5, Display.getHeight() / 10), "Pieces taken by white", 0);
        tField.add();

        highlightModel = new TexturedModel(OBJLoader.loadObjModel("assets/default_models/highlight.obj", loader), new ModelTexture(loader.loadTexture("assets/default_textures/y.png"), true));
        checkmarkModel = new TexturedModel(OBJLoader.loadObjModel("assets/default_models/checkmark.obj", loader), new ModelTexture(loader.loadTexture("assets/default_textures/y.png"), false));


        /*for (int i = 0; i < 30; i++) {
            takenPiece = board.getPieces().get(i);
            processTakenPieces();
        }*/
    }

    private void initBoard() {
        int squareSize = 2;
        ModelTexture black = new ModelTexture(loader.loadTexture("assets/default_textures/b.png"), false);
        ModelTexture white = new ModelTexture(loader.loadTexture("assets/default_textures/w.png"), true);
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

        border = new Entity(new TexturedModel(OBJLoader.loadObjModel("assets/default_models/border.obj", loader), new ModelTexture(loader.loadTexture("assets/default_textures/wood.png"), false)),
                new Vector3f(0, 0, 0), 0, 0, 0, spacing, null);
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

        renderer.processEntity(border);
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

    private void processTakenPieces() {
        if (takenPiece == null) return;
        takenPieces.computeIfAbsent(takenPiece.getPlayer(), k -> new ArrayList<>());

        int index = takenPieces.get(takenPiece.getPlayer()).size();

        float aspect = (float) Display.getWidth() / Display.getHeight();

        float div = 2f / 40;
        Vector2f scale = new Vector2f(div, div * aspect);

        int wrap = 4;
        float x, y = (float) (1 - scale.y * 1.5f - Math.floor((double) index / wrap) * scale.y * 2 - .2f);

        if (board.getPlayers().indexOf(takenPiece.getPlayer()) % 2 == 0)
            x = (float) -1 + scale.x + (index % wrap) * (div + .04f);
        else
            x = (float) 1 - scale.x - (index % wrap) * (div + .04f);

        String rep = takenPiece.getPlayer().representation + takenPiece.representation;
        GUITexture icon = new GUITexture(loader.loadTexture("assets/default_icons/" + rep + ".png"), new Vector2f(x, y), scale);
        takenPieces.get(takenPiece.getPlayer()).add(icon);

        takenPiece = null;
    }

    private void checkMouse() {
        if (winner != null) {
            if (Mouse.isButtonDown(0) && Mouse.next())
                winner = null;
            return;
        }

        for (Piece piece : board.getPieces()) {
            Entity entity = entities.get(piece);
            if (entity.getPicker().isIntersecting(mousePicker.getCurrentRay(), camera.getPosition())) {
                if (mouseButtonsReleased[0] && selectingPiece) {
                    selectedPiece = piece;
                    System.out.println(selectedPiece);
                    if (selectedPiece != null) {
                        Vector3f pos = new Vector3f(selectedPiece.getPosition().x * spacing, 0, selectedPiece.getPosition().y * spacing);
                        Light light = new Light(pos, new Vector3f(0, 1f, 0), true);
                        highlightLights.add(light);
                    }
                    synchronized (this) {
                        this.notify();
                    }
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
                    synchronized (this) {
                        this.notify();
                    }
                    return;
                }
            }

            selectedSquare = null;
            highlights.clear();
            synchronized (this) {
                this.notify();
            }
        }
    }

    private void processAll() {
        processEntities();
        processBoard();
        processHighlights();
        processCheckmark();
        processTakenPieces();
    }

    @Override
    public void run() {
        init();

        while (!Display.isCloseRequested() && winner == null) {

            camera.move(renderer.getProjectionMatrix());
            updatePieces();
            updateMouseButtons();

            if (squares.isEmpty()) initBoard();
            processAll();

            checkMouse();

            try {
                ArrayList<Light> lights = new ArrayList<>(highlightLights);
                lights.add(sun);
                renderer.render(lights, camera);
                GUIMaster.render(guiRenderer);

                for (ArrayList<GUITexture> textures : takenPieces.values())
                    guiRenderer.render(textures);

                TextMaster.render();

                DisplayManager.updateDisplay();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        highlightLights.clear();
        highlights.clear();
        if (winner != null)
            GUIMaster.addElement(new TextField(Colours.BLACK, Colours.WHITE, new Vector2f(Display.getWidth() / 2, Display.getHeight() / 2), new Vector2f(Display.getWidth() / 4, Display.getHeight() / 4), winner.representation + " wins", 1));
        while (winner != null && !Display.isCloseRequested()) {
            checkMouse();
            processAll();
            ArrayList<Light> lights = new ArrayList<>();
            lights.add(sun);
            renderer.render(lights, camera);
            GUIMaster.render(guiRenderer);
            TextMaster.render();
            DisplayManager.updateDisplay();
        }

        TextMaster.cleanUp();
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
        camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(90, 0, 0), 60, new Vector3f(0, 0, 0));
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

    public void updateTakenPieces(Piece takenPiece) {
        this.takenPiece = takenPiece;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
