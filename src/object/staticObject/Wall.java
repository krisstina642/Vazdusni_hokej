package object.staticObject;

import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.util.Duration;
import object.movableObject.MovableObject;

public class Wall extends StaticObject {
	private Type        type;
	private GameStopper gameStopper;
	private PhongMaterial phongMaterial;
	private TranslateTransition translateTransition;


	
	public Wall ( double width, double height, double depth, Type type, GameStopper gameStopper ) {
		Box wall = new Box ( width, height, depth );
		phongMaterial=new PhongMaterial ();
		if (type==Type.LEFT || type==Type.RIGHT) phongMaterial.setDiffuseColor(Color.BLUE);
		else phongMaterial.setDiffuseMap(new Image("wall.jpg"));

		wall.setMaterial ( phongMaterial );
		super.getChildren ( ).addAll ( wall );
		
		this.type = type;
		this.gameStopper = gameStopper;
		translateTransition=new TranslateTransition(Duration.seconds(0.3),this);

		switch (type) {
			case VERTICAL_LEFT: {
				translateTransition.setToX(-10);
				break;
			}
			case VERTICAL_RIGHT: {
				translateTransition.setToX(10);
				break;
			}
			case UPPER: {
				translateTransition.setToZ(10);
				break;
			}
			case LOWER: {
				translateTransition.setToZ(-10);
			}
		}

		translateTransition.setInterpolator(Interpolator.LINEAR);
		translateTransition.setAutoReverse(true);
		translateTransition.setCycleCount(2);
	}

	public void moveWall(){
		translateTransition.playFrom(Duration.seconds(0));
	}
	
	public Wall ( double width, double height, double depth, Type type ) {
		this ( width, height, depth, type, null );
	}
	
	@Override public boolean collision (MovableObject movableObject) {
		boolean collisionDetected = super.collision ( movableObject);
		
		if ( collisionDetected ) {
			switch ( type ) {
				case UPPER:
				case LOWER: {
					movableObject.invertSpeedZ ( );
					break;
				}
				case VERTICAL_LEFT:
				case VERTICAL_RIGHT:{
					movableObject.invertSpeedX ( );
					break;
				}

				case LEFT:
				case RIGHT:
				{
					AnimationTimer animationTimer=new AnimationTimer() {
						Color c=null;
						@Override
						public void handle(long now) {
							if (c == null) c=Color.color(1,0,0);
							else {
								if ((c.getRed()-0.01)<0 || (c.getBlue()+0.01)>1) {
									phongMaterial.setDiffuseColor(Color.color(0,0,1));
									this.stop();
								}
								else c = Color.color(c.getRed()-0.01,0,c.getBlue()+0.01);
							}
							phongMaterial.setDiffuseColor(c);
						}
					};
					animationTimer.start();
					if ( this.gameStopper != null ) {
						this.gameStopper.stopGame ( type);
					}
					break;
				}
			}
		}
		
		return collisionDetected;
	}
	
	public enum Type {
		UPPER, LOWER, LEFT, RIGHT, VERTICAL_LEFT, VERTICAL_RIGHT
	}

	public Type getType() {
		return type;
	}

	public static interface GameStopper {
		void stopGame (Type type);
		void stopGame ();
	}
}