package com.example.shooter;

public class GameState {

    public static enum State{
        MENU,
        GAME,
        END
    }
    private State state;

    public GameState(){
        state=State.MENU;
    }


    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
