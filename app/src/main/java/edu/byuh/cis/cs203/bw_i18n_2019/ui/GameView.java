package edu.byuh.cis.cs203.bw_i18n_2019.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import edu.byuh.cis.cs203.bw_i18n_2019.R;
import edu.byuh.cis.cs203.bw_i18n_2019.graphics.DepthCharge;
import edu.byuh.cis.cs203.bw_i18n_2019.graphics.ImageCache;
import edu.byuh.cis.cs203.bw_i18n_2019.graphics.Missile;
import edu.byuh.cis.cs203.bw_i18n_2019.graphics.Sprite;
import edu.byuh.cis.cs203.bw_i18n_2019.graphics.Submarine;
import edu.byuh.cis.cs203.bw_i18n_2019.graphics.Airplane;
import edu.byuh.cis.cs203.bw_i18n_2019.graphics.Battleship;
import edu.byuh.cis.cs203.bw_i18n_2019.misc.Direction;
import edu.byuh.cis.cs203.bw_i18n_2019.misc.Size;
import edu.byuh.cis.cs203.bw_i18n_2019.misc.TickListener;
import edu.byuh.cis.cs203.bw_i18n_2019.misc.Timer;

public class GameView extends View implements TickListener {

    private Bitmap water;
    private Battleship battleship;
    private List<Airplane> planes;
    private List<Submarine> subs;
    private boolean init;
    private List<DepthCharge> bombs;
    private List<Missile> missiles;
    private Timer horloge;
    private boolean leftPop, rightPop;
    private int score;
    private Paint scorePaint, timePaint;
    private int countdown;
    private long timeNow, timeBefore;
    private long bombTick;
    private MediaPlayer dcSound, leftSound, rightSound, airExplosion, waterExplosion;


    /**
     * Constructor for our View subclass. Loads all the images.
     * @param context a reference to our main Activity class
     */
    public GameView(Context context) {
        super(context);
        bombs = new ArrayList<>();
        missiles = new ArrayList<>();
        planes = new ArrayList<>();
        subs = new ArrayList<>();
        scorePaint = new Paint();
        scorePaint.setColor(Color.BLACK);
        scorePaint.setStyle(Paint.Style.FILL);
        scorePaint.setTextAlign(Paint.Align.LEFT);
        timePaint = new Paint(scorePaint);
        timePaint.setTextAlign(Paint.Align.RIGHT);
        leftPop = false;
        rightPop = false;
        dcSound = MediaPlayer.create(getContext(), R.raw.depth_charge);
        leftSound = MediaPlayer.create(getContext(), R.raw.left_gun);
        rightSound = MediaPlayer.create(getContext(), R.raw.right_gun);
        airExplosion = MediaPlayer.create(getContext(), R.raw.plane_explode);
        waterExplosion = MediaPlayer.create(getContext(), R.raw.sub_explode);
        init = false;
        restart();
    }

    /**
     * this method restarst the game
     */
    private void restart() {
        score = 0;
        countdown = 180;
        bombTick = 0;
        timeBefore = System.currentTimeMillis();
    }

    /**
     * Scales, positions, and renders the scene
     * @param c the Canvas object, provided by system
     */
    @Override
    public void onDraw(Canvas c) {
        c.drawColor(Color.WHITE);
        int w = getWidth();
        int h = getHeight();
        if (init == false) {
            init = true;
            ImageCache.init(getResources(), w, h);

            scorePaint.setTextSize(h/20);
            timePaint.setTextSize(scorePaint.getTextSize());

            water = ImageCache.getWaterImage();
            battleship = Battleship.getInstance();
            float battleshipX = w/2-battleship.getWidth()/2; //center the ship
            float battleshipY = h/2-battleship.getHeight()+water.getHeight(); //put the ship above the water line
            battleship.setLocation(battleshipX, battleshipY);

            //inform Airplane class of acceptable upper/lower limits of flight
            Airplane.setSkyLimits(0, battleship.getTop()-ImageCache.getAirplaneImage(Size.LARGE, Direction.RIGHT_TO_LEFT).getHeight()*2);

            //inform Submarine class of acceptable upper/lower limits of depth
            Submarine.setWaterDepth(h/2 + water.getHeight()*2, h-water.getHeight()*2);

            //instantiate enemy vessels
            float enemySpeed = Prefs.getPlaneSpeed(getContext());
            for (int i=0; i<Prefs.getNumPlanes(getContext()); i++) {
                planes.add(new Airplane(enemySpeed));
            }
            enemySpeed = Prefs.getSubSpeed(getContext());
            for (int i=0; i<Prefs.getNumSubs(getContext()); i++) {
                subs.add(new Submarine(enemySpeed));
            }

            //Once everything is in place, start the animation loop!
            horloge = new Timer();
            for (Airplane p : planes) {
                horloge.subscribe(p);
            }
            for (Submarine s : subs) {
                horloge.subscribe(s);
            }
            horloge.subscribe(this);
        }

        //now draw everything
        float waterX = 0;
        while (waterX < w) {
            c.drawBitmap(water, waterX, h/2, null);
            waterX += water.getWidth();
        }
        for (Airplane a : planes) {
            a.draw(c);
        }
        for (Submarine s : subs) {
            s.draw(c);
        }

        for (DepthCharge d : bombs) {
            d.draw(c);
        }
        for (Missile d : missiles) {
            d.draw(c);
        }
        battleship.draw(c);

        //extra credit: draw the "pop" at the mouth of the cannon
        Bitmap pop = ImageCache.getCannonFire();
        if (leftPop) {
            final PointF popLocation = battleship.getLeftCannonPosition();
            c.drawBitmap(pop, popLocation.x-pop.getWidth(), popLocation.y-pop.getHeight(), null);
            leftPop = false;
        }
        if (rightPop) {
            final PointF popLocation = battleship.getRightCannonPosition();
            c.drawBitmap(pop, popLocation.x, popLocation.y-pop.getHeight(), null);
            rightPop = false;
        }

        final String scoreLabel = getResources().getString(R.string.score);
        c.drawText(scoreLabel + ": " + score, 5, h*0.6f, scorePaint);
        final String timeLabel = getResources().getString(R.string.time);
        String temps = String.format(timeLabel + ": %d:%02d", countdown /60, countdown %60);
        c.drawText(temps, w-5, h*0.6f, timePaint);

    }

    /**
     * Deal with touch events. Launch missiles and depth charges
     * @param m the MotionEvent object, provided by the OS
     * @return always true. It just works better that way.
     */
    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            float x = m.getX();
            float y = m.getY();
            //did the user tap the bottom half of the screen? Depth Charge!
            if (y > getHeight()/2) {
                if (Prefs.getRapidDC(getContext()) || visibleBombs()==false) {
                    launchNewDepthCharge();
                }
            } else {
                //did the user tap the top half of the screen? missile!
                if (Prefs.getRapidGuns(getContext()) || visibleMissiles()==false) {
                    if (x < getWidth() / 2) {
                        launchNewMissile(Direction.RIGHT_TO_LEFT);
                    } else {
                        launchNewMissile(Direction.LEFT_TO_RIGHT);
                    }
                }
            }
            cleanupOffscreenObjects();
        }
        return true;
    }

    /**
     * creates new depth charges
     */
    private void launchNewDepthCharge() {
        DepthCharge dc = new DepthCharge();
        dc.setCentroid(getWidth()/2, getHeight()/2);
        bombs.add(dc);
        horloge.subscribe(dc);
    }

    /**
     * creates ne missiles
     * @param d
     */
    private void launchNewMissile(Direction d) {
        Missile miss = new Missile(d);
        if (d == Direction.RIGHT_TO_LEFT) {
            miss.setBottomRight(battleship.getLeftCannonPosition());
            if (Prefs.getSoundFX(getContext())) {
                leftSound.start();
            }
            leftPop = true;
        } else {
            miss.setBottomLeft(battleship.getRightCannonPosition());
            if (Prefs.getSoundFX(getContext())) {
                rightSound.start();
            }
            rightPop = true;
        }
        missiles.add(miss);
        horloge.subscribe(miss);
    }

    /**
     * cleans off object that go off screen and remove the form list and timer
     */
    private void cleanupOffscreenObjects() {
        //clean up depth charges that go off-screen
//        List<Sprite> doomed = new ArrayList<>();
//        for (DepthCharge dc : bombs) {
//            if (dc.getTop() > getHeight()) {
//                doomed.add(dc);
//            }
//        }
//        for (Sprite d : doomed) {
//            bombs.remove(d);
//            horloge.unsubscribe(d);
//        }
//        doomed.clear();

        List<DepthCharge> doome = bombs.stream().filter(bombs -> getTop() > getHeight()).collect(Collectors.toList());
        doome.forEach(bombs -> horloge.unsubscribe(bombs));
        bombs.removeIf(bombs -> bombs.getTop() > getHeight());

        List<Missile> doom = missiles.stream().filter(missiles -> getBottom() < 0).collect(Collectors.toList());
        doom.forEach(missiles -> horloge.unsubscribe(missiles));
        missiles.removeIf(missiles -> missiles.getBottom() < 0);


       //clean up missiles that go off-screen
//        for (Missile dc : missiles) {
//            if (dc.getBottom() < 0) {
//                doomed.add(dc);
//            }
//        }
//        for (Sprite d : doomed) {
//            missiles.remove(d);
//            horloge.unsubscribe(d);
//        }
    }

    /**
     * detect if objects overlap each other
     */
    private void detectCollisions() {
        for (Submarine s : subs) {
            for (DepthCharge d : bombs) {
                if (d.overlaps(s)) {
                    s.explode();
                    score += s.getPointValue();
                    if (Prefs.getSoundFX(getContext())) {
                        waterExplosion.start();
                    }
                    //hide the depth charge off-screen; it will get cleaned
                    //up at the next touch event.
                    d.setLocation(0,ImageCache.screenHeight());
                }
            }
        }

        for (Airplane p : planes) {
            for (Missile m : missiles) {
                if (p.overlaps(m)) {
                    p.explode();
                    score += p.getPointValue();
                    if (Prefs.getSoundFX(getContext())) {
                        airExplosion.start();
                    }
                    //hide the missile charge off-screen; it will get cleaned
                    //up at the next touch event.
                    m.setLocation(0,-ImageCache.screenHeight());
                }
            }
        }
    }

    /**
     * every tick of the gae, this happens
     */
    @Override
    public void tick() {
        timeNow = System.currentTimeMillis();
        if (timeNow-timeBefore > 1000) {
            countdown--;
            timeBefore = System.currentTimeMillis();
            if (countdown <= 0) {
                endgame();
            }
        }
        invalidate();
        detectCollisions();
        //cleanupOffscreenObjects();

        if (bombTick % 10 == 0 && visibleBombs()) {
            if (Prefs.getSoundFX(getContext())) {
                dcSound.start();
            }
        }
        bombTick++;
    }

    /**
     * check if th ebombs are on screen
     * @return
     */
    private boolean visibleBombs() {
        boolean result = false;
        for (DepthCharge d : bombs) {
            if (d.getTop() < ImageCache.screenHeight()) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * check if missiles are on screen
     * @return
     */
    private boolean visibleMissiles() {
        boolean result = false;
        for (Missile d : missiles) {
            if (d.getBottom() > 0) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * when the game ends,this method wil run
     */
    private void endgame() {
        horloge.pause();
        String message = "";
        int oldScore = 0;
        //attempt to load the old score
        final String scoreFile = "scores.txt";
        try (InputStream is = getContext().openFileInput(scoreFile);
             Scanner s = new Scanner(is)){
//            InputStream is = getContext().openFileInput(scoreFile);
//            Scanner s = new Scanner(is);
            oldScore = s.nextInt();
        } catch (FileNotFoundException e) {
            //do nothing, just use 0 as old score
        } catch (IOException e){
        }
        if (oldScore < score) {
            message = getResources().getString(R.string.congrats);
            //now, save the new score
            try (OutputStream os = getContext().openFileOutput(scoreFile, Context.MODE_PRIVATE)){
                os.write(("" + score).getBytes());
            } catch (IOException e) {
                //do nothing
            }
        } else {
            message = getResources().getString(R.string.consolation, oldScore);
        }


        message += " " + getResources().getString(R.string.play_again);

        //Now, prep the dialog box
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setTitle(R.string.game_over)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        horloge.resume();
                        restart();
                    }
                })
                .setNegativeButton(R.string.no, (dialog, which) -> çaSuffit());
        AlertDialog box = ab.create();
        box.show();
    }

    private void çaSuffit() {
        ((Activity)getContext()).finish();
    }

    /**
     * pause timer
     */
    public void gotoBackground() {
        if (horloge != null) {
            horloge.pause();
        }
    }

    /**
     * resume timer
     */
    public void gotoForeground() {
        if (horloge != null) {
            horloge.resume();
        }
    }
}
