package oogasalad.model.engine.component;

import javafx.util.Pair;

import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.enumerate.ComponentTag;

public class SmoothMovement extends GameComponent {

    private Pair<Double, Double> previousPosition;
    private Pair<Double, Double> currentPosition;
    private Transform transform;
    private PhysicsHandler physicsHandler;
    private double speedLimit;

    public SmoothMovement() {
    }

    @Override
    public ComponentTag componentTag() {
        return ComponentTag.SMOOTH_MOVEMENT;
    }

    @Override
    protected void awake() {
        transform = getParent().getComponent(Transform.class);
        currentPosition = new Pair<Double, Double>(transform.getX(), transform.getY());
        previousPosition = new Pair<>(currentPosition.getKey(), currentPosition.getValue());
        physicsHandler = getParent().getComponent(Follower.class).getFollowObject().getComponent(PhysicsHandler.class);
        speedLimit = 0;
    }

    @Override
    protected void update(double deltaTime) {
        double minSpeed = Math.sqrt(Math.pow(physicsHandler.getVelocityX(), 2) + Math.pow(physicsHandler.getVelocityY(), 2));
        double playerAcceleration = Math.sqrt(Math.pow(physicsHandler.getAccelerationX(), 2) + Math.pow(physicsHandler.getAccelerationY(), 2));
        double acceleration = Math.max(1, playerAcceleration);
        currentPosition = new Pair<>(transform.getX(), transform.getY());
        double distance = Math.sqrt(Math.pow(currentPosition.getKey() - previousPosition.getKey(), 2)
                + Math.pow(currentPosition.getValue() - previousPosition.getValue(), 2));
        double maxDistance = speedLimit * deltaTime;
        if (distance > maxDistance) {
            double ratio = maxDistance / distance;
            double newX = previousPosition.getKey() + (currentPosition.getKey() - previousPosition.getKey()) * ratio;
            double newY = previousPosition.getValue() + (currentPosition.getValue() - previousPosition.getValue()) * ratio;
            transform.setX(newX);
            transform.setY(newY);
            speedLimit = Math.max(minSpeed + acceleration * deltaTime, speedLimit + acceleration * deltaTime);
        } else {
            speedLimit = Math.max(minSpeed, speedLimit - acceleration * deltaTime);
        }
        previousPosition = new Pair<>(transform.getX(), transform.getY());
    }

}
