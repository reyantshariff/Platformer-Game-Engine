package oogasalad.engine.gameComponent;

import oogasalad.engine.base.ComponentTag;
import oogasalad.engine.base.GameComponent;
import oogasalad.engine.base.GameObject;

public class Transform extends GameComponent {
    public static final ComponentTag TAG = ComponentTag.TRANSFORM;

    private double x;
    private double y;
    private double rotation;
    private double scaleX;
    private double scaleY;

    public Transform() {
        super(TAG);
        this.x = 0;
        this.y = 0;
        this.rotation = 0;
        this.scaleX = 1;
        this.scaleY = 1;
    }

    public Transform(double x, double y) {
        this();
        this.x = x;
        this.y = y;
    }

    public Transform(double x, double y, double scaleX, double scaleY) {
        this(x, y);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public Transform(double x, double y, double scaleX, double scaleY, double rotation) {
        this(x, y, scaleX, scaleY);
        this.rotation = rotation;
    }

    public Transform(double x, double y, double rotation) {
        this(x, y);
        this.rotation = rotation;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }
}
