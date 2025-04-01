package oogasalad.games.dinosaur.components;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;

public class VelocityComponent extends GameComponent {
   private double vx=0;
   private double vy=0;

   public void setVx(double v)
   {
     this.vx = v;
   }

   public double getVx()
   {
     return vx;
   }

   public void setVy(double v)
   {
     this.vy = v;
   }

   public double getVy()
   {
     return vy;
   }

  @Override
  public ComponentTag componentTag() {
    return ComponentTag.PHYSICS;
  }
}
