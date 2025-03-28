package com.example.shooter;

import com.example.shooter.timer.Updatable;
import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameTime extends Group implements Updatable {


    private long time;
    private Text timeText;

    public GameTime(){
        timeText=new Text();
        timeText.setFont(new Font(40));
        timeText.setText("00:00");
        super.getChildren().add(timeText);
        this.time=0;
    }
    @Override
    public boolean update(long dns) {
        time+=dns/1000000;
        String min=Long.toString(time/60000);
        String sec=Long.toString((time/1000)%60);
        min=String.format("%02d",Integer.parseInt(min));
        sec=String.format("%02d",Integer.parseInt(sec));
        timeText.setText(min+":"+sec);
        return false;
    }
    public long getTimeMs(){
        return time;
    }
}
