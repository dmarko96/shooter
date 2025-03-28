package com.example.shooter;

import com.example.shooter.consumable.Consumable;
import com.example.shooter.consumable.ConsumableCoin;
import com.example.shooter.consumable.ConsumableHeart;
import com.example.shooter.consumable.ConsumableShield;
import com.example.shooter.player.Player;
import com.example.shooter.timer.MyTimer;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Translate;

import java.util.List;

import static com.example.shooter.Constants.*;

public class GameLogic extends AnimationTimer {

    private Player player;
    private List<Enemy> enemies;
    private Group root;

    private long timeToAttack;
    private long last;
    private int difficulty;
    private MyTimer bulletTimer;

    private Shape negativeField;
    private long lastConsumable;
    private long timeToConsumable;

    // difficulty=0 easiest, difficulty 1=medium, difficulty 2=hard
    // first number - random delay, second - minimum time between attacks
    private static final int [][]difficultyScale={ {5,2}, {3,2}, {2,0}};

    private long [][]enemiesBarrier;

    private List<Consumable> consumables;

    private final double consumableChance[]={COIN_CHANCE,HEART_CHANCE,SHIELD_CHANCE};
    public GameLogic(Player player, List<Enemy> enemies, Group root, MyTimer bulletTimer, int difficulty, Shape negativeField, List<Consumable> consumables) {
        this.player = player;
        this.enemies = enemies;
        this.root = root;
        this.timeToAttack=1000;
        this.difficulty=difficulty;
        this.bulletTimer=bulletTimer;
        this.negativeField=negativeField;
        this.consumables=consumables;
        this.timeToConsumable=5000;
        enemiesBarrier=new long[6][6];
    }

    @Override
    public void handle(long now) {
        handleAttacking(now);
        handleConsumables(now);
        handleBarrier(now);
    }

    public void handleAttacking(long now){
        if(enemies.size()==0)return;
        if(last==0){
            last=now;
        }
        if( (now-last)/1000000 >=timeToAttack){
            int idEnemyAttacking = (int)(Math.random()*enemies.size());
            bulletTimer.add(enemies.get(idEnemyAttacking).attack(player,root));
            timeToAttack= (long)(Math.random()*difficultyScale[difficulty][0]) + difficultyScale[difficulty][1];
            timeToAttack*=1000;
            last=now;
        }
    }

    public void handleConsumables(long now){
        if(lastConsumable==0){
            lastConsumable=now;
        }
        if( (now-lastConsumable)/1000000>=timeToConsumable ) {
            double consumableType=Math.random();
            Translate consumablePosition=getRandomPosition();
            double consumableRadius=CONSUMABLE_RADIUS;
            Consumable consumable;
            if(consumableType<=consumableChance[0]) {
                consumable = new ConsumableCoin(consumableRadius, consumablePosition);
            } else if (consumableType<=consumableChance[1]+consumableChance[0]) {
                consumable=new ConsumableHeart(consumableRadius,consumablePosition);
            }else{
                consumable=new ConsumableShield(consumableRadius,consumablePosition);
            }

            root.getChildren().add(consumable);
            consumables.add(consumable);
            timeToConsumable=(int)(Math.random()*5)+5;
            timeToConsumable*=1000;
            lastConsumable=now;
        }
    }

    public void handleBarrier(long now){
        for (int i = 0; i < enemies.size(); i++) {
            if(enemiesBarrier[i][0]==0){
                enemiesBarrier[i][0]=now;
            }
            if((now-enemiesBarrier[i][0])/1000000>=enemiesBarrier[i][1]){
                enemies.get(i).changeBarrierState();
                enemiesBarrier[i][0]=now;
                long t=(int)(Math.random()*3)+2;
                t*=1000;
                enemiesBarrier[i][1]=t;
            }
        }
    }
    public Translate getRandomPosition(){
        Translate res=null;
        Circle boundsTestCircle=new Circle(CONSUMABLE_RADIUS*2);
        Translate testTranslate = new Translate(0.0,0.0);
        boundsTestCircle.getTransforms().add(testTranslate);
        root.getChildren().add(boundsTestCircle);
        boundsTestCircle.setFill(Color.TRANSPARENT);
        boolean isValid=false;

        while(!isValid) {
            double testX = Math.random() * WINDOW_WIDTH*0.7+WINDOW_WIDTH*0.15;
            double testY = Math.random() * WINDOW_HEIGHT*0.7+WINDOW_HEIGHT*0.15;
            testTranslate.setX(testX);
            testTranslate.setY(testY);
            boundsTestCircle.getTransforms().setAll(testTranslate);
            Shape intersect = Shape.intersect(boundsTestCircle, Shape.union(negativeField,player.getShape() ));
            if (intersect.getBoundsInLocal().getWidth() == -1) {
                isValid = true;
                res=testTranslate;
                root.getChildren().remove(boundsTestCircle);
            }
        }
        return res;
    }

}
