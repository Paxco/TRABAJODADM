package com.example.pasmarla.pruebainterfazdadm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class pong extends AppCompatActivity {

    PongView pongView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //setContentView(R.layout.activity_juego_pong);
        setContentView(R.layout.activity_pong);

        pongView = new PongView(this);

        setContentView(pongView);





    }

    class PongView extends SurfaceView implements Runnable {




        // This is our thread
        Thread gameThread = null;

        // This is new. We need a SurfaceHolder
        // When we use Paint and Canvas in a thread
        // We will see it in action in the draw method soon.
        SurfaceHolder ourHolder;

        // A boolean which we will set and unset
        // when the game is running- or not.
        volatile boolean playing;

        // Game is paused at the start
        boolean paused = true;

        // A Canvas and a Paint object
        Canvas canvas;
        Paint paint;

        // This variable tracks the game frame rate
        long fps;

        // This is used to help calculate the fps
        private long timeThisFrame;

        // The size of the screen in pixels
        int screenX;
        int screenY;

        // The players paddle
        Jugador1 j1;
        Jugador2 j2;


        // A Ball
        Ball Ball;



        // For sound FX
        SoundPool soundPool;
        int beep1ID = -1;
        int beep2ID = -1;
        int beep3ID = -1;
        int loseLifeID = -1;
        int explodeID = -1;

        // The score
        int score = 0;

        // Lives

        //int lives =;



        // When the we initialize (call new()) on gameView
        // This special constructor method runs
        public PongView(Context context) {
            // The next line of code asks the
            // SurfaceView class to set up our object.
            // How kind.
            super(context);

            // Initialize ourHolder and paint objects
            ourHolder = getHolder();
            paint = new Paint();

            // Get a Display object to access screen details
            Display display = getWindowManager().getDefaultDisplay();
            // Load the resolution into a Point object
            Point size = new Point();
            display.getSize(size);

            screenX = size.x;
            screenY = size.y;

            j1 = new Jugador1(screenX, screenY);
            j2=  new Jugador2(screenX, screenY);

            // Create a Ball
            Ball = new Ball(screenX, screenY);

            // Load the sounds

            // This SoundPool is deprecated but don't worry
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

            try{
                // Create objects of the 2 required classes
                AssetManager assetManager = context.getAssets();
                AssetFileDescriptor descriptor;

                // Load our fx in memory ready for use
                descriptor = assetManager.openFd("beep1.ogg");
                beep1ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("beep2.ogg");
                beep2ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("beep3.ogg");
                beep3ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("loseLife.ogg");
                loseLifeID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("explode.ogg");
                explodeID = soundPool.load(descriptor, 0);

            }catch(IOException e){
                // Print an error message to the console
                Log.e("error", "failed to load sound files");
            }

           Restart();

        }

        public void Restart(){

            // Reiniciar la pelota
            Ball.reset(screenX, screenY);

                 }
        @Override
        public void run() {
            while (playing) {
                // Capture the current time in milliseconds in startFrameTime
                long startFrameTime = System.currentTimeMillis();



                // Update the frame

                if(!paused){
                    update();                 }
                // Draw the frame
                draw();
                // Calculate the fps this frame
                // We can then use the result to
                // time animations and more.
                timeThisFrame = System.currentTimeMillis() - startFrameTime;                 if (timeThisFrame >= 1) {
                    fps = 1000 / timeThisFrame;
                }

            }

        }

        // Everything that needs to be updated goes in here
        // Movement, collision detection etc.
        public void update() {

            // Move the paddle if required
            j1.update(fps);
            j2.update(fps);

            Ball.update(fps);

            if(!(RectF.intersects(j1.getRect(), Ball.getRect())||RectF.intersects(j2.getRect(), Ball.getRect()))) {

                 if (Ball.devuelve() > j2.posicion()) {
                    j2.setMovementState(j2.UP);
                } else {
                    j2.setMovementState(j2.DOWN);
                }}


// Check for Ball colliding with paddle

                if(RectF.intersects(j1.getRect(),Ball.getRect())) {
                    Log.d("toco", "paddle");
                    Ball.reverseXVelocity();
                    Ball.reverseYVelocity();
                    Ball.clearObstacleY(j1.getRect().top-2);

                    soundPool.play(beep1ID, 1, 1, 0, 0, 1);             }


                if(RectF.intersects(j2.getRect(),Ball.getRect())) {
                    Log.d("toco", "paddle2");
                    Ball.reverseXVelocity();
                    Ball.reverseYVelocity();
                    Ball.clearObstacleY(j2.getRect().top-2);

                    soundPool.play(beep1ID, 1, 1, 0, 0, 1);             }
                // Bounce the ball back when it hits the bottom of screen

                if(Ball.getRect().bottom > screenY){
                    Ball.reverseYVelocity();
                    Ball.clearObstacleY(screenY - 2);

                    // Lose a life

                    soundPool.play(loseLifeID, 1, 1, 0, 0, 1);


                }

                // Bounce the ball back when it hits the top of screen
                if(Ball.getRect().top < 0){
                    Ball.reverseYVelocity();
                    Ball.clearObstacleY(12);

                    soundPool.play(beep2ID, 1, 1, 0, 0, 1);
                }

                // If the ball hits left wall bounce
                if(Ball.getRect().left < 0){
                    Ball.reverseXVelocity();

                    Ball.clearObstacleX(2);
                    soundPool.play(beep3ID, 1, 1, 0, 0, 1);
                }
                // If the ball hits right wall bounce
                if(Ball.getRect().right > screenX - 10){
                    Ball.reverseXVelocity();
                    Ball.clearObstacleX(screenX - 22);

                    soundPool.play(beep3ID, 1, 1, 0, 0, 1);
                }

                // Pause if cleared screen
                if(score == 50 * 10){
                    paused = true;
                    Restart();
                }



        }


            // Draw the newly updated scene
        public void draw() {

            // Make sure our drawing surface is valid or we crash
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();

                // Draw the background color
                canvas.drawColor(Color.argb(255, 26, 128, 182));

                // Choose the brush color for drawing
                paint.setColor(Color.argb(255, 255, 255, 255));

                // Draw the paddle
                canvas.drawRect(j1.getRect(), paint);
                canvas.drawRect(j2.getRect(), paint);


                // Draw the Ball
                canvas.drawRect(Ball.getRect(), paint);

                // Change the brush color for drawing
                paint.setColor(Color.argb(255,  249, 129, 0));


                // Draw the score
                paint.setTextSize(40);
                //canvas.drawText("Score: " + score + "   Lives: " + lives, 10,50, paint);

                // Has the player cleared the screen?
               /* if(score == numBricks * 10){
                    paint.setTextSize(90);
                    canvas.drawText("YOU HAVE WON!", 10,screenY/2, paint);

                    Intent mi_intent = new Intent(Juego_pong.this, introduce_record.class);


                    finish();
                    startActivity(mi_intent);
                }

                // Has the player lost?
                if(lives<=0){
                    paint.setTextSize(90);
                    canvas.drawText("YOU HAVE LOST!", 10,screenY/2, paint);
                    //ESCRITURA
                    final SharedPreferences preferencias = getSharedPreferences("scr",MODE_PRIVATE);

                    SharedPreferences.Editor editor=preferencias.edit();
                    editor.putInt("id3", score);
                    editor.commit();

                    Intent mi_intent = new Intent(Juego_pong.this, introduce_record.class);

                    finish();
                    startActivity(mi_intent);
                }*/
                // Draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);             }         }
        // If SimpleGameEngine Activity is paused/stopped
        // shutdown our thread.
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining  thread");
            }         }
        // If SimpleGameEngine Activity is started theb
        // start our thread.
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }


        // The SurfaceView class implements onTouchListener
        // So we can override this method and detect screen touches.
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:
                    paused = false;


                    if(motionEvent.getY() > j1.posicion()){

                         j1.setMovementState(j1.UP);
                    }

                    else{
                        if(motionEvent.getY() == j1.posicion()){j1.setMovementState(j1.STOPPED);}

                        else j1.setMovementState(j1.DOWN);

                    }

                    break;

                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:


                    j1.setMovementState(j1.STOPPED);
                    j2.setMovementState(j2.STOPPED);
                    break;
            }


            return true;
        }

    }
    // This is the end of our BreakoutView inner class

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        pongView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        pongView.pause();
    }














}
