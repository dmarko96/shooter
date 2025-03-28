package com.example.shooter;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Barrier extends Rectangle {
    private boolean active;
    public Barrier(double x,double y){
        super(x,y);
        active=true;
        super.setFill(Color.BLUE);
    }

    public boolean isActive(){
        return active;
    }
    public void setActive(boolean state){
        if(state){
            super.setFill(Color.BLUE);
        }
        else super.setFill(Color.TRANSPARENT);
        active=state;
    }
    public void changeBarrierState(){
        if(active){
            active=false;
            super.setFill(Color.TRANSPARENT);
        }
        else {
            active=true;
            super.setFill(Color.BLUE);
        }
    }
}
