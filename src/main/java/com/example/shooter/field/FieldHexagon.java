package com.example.shooter.field;

import com.example.shooter.Main;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import static com.example.shooter.Constants.*;

public class FieldHexagon extends Field{
    public FieldHexagon(double radius, Translate position) {
        super(position);
        radius=radius*1.2;
        double hexHeight=radius*Math.sqrt(3)/2;

        Polygon field=new Polygon();
        field.getPoints().addAll(
                -radius/2,-hexHeight,
                radius/2,-hexHeight,
                radius,0.0,
                radius/2,hexHeight,
                -radius/2,hexHeight,
                -radius,0.0
        );
        enemyNumber=6;
        super.getChildren().add(field);
        Image image = new Image(Main.class.getResourceAsStream("grass.jpg"));
        ImagePattern imagePattern = new ImagePattern(image);
        field.setFill(imagePattern);
        for (int i = 0; i < enemyNumber; i++) {
            Affine enemyAffine = new Affine();
            enemyAffine.append(new Translate(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2));
            enemyAffine.append(new Rotate(360. / enemyNumber * i+30));
            enemyAffine.append(new Translate(enemyDistance, 0));
            enemyTransform.add(enemyAffine);
        }


    }
}
