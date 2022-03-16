package object.Clock;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import object.staticObject.Wall;

public class Stoperica {

    private AnimationTimer timer;
    private Text text ;
    private int SECONDS;
    private int seconds;
    private long counter;
    private Wall.GameStopper gameStopper;

    public Stoperica(int vreme, Wall.GameStopper main){
        text = new Text("");
        text.setFont(Font.font("Arial", FontWeight.BOLD,20));
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(0.5);
        text.setFill(Color.WHITE);
        this.gameStopper=main;

        SECONDS=vreme;
        seconds=vreme;
        counter = 0;
    }

    public void start() {
        //System.out.println("start timer");
        text.setText("Vreme: "+seconds+" s");
        text.setTranslateX(-text.getBoundsInLocal().getWidth()/2);

        timer = new AnimationTimer() {
            public void handle(long now) {
                //  System.out.println("TAAAJMERR");
                if (counter == 60) {
                    seconds--;
                    text.setText("Vreme: "+ Integer.toString(seconds) + " s");
                    text.setTranslateX(-text.getBoundsInLocal().getWidth()/2);
                    if (seconds==0) {
                        gameStopper.stopGame();
                        this.stop();
                    }
                    counter = 0;
                } else {
                    counter ++;
                }
            }
        };
        timer.start();
    }

    public void stop() {
        // System.out.println("stop timer");
        timer.stop();
    };
    public void reset() {
        timer.stop();
        counter = 0;
        seconds = SECONDS;
        text.setText("Vreme: "+Integer.toString(seconds) + "0 s");
    };

    public Text getText(){
        return text;
    }

    public int getSeconds() {
        return seconds;
    }
}



