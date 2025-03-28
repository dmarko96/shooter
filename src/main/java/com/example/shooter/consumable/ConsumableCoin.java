package com.example.shooter.consumable;

import com.example.shooter.player.Player;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

public class ConsumableCoin extends Consumable{
    public ConsumableCoin(double radius, Translate position) {
        super(radius, position);
        Circle coin=new Circle(radius);
        coin.setFill(Color.GOLD);
        super.getChildren().add(coin);
    }

    @Override
    public void handleConsumable(Player player) {
        player.addMissile();
    }
}
