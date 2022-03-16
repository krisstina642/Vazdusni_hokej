package object.movableObject;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Line;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Puck1 extends Puck {

	public Puck1(double radius, double height ) {
		super ();
		double radius2 = radius * 0.75;
		double height2 = height * 0.5;

		Cylinder puck = new Cylinder ( radius2, height2 );
		puck.setMaterial ( new PhongMaterial ( Color.RED ) );

		MeshView mv=super.obruc(radius,radius2,height,Color.RED);

		super.getChildren ( ).addAll (
				puck, mv
		);
	}

}
