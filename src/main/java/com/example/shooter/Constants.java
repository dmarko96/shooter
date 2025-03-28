package com.example.shooter;

public class Constants {
	public static final double WINDOW_WIDTH = 750;
	public static final double WINDOW_HEIGHT = 750;
	
	public static final double PLAYER_RADIUS = 20;
	public static final double PLAYER_STEP   = 2.5;
	public static final double BULLET_SPEED  = 10;
	
	public static final double FIELD_RADIUS = Math.min ( WINDOW_WIDTH, WINDOW_WIDTH ) * 0.3;
	
	public static final int    NUMBER_OF_ENEMIES      = 4;
	public static final double ENEMY_RADIUS = PLAYER_RADIUS * 1.4;
	public static final double ENEMY_PLACEMENT_RADIUS = Math.min ( WINDOW_WIDTH, WINDOW_HEIGHT ) * 0.42;
	public static final int PLAYER_LIFE=4;
	public static final int STRONG_ATTACK=4;
	public static final double CONSUMABLE_RADIUS=PLAYER_RADIUS*0.5;

	public static final int TERRAIN_NUMBER=3;
	public static final int CANNON_NUMBER=3;
	public static final int DIFFICULTY_NUMBER=3;
	public static final double COIN_CHANCE=0.6;
	public static final double HEART_CHANCE=0.3;
	public static final double SHIELD_CHANCE=0.1;

}
