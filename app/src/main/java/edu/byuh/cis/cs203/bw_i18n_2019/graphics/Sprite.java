package edu.byuh.cis.cs203.bw_i18n_2019.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import edu.byuh.cis.cs203.bw_i18n_2019.misc.TickListener;

public abstract class Sprite implements TickListener {
    protected Bitmap image;
    protected RectF bounds;
    protected PointF velocity;
    //protected float screenWidth, screenHeight;

    /**
     * default constructor.
     */
    public Sprite() {
        bounds = new RectF();
        velocity = new PointF();
    }

    /**
     * set the sprite's top-left corner to (x,y)
     * @param x the new X coordinate
     * @param y the new Y coordinate
     */
    public void setLocation(float x, float y) {
        bounds.offsetTo(x, y);
    }

    /**
     * helper method for positioning the sprite's center
     * @param x the new X coordinate
     * @param y the new Y coordinate
     */
    public void setCentroid(float x, float y) {
        bounds.offsetTo(x-bounds.width()/2, y-bounds.height()/2);
    }

    /**
     * set the sprite's bottom-left corner to (x,y)
     * @param p the new point
     */
    public void setBottomLeft(PointF p) {
        bounds.offsetTo(p.x, p.y-bounds.height());
    }

    /**
     * set the sprite's bottom-right corner to (x,y)
     * @param p the new point
     */
    public void setBottomRight(PointF p) {
        bounds.offsetTo(p.x-bounds.width(), p.y-bounds.height());
    }

    /**
     * Render the sprite at its pre-set location
     * @param c the Android Canvas object
     */
    public void draw(Canvas c) {
        c.drawBitmap(image, bounds.left,  bounds.top, null);
    }

    /**
     * Move the sprite a few pixels, based on its current velocity.
     */
    public void move() {
        bounds.offset(velocity.x, velocity.y);
    }

    /**
     * helper method to find out how tall the sprite is
     * @return the height of the sprite, in pixels
     */
    public float getHeight() {
        return bounds.height();
    }

    /**
     * helper method to return the top Y coordinate of the sprite
     * @return the top Y coordinate of the sprite
     */
    public float getTop() {
        return bounds.top;
    }

    /**
     * helper method to return the lower Y coordinate of the sprite
     * @return the lower Y coordinate of the sprite
     */
    public float getBottom() {
        return bounds.bottom;
    }

    /**
     * helper method to find out how wide the sprite is
     * @return the width of the sprite, in pixels
     */
    public float getWidth() {
        return bounds.width();
    }

    /**
     * Determines whether "this" sprite's bounding box intersects another sprite's bounding box.
     * @param s the sprite to compare against this one.
     * @return true if they overlap; false if not.
     */
    public boolean overlaps(Sprite s) {
        return RectF.intersects(s.bounds, this.bounds);
    }

    @Override
    public void tick() {
        move();
    }
}
