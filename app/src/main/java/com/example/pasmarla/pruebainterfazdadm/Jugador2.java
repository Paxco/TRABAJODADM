package com.example.pasmarla.pruebainterfazdadm;

import android.graphics.RectF;

/**
 * Created by Paxco on 24/03/2016.
 */
public class Jugador2 {
    // RectF is an object that holds four coordinates - just what we need
    private RectF rect;

    // How long and high our j1 will be
    private float height;
    private float length;

    // X is the far left of the rectangle which forms our j1
    private float x;

    // Y is the top coordinate
    private float y;
    private float ymax;

    // This will hold the pixels per second speedthat the j1 will move
    private float j1Speed;

    // Which ways can the j1 move
    public final int STOPPED = 0;
    public final int UP = 1;
    public final int DOWN = 2;

    // Is the j1 moving and in which direction
    private int j1Moving = STOPPED;

    // This the the constructor method
    // When we create an object from this class we will pass
    // in the screen width and height
    public Jugador2(int screenX, int screenY){
        // 130 pixels wide and 20 pixels high
        length = 20;
        height = 130;

        // Start j1 in roughly the sceen centre
        y = screenY / 2;
        x = screenX*9/10;
        ymax=screenY;

        rect = new RectF(x, y, x + length, y + height);

        // How fast is the j1 in pixels per second
        j1Speed = 350;
    }

    // This is a getter method to make the rectangle that
    // defines our j1 available in BreakoutView class
    public RectF getRect(){
        return rect;
    }

    // This method will be used to change/set if the j1 is going left, right or nowhere
    public void setMovementState(int state){
        j1Moving = state;
    }

    // This update method will be called from update in BreakoutView
    // It determines if the j1 needs to move and changes the coordinates
    // contained in rect if necessary
    public void update(long fps){
        if(j1Moving == UP){
            if(y>=ymax*9/10){;}

            else y = y + j1Speed / fps;

        }

        if(j1Moving == DOWN){
            if(y<=ymax/15){}


            else y = y - j1Speed / fps;

        }

        rect.top = y;
        rect.bottom = y + height;
    }
    // envia referencia
    public float posicion(){return y;}

}
