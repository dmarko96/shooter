package com.example.shooter.field;

import com.example.shooter.Main;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import static com.example.shooter.Constants.*;

public class FieldRect extends Field{
    public FieldRect(double radius, Translate position) {
        super(position);

        radius=radius*1.8;
        Rectangle field = new Rectangle(radius,radius);
        field.getTransforms().add(new Translate(-radius*0.5,-radius*0.5));
        super.getChildren().add(field);

        Image image = new Image(Main.class.getResourceAsStream("grass.jpg"));
        ImagePattern imagePattern = new ImagePattern(image);
        field.setFill(imagePattern);

        for (int i = 0; i < enemyNumber; i++) {
            Affine enemyAffine = new Affine();
            enemyAffine.append(new Translate(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2));
            enemyAffine.append(new Rotate(360. / enemyNumber * i + 45));
            enemyAffine.append(new Translate(enemyDistance*1.2, 0));
            enemyTransform.add(enemyAffine);
        }
    }
}
