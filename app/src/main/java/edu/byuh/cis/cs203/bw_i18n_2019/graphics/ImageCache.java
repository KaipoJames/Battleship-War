package edu.byuh.cis.cs203.bw_i18n_2019.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edu.byuh.cis.cs203.bw_i18n_2019.R;
import edu.byuh.cis.cs203.bw_i18n_2019.misc.Direction;
import edu.byuh.cis.cs203.bw_i18n_2019.misc.Size;

public class ImageCache {
    private static float screenWidth, screenHeight;
    private static Bitmap battleship;
    private static Bitmap water;
    private static Bitmap airplane_small_left2right;
    private static Bitmap airplane_med_left2right;
    private static Bitmap airplane_big_left2right;
    private static Bitmap airplane_small_right2left;
    private static Bitmap airplane_med_right2left;
    private static Bitmap airplane_big_right2left;
    private static Bitmap submarine_small_left2right;
    private static Bitmap submarine_med_left2right;
    private static Bitmap submarine_big_left2right;
    private static Bitmap submarine_small_right2left;
    private static Bitmap submarine_med_right2left;
    private static Bitmap submarine_big_right2left;
    private static Bitmap depth_charge;
    private static Bitmap explosion_submarine;
    private static Bitmap explosion_airplane;
    private static Bitmap pop;

    public static void init(Resources res, float w, float h) {
        screenWidth = w;
        screenHeight = h;
        battleship = loadAndScale(res, R.drawable.battleship, w*0.4f);
        water = loadAndScale(res, R.drawable.water, w/45);
        airplane_small_left2right = loadAndScale(res, R.drawable.little_airplane_flip, w*0.05f);
        airplane_med_left2right = loadAndScale(res, R.drawable.medium_airplane_flip, w*0.083f);
        airplane_big_left2right = loadAndScale(res, R.drawable.big_airplane_flip, w*0.12f);
        airplane_small_right2left = loadAndScale(res, R.drawable.little_airplane, w*0.05f);
        airplane_med_right2left = loadAndScale(res, R.drawable.medium_airplane, w*0.083f);
        airplane_big_right2left = loadAndScale(res, R.drawable.big_airplane, w*0.12f);
        submarine_small_left2right = loadAndScale(res, R.drawable.little_submarine, w*0.05f);
        submarine_med_left2right = loadAndScale(res, R.drawable.medium_submarine, w*0.083f);
        submarine_big_left2right = loadAndScale(res, R.drawable.big_submarine, w*0.1f);
        submarine_small_right2left = loadAndScale(res, R.drawable.little_submarine_flip, w*0.05f);
        submarine_med_right2left = loadAndScale(res, R.drawable.medium_submarine_flip, w*0.083f);
        submarine_big_right2left = loadAndScale(res, R.drawable.big_submarine_flip, w*0.1f);
        depth_charge = loadAndScale(res, R.drawable.depth_charge, w*0.025f);
        pop = loadAndScale(res, R.drawable.star, w*0.03f);
        explosion_airplane = loadAndScale(res, R.drawable.airplane_explosion, w*0.12f);
        explosion_submarine = loadAndScale(res, R.drawable.submarine_explosion, w*0.12f);
    }

    private static Bitmap loadAndScale(Resources res, int id, float newWidth) {
        Bitmap original = BitmapFactory.decodeResource(res, id);
        float aspectRatio = (float)original.getHeight()/(float)original.getWidth();
        float newHeight = newWidth * aspectRatio;
        return Bitmap.createScaledBitmap(original, (int)newWidth, (int)newHeight, true);
    }

    public static Bitmap getAirplaneImage(Size s, Direction d) {
        if (d==Direction.LEFT_TO_RIGHT) {
            if (s==Size.LARGE) {
                return airplane_big_left2right;
            } else if (s==Size.MEDIUM) {
                return airplane_med_left2right;
            } else {
                return airplane_small_left2right;
            }
        } else {
            if (s==Size.LARGE) {
                return airplane_big_right2left;
            } else if (s==Size.MEDIUM) {
                return airplane_med_right2left;
            } else {
                return airplane_small_right2left;
            }
        }
    }

    public static Bitmap getSubmarineImage(Size s, Direction d) {
        if (d==Direction.LEFT_TO_RIGHT) {
            if (s==Size.LARGE) {
                return submarine_big_left2right;
            } else if (s==Size.MEDIUM) {
                return submarine_med_left2right;
            } else {
                return submarine_small_left2right;
            }
        } else {
            if (s==Size.LARGE) {
                return submarine_big_right2left;
            } else if (s==Size.MEDIUM) {
                return submarine_med_right2left;
            } else {
                return submarine_small_right2left;
            }
        }
    }

    public static Bitmap getWaterImage() {
        return water;
    }

    public static Bitmap getBattleshipImage() {
        return battleship;
    }

    public static Bitmap getDepthChargeImage() {
        return depth_charge;
    }

    public static Bitmap getSubmarineExplosion() {
        return explosion_submarine;
    }

    public static Bitmap getAirplaneExposion() {
        return explosion_airplane;
    }

    public static Bitmap getCannonFire() {
        return pop;
    }

    public static float screenWidth() {
        return screenWidth;
    }

    public static float screenHeight() {
        return screenHeight;
    }

}
