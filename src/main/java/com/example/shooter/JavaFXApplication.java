package com.example.shooter;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.shooter.Constants.*;

public class JavaFXApplication extends Application {
	
	@Override
	public void start ( Stage stage ) throws IOException {
		Image backgroundWater=new Image(Main.class.getResourceAsStream("water.jpg"));
		ImagePattern backgroundWatterPattern= new ImagePattern(backgroundWater);

		//MENU BEFORE GAME
		MenuState menuState=new MenuState();
		Group menuRoot=new Group();
		menuRoot.getChildren().addAll(menuState);
		Scene menuScene=new Scene(menuRoot,WINDOW_WIDTH,WINDOW_HEIGHT);
		menuScene.addEventHandler(KeyEvent.KEY_PRESSED,keyEvent -> {
			switch (keyEvent.getCode()){
				case A:
					menuState.left();
					break;
				case D:
					menuState.right();
					break;
				case S:
					menuState.down();
					break;
				case W:
					menuState.up();
					break;
				case ENTER:
					Group root=new Group();
					GameScene gameScene=new GameScene(root,menuState.getPlayer(),menuState.getTerrain(),menuState.getDifficulty());
					gameScene.setFill(backgroundWatterPattern);
					stage.setScene(gameScene);
					break;
				case ESCAPE:
					System.exit(0);
			}
		});

		menuScene.setFill(backgroundWatterPattern);
		stage.setScene(menuScene);
		stage.setTitle ( "Shooter" );
		stage.setResizable ( false );
		stage.show ( );
	}
	
	public static void main ( String[] args ) {
		launch ( );
	}
}