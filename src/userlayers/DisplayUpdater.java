package userlayers;

import board.Board;
import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import pieces.Piece;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;

import java.util.ArrayList;

public class DisplayUpdater implements Runnable {

    private final int spacing;
    private final Board board;
    private final MasterRenderer renderer;
    private final Camera camera;
    private final Light light;
    private final ArrayList<Entity> entities = new ArrayList<>();
    private Loader loader;

    private void addPieces() {
        entities.clear();
        for (Piece piece : board.getPieces()) {
            TexturedModel model = new TexturedModel(OBJLoader.loadObjModel("assets/default_models/pawn.obj", loader),
                    new ModelTexture(loader.loadTexture("assets/default_textures/" + piece.getPlayer().representation + ".png")));

            Entity entity = new Entity(model, new Vector3f(piece.getPosition().x * spacing, 0, piece.getPosition().y * spacing), 0, 0, 0, 1);
            entities.add(entity);
        }
    }

    @Override
    public void run() {

        while (true) {
            camera.setPosition(new Vector3f(((float) board.maxX / 2) * spacing, 40, ((float) board.maxY / 2) * spacing));

            for (Entity entity : entities)
                renderer.processEntity(entity);

            try {
                renderer.render(light, camera);
                DisplayManager.updateDisplay();
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("rendered");
        }
    }

    public DisplayUpdater(Board board, int spacing) {
        this.board = board;
        this.spacing = 2;

        DisplayManager.createDisplay();
        loader = new Loader();
        addPieces();
        light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));
        camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(90, 0, 0), 45);
        this.renderer = new MasterRenderer("assets/shaders", camera);
    }
}
