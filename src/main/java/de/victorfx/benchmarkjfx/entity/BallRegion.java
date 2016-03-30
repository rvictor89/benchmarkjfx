package de.victorfx.benchmarkjfx.entity;

import java.awt.*;

/**
 * @author Ramon Victor March 2016.
 */
public class BallRegion {

    private final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private Insets insets;
    private final double ballRadius;
    private final double maxSpeed;

    public BallRegion() {
        int width = 640;
        int height = 480;
        insets = new Insets(0, 0, height, width);
        ballRadius = 26;
        maxSpeed = 3.0;
    }

    public void setToDeviceDimensions() {
        int width = device.getDisplayMode().getWidth();
        int height = device.getDisplayMode().getHeight();
        Insets insets = new Insets(0, 0, height, width);
        setInsets(insets);
    }

    public Insets getInsets() {
        return insets;
    }

    public void setInsets(Insets insets) {
        this.insets = insets;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getBallRadius() {
        return ballRadius;
    }
}
