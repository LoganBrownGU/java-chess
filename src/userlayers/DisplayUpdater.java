package userlayers;

import board.Board;
import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import pieces.Piece;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayUpdater implements Runnable {

    private final int spacing;
    private final Board board;
    private final Camera camera;
    private final Light light;
    private final HashMap<Piece, Entity> entities = new HashMap<>();
    private final Loader loader;
    private final G3DUserLayer parent;

    private void addPieces() {
        //entities.clear();
        for (Piece piece : board.getPieces()) {
            if (entities.get(piece) != null) continue;

            try {
                TexturedModel model = new TexturedModel(OBJLoader.loadObjModel("assets/default_models/Pawn.obj", loader),
                        new ModelTexture(loader.loadTexture("assets/default_textures/" + piece.getPlayer().representation + ".png")));

                Entity entity = new Entity(model, new Vector3f(piece.getPosition().x * spacing, 0, piece.getPosition().y * spacing), 0, 0, 0, 1);
                entities.put(piece, entity);
            } catch (NullPointerException ignored) {}
        }
    }

    @Override
    public void run() {
        DisplayManager.createDisplay("Chess");
        MasterRenderer renderer = new MasterRenderer("assets/shaders", camera);
        camera.setPosition(new Vector3f(((float) board.maxX / 2) * spacing, 10, (float) board.maxY * spacing * 2.5f));

        while (Display.isCreated()) {
            camera.move();
            addPieces();

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
