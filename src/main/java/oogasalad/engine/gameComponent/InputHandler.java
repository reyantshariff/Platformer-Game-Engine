package oogasalad.engine.gameComponent;
import oogasalad.engine.base.ComponentTag;
import oogasalad.engine.base.GameComponent;
import oogasalad.engine.base.GameAction;
import oogasalad.engine.base.KeyCode;

public class InputHandler extends GameComponent {
    public static final ComponentTag TAG = ComponentTag.NONE;

    public InputHandler() {
        super(TAG);
    }

    @Override
    public void start() {
    }

    public void registerAction(KeyCode input, GameAction action) {
        getParent().getScene().getInputMapping().addMapping(input, action);
    }

}
