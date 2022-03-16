package object.staticObject;


import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Paddle2 extends Paddle {
	
	public Paddle2(double width, double height, double depth, PaddleEnergyUpdate paddleUpdate ) {
		Box paddle = new Box ( width*0.4, height, depth );
		paddle.setTranslateY(-0.3*height);
		paddle.setMaterial ( new PhongMaterial ( Color.GREEN ) );

		float points[]=new float[]{
				(float) (-width/2), (float) (0.2*height), (float) (depth/2),
				(float) (-width*0.2), (float) (0.2*height), (float) (depth/2),
				(float) (-width*0.2), (float) (-0.2*height), (float) (depth/2),
				(float) (-width/2), (float) (0.2*height), (float) (-depth/2),
				(float) (-width*0.2), (float) (0.2*height), (float) (-depth/2),
				(float) (-width*0.2), (float) (-0.2*height), (float) (-depth/2),

				(float) (width/2), (float) (0.2*height), (float) (depth/2),
				(float) (width*0.2), (float) (0.2*height), (float) (depth/2),
				(float) (width*0.2), (float) (-0.2*height), (float) (depth/2),
				(float) (width/2), (float) (0.2*height), (float) (-depth/2),
				(float) (width*0.2), (float) (0.2*height), (float) (-depth/2),
				(float) (width*0.2), (float) (-0.2*height), (float) (-depth/2)
		};

		float texture[]=new float[]{
				0,0
		};

		int faces[]=new int[]{
				0,0,1,0,2,0,
				0,0,2,0,1,0,
				3,0,4,0,5,0,
				3,0,5,0,4,0,
				0,0,2,0,3,0,
				0,0,3,0,2,0,
				5,0,2,0,3,0,
				2,0,5,0,3,0,

				6,0,7,0,8,0,
				6,0,8,0,7,0,
				9,0,10,0,11,0,
				9,0,11,0,10,0,
				6,0,8,0,9,0,
				6,0,9,0,8,0,
				11,0,8,0,9,0,
				8,0,11,0,9,0,
		};
		TriangleMesh tml= new TriangleMesh();
		tml.getPoints().setAll(points);
		tml.getTexCoords().setAll(texture);
		tml.getFaces().setAll(faces);

		MeshView mv=new MeshView(tml);
		mv.setMaterial(new PhongMaterial ( Color.GREEN ));

		this.getChildren().addAll(paddle,mv);
		super.setPaddleUpdate(paddleUpdate);
	}

	public Paddle2(double width, double height, PaddleEnergyUpdate paddleUpdate) {
		this(width, height, width*2/5, paddleUpdate);
	}
	

}
