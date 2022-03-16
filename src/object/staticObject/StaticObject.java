package object.staticObject;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import object.movableObject.MovableObject;

import java.awt.*;

public abstract class StaticObject extends Group {
	private boolean lastCollisionDetected;
	
	public boolean collision (MovableObject movableObject, Point vektorNormale) {

		boolean collisionDetected = false;

		if (this.getBoundsInParent ( ).intersects ( movableObject.getBoundsInParent ( ) ) ){

			double centerStaticX=(this.getBoundsInParent().getMaxX()+this.getBoundsInParent().getMinX())/2;
			double centerStaticZ=(this.getBoundsInParent().getMaxZ()+this.getBoundsInParent().getMinZ())/2;
			double centerMovableX=(movableObject.getBoundsInParent().getMaxX()+movableObject.getBoundsInParent().getMinX())/2;
			double centerMovableZ=(movableObject.getBoundsInParent().getMaxZ()+movableObject.getBoundsInParent().getMinZ())/2;

			Shape intersect=Shape.intersect(new Circle(centerStaticX,centerStaticZ,this.getBoundsInLocal().getWidth()/2, Color.RED),
					new Circle(centerMovableX,centerMovableZ,movableObject.getBoundsInLocal().getWidth()/2, Color.RED));

			if(intersect!=null && (intersect.getBoundsInParent().getMaxX()+intersect.getBoundsInParent().getMinX()<-1 || intersect.getBoundsInParent().getMaxX()+intersect.getBoundsInParent().getMinX()>1)) {

				double tackaPresekaX=(intersect.getBoundsInParent().getMaxX()+intersect.getBoundsInParent().getMinX())/2;
				double tackaPresekaY=(intersect.getBoundsInParent().getMaxY()+intersect.getBoundsInParent().getMinY())/2;
				//System.out.println("tacka preseka "+tackaPresekaX+" "+tackaPresekaY);
				collisionDetected=true;
			vektorNormale.setLocation(tackaPresekaX-centerStaticX,tackaPresekaY-centerStaticZ);
				///System.out.println(intersect.getBoundsInLocal().getMinX()+ " "+ intersect.getBoundsInLocal().getMaxX());
			}


		}
		boolean returnValue = this.lastCollisionDetected == false && collisionDetected == true;
		this.lastCollisionDetected = collisionDetected;
		
		return returnValue;
	}

	public boolean collision (MovableObject movableObject) {
		boolean collisionDetected = false;
		if ( this.getBoundsInParent ( ).intersects ( movableObject.getBoundsInParent ( ) ) ) {
			collisionDetected = true;
		}

		boolean returnValue = this.lastCollisionDetected == false && collisionDetected == true;
		this.lastCollisionDetected = collisionDetected;

		return returnValue;
	}

}