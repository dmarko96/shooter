package com.example.shooter.timer;

import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;

public class MyTimer extends AnimationTimer {
	private long last;
	private List<Updatable> updatables;
	
	public MyTimer ( Updatable ...updatables ) {
		this.updatables = new ArrayList<> ( );
		for ( int i = 0; i < updatables.length; ++i ) {
			this.updatables.add ( updatables[i] );
		}
	}
	
	@Override
	public synchronized void handle ( long now ) {
		if ( this.last == 0 ) {
			this.last = now;
		}
		long dns  = now - this.last;
		this.last = now;
		
		this.updatables.removeIf ( updatable -> updatable.update ( dns ) );
	}
	
	public synchronized void add ( Updatable updatable ) {
		this.updatables.add ( updatable );
	}
}
