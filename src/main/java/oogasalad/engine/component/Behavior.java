package oogasalad.engine.component;

import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.serialization.SerializableField;

public abstract class Behavior extends GameComponent {
    @Override
    public final ComponentTag componentTag() { return ComponentTag.BEHAVIOR; }

    @SerializableField
    private boolean active;

    /**
     * Set the active state of the behavior
     * @param active the active state
     */
    public final void setActive(boolean active) {
        this.active = active;
        onEnable();
    }

    /**
     * Get the active state of the behavior
     */
    public final boolean getActive() {
        onDisable();
        return active;
    }

    /**
     * This the method that will be called when the behavior is set to enabled
     * NOTE: This method should be override if needed.
     */
    public void onEnable() {};

    /**
     * This the method that will be called when the behavior is set to disabled
     * NOTE: This method should be override if needed.
     */
    public void onDisable() {};

}
