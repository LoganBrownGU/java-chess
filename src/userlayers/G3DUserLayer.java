package userlayers;

import board.Board;
import entities.Camera;
import entities.Entity;
import entities.Light;
import main.Coordinate;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import pieces.Piece;
import players.Player;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;

import java.util.Random;

public class G3DUserLayer implements UserLayer {

    private Board board;
    private final Camera camera = new Camera();
    private MasterRenderer renderer;
    private Loader loader;
    private final Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));
    private boolean active = false;

    private void addPieces() {
        int spacing = 2;
        camera.setPitch(90);
        camera.setRoll(0);
        camera.setYaw(0);
        camera.setPosition(new Vector3f(((float) board.maxX / 2) * spacing, 40, ((float) board.maxY / 2) * spacing));

        for (int i = 0; i < 120; i++) {
            for (Piece piece : board.getPieces()) {
                TexturedModel model = new TexturedModel(OBJLoader.loadObjModel("default_models/pawn", loader), new ModelTexture(loader.loadTexture("w")));
                Entity entity = new Entity(model, new Vector3f(piece.getPosition().x * spacing, 0, piece.getPosition().y * spacing), 0, 0, 0, 1);
                renderer.processEntity(entity);
            }

            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }
    }

    @Override
    public Piece getPiece(Player p) {
        return board.getPieces().get(0);
    }

    @Override
    public Coordinate getMove() {
        return null;
    }

    @Override
    public void update() {
        if (!active) return;

        addPieces();
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public void showWinner(Player winner) {

    }

    @Override
    public void showCheck(Player checking, Player checked) {

    }

    @Override
    public String getPromotion() {
        return null;
    }

    @Override
    public boolean confirmCastling() {
        return false;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;

        if (active) {
            DisplayManager.createDisplay();
            loader = new Loader();
            camera.setFov(45);
            renderer = new MasterRenderer(camera);
        }
    }

    public G3DUserLayer() {

    }
}