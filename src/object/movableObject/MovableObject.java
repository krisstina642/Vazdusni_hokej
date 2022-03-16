package object.movableObject;

import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Translate;

public abstract class MovableObject extends Group {
	private Translate position;
	private Point3D   speed;
	private static final double Max_SPEED_X = 500;
	private static final double Max_SPEED_Z = 500;

	private static final double Trenje = 0.999995;

	public MovableObject (Translate position, Point3D speed ) {
		this.position = position;
		this.speed = speed;
		
		super.getTransforms ( ).addAll (
				this.position
		);
	}
	
	public void update ( double dt ) {
		this.speed = new Point3D ( this.speed.getX ( ) * Trenje, this.speed.getY ( ) * Trenje, this.speed.getZ ( ) * Trenje);

		double newX = position.getX ( ) + speed.getX ( ) * dt ;
		double newY = position.getY ( ) + speed.getY ( ) * dt ;
		double newZ = position.getZ ( ) + speed.getZ ( ) * dt ;
		
		this.position.setX ( newX );
		this.position.setY ( newY );
		this.position.setZ ( newZ );
	}
	
	public void invertSpeedX ( ) {
		this.speed = new Point3D ( -this.speed.getX ( ), this.speed.getY ( ), this.speed.getZ ( ) );
	}

	public void invertSpeedX ( double energy ) {

		double i=koefBrzine(energy);
		this.speed = new Point3D ( -this.speed.getX ( ) * i, this.speed.getY ( )*i, this.speed.getZ ( )*i );
	}

	private double koefBrzine(double energy){

		double i = energy*2+1;
		if (this.speed.getX ( ) * i < -Max_SPEED_X || this.speed.getX ( ) * i > Max_SPEED_X){
			i=  Math.abs(Max_SPEED_X / this.speed.getX ( ));
		}
		if (this.speed.getZ ( ) * i < -Max_SPEED_Z || this.speed.getZ ( ) * i > Max_SPEED_Z){
			i=  Math.abs(Max_SPEED_Z / this.speed.getZ ( ));
		}
		return i;
	}
	
	public void invertSpeedZ ( ) {
		this.speed = new Point3D ( this.speed.getX ( ), this.speed.getY ( ), -this.speed.getZ ( ) );
	}

	public void invertUodnosuNaNormalu (Point3D point3D, double energy) {
			double i=  koefBrzine(energy);

		//System.out.println("brzina "+ this.speed.getX() + "  "+ this.speed.getZ());

		double ugaoIzmedjuVektora=(this.speed.getX()*point3D.getX()+point3D.getZ()*this.speed.getZ())
				/(Math.sqrt(Math.pow(this.speed.getX(),2)+Math.pow(this.speed.getZ(),2))*Math.sqrt(Math.pow(point3D.getX(),2)+Math.pow(point3D.getZ(),2)));

		//System.out.println("ugao izmedju vektora je "+ ugaoIzmedjuVektora);

		if (ugaoIzmedjuVektora>1 || ugaoIzmedjuVektora<-1) ugaoIzmedjuVektora=0;
		else ugaoIzmedjuVektora=Math.acos(ugaoIzmedjuVektora);

		//System.out.println("ugao izmedju vektora je "+ Math.toDegrees(ugaoIzmedjuVektora));

		double vektorskiProizvodY = this.speed.getX()*point3D.getZ()-this.speed.getZ()*point3D.getX();
		//System.out.println("Vektorski Proizvod "+ vektorskiProizvodY);


			if (vektorskiProizvodY>0) rotirajOkoY(-ugaoIzmedjuVektora*2+Math.PI);
				else rotirajOkoY(-Math.PI+2*ugaoIzmedjuVektora);

		this.speed = new Point3D ( this.speed.getX ( ) * i, this.speed.getY ( )*i, this.speed.getZ ( )*i );
	}

	public void rotirajOkoY(double ugao)
	{

		double xprim = this.speed.getX() * Math.cos(ugao) + this.speed.getZ() * Math.sin(ugao);
		double zprim = -this.speed.getX() * Math.sin(ugao) + this.speed.getZ() * Math.cos(ugao);
		this.speed = new Point3D ( xprim, this.speed.getY ( ), zprim );
	}

	public void setPosition(Translate position, Point3D speed ) {
		this.position = position;
		this.speed = speed;

		super.getTransforms( ).setAll (
				this.position
		);

	}
}
