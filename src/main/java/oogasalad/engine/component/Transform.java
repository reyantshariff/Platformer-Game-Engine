package oogasalad.engine.component;

import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.serialization.SerializableField;

/**
 * A component that defines the spatial and visual properties of a GameObject,
 * including position, scale, rotation, and image path.
 *
 * This is typically the foundational component for rendering and positioning
 * game entities within the scene.
 *
 * @author Hsuan-Kai Liao, Christian Bepler
 */
public class Transform extends GameComponent {

    /**
     * Returns the component tag identifying this as a transform-related component.
     *
     * @return {@link ComponentTag#TRANSFORM}
     */
    @Override
    public ComponentTag componentTag() {
        return ComponentTag.TRANSFORM;
    }

    /** The x-coordinate of the GameObject. */
    @SerializableField
    private double x;

    /** The y-coordinate of the GameObject. */
    @SerializableField
    private double y;

    /** The rotation angle (in degrees or radians, based on engine convention). */
    @SerializableField
    private double rotation;

    /** The scale factor along the x-axis. */
    @SerializableField
    private double scaleX;

    /** The scale factor along the y-axis. */
    @SerializableField
    private double scaleY;

    /** The file path to the image used to visually represent the GameObject. */
    @SerializableField
    private String imagePath;

    /**
     * Returns the x-coordinate of the GameObject.
     *
     * @return the x position
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the GameObject.
     *
     * @param x the new x position
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns the y-coordinate of the GameObject.
     *
     * @return the y position
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the GameObject.
     *
     * @param y the new y position
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns the rotation of the GameObject.
     *
     * @return the rotation angle
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation of the GameObject.
     *
     * @param rotation the new rotation angle
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    /**
     * Returns the x-axis scale factor.
     *
     * @return the scale along the x-axis
     */
    public double getScaleX() {
        return scaleX;
    }

    /**
     * Sets the x-axis scale factor.
     *
     * @param scaleX the new scale value along the x-axis
     */
    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    /**
     * Returns the y-axis scale factor.
     *
     * @return the scale along the y-axis
     */
    public double getScaleY() {
        return scaleY;
    }

    /**
     * Sets the y-axis scale factor.
     *
     * @param scaleY the new scale value along the y-axis
     */
    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    /**
     * Returns the image path representing the GameObject's appearance.
     *
     * @return the image path string
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Sets the image path to visually represent the GameObject.
     *
     * @param imagePath the new image file path
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}