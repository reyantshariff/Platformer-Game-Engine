package oogasalad.engine.gameComponent;

import oogasalad.engine.base.ComponentTag;
import oogasalad.engine.base.GameComponent;
import oogasalad.engine.base.GameObject;

public class Transform extends GameComponent {
    @Override
    public oogasalad.engine.base.ComponentTag componentTag() { return null; }

    private double x;
    private double y;
    private double rotation;
    private double scaleX;
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
