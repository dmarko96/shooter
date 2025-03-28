package com.example.shooter;

import com.example.shooter.timer.Updatable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static com.example.shooter.Constants.PLAYER_RADIUS;

public class PlayerShield extends Circle implements Updatable {

    private long timeLeft=0;
    private boolean active;
    public PlayerShield(){
        super(PLAYER_RADIUS*2);
        super.setFill(Color.BLUE);
        super.setOpacity(0.5);
        active=false;
    }

    public void setActive(boolean a){
        active=a;
        if(active){
            super.setFill(Color.BLUE);
            super.setOpacity(0.5);
            timeLeft+=(long)5*1000000000;
        }
        else super.setFill(Color.TRANSPARENT);
    }
    public boolean getActive(){
        return active;
    }

    @Override
    public boolean update(long dns) {
        if(active) {
            timeLeft -= dns;
            if (timeLeft <= 0) {
                setActive(false);
            }
        }
        return false;
    }
}
