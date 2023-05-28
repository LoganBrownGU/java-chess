package userlayers.graphics3d;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera extends entities.Camera {

    private final Vector3f focusPosition;

    @Override
    public void move(Matrix4f projectionMatrix) {
        Vector3f toFocus = Vector3f.sub(focusPosition, this.getPosition(), null);
    }

    public Camera(Vector3f position, Vector3f rotation, float fov, Vector3f focusPosition) {
        super(position, rotation, fov);
        this.focusPosition = focusPosition;
    }
}
