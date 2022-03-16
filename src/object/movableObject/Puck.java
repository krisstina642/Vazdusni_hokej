package object.movableObject;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Translate;

public class Puck extends MovableObject {

    private static final double SPEED_X_MIN = 90;
    private static final double SPEED_X_MAX = 120;
    private static final double SPEED_Z_MIN = 30;
    private static final double SPEED_Z_MAZ = 60;
    private int power;

    public Puck() {
        super ( new Translate ( 0, 0, 0 ), Puck.getRandomSpeed ( ) );
        power=1;
    }
    public Puck(int power) {
        super ( new Translate ( 0, 0, 0 ), Puck.getRandomSpeed ( ) );
        this.power=power;
    }

    public int getPower(){
        return power;
    };

    protected static Point3D getRandomSpeed ( ) {
        double x = Math.random ( ) * ( Puck.SPEED_X_MAX - Puck.SPEED_X_MIN ) + SPEED_X_MIN;
        double z = Math.random ( ) * ( Puck.SPEED_Z_MAZ - Puck.SPEED_Z_MIN ) + SPEED_Z_MIN;
        return new Point3D ( x, 0, z );
    }

    public static MeshView obruc(double radius,  double radius2, double height, Color color){
        int n = 100;
        float points[]= new float[n*4*3];

        for (int i=0; i<n; i++){
            double  angle= Math.PI*2/n*i;

            points[ i*12 ]	 	= (float) (radius2 * Math.sin(angle));
            points[ i*12 + 1]	= (float) (height / 2);
            points[ i*12 + 2] 	= (float) (radius2 * Math.cos(angle));

            points[ i*12 + 3]	= (float) (radius * Math.sin(angle));
            points[ i*12 + 4] 	= (float) (height / 2);
            points[ i*12 + 5] 	= (float) (radius * Math.cos(angle));

            points[ i*12 + 6]	= (float) (radius * Math.sin(angle));
            points[ i*12 + 7] 	= (float) (-height / 2);
            points[ i*12 + 8] 	= (float) (radius * Math.cos(angle));

            points[ i*12 + 9]	= (float) (radius2 * Math.sin(angle));
            points[ i*12 + 10] 	= (float) (-height / 2);
            points[ i*12 + 11] 	= (float) (radius2 * Math.cos(angle));

        }

        float texture[]=new float[]{
                0.5F,0.5F
        };

        int faces[]=new int[n*8*12];

        for (int i=0; i<n; i++){

            int unutrasnjaPoz1 = i*4;
            int unutrasnjaNeg1 = i*4+3;
            int spoljasnjaPoz1 = i*4+1;
            int spoljasnjaNeg1 = i*4+2;

            int unutrasnjaPoz2 = (i*4+4) % (n*4);
            int unutrasnjaNeg2 = (i*4+7) % (n*4);
            int spoljasnjaPoz2 = (i*4+5) % (n*4);
            int spoljasnjaNeg2 = (i*4+6) % (n*4);

            faces[i*96]=unutrasnjaPoz1;
            faces[i*96+2]=unutrasnjaPoz2;
            faces[i*96+4]=unutrasnjaNeg1;

            faces[i*96+6]=unutrasnjaPoz1;
            faces[i*96+8]=unutrasnjaNeg1;
            faces[i*96+10]=unutrasnjaPoz2;

            faces[i*96+12]=unutrasnjaPoz2;
            faces[i*96+14]=unutrasnjaNeg2;
            faces[i*96+16]=unutrasnjaNeg1;

            faces[i*96+18]=unutrasnjaPoz2;
            faces[i*96+20]=unutrasnjaNeg1;
            faces[i*96+22]=unutrasnjaNeg2;

            faces[i*96+24]=spoljasnjaPoz1;
            faces[i*96+26]=spoljasnjaPoz2;
            faces[i*96+28]=spoljasnjaNeg1;

            faces[i*96+30]=spoljasnjaPoz1;
            faces[i*96+32]=spoljasnjaNeg1;
            faces[i*96+34]=spoljasnjaPoz2;

            faces[i*96+36]=spoljasnjaPoz2;
            faces[i*96+38]=spoljasnjaNeg2;
            faces[i*96+40]=spoljasnjaNeg1;

            faces[i*96+42]=spoljasnjaPoz2;
            faces[i*96+44]=spoljasnjaNeg1;
            faces[i*96+46]=spoljasnjaNeg2;

            faces[i*96+48]=spoljasnjaPoz1;
            faces[i*96+50]=spoljasnjaPoz2;
            faces[i*96+52]=unutrasnjaPoz1;

            faces[i*96+54]=spoljasnjaPoz1;
            faces[i*96+56]=unutrasnjaPoz1;
            faces[i*96+58]=spoljasnjaPoz2;

            faces[i*96+60]=spoljasnjaPoz2;
            faces[i*96+62]=unutrasnjaPoz1;
            faces[i*96+64]=unutrasnjaPoz2;

            faces[i*96+66]=spoljasnjaPoz2;
            faces[i*96+68]=unutrasnjaPoz2;
            faces[i*96+70]=unutrasnjaPoz1;

            faces[i*96+72]=spoljasnjaNeg1;
            faces[i*96+74]=spoljasnjaNeg2;
            faces[i*96+76]=unutrasnjaNeg1;

            faces[i*96+78]=spoljasnjaNeg1;
            faces[i*96+80]=unutrasnjaNeg1;
            faces[i*96+82]=spoljasnjaNeg2;

            faces[i*96+84]=spoljasnjaNeg2;
            faces[i*96+86]=unutrasnjaNeg1;
            faces[i*96+88]=unutrasnjaNeg2;

            faces[i*96+90]=spoljasnjaNeg2;
            faces[i*96+92]=unutrasnjaNeg2;
            faces[i*96+94]=unutrasnjaNeg1;

            for (int j=1;j<96; j+=2){
                faces[i*96+j]=0;
            }
        }

        TriangleMesh tm=new TriangleMesh();
        tm.getPoints().setAll(points);
        tm.getTexCoords().setAll(texture);
        tm.getFaces().setAll(faces);

        MeshView mv=new MeshView(tm);
        mv.setMaterial(new PhongMaterial(color));

        return mv;
    }

    public void resetPosition( ) {
        super.setPosition ( new Translate ( 0, 0, 0 ), Puck.getRandomSpeed ( ) );
    }


}
