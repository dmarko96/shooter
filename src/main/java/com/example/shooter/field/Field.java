package com.example.shooter.field;

import javafx.scene.Group;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

import static com.example.shooter.Constants.ENEMY_PLACEMENT_RADIUS;

public abstract class Field extends Group {
	protected int enemyNumber=4;
	protected double enemyDistance=ENEMY_PLACEMENT_RADIUS;
	protected List<Affine> enemyTransform;
	public Field ( Translate position ) {
		super.getTransforms ( ).add ( position );
		enemyTransform=new ArrayList<>();
	}
	public int getEnemyNumber() {
		return enemyNumber;
	}

	public Affine getEnemyTransform(int i){
		if(i>=enemyNumber)return null;
		return enemyTransform.get(i);
	}
}
