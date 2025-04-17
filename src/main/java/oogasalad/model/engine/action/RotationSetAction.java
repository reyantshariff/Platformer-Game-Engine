package oogasalad.model.engine.action;

import oogasalad.model.engine.component.Transform;

public class RotationSetAction extends SetComponentValueAction<Double, Transform> {

    @Override
    protected Transform supplyComponent() {
        return getComponent(Transform.class);
    }

    @Override
    protected java.util.function.BiConsumer<Transform, Double> provideSetter() {
        return Transform::setRotation;
    }

    @Override
    protected Double defaultParameter() {
        return 0.0;
    }

}
