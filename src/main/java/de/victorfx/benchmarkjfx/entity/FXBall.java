package de.victorfx.benchmarkjfx.entity;

/**
 * @author Ramon Victor March 2016.
 * Based on the modified version by TBEERNOT 2011 (tbeernot.wordpress.com).
 */
public class FXBall {
    private BallRegion region = new BallRegion();
    protected double x = 0;
    protected double y = 0;
    protected double vx = 0;
    protected double vy = 0;
    protected double r = 0;
    protected double d = 0;
    protected double d2 = 0;

    public FXBall() {
        this.x = (region.getInsets().right - region.getInsets().left - 2 * region.getBallRadius()) * Math.random();
        this.y = (region.getInsets().bottom - region.getInsets().top - 2 * region.getBallRadius()) * Math.random();
        this.vx = 2 * region.getMaxSpeed() * Math.random() - region.getMaxSpeed();
        this.vy = 2 * region.getMaxSpeed() * Math.random() - region.getMaxSpeed();
        this.d = 2 * region.getBallRadius();
    }

    public void move() {
        this.x += this.vx;
        this.y += this.vy;

        if (this.x < region.getInsets().left && this.vx < 0) {
            this.vx = -this.vx;
        }
        if (this.y < region.getInsets().top && this.vy < 0) {
            this.vy = -this.vy;
        }
        if (this.x > region.getInsets().right - this.d && this.vx > 0) {
            this.vx = -this.vx;
        }
        if (this.y > region.getInsets().bottom - this.d && this.vy > 0) {
            this.vy = -this.vy;
        }
    }

    public BallRegion getRegion() {
        return region;
    }

    public void setRegion(BallRegion region) {
        this.region = region;
    }
}
