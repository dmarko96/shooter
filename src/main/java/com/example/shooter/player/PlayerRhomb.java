package com.example.shooter.player;

import com.example.shooter.PlayerShield;
import com.example.shooter.consumable.Consumable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Translate;

import java.util.List;

public class PlayerRhomb extends Player{
    public PlayerRhomb(double radius, Translate position, Shape field, Rectangle healthBar, List<Rectangle> missiles, List<Consumable> consumables, PlayerShield playerShield) {
        super(radius*0.7, position, field, healthBar, missiles,consumables,playerShield);
        double hexHeight=radius*Math.sqrt(3)/2;
        Polygon bodyPolygon=new Polygon();
        double scaledRadius=radius*0.7;
        bodyPolygon.getPoints().addAll(
                0.0,-scaledRadius,
                scaledRadius,0.0,
                0.0,scaledRadius,
                -scaledRadius,0.0
        );
        bodyPolygon.setFill(Color.AZURE);
        bodyPolygon.setStroke(Color.BLACK);
        body.getChildren().add(bodyPolygon);
        maxLife=2;
        life=2;
        playerSpeed=4;
    }
}
