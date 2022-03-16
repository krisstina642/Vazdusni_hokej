package object.staticObject;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;


public class Reflektor extends Group {

    public Reflektor(int poluprecnikManjeg, int poluprecnikVeceg){
        int gustina=50;

        float points[]=new float[gustina*2*3];
        int faces[]=new int[gustina*2*12];
        float texture[]=new float[]{0.5f,0.5f};

        for(int i=0;i<gustina;i++){
            float xManjeg= (float) (Math.cos(Math.toRadians(360/gustina*i))*poluprecnikManjeg);
            float yManjeg= (float) (Math.sin(Math.toRadians(360/gustina*i))*poluprecnikManjeg);
            float xVeceg= (float) (Math.cos(Math.toRadians(360/gustina*i))*poluprecnikVeceg);
            float yVeceg= (float) (Math.sin(Math.toRadians(360/gustina*i))*poluprecnikVeceg);

            points[i*3]=xManjeg;
            points[i*3+1]=yManjeg;
            points[i*3+2]=0;

            points[gustina*3+i*3]=xVeceg;
            points[gustina*3+i*3+1]=yVeceg;
            points[gustina*3+i*3+2]=(poluprecnikManjeg+poluprecnikVeceg)/2;
        }

        for(int i=0;i<gustina;i++){
            int dot2=(i+1)%gustina;
            int dot3=(i+gustina);
            int dot4=dot2+gustina;

            faces[i*24]=i;
            faces[i*24+1]=0;
            faces[i*24+2]=dot2;
            faces[i*24+3]=0;
            faces[i*24+4]=dot4;
            faces[i*24+5]=0;

            faces[i*24+6]=i;
            faces[i*24+7]=0;
            faces[i*24+8]=dot4;
            faces[i*24+9]=0;
            faces[i*24+10]=dot2;
            faces[i*24+11]=0;

            faces[i*24+12]=dot4;
            faces[i*24+13]=0;
            faces[i*24+14]=i;
            faces[i*24+15]=0;
            faces[i*24+16]=dot3;
            faces[i*24+17]=0;

            faces[i*24+18]=i;
            faces[i*24+19]=0;
            faces[i*24+20]=dot4;
            faces[i*24+21]=0;
            faces[i*24+22]=dot3;
            faces[i*24+23]=0;
        }

        Circle c=new Circle(poluprecnikManjeg);
        c.setFill(Color.GRAY);
        c.setStroke(Color.GRAY);

        TriangleMesh tm=new TriangleMesh();
        tm.getFaces().setAll(faces);
        tm.getPoints().setAll(points);
        tm.getTexCoords().setAll(texture);

        MeshView mv=new MeshView(tm);
        mv.setMaterial(new PhongMaterial(Color.GRAY));
        this.getChildren().addAll(mv,c);

    }
}
