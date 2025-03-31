package oogasalad.engine.gameComponent;

import oogasalad.engine.base.ComponentTag;
import oogasalad.engine.base.GameComponent;

public abstract class Behavior extends GameComponent {
    @Override
    public final ComponentTag componentTag() { return ComponentTag.BEHAVIOR; }

    /**
     * This the method that will be called when the behavior is set to enabled
     */
    public void OnEnable() {};

    /**
     * This the method that will be called when the behavior is set to disabled
     */
    public void OnDisable() {};

}
