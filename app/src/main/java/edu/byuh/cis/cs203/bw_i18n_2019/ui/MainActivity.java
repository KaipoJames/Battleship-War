package edu.byuh.cis.cs203.bw_i18n_2019.ui;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {

    private GameView gv;

    /**
     * Set up the Activity and load the GameView
     * @param savedInstanceState required by API, we just pass it to super-constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gv = new GameView(this);
        setContentView(gv);
    }

    /**
     * pause timer
     */
    @Override
    public void onPause() {
        super.onPause();
        gv.gotoBackground();
    }

    /**
     * resume timer
     */
    @Override
    public void onResume() {
        super.onResume();
        gv.gotoForeground();
    }
}
