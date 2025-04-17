package oogasalad.model.engine.component;

import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.base.serialization.SerializableField;

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
