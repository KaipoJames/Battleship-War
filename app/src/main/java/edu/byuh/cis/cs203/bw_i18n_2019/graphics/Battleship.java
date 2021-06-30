package edu.byuh.cis.cs203.bw_i18n_2019.graphics;

import android.graphics.PointF;

public class Battleship extends Sprite {

    private volatile static Battleship uniqueInstance=null;

    /**
     * default cosntructor
     */
    private Battleship() {
        super();
        image = ImageCache.getBattleshipImage();
        bounds.set(0,0,image.getWidth(), image.getHeight());
    }

    /**
     * A method to get an instance of the battleship object
     * @return a single instance of battleship
     */
    public static synchronized Battleship getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Battleship();
        }
        return uniqueInstance;
    }

//    @Override
//    protected float relativeWidth() {
//        return 0.4f;
//    }

    /**
     * finds where the right canon is
     * @return Point f
     */
    public PointF getRightCannonPosition() {
        return new PointF(bounds.left + bounds.width()*(167f/183f), bounds.top);
    }

    /**
     * finds where the left canon is
     * @return Point F
     */
    public PointF getLeftCannonPosition() {
        return new PointF(bounds.left + bounds.width()*(22f/183f), bounds.top);
    }

}
