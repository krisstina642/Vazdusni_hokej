import javafx.animation.AnimationTimer;
import object.movableObject.MovableObject;
import object.staticObject.StaticObject;
import object.staticObject.Wall;
import java.util.Arrays;
import java.util.List;

public class Timer extends AnimationTimer {
	private MovableObject      movableObject;
	private List<StaticObject> staticObjects;
	private long               previous;
	
	public Timer ( MovableObject movableObject, StaticObject... staticObjects ) {
		this.movableObject = movableObject;
		this.staticObjects = Arrays.asList ( staticObjects );
	}

	public void setMovableObject(MovableObject movableObject){
		this.movableObject=movableObject;
	}
	
	@Override public void handle ( long now ) {
		if ( this.previous == 0 ) {
			this.previous = now;
		}

		double dt = ( now - this.previous ) / 1e9;
		this.previous = now;
		
		if (this.movableObject!=null) { this.movableObject.update ( dt );
		
		this.staticObjects.forEach ( staticObject -> {
			boolean bool= staticObject.collision ( this.movableObject );
			if (bool==true && staticObject instanceof Wall){
				Wall wall= (Wall) staticObject;
				if (wall.getType() == Wall.Type.VERTICAL_LEFT || wall.getType()==Wall.Type.VERTICAL_RIGHT  || wall.getType()==Wall.Type.UPPER || wall.getType()==Wall.Type.LOWER  ){
					//TranslateTransition tt=new TranslateTransition(Duration.seconds(0.3),wall);
					wall.moveWall();


				}
			}

		} );
	}
}}
