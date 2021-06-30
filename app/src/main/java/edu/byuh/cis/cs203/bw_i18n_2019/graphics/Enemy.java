package edu.byuh.cis.cs203.bw_i18n_2019.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import edu.byuh.cis.cs203.bw_i18n_2019.misc.Direction;
import edu.byuh.cis.cs203.bw_i18n_2019.misc.Size;

public abstract class Enemy extends Sprite {
    protected Size size;
    protected Direction dir;
    private boolean exploding;
    private float speedFactor;

    /**
     * default constructor. Randomly selects a size (small, medium, large) for the sprite
     */
    public Enemy(float speed) {
        super();
        exploding = false;
        speedFactor = speed;
        resetRandom();
    }

    @Override
    public void move() {
        super.move();

        //change the velocity 10% of the time
        if (Math.random() < 0.1) {
            velocity.x = getRandomVelocity();
        }

        //handle wraparound
        if (bounds.right < 0 || bounds.left > ImageCache.screenWidth()) {
            resetRandom();
        }
    }

    protected void resetRandom() {
        double r1 = Math.random();
        double r2 = Math.random();
        //Direction newDirection;
        //Size newSize;
        if (r1 < 0.5) {
            dir = Direction.RIGHT_TO_LEFT;
        } else {
            dir = Direction.LEFT_TO_RIGHT;
        }
        if (r2 < 0.3333) {
            size = Size.LARGE;
        } else if (r2 < 0.667) {
            size = Size.MEDIUM;
        } else {
            size = Size.SMALL;
        }
        loadImage();
        //reset(newSize, newDirection);
        if (dir == Direction.RIGHT_TO_LEFT) {
            velocity.x = -1;
            velocity.x = getRandomVelocity();
            bounds.offsetTo(ImageCache.screenWidth()-1, getRandomHeight());
        } else {
            velocity.x = 1;
            velocity.x = getRandomVelocity();
            bounds.offsetTo(-image.getWidth()+1, getRandomHeight());
        }
    }

    /**
     * loads the correct image (type, size, and direction)
     * based on the subclass type and value of size/direction fields
     */
    protected abstract void loadImage();

    /**
     * get the correct point value for the enemy that just got hit.
     * @return a number of points
     */
    public abstract int getPointValue();

    /**
     * get the correct explosion image for this enemy
     * @return the explosion
     */
    protected abstract Bitmap explodingImage();

    /**
     * Randomly generate a height for an airplane.
     * @return a new Y position for the airplane
     */
    protected abstract float getRandomHeight();

    /**
     * sets a new X velocity for the sprite.
     * I multiply it by signum to ensure that we preserve the direction
     * (left or right) of the velocity. Positive stays positive, negative
     * stays negative
     * @return a new random X velocity
     */
    protected float getRandomVelocity() {
        return (float)(Math.random() * speedFactor * ImageCache.screenWidth() * Math.signum(velocity.x));
    }

    /**
     * change enemy to exploding mode. Change image and velocity.
     */
    public void explode() {
        image = explodingImage();
        float newLeft = bounds.centerX() - image.getWidth()/2;
        float newRight = newLeft + image.getWidth();
        float newTop = bounds.centerY() - image.getHeight()/2;
        float newBottom = newTop + image.getHeight();
        bounds.set(newLeft, newTop, newRight, newBottom);
        velocity.set(0, 0);
        exploding = true;
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);
        if (exploding) {
            exploding = false;
            resetRandom();
        }
    }

}
