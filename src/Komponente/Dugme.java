package Komponente;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Dugme extends Group {
    private boolean clicked;
    private VBox vb;

    public Dugme(String string){
        vb=new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setOpacity(1);
        vb.backgroundProperty().set(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(2), new Insets(-5))));
        vb.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,new CornerRadii(2), null, new Insets(-5))));
        Text t1=new Text(string);
        t1.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        vb.getChildren().add(t1);
        this.getChildren().add(vb);

        this.setOnMouseEntered(e->
        {
            vb.backgroundProperty().set(new Background(new BackgroundFill(Color.CYAN,new CornerRadii(2), new Insets(-5))));
            vb.setScaleX(1.2);
            vb.setScaleY(1.2);
        });
        this.setOnMouseExited(e->
        {
            vb.setScaleX(1);
            vb.setScaleY(1);
            vb.backgroundProperty().set(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(2), new Insets(-5))));
        });
    }

    public Dugme(Image image, int width, int height){
        vb=new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setOpacity(1);
        clicked=false;

        Rectangle rec= new Rectangle(width,height);
        rec.setFill(new ImagePattern(image));

        setBlack();
        this.getChildren().add(vb);

        vb.getChildren().add(rec);

        this.setOnMouseEntered(e->
        {
            setRed();
        });
        this.setOnMouseExited(e->
        {
            if (clicked) return;
            setBlack();
        });
        this.setOnMousePressed(e->
        {
            if (clicked) {
                unclick();
            }
            else {
                clicked=true;
                setRed();
                }
        });

    }
    private void setBlack(){
        vb.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,new CornerRadii(2), null, new Insets(-3))));
        vb.backgroundProperty().set(new Background(new BackgroundFill(Color.BLACK,new CornerRadii(2), new Insets(-3))));
    }

    private void setRed(){
        vb.setBorder(new Border(new BorderStroke(Color.RED,BorderStrokeStyle.SOLID,new CornerRadii(2), null, new Insets(-3))));
        vb.backgroundProperty().set(new Background(new BackgroundFill(Color.RED,new CornerRadii(2), new Insets(-3))));
    }

    public void unclick(){
        clicked=false;
        setBlack();
    }

    public boolean getClicked(){
        return clicked;
    }
}
