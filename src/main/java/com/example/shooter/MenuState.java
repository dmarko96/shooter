package com.example.shooter;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;

import static com.example.shooter.Constants.WINDOW_HEIGHT;
import static com.example.shooter.Constants.WINDOW_WIDTH;

public class MenuState extends Group {

    private final int []state;
    public static final String[][] text=new String[][]{ {"Player     <  4hp, Avg  >","Player     <  8hp, Slow >","Player     <  2hp, Fast >"},
                                                        {"Terrain    <   Circle   >","Terrain    <   Square   >","Terrain    <   Hexagon  >"},
                                                        {"Difficulty <    Easy    >","Difficulty <   Medium   >","Difficulty <    Hard    >"}};

    private final Text playerText;
    private final Text terrainText;
    private final Text difficultyText;
    private final Text controlsText;
    private int level;
    public MenuState(){
        level=0;
        state=new int[]{0,0,0};
        Font myFont=new Font(60);

        playerText= new Text(text[0][0]);
        playerText.setStroke(Color.RED);
        terrainText= new Text(text[1][0]);
        difficultyText= new Text(text[2][0]);
        controlsText=new Text("movement - w,a,s,d\nshoot - LMB, missile - RMB\nstart - Enter\nquit - Escape");
        controlsText.setFont(new Font(30));
        controlsText.setTextAlignment(TextAlignment.CENTER);

        playerText.setFont(myFont);
        terrainText.setFont(myFont);
        difficultyText.setFont(myFont);

        playerText.getTransforms().add(new Translate(WINDOW_WIDTH*0.1,WINDOW_HEIGHT*0.2));
        terrainText.getTransforms().add(new Translate(WINDOW_WIDTH*0.1,WINDOW_HEIGHT*0.4));
        difficultyText.getTransforms().add(new Translate(WINDOW_WIDTH*0.1,WINDOW_HEIGHT*0.6));
        controlsText.getTransforms().add(new Translate(WINDOW_WIDTH*0.25,WINDOW_HEIGHT*0.75));
        super.getChildren().addAll(playerText,terrainText,difficultyText,controlsText);
    }

    public int getDifficulty(){
        return state[2];
    }
    public int getTerrain(){
        return state[1];
    }
    public int getPlayer(){
        return state[0];
    }

    public void up(){
        level=(level-1+3)%3;
        setText();
    }
    public void down(){
        level=(level+1+3)%3;
        setText();
    }
    public void left(){
        state[level]=(state[level]-1+3)%3;
        setText();
    }
    public void right(){
        state[level]=(state[level]+1+3)%3;
        setText();
    }

    public void setText(){
        playerText.setStroke(Color.BLACK);
        terrainText.setStroke(Color.BLACK);
        difficultyText.setStroke(Color.BLACK);
        if(level==0){
            playerText.setText( text[level][state[level]] );
            playerText.setStroke(Color.RED);
        }
        else if(level==1){
            terrainText.setText( text[level][state[level]] );
            terrainText.setStroke(Color.RED);
        }
        else{
            difficultyText.setText( text[level][state[level]] );
            difficultyText.setStroke(Color.RED);
        }
    }

}
