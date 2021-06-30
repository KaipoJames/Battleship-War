package edu.byuh.cis.cs203.bw_i18n_2019.graphics;

import android.graphics.PointF;

public class DepthCharge extends Sprite {

    public DepthCharge() {
        super();
        image = ImageCache.getDepthChargeImage();
        velocity = new PointF(0, ImageCache.screenHeight()/100);
        bounds.set(0,0,image.getWidth(), image.getHeight());
    }

//    @Override
//    protected float relativeWidth() {
//        return 0.025f;
//    }
//
//    @Override
//    public void scale(float w, float h) {
//        super.scale(w,h);
//        velocity = new PointF(0, h/100);
//    }
}
