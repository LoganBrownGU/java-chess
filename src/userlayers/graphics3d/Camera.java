package userlayers.graphics3d;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera extends entities.Camera {

    private final Vector3f focusPosition;
    private float distanceFromFocus = 25;
    private float angleAroundFocus = 0;
    private final float MAX_PITCH = 80, MIN_DISTANCE = 3;
    private final float SENS_X = .1f, SENS_Y = .3f, SENS_ZOOM = 0.05f;

    private void calculateZoom() {
        if (distanceFromFocus >= MIN_DISTANCE)
            distanceFromFocus -= Mouse.getDWheel() * SENS_ZOOM;
        else
            distanceFromFocus = MIN_DISTANCE;
    }

    private void calculatePitchAndAngle() {
        if (!Mouse.isButtonDown(2) && !Mouse.isButtonDown(1)) return;

        if (getRotation().x >= 0 && getRotation().x <= MAX_PITCH)
            getRotation().x -= Mouse.getDY() * SENS_X;

        if (getRotation().x > MAX_PITCH) getRotation().x = MAX_PITCH;
        if (getRotation().x < 0) getRotation().x = 0;

        angleAroundFocus -= Mouse.getDX() * SENS_Y;
        if (angleAroundFocus > 360 || angleAroundFocus < -360) angleAroundFocus = 0;
    }

    @Override
    public void move(Matrix4f projectionMatrix) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) getRotation().x -= .5f;
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) getRotation().x += .5f;

        calculateZoom();
        calculatePitchAndAngle();

        float verDistance = (float) (distanceFromFocus * Math.sin(Math.toRadians(getRotation().x)));
        float horDistance = (float) (distanceFromFocus * Math.cos(Math.toRadians(getRotation().x)));
        getPosition().y = focusPosition.y + verDistance;
        float dX = (float) (horDistance * Math.sin(Math.toRadians(angleAroundFocus)));
        float dZ = (float) (horDistance * Math.cos(Math.toRadians(angleAroundFocus)));
        getPosition().x = focusPosition.x - dX;
        getPosition().z = focusPosition.z - dZ;

        getRotation().y = 180 - angleAroundFocus;
    }

    public Camera(Vector3f position, Vector3f rotation, float fov, Vector3f focusPosition) {
        super(position, rotation, fov);
        this.focusPosition = focusPosition;
    }
}
