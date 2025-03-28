package com.example.shooter.player;

import com.example.shooter.timer.Updatable;

public class PlayerMovement implements Updatable {

    private final int[] stepDir;

    private static PlayerMovement instance;
    private final Player player;

    private PlayerMovement(){
        stepDir=new int[4];
        player=null;
    }
    private PlayerMovement(Player player){
        stepDir=new int[4];
        this.player=player;
    }

    public static PlayerMovement getInstance(Player player){
        if(instance==null){
            instance=new PlayerMovement(player);
        }
        return instance;
    }

    // up=0, down=1, left=2,right=3
    public void keyPressed(int num){
        stepDir[num]=1;
    }
    public void keyReleased(int num){
        stepDir[num]=0;
    }

    @Override
    public boolean update(long dns) {

        int dirY= -stepDir[0]+stepDir[1];
        int dirX=-stepDir[2]+stepDir[3];

        long avgFrameLength=1000000000/60;
        double frameMod=(double)dns/avgFrameLength;

        double playerSpeed= player.getPlayerSpeed()*frameMod;
        if(dirY!=0 && dirX!=0){
            playerSpeed=playerSpeed/Math.sqrt(2);
        }

        double stepX=dirX*playerSpeed;
        double stepY=dirY*playerSpeed;
        player.move(stepX,stepY);

        return false;
    }
}
