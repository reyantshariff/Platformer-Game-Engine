package oogasalad.model.engine.component;

import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.serialization.SerializableField;

/**
 * The Renderer class is a component that is responsible for rendering the image of the game object.
 * It is an abstract class that extends the GameComponent class.
 *
 * @author Christian Bepler
 */

public abstract class Renderer extends GameComponent {

    @SerializableField
    private Integer zIndex;

    @Override
    public ComponentTag componentTag() {
        return ComponentTag.RENDER;
    }

    /**
     * Returns the z-index for rendering the image.
     *
     * @return the z-index
     */
    public int getZIndex() {
        return zIndex;
    }

    /**
     * Sets the z-index for rendering the image.
     *
     * @param zIndex the new z-index
     */
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }
}
