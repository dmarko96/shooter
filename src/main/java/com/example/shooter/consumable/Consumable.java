package com.example.shooter.consumable;

import com.example.shooter.player.Player;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Translate;

public abstract class Consumable extends Group {

    private final Circle collisionCircle;
    public Consumable(double radius, Translate position){
        super.getTransforms().add(position);

        collisionCircle=new Circle(radius*0.9);
        collisionCircle.setFill(Color.TRANSPARENT);
        super.getChildren().add(collisionCircle);
    }


    public boolean handleCollision(Player player){
        Shape intersect=Shape.intersect(collisionCircle,player.getShape());
        if(intersect.getBoundsInLocal().getWidth()!=-1){
            handleConsumable(player);
            return true;
        }
        return false;
    }

    public abstract void handleConsumable(Player player);


}
