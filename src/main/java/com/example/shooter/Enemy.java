package com.example.shooter;

import com.example.shooter.player.Player;
import com.example.shooter.timer.MyTimer;
import com.example.shooter.timer.Updatable;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.List;

import static com.example.shooter.Constants.*;
import static com.example.shooter.Constants.WINDOW_HEIGHT;

public class Enemy extends Group {


	private int life;
	private Circle body;
	private Polygon gun;

	private Rotate rotate;
	private Translate gunInertiaTranslate;

	private Point2D initialGunVector;
	private Barrier barrier;
	public static List<Enemy> enemies;
	public Enemy ( double radius ,int life) {
		this.life=life;
		initialGunVector=new Point2D(1,0);

		body = new Circle ( radius );
		body.setFill ( Color.RED );

		//gun
		double gunWidth= ENEMY_RADIUS/5;
		gun=new Polygon();
		gun.getPoints().addAll(
				0.0,0.0,
				gunWidth,0.0,
				gunWidth,2*radius-2*gunWidth,
				2.5*gunWidth,2*radius,
				-1.5*gunWidth,2*radius,
				0.0,2*radius-2*gunWidth
		);
		gun.setFill(Color.PURPLE);
		gun.setStroke(Color.BLACK);

		this.rotate=new Rotate();
		this.gunInertiaTranslate=new Translate();
		gun.getTransforms().addAll(this.rotate,new Translate(-gunWidth*0.5,0));
		gun.getTransforms().add(gunInertiaTranslate);


		//barrier
		double barrierWidth=WINDOW_WIDTH*0.015;
		double barrierHeight=WINDOW_HEIGHT*0.15;
		barrier=new Barrier(barrierWidth,barrierHeight);
		barrier.getTransforms().add(new Translate(-barrierWidth/2-70,-barrierHeight/2));

		super.getChildren ( ).addAll ( body,gun, barrier );
	}

	public static void setEnemies(List<Enemy> enemies){
		Enemy.enemies=enemies;
	}

	public boolean damage(int dmg){
		life= life>=dmg?life-dmg:0;
		body.setOpacity((double)life/4);
		gun.setOpacity((double)life/4);

		return life==0;
	}

	public boolean handleCollision ( Bullet bullet ) {
		Shape intersect=Shape.intersect(body,bullet);
		return intersect.getBoundsInLocal().getWidth()!=-1;
	}

	public boolean handleBarrierCollision(Bullet bullet){
		if(!barrier.isActive())return false;
		Shape intersect=Shape.intersect(bullet,barrier);
		return intersect.getBoundsInLocal().getWidth()!=-1;
	}

	public void rotate(Point2D vector){
		double angle=vector.angle(initialGunVector);
		rotate.setAngle(angle);
	}

	public void setInitialGunVector(){
		Bounds gunBounds=gun.localToScene(gun.getBoundsInLocal());
		Bounds bodyBounds=body.localToScene(body.getBoundsInLocal());

		initialGunVector= new Point2D(gunBounds.getCenterX()-bodyBounds.getCenterX(),gunBounds.getCenterY()-bodyBounds.getCenterY()).normalize();
	}

	public Point2D getCenterScene(){
		Bounds bodyBounds=body.localToScene(body.getBoundsInLocal());
		return new Point2D(bodyBounds.getCenterX(),bodyBounds.getCenterY());
	}

	private Bullet fireProjectile(Point2D speed){
		Point2D offset= speed.multiply(2.3*ENEMY_RADIUS);
		Bounds bodyBounds=body.localToScene(body.getBoundsInLocal());
		double x=bodyBounds.getCenterX()+offset.getX();
		double y=bodyBounds.getCenterY()+offset.getY();

		Translate pos=new Translate(x,y);

		return new Bullet( ENEMY_RADIUS*0.3,pos,speed.multiply(0.1),Color.BEIGE,1 );
	}

	public Updatable attack(Player player, Group root){
		Point2D playerPos=player.getCenterScene();
		Point2D enemyPos=this.getCenterScene();

		Point2D vector = new Point2D(
				-enemyPos.getX() + playerPos.getX(),
				-enemyPos.getY() + playerPos.getY()
		).normalize();
		Bullet bul=this.fireProjectile(vector);
		root.getChildren().add(bul);
		Updatable updatable = dns -> {
			Boolean outOfBounds = bul.update(dns, 0, WINDOW_WIDTH, 0, WINDOW_HEIGHT);
			Boolean collided=false;
			if (outOfBounds) {
				root.getChildren().remove(bul);
				return true;
			}
			else if(bul.getEnemyBullet()) {
					collided = player.handleShieldCollision(bul);
					if (collided) {
						//collided with shield, change direction
//						bul.toBack();
//						root.getChildren().remove(bul);
						bul.changeToPlayerBullet(player.getNormalVector(bul.getPosition()));


					} else {
						collided = player.handleCollision(bul);
						if (collided) {
							bul.toBack();
							root.getChildren().remove(bul);
							player.damageLife(1);
						}
						return collided;
					}
//				return outOfBounds || collided;
			}
			else {
				for (int i = 0; i < enemies.size(); ++i) {
					collided=enemies.get(i).handleBarrierCollision(bul);
					if(collided){
						root.getChildren().remove(bul);
						break;
					}
					collided = enemies.get(i).handleCollision(bul);
					if (collided) {
						root.getChildren().remove(bul);
						if (enemies.get(i).damage(bul.getDamage())) {
							root.getChildren().remove(enemies.get(i));

							enemies.remove(i);
						}
						break;
					}
				}
				return collided;
			}
			return false;
		};
		Timeline gunInertia=new Timeline(
				new KeyFrame(
						Duration.millis(500),
						new KeyValue(gunInertiaTranslate.yProperty(),-10)
				),
				new KeyFrame(
						Duration.millis(1000),
						new KeyValue(gunInertiaTranslate.yProperty(),0)
				)
		);
		gunInertia.setAutoReverse(false);
		gunInertia.setCycleCount(1);
		gunInertia.play();
		return updatable;
	}

	public void setBarrierState(boolean state){
		barrier.setActive(state);
	}
	public void changeBarrierState(){
		barrier.changeBarrierState();
	}

}
