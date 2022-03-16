package object.movableObject;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;

public class Puck3 extends Puck {

    public Puck3(double radius, double height ) {
        super (2);
        double radius2 = radius * 2/3;
        double height2 = height * 0.4;

        double radius3 = radius/2+radius/8;
        double radius4 = radius/2-radius/8;

        double radius5 =radius/3;


        Cylinder puck = new Cylinder ( radius5, height );
        puck.setMaterial ( new PhongMaterial ( Color.BLACK ) );

        Cylinder puckBase = new Cylinder ( radius, height2 );
        puckBase.setMaterial ( new PhongMaterial ( Color.YELLOW ) );

        MeshView mvObrucVeliki = super.obruc(radius, radius2, height, Color.YELLOW);
        MeshView mvObrucSrednji = super.obruc(radius3, radius4, height, Color.RED);

        super.getChildren ( ).addAll (
                puckBase, mvObrucSrednji, mvObrucVeliki, puck
        );
    }

}
