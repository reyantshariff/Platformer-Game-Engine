package oogasalad.model.engine.action;

import oogasalad.model.engine.component.Transform;

public class ScaleXSetAction extends SetComponentValueAction<Double, Transform> {

    @Override
    protected Transform supplyComponent() {
        return getComponent(Transform.class);
    }

    @Override
    protected java.util.function.BiConsumer<Transform, Double> provideSetter() {
        return Transform::setScaleX;
    }

    @Override
    protected Double defaultParameter() {
        return 0.0;
    }

}
