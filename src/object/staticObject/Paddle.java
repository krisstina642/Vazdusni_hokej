package object.staticObject;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Translate;
import object.movableObject.MovableObject;

public class Paddle extends StaticObject {
    private Translate translate;
    private double energy;
    private static final double energyLoss=0.2;
    protected PaddleEnergyUpdate paddleUpdate;

    public Paddle(double width, double height, double depth, PaddleEnergyUpdate paddleUpdate ) {
        this.paddleUpdate=paddleUpdate;
        this.energy=1;
        Box paddle = new Box ( width, height, depth );
        paddle.setMaterial ( new PhongMaterial ( Color.BLUE ) );
        super.getChildren ( ).addAll ( paddle );
        this.translate = new Translate ( );
        super.getTransforms ( ).addAll (
                this.translate
        );
    }

    public Paddle(double width, double height, PaddleEnergyUpdate paddleUpdate ) {
        this(width,height,width*2/5, paddleUpdate);
    }

    public Paddle() {
        this.energy=1;
        this.translate = new Translate ( );
        super.getTransforms ( ).addAll (
                this.translate
        );
    }

    public void setPaddleUpdate(PaddleEnergyUpdate paddleUpdate) {
        this.paddleUpdate = paddleUpdate;
    }

    @Override public boolean collision (MovableObject movableObject ) {
        boolean collisionDetected = super.collision ( movableObject );
        if ( collisionDetected ) {
            movableObject.invertSpeedX ( energy );
            reduceEnergy();
        }
        return collisionDetected;
    }

    public void move ( double dx, double dy, double dz ) {
        this.translate.setX ( this.translate.getX ( ) + dx );
        this.translate.setY ( this.translate.getY ( ) + dy );
        this.translate.setZ ( this.translate.getZ ( ) + dz );
    }

    public double getZ ( ) {
        return this.translate.getZ ( );
    }

    public void reduceEnergy(){
       if (energy>0) energy=energy-energyLoss;
        paddleUpdate.updateEnergy(this);
    }

    public void fillEnergy(){
        energy=1;
    }

    public double getEnergy(){
        return energy;
    }

    public static interface PaddleEnergyUpdate {
        void updateEnergy(Paddle p);
    }
}

