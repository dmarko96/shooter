package com.example.shooter;

import com.example.shooter.consumable.Consumable;
import com.example.shooter.field.Field;
import com.example.shooter.field.FieldCircle;
import com.example.shooter.field.FieldHexagon;
import com.example.shooter.field.FieldRect;
import com.example.shooter.player.*;
import com.example.shooter.timer.MyTimer;
import com.example.shooter.timer.Updatable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.shooter.Constants.*;
import static com.example.shooter.Constants.ENEMY_RADIUS;

public class GameScene extends Scene {

    public GameScene(Parent parent,int playerCannon,int fieldType, int difficulty) {
        super(parent, WINDOW_WIDTH,WINDOW_HEIGHT);
        Group root= (Group) parent;
        MyTimer timer = new MyTimer ( );

        GameState currentGameState=new GameState();
        currentGameState.setState(GameState.State.GAME);
        int enemyLife=4+difficulty*2;

        Translate fieldPosition = new Translate ( WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2 );
        Field field = switch (fieldType) {
            case 0 -> new FieldCircle(FIELD_RADIUS, fieldPosition);
            case 1 -> new FieldRect(FIELD_RADIUS, fieldPosition);
            case 2 -> new FieldHexagon(FIELD_RADIUS, fieldPosition);
            default -> null;
        };
        //negative field bounds
        Rectangle negativeField=new Rectangle(WINDOW_WIDTH,WINDOW_HEIGHT);
        negativeField.setFill(Color.TRANSPARENT);

        //Clock display
        GameTime gameTime=new GameTime();

        //Player Health bar
        Rectangle playerHealthBar=new Rectangle(WINDOW_WIDTH,WINDOW_HEIGHT*0.02);
        playerHealthBar.setFill(Color.GREEN);
        //Player missile count
        List<Rectangle> playerMissiles=new ArrayList<>();
        for(int i=0;i<STRONG_ATTACK;i++){
            double rectWidth=WINDOW_WIDTH*0.02;
            double rectHeight=WINDOW_HEIGHT*0.1;
            Rectangle rect=new Rectangle(rectWidth,rectHeight);
            rect.setFill(Color.RED);
            rect.getTransforms().add(new Translate(WINDOW_WIDTH*0.99-rectWidth-i*rectWidth*2,WINDOW_HEIGHT*0.01));
            playerMissiles.add(rect);
        }
        //consumables list
        List<Consumable> consumables=new ArrayList<>();

        PlayerShield playerShield=new PlayerShield();
        timer.add(playerShield);
        Translate playerPosition = new Translate ( WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2 );
        Shape allowedArea=Shape.subtract(negativeField, (Shape)field.getChildren().get(0));
        // 0-hex, 1-circle, 2-rhombus
        Player player = switch (playerCannon) {
            case 0 ->
                    new PlayerHexagon(PLAYER_RADIUS, playerPosition, allowedArea, playerHealthBar, playerMissiles, consumables,playerShield);
            case 1 ->
                    new PlayerCircle(PLAYER_RADIUS, playerPosition, allowedArea, playerHealthBar, playerMissiles, consumables,playerShield);
            case 2 ->
                    new PlayerRhomb(PLAYER_RADIUS, playerPosition, allowedArea, playerHealthBar, playerMissiles, consumables,playerShield);
            default -> null;
        };
        PlayerMovement playerMovement=PlayerMovement.getInstance(player);

        List<Enemy>  enemies = new ArrayList<> ( );
        Enemy.setEnemies(enemies);
        for ( int i = 0; i < field.getEnemyNumber(); ++i ) {
            Enemy enemy = new Enemy ( ENEMY_RADIUS,enemyLife );

            enemy.getTransforms ( ).add(field.getEnemyTransform(i));
            enemy.setInitialGunVector();

            Updatable enemyUpdate= dns -> {

                Point2D playerPos=player.getCenterScene();
                Point2D enemyPos=enemy.getCenterScene();

                Point2D vector = new Point2D(
                        -enemyPos.getX() + playerPos.getX(),
                        -enemyPos.getY() + playerPos.getY()
                ).normalize();

                enemy.rotate(vector);
                return false;
            };
            timer.add(enemyUpdate);
            root.getChildren ( ).add ( enemy );
            enemies.add ( enemy );
        }


        //EnemyAttackLogic
        //diff=0 - easiest , diff=1 - medium, diff=2 - hardest
        GameLogic gameLogic =new GameLogic(player,enemies,root,timer,difficulty,Shape.subtract(negativeField,(Shape) field.getChildren().get(0)),consumables);
        //add ui elements
        root.getChildren().addAll(field, player,negativeField,playerHealthBar,gameTime);
        gameTime.getTransforms().add(new Translate(WINDOW_WIDTH/20,WINDOW_HEIGHT/20));
        playerHealthBar.getTransforms().add(new Translate(0,WINDOW_HEIGHT*0.98));
        for(Rectangle r:playerMissiles){
            root.getChildren().add(r);
        }
        //victory text



        //endgame check
        Updatable endGameCheck=dns -> {
            if(player.getLife()<=0 ||enemies.size()==0) {
                currentGameState.setState(GameState.State.END);
                Text endGameText=new Text("");
                endGameText.setFont(new Font(80));
                root.getChildren().add(endGameText);
                endGameText.getTransforms().add(new Translate(WINDOW_WIDTH/2-100,WINDOW_HEIGHT/2+10));

                if (player.getLife() <= 0) {
                    //defeat
                    endGameText.setText("Game Over");
                    System.out.println(endGameText.getX());
                }
                if (enemies.size() == 0) {
                    //victory
                    endGameText.setText("Victory");
                }
                timer.stop();
                gameLogic.stop();
                return true;
            }
            return false;
        };
        //add updatable then start timer
        timer.add(gameTime);
        timer.add(playerMovement);
        timer.add(endGameCheck);
        timer.start();
        gameLogic.start();

        super.addEventHandler ( KeyEvent.KEY_PRESSED, keyEvent -> {
            if(currentGameState.getState()== GameState.State.GAME) {
                int k = -1;
                switch (keyEvent.getCode()) {
                    case W: {
                        k = 0;
                        break;
                    }
                    case S: {
                        k = 1;
                        break;
                    }
                    case A: {
                        k = 2;
                        break;
                    }
                    case D: {
                        k = 3;
                        break;
                    }
                    case ESCAPE: {
                        System.exit(0);
                    }
                }
                if (k != -1) playerMovement.keyPressed(k);
            }
            else if(currentGameState.getState()== GameState.State.END){
                if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ESCAPE) {
                    System.exit(0);
                }
            }
        });

        super.addEventHandler( KeyEvent.KEY_RELEASED, keyEvent -> {
            if(currentGameState.getState()== GameState.State.GAME) {
                int k = -1;
                switch (keyEvent.getCode()) {
                    case W: {
                        k = 0;
                        break;
                    }
                    case S: {
                        k = 1;
                        break;
                    }
                    case A: {
                        k = 2;
                        break;
                    }
                    case D: {
                        k = 3;
                        break;
                    }
                }
                if (k != -1) playerMovement.keyReleased(k);
            }
        } );

        super.addEventHandler ( MouseEvent.ANY, mouseEvent -> {
            if(currentGameState.getState()== GameState.State.GAME) {
                Bullet bullet = player.handleMouseEvent(mouseEvent);
                if (bullet != null) {
                    root.getChildren().add(bullet);
                    Updatable updatable = dns -> {
                        boolean outOfBounds = bullet.update(
                                dns,
                                0,
                                WINDOW_WIDTH,
                                0,
                                WINDOW_HEIGHT
                        );
                        boolean collided = false;
                        if (outOfBounds) {
                            root.getChildren().remove(bullet);
                        } else {
                            for (int i = 0; i < enemies.size(); ++i) {
                                collided=enemies.get(i).handleBarrierCollision(bullet);
                                if(collided){
                                    root.getChildren().remove(bullet);
                                    break;
                                }
                                collided = enemies.get(i).handleCollision(bullet);
                                if (collided) {
                                    root.getChildren().remove(bullet);
                                    if (enemies.get(i).damage(bullet.getDamage())) {
                                        root.getChildren().remove(enemies.get(i));

                                        enemies.remove(i);
                                    }
                                    break;
                                }
                            }
                        }
                        return outOfBounds || collided;
                    };
                    timer.add(updatable);
                }
            }
        } );

    }
}
