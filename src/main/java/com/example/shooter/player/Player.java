package com.example.shooter.player;

import com.example.shooter.Bullet;
import com.example.shooter.PlayerShield;
import com.example.shooter.consumable.Consumable;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.List;

import static com.example.shooter.Constants.*;

public abstract class Player extends Group {
	private final double radius;
	private double mouseX;
	private double mouseY;
	private final Translate position;
	private final Rotate rotate;
	private final Shape field;
	private short strongProjectiles=4;
	private final Rectangle healthBar;
	private final List<Rectangle> missiles;
	private final Circle boundsTestCircle;
	private final List<Consumable> consumables;
	private final PlayerShield shield;
	protected Group body;
	protected int life=PLAYER_LIFE;
	protected int maxLife=PLAYER_LIFE;
	protected double playerSpeed=PLAYER_STEP;


	public Player (double radius, Translate position, Shape field, Rectangle healthBar, List<Rectangle> missiles,List<Consumable> consumables,PlayerShield pShield) {
		this.radius   = radius;
		this.position = position;
		this.field=field;
		this.healthBar=healthBar;
		this.missiles=missiles;
		this.consumables=consumables;


		body=new Group();
		
		this.rotate = new Rotate ( );
		final double gunWidth = 0.3 * radius;
		final double gunHeight = 2 * radius;
		Rectangle gun = new Rectangle ( gunWidth, gunHeight );
		gun.setFill ( Color.PURPLE );
		gun.getTransforms ( ).addAll (
				this.rotate,
				new Translate ( -gunWidth / 2, 0 )
		);

		//bounds test circle
		boundsTestCircle=new Circle(radius);
		boundsTestCircle.setFill(Color.TRANSPARENT);

		//protective shield
		shield=pShield;
		shield.setActive(false);
		
		super.getChildren ( ).addAll (body, gun ,boundsTestCircle,shield);
		
		super.getTransforms ( ).add ( position );
	}
	
	private void updateGun ( ) {
		Point2D vector = new Point2D (
				this.mouseX - this.position.getX ( ),
				this.mouseY - this.position.getY ( )
		).normalize ( );
		
		double angle = -Math.signum ( vector.getX ( ) ) * vector.angle ( 0, 1 );
		this.rotate.setAngle ( angle );
	}
	public void move(double stepX,double stepY){
		boolean collision=false;
		boundsTestCircle.getTransforms().setAll(new Translate(stepX,stepY));
		Shape intersect=Shape.intersect(boundsTestCircle,field);
		if(intersect.getBoundsInLocal().getWidth()!=-1){
			collision=true;
		}
		boundsTestCircle.getTransforms().setAll(new Translate(-stepX,-stepY));

		double newX = this.position.getX ( ) + stepX;
		double newY = this.position.getY ( ) + stepY;

		if(!collision) {
			this.position.setX(newX);
			this.position.setY(newY);
			checkConsumableCollision();
			this.updateGun();
		}
	}

	public void checkConsumableCollision(){
		for (int i = 0; i <consumables.size(); i++) {
			if(consumables.get(i).handleCollision(this)){
				Consumable cons=consumables.get(i);
				Group root=(Group) super.getParent();
				root.getChildren().remove(cons);
				consumables.remove(cons);
			}
		}
	}
	
	public Bullet handleMouseEvent (MouseEvent mouseEvent ) {
		this.mouseX = mouseEvent.getX ( );
		this.mouseY = mouseEvent.getY ( );
		
		this.updateGun ( );
		
		Bullet bullet = null;
		
		if ( mouseEvent.getEventType ( ).equals ( MouseEvent.MOUSE_PRESSED ) && (mouseEvent.isPrimaryButtonDown ( ) || mouseEvent.isSecondaryButtonDown()) ) {
			Point2D speed = new Point2D (
					this.mouseX - this.position.getX ( ),
					this.mouseY - this.position.getY ( )
			).normalize ( );

			Point2D offset = speed.multiply ( 2.1  * radius );
			
			double x = this.position.getX ( ) + offset.getX ( );
			double y = this.position.getY ( ) + offset.getY ( );
			
			Translate position = new Translate ( x, y );

			if(mouseEvent.isPrimaryButtonDown()) {
				bullet = new Bullet(0.3 * radius, position, speed, Color.ORANGE, 1);
			}
			else {
				if(strongProjectiles>0) {
					bullet = new Bullet(0.5 * radius, position, speed.multiply(0.3), Color.BLUE, 2);
					strongProjectiles--;
					missiles.get(strongProjectiles).setFill(Color.TRANSPARENT);
				}
			}
		}
		return bullet;
	}

	public void addMissile(){
		if(strongProjectiles<4) {
			missiles.get(strongProjectiles).setFill(Color.RED);
			strongProjectiles++;
		}
	}

	public Point2D getCenterScene(){
		Bounds bodyBounds=body.localToScene(body.getBoundsInLocal());
		return new Point2D(bodyBounds.getCenterX(),bodyBounds.getCenterY());
	}

	public boolean handleCollision(Bullet bullet){
		Bounds bounds = body.localToScene(body.getBoundsInLocal());

		return bounds.intersects ( bullet.localToScene (bullet.getBoundsInLocal() ) );
	}

	public boolean handleShieldCollision(Bullet bullet){
		if(!shield.getActive())return false;
		Bounds bounds =shield.localToScene(shield.getBoundsInLocal());
		return bounds.intersects(bullet.localToScene(bullet.getBoundsInLocal()));
	}

	public void damageLife(int damage){
		life-=damage;
		if(life>=maxLife)life=maxLife;
		if(life>=0) {
			Timeline healthBarAnimation = new Timeline(
					new KeyFrame(Duration.millis(1000),
							new KeyValue(healthBar.widthProperty(), (double) life / maxLife * WINDOW_WIDTH))
			);
			healthBarAnimation.setAutoReverse(false);
			healthBarAnimation.setCycleCount(1);
			healthBarAnimation.play();
		}
	}

	public int getLife(){
		return life;
	}

	public double getPlayerSpeed() {
		return playerSpeed;
	}

	public Shape getShape(){
		return boundsTestCircle;
	}

	public void setShield(boolean state){
		shield.setActive(state);
	}
	public boolean getShield(){
		return shield.getActive();
	}

	public Point2D getNormalVector(Point2D pos){
		Point2D playerCenter=getCenterScene();
		Point2D n=new Point2D(
				pos.getX()-playerCenter.getX(),
				pos.getY()-playerCenter.getY()
		).normalize();
		return n;
	}

}
