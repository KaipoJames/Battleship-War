package edu.byuh.cis.cs203.bw_i18n_2019.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import edu.byuh.cis.cs203.bw_i18n_2019.misc.Direction;

public class Missile extends Sprite {

    private Paint missilePaint;
    private Direction dir;

    public Missile(Direction d) {
        super();
        dir = d;
        missilePaint = new Paint();
        missilePaint.setColor(Color.DKGRAY);
        missilePaint.setStyle(Paint.Style.STROKE);
        final float w = ImageCache.screenWidth();
        float newWidth = w * 0.05f;
        float newHeight = newWidth;
        bounds.set(0,0,newWidth,newHeight);
        missilePaint.setStrokeWidth(w*0.0025f);
        final float fudge = 25f;
        if (dir == Direction.LEFT_TO_RIGHT) {
            velocity = new PointF(w/fudge, -w/fudge);
        } else {
            velocity = new PointF(-w/fudge, -w/fudge);
        }

    }

//    @Override
//    public void scale(float w, float h) {
//        screenHeight = h;
//        screenWidth = w;
//        float newWidth = w * relativeWidth();
//        float newHeight = newWidth;
//        bounds.set(0,0,newWidth,newHeight);
//        missilePaint.setStrokeWidth(w*0.0025f);
//        final float fudge = 25f;
//        if (dir == Direction.LEFT_TO_RIGHT) {
//            velocity = new PointF(w/fudge, -w/fudge);
//        } else {
//            velocity = new PointF(-w/fudge, -w/fudge);
//        }
//    }

    @Override
    public void draw(Canvas c) {
        if (dir == Direction.LEFT_TO_RIGHT) {
            c.drawLine(bounds.left, bounds.bottom, bounds.right, bounds.top, missilePaint);
        } else {
            c.drawLine(bounds.left, bounds.top, bounds.right, bounds.bottom, missilePaint);
        }
    }

//    @Override
//    protected float relativeWidth() {
//        return 0.05f;
//    }
}
