package object.movableObject;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Puck2 extends Puck {
    public Puck2 ( double radius, double height ) {
        super ();

        Cylinder puck = new Cylinder ( radius, height );
        puck.setMaterial ( new PhongMaterial ( Color.BLACK ) );
        Line l=new Line(-radius/2,-radius/2,radius/2,radius/2);
        l.setStrokeWidth(5);
        l.setStroke(Color.YELLOW);
        l.getTransforms().addAll(
                new Translate(0,-height/2,0),
                new Rotate(90,Rotate.X_AXIS)
        );

        Line l2=new Line(-radius/2,radius/2,radius/2,-radius/2);
        l2.setStrokeWidth(5);
        l2.setStroke(Color.YELLOW);
        l2.getTransforms().addAll(
                new Translate(0,-height/2,0),
                new Rotate(90,Rotate.X_AXIS)
        );

        super.getChildren ( ).addAll (
                puck,l,l2
        );
    }


}
