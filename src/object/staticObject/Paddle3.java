package object.staticObject;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import object.movableObject.MovableObject;
import object.movableObject.Puck;

import java.awt.*;
import java.util.Vector;

public class Paddle3 extends Paddle {
    private double radius;

    public Paddle3 ( double poluprecnik, double visina, PaddleEnergyUpdate paddleUpdate) {
        double poluprecnikUzegDela=poluprecnik/2;
        this.radius=poluprecnik;

        Sphere c=new Sphere(poluprecnikUzegDela);
        c.setMaterial(new PhongMaterial(Color.RED));
        c.setTranslateY(-visina/3*2);
        Cylinder cylinder=new Cylinder(poluprecnik,visina/3);
        cylinder.setMaterial(new PhongMaterial(Color.RED));

        Cylinder cylinder2=new Cylinder(poluprecnikUzegDela,2*visina/3);
        cylinder2.setMaterial(new PhongMaterial(Color.RED));
        cylinder2.setTranslateY(-visina/3);

        MeshView mv= Puck.obruc(poluprecnik,poluprecnik*3/4,visina/5*4, Color.RED);

        this.getChildren().addAll(c,cylinder,cylinder2, mv);
        super.setPaddleUpdate(paddleUpdate);
    }

    @Override public boolean collision ( MovableObject movableObject ) {
        Point vektorNormale=new Point(0,0);
        boolean collisionDetected = super.collision ( movableObject, vektorNormale );
       // if(vektorNormale!=null && vektorNormale.getX()!=0) System.out.println(vektorNormale.getX());


        if ( collisionDetected ) {
           // System.out.println("Vektor normale x "+ vektorNormale.getX()+ " vektor y " + vektorNormale.getY());
            movableObject.invertUodnosuNaNormalu (new Point3D(vektorNormale.getX(),0,vektorNormale.getY()) , super.getEnergy());
            super.reduceEnergy();
        }

        return collisionDetected;
    }
}
