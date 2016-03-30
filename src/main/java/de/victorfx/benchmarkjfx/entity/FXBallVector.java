package de.victorfx.benchmarkjfx.entity;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ramon Victor March 2016.
 * Based on the modified version by TBEERNOT 2011 (tbeernot.wordpress.com).
 */
public class FXBallVector extends FXBall {

    private final Circle circle;

    public FXBallVector() {
        List<Stop> stops = new ArrayList<>();
        stops.add(new Stop(0.2, Color.RED));
        stops.add(new Stop(0.8, Color.RED.darker()));
        stops.add(new Stop(1.0, Color.RED.darker().darker()));
        RadialGradient gradient = new RadialGradient(0, 0, 20, 20, 20, false, CycleMethod.NO_CYCLE, stops);
        circle = new Circle(20, 20, 20, gradient);
        move();
    }

    @Override
    public void move() {
        super.move();
        circle.setTranslateX(this.x);
        circle.setTranslateY(this.y);
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public FXBallVector clone() {
        FXBallVector fxBallVector = new FXBallVector();
        fxBallVector.getRegion().setInsets(this.getRegion().getInsets());
        ((Group) circle.getParent()).getChildren().add(fxBallVector.circle);
        return fxBallVector;
    }

    public Circle getCircle() {
        return circle;
    }
}
