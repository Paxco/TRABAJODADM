package com.example.pasmarla.pruebainterfazdadm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private View mContentView;
    private View mControlsView;
    private boolean mVisible;
    private Button back;
    private Button play;
    private Button score;
    private String s_play;
    private String s_score;
    private String s_back;
    private TextView dif;
    private String dif_s;
    private String ez_s;
    private String norm_s;
    private String hard_s;
    private RadioButton ez;
    private RadioButton norm;
    private RadioButton hard;
    private int position;
    private int position2;
    private RadioGroup Radiogp;
    private int diff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        //Escritura lectura fichero preferencias.
        final SharedPreferences preferencias = getSharedPreferences("idioma",MODE_PRIVATE);



        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();

            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.back).setOnTouchListener(mDelayHideTouchListener);
        back= (Button) findViewById(R.id.back);
        play= (Button) findViewById(R.id.play);
        score= (Button) findViewById(R.id.score);
        dif=(TextView)findViewById(R.id.texto);
        ez=(RadioButton)findViewById(R.id.ez);
        norm=(RadioButton)findViewById(R.id.norm);
        hard=(RadioButton)findViewById(R.id.hard);


        // IDIOMAS

        final int idiom=preferencias.getInt("id",position);
        if(idiom==0){
            s_play="Jugar";
            s_score="Puntuaciones";
            s_back="Volver";
            dif_s="Selecciona dificultad:";
            ez_s="Fácil";
            norm_s="Normal";
            hard_s="Difícil";
            }
        if(idiom==1){
            s_play="かいし";
            s_score="とくてん";
            s_back="かえる";
            dif_s= "めんどうをえらぶ:";
            ez_s="やしゃし";
            norm_s="へいじょう";
            hard_s="むずかし";}
        if(idiom==2){
            s_play="Start";
            s_score="High-Scores";
            s_back="Back";
            dif_s="Select difficulty:";
            ez_s="Easy peasy";
            norm_s="Git gud";
            hard_s="Hardcore";}

        play.setText(s_play);
        score.setText(s_score);
        back.setText(s_back);
        dif.setText(dif_s);
        ez.setText(ez_s);
        norm.setText(norm_s);
        hard.setText(hard_s);



        position2=3;

        Radiogp=(RadioGroup)findViewById(R.id.Radiogp);
        Radiogp.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        Log.d("Dwe","Elegiste:"+Integer.toString(checkedId));
                        if(ez.isChecked()) {
                            position2=5;
                            Log.d("id", "facil");


                        }
                        if(norm.isChecked()) {
                            position2=3;
                            Log.d("id", "medio");


                        }
                        if(hard.isChecked()) {
                            position2=1;
                            Log.d("id", "dificil");

                            }





                    }
                });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mi_intent = new Intent(FullscreenActivity.this, MainActivity.class);


                finish();
                startActivity(mi_intent);

            }

        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mi_intent = new Intent(FullscreenActivity.this, Juego_breakout.class);
                //ESCRITURA
                final SharedPreferences preferencias = getSharedPreferences("diff",MODE_PRIVATE);

                SharedPreferences.Editor editor=preferencias.edit();
                editor.putInt("id2",position2 );
                editor.commit();

                finish();
                startActivity(mi_intent);

            }

        });

        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mi_intent = new Intent(FullscreenActivity.this, record.class);




                finish();
                startActivity(mi_intent);

            }

        });



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
