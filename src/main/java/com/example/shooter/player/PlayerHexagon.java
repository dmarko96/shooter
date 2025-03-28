package com.example.shooter.player;

import com.example.shooter.PlayerShield;
import com.example.shooter.consumable.Consumable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Translate;

import java.util.List;

public class PlayerHexagon extends Player{
    public PlayerHexagon(double radius, Translate position, Shape field, Rectangle healthBar, List<Rectangle> missiles, List<Consumable> consumables, PlayerShield playerShield) {
        super(radius, position, field, healthBar, missiles,consumables,playerShield);
        Polygon bodyHex = new Polygon();
		double hexHeight=radius*Math.sqrt(3)/2;
		bodyHex.getPoints().addAll(
				-radius/2,-hexHeight,
				radius/2,-hexHeight,
				radius,0.0,
				radius/2,hexHeight,
				-radius/2,hexHeight,
				-radius,0.0
		);
        body.getChildren().add(bodyHex);
		bodyHex.setFill(Color.YELLOW);
		bodyHex.setStroke(Color.BLACK);

    }
}
