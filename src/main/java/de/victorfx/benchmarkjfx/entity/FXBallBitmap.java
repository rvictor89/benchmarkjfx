package de.victorfx.benchmarkjfx.entity;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Ramon Victor March 2016.
 * Based on the modified version by TBEERNOT 2011 (tbeernot.wordpress.com).
 */
public class FXBallBitmap extends FXBall {
    private final ImageView imageView;

    public FXBallBitmap(Image image) {
        this.imageView = new ImageView(image);
        move();
    }

    public ImageView getImageView() {
        return imageView;
    }

    @Override
    public void move() {
        super.move();
        this.imageView.setX(this.x);
        this.imageView.setY(this.y);
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public FXBallBitmap clone() {
        FXBallBitmap fxBallBitmap = new FXBallBitmap(imageView.getImage());
        fxBallBitmap.getRegion().setInsets(this.getRegion().getInsets());
        ((Group) imageView.getParent()).getChildren().add(fxBallBitmap.imageView);
        return fxBallBitmap;
    }

}
