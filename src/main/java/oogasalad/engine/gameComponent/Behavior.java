package oogasalad.engine.gameComponent;

import oogasalad.engine.base.ComponentTag;
import oogasalad.engine.base.GameComponent;

public abstract class Behavior extends GameComponent {
    public static final ComponentTag TAG = ComponentTag.BEHAVIOR;

    public Behavior() {
        super(TAG);
    }

}
