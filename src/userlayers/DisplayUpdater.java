package userlayers;

import board.Board;
import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import pieces.Pawn;
import pieces.Piece;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;

import java.util.HashMap;

public class DisplayUpdater implements Runnable {

    private final int spacing;
    private final Board board;
    private final Camera camera;
    private final Light light;
    private final HashMap<Piece, Entity> entities = new HashMap<>();
    private final Loader loader;
    private final G3DUserLayer parent;
    private Entity boardModel = null;

    private void addPieces() {
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

                    Entity entity = new Entity(model, new Vector3f(piece.getPosition().x * spacing, 0, piece.getPosition().y * spacing), 0, 90 * directions[playerIdx], 0, 1);
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

    @Override
    public void run() {
        DisplayManager.createDisplay("Chess", 1280, 720, true, false);
        MasterRenderer renderer = new MasterRenderer("assets/shaders", camera);
        camera.setPosition(new Vector3f(((float) board.maxX / 2) * spacing, 10, (float) board.maxY * spacing * 2.5f));

        int count = 0;
        while (Display.isCreated()) {
            long start = System.nanoTime();

            camera.move();
            addPieces();

            if (boardModel == null) {
                TexturedModel model = new TexturedModel(OBJLoader.loadObjModel("assets/default_models/board.obj", loader),
                        new ModelTexture(loader.loadTexture("assets/default_textures/board.png")));

                boardModel = new Entity(model, new Vector3f(-1, 0, -1), 0, 0, 0, 1);
            }

            renderer.processEntity(boardModel);
            for (Piece piece : board.getPieces()) {
                Entity entity = entities.get(piece);
                if (entity == null) continue;

                renderer.processEntity(entities.get(piece));
            }

            try {
                renderer.render(light, camera);
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

        System.out.println("end");
        parent.endGame();
    }

    public DisplayUpdater(Board board, int spacing, G3DUserLayer parent) {
        this.board = board;
        this.spacing = spacing;

        loader = new Loader();
        light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));
        camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(20, 0, 0), 45);
        this.parent = parent;
    }
}
