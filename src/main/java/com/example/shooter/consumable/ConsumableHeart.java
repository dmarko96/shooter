package com.example.shooter.consumable;

import com.example.shooter.player.Player;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Translate;

public class ConsumableHeart extends Consumable{
    public ConsumableHeart(double radius, Translate position) {
        super(radius, position);
        Polygon heart=new Polygon();
        heart.getPoints().addAll(
                0.0,-radius*0.5,
                0.25*radius,-1*radius,
                0.75*radius,-1*radius,
                1.0*radius,-0.25*radius,
                0.0,radius,
                -1.0*radius,-0.25*radius,
                -0.75*radius,-1*radius,
                -0.25*radius,-1*radius
        );
        heart.setFill(Color.RED);
        super.getChildren().add(heart);
    }

    @Override
    public void handleConsumable(Player player) {
        player.damageLife(-1);
    }
}
