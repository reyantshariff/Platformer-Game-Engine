package oogasalad.engine.component;

import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.serialization.SerializableField;

public class Transform extends GameComponent {
    @Override
    public ComponentTag componentTag() { return null; }

    Transform() {
        getSerializedFields();
    }

    @SerializableField
    private double x;
    @SerializableField
    private double y;
    @SerializableField
    private double rotation;
    @SerializableField
    private double scaleX;
    @SerializableField
    private double scaleY;

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
