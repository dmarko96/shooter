package com.example.shooter;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

import static com.example.shooter.Constants.*;

public class Bullet extends Circle {
	private Translate position;
	private Point2D speed;
	private Color color;
	private int damage=1;
	private boolean enemyBullet=true;
	public Bullet ( double radius, Translate position, Point2D speed , Color color,int damage) {
		super ( radius, color);
		
		this.position = position;
		this.speed    = speed;
		this.damage=damage;
		
		super.getTransforms ( ).add ( this.position );
	}

	public boolean update ( long dns, double left, double right, double up, double down ) {
		double newX = this.position.getX ( ) + this.speed.getX ( ) * BULLET_SPEED;
		double newY = this.position.getY ( ) + this.speed.getY ( ) * BULLET_SPEED;
		
		this.position.setX ( newX );
		this.position.setY ( newY );
		
		double radius = super.getRadius ( );
		
		boolean isXOutOfBounds = newX <= ( left - radius ) || newX >= ( right + radius );
		boolean isYOutOfBounds = newY <= ( up - radius ) || newY >= ( down + radius );
		
		return isXOutOfBounds && isYOutOfBounds;
	}

	public int getDamage(){
		return damage;
	}

	public void changeToPlayerBullet(Point2D normal){
		super.setFill(Color.ORANGE);
		this.speed= speed.subtract(normal.multiply(speed.dotProduct(normal)*2));
		enemyBullet=false;
	}
	public boolean getEnemyBullet(){
		return enemyBullet;
	}

	public Point2D getPosition(){
		Bounds b=super.localToScene(super.getBoundsInLocal());
		return new Point2D(b.getCenterX(),b.getCenterY());
	}
}
