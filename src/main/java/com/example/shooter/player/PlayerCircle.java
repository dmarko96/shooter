package com.example.shooter.player;

import com.example.shooter.PlayerShield;
import com.example.shooter.consumable.Consumable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Translate;

import java.util.List;

public class PlayerCircle extends Player{
    public PlayerCircle(double radius, Translate position, Shape field, Rectangle healthBar, List<Rectangle> missiles, List<Consumable> consumables, PlayerShield playerShield) {
        super(radius*1.2, position, field, healthBar, missiles,consumables,playerShield);

        radius=radius*1.2;
        Circle bodyCircle=new Circle(radius);
        bodyCircle.setFill(Color.ROSYBROWN);
        bodyCircle.setStroke(Color.BLACK);
        body.getChildren().add(bodyCircle);
        maxLife=8;
        life=8;
        playerSpeed=1.5;
    }
}
