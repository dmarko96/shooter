package com.example.shooter.consumable;

import com.example.shooter.player.Player;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Translate;

public class ConsumableShield extends Consumable{
    public ConsumableShield(double radius, Translate position) {
        super(radius, position);
        double hexHeight=radius*Math.sqrt(3)/2;
        Polygon shield=new Polygon();
        shield.getPoints().addAll(
                -radius/2,-hexHeight,
                radius/2,-hexHeight,
                radius,0.0,
                radius/2,hexHeight,
                -radius/2,hexHeight,
                -radius,0.0
        );
        shield.setFill(Color.LIGHTBLUE);
        super.getChildren().add(shield);
    }
    @Override
    public void handleConsumable(Player player) {
        player.setShield(true);
    }
}
