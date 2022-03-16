import Komponente.Dugme;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import object.Clock.Stoperica;
import object.movableObject.Puck;
import object.movableObject.Puck1;
import object.movableObject.Puck2;
import object.movableObject.Puck3;
import object.staticObject.*;


public class Main extends Application implements Paddle.PaddleEnergyUpdate, Wall.GameStopper, EventHandler<KeyEvent>{
	
	private static final double WIDTH            = 800;
	private static final double HEIGHT           = 800;
	private static final double NEAR_CLIP        = 0.1;
	private static final double FAR_CLIP         = 5000;
	private static final double CAMERA_Z         = -2500;
	private static final double PUCK_RADIUS      = 20;
	private static final double PUCK_HEIGHT      = 20;
	private static final double LONG_WALL_WIDTH  = 1000;
	private static final double SHORT_WALL_WIDTH = 500;
	private static final double WALL_HEIGHT      = PUCK_HEIGHT;
	private static final double WALL_DEPTH       = 20;
	private static final double PADDLE_WIDTH     = 50;
	private static final double PADDLE_HEIGHT    = 50;
	private static final double STEP             = 10;
	private static final double ENERGY_WIDTH		 = WIDTH/3;
	private              Timer  timer;
	private Paddle leftPaddle, rightPaddle;
	private PerspectiveCamera camera;
	private PerspectiveCamera camera2;
	private PerspectiveCamera levaKamera;
	private PerspectiveCamera desnaKamera;
	private static Translate levaKameraPozicija;
	private static Translate desnaKameraPozicija;
	private static PointLight levoSvetlo;
	private static PointLight desnoSvetlo;

	private static Group leviZivoti;
	private static Group desniZivoti;
	private static Rectangle leviEnergija;
	private static Rectangle desniEnergija;
	private static Group pomocnaGrupa;
	private static Group root;

	private static SubScene scene;
	private static Puck puck;
	private static int brojPogodaka;
	private static int random;
	private Stage primaryStage;

	private Stoperica stoperica;

	
	public static void main ( String[] arguments ) {
		launch ( arguments );
	}
	
	private void createCameras ( ) {

		Translate zoom;

		levaKamera=new PerspectiveCamera(true);
		this.levaKamera.setNearClip ( NEAR_CLIP );
		this.levaKamera.setFarClip ( FAR_CLIP );
		levaKameraPozicija=new Translate (  0,0, 0 );
		this.levaKamera.getTransforms ( ).addAll (
				levaKameraPozicija,
				new Rotate(90,Rotate.Y_AXIS),
				new Translate (  0,-PADDLE_HEIGHT*1.5, -LONG_WALL_WIDTH / 2*1.15 ),
				new Rotate(-10,Rotate.X_AXIS)
		);

		desnaKamera=new PerspectiveCamera(true);
		this.desnaKamera.setNearClip ( NEAR_CLIP );
		this.desnaKamera.setFarClip ( FAR_CLIP );
		desnaKameraPozicija=new Translate (  0,0, 0 );
		this.desnaKamera.getTransforms ( ).addAll (
				desnaKameraPozicija,
				new Rotate(-90,Rotate.Y_AXIS),
				new Translate (  0,-PADDLE_HEIGHT*1.5, -LONG_WALL_WIDTH / 2*1.15 ),
				new Rotate(-10,Rotate.X_AXIS)
		);


		this.camera = new PerspectiveCamera ( true );
		this.camera.setNearClip ( NEAR_CLIP );
		this.camera.setFarClip ( FAR_CLIP );
		zoom=new Translate(0,0,CAMERA_Z);
		Rotate cameraRotX=new Rotate ( -45, Rotate.X_AXIS );
		Rotate cameraRotY=new Rotate ( 0, Rotate.Y_AXIS );

		this.camera.getTransforms ( ).addAll (
				cameraRotY,
				cameraRotX,
				zoom
		);

		scene.setOnScroll(event -> {
			double currZoom=zoom.getZ();
			if(event.getDeltaY()>0 && currZoom+5<-2000) zoom.setZ(currZoom+5);
			else if (event.getDeltaY()<0 && currZoom-5>-3000) zoom.setZ(currZoom-5);
		});

		scene.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
			double x;
			double y;
			@Override
			public void handle(MouseEvent event) {
				if (event.getEventType()==MouseEvent.MOUSE_PRESSED && scene.getCamera()==camera){
					x=event.getSceneX();
					y=event.getSceneY();
				}
				else if(event.getEventType()==MouseEvent.MOUSE_DRAGGED && scene.getCamera()==camera){
					double dx=x-event.getSceneX();
					double dy=y-event.getSceneY();

					x=event.getSceneX();
					y=event.getSceneY();

					double angleX=cameraRotX.getAngle()+dy*0.05;
					double angleY=cameraRotY.getAngle()+dx*0.05;

					if (angleX<-90 || angleX>0) return;

					cameraRotX.setAngle(angleX);
					cameraRotY.setAngle(angleY);

				}
			}
		});

		this.camera2 = new PerspectiveCamera ( true );
		this.camera2.setNearClip ( NEAR_CLIP );
		this.camera2.setFarClip ( FAR_CLIP );
		this.camera2.getTransforms ( ).addAll (
				new Rotate ( -90, Rotate.X_AXIS ),
				zoom
		);


	}
	
	private Scene getScene ( ) {
		this.root = new Group ( );
		
		scene = new SubScene(this.root, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED );
		this.root.getChildren ( ).addAll ( puck );
		
		Wall upperWall = new Wall ( LONG_WALL_WIDTH, WALL_HEIGHT, WALL_DEPTH, Wall.Type.UPPER );
		Wall lowerWall = new Wall ( LONG_WALL_WIDTH, WALL_HEIGHT, WALL_DEPTH, Wall.Type.LOWER );
		Wall leftShortWall  = new Wall ( SHORT_WALL_WIDTH/2, WALL_HEIGHT, WALL_DEPTH, Wall.Type.LEFT, this );
		Wall rightShortWall = new Wall ( SHORT_WALL_WIDTH/2, WALL_HEIGHT, WALL_DEPTH, Wall.Type.RIGHT, this );
		Wall leftWall  = new Wall ( SHORT_WALL_WIDTH, WALL_HEIGHT, WALL_DEPTH, Wall.Type.VERTICAL_LEFT );
		Wall rightWall = new Wall ( SHORT_WALL_WIDTH, WALL_HEIGHT, WALL_DEPTH, Wall.Type.VERTICAL_RIGHT );
		
		upperWall.getTransforms ( ).addAll (
				new Translate ( 0, 0, SHORT_WALL_WIDTH / 2 + WALL_DEPTH / 2 )
		);
		lowerWall.getTransforms ( ).addAll (
				new Translate ( 0, 0, -( SHORT_WALL_WIDTH / 2 + WALL_DEPTH / 2 ) )
		);
		leftShortWall.getTransforms ( ).addAll (
				new Translate ( -( LONG_WALL_WIDTH / 2 - WALL_DEPTH / 2 ), 0, 0 ),
				new Rotate ( 90, Rotate.Y_AXIS )
		);
		rightShortWall.getTransforms ( ).addAll (
				new Translate ( LONG_WALL_WIDTH / 2 - WALL_DEPTH / 2, 0, 0 ),
				new Rotate ( 90, Rotate.Y_AXIS )
		);
		leftWall.getTransforms ( ).addAll (
				new Translate ( -( LONG_WALL_WIDTH / 2 - WALL_DEPTH / 2 ), 0, 0 ),
				new Rotate ( 90, Rotate.Y_AXIS )
		);
		rightWall.getTransforms ( ).addAll (
				new Translate ( LONG_WALL_WIDTH / 2 - WALL_DEPTH / 2, 0, 0 ),
				new Rotate ( 90, Rotate.Y_AXIS )
		);

		Group teren = new Group();

		Rectangle tlo = new Rectangle(-LONG_WALL_WIDTH/2,-SHORT_WALL_WIDTH/2, LONG_WALL_WIDTH, SHORT_WALL_WIDTH);
		tlo.setFill( Color.CYAN );
		Circle srednjiKrug = new Circle(0,0,SHORT_WALL_WIDTH/5,null);
		srednjiKrug.setStroke( Color.RED );
		srednjiKrug.setStrokeWidth( 5 );
		Line srednjaLinija = new Line(0,-SHORT_WALL_WIDTH/2,0,SHORT_WALL_WIDTH/2);
		srednjaLinija.setStroke( Color.RED );
		srednjaLinija.setStrokeWidth( 5 );
		Arc leviPolukrug = new Arc(-LONG_WALL_WIDTH/2,0,SHORT_WALL_WIDTH/4,SHORT_WALL_WIDTH/4,-85,170);
		leviPolukrug.setStroke( Color.RED );
		leviPolukrug.setFill( null );
		leviPolukrug.setStrokeWidth( 5 );
		Arc desniPolukrug = new Arc(LONG_WALL_WIDTH/2,0,SHORT_WALL_WIDTH/4,SHORT_WALL_WIDTH/4,95,170 );
		desniPolukrug.setStroke( Color.RED );
		desniPolukrug.setFill( null );
		desniPolukrug.setStrokeWidth( 5 );

		teren.getChildren().addAll( tlo, srednjiKrug, srednjaLinija, leviPolukrug,desniPolukrug );
		teren.getTransforms().addAll( new Rotate(90,Rotate.X_AXIS) );
		
		this.root.getChildren ( ).addAll ( teren, upperWall, lowerWall, leftWall, rightWall, leftShortWall, rightShortWall );

		this.leftPaddle.getTransforms ( ).addAll (
				new Translate ( -LONG_WALL_WIDTH / 2 * 0.8, 0, 0 ),
				new Rotate ( 90, Rotate.Y_AXIS )
		);
		this.rightPaddle.getTransforms ( ).addAll (
				new Translate ( LONG_WALL_WIDTH / 2 * 0.8, 0, 0 ),
				new Rotate ( 90, Rotate.Y_AXIS )
		);

		Reflektor reflektorLevi=new Reflektor(50,100);
		reflektorLevi.getTransforms().addAll(
				new Translate(-LONG_WALL_WIDTH/2,-LONG_WALL_WIDTH/3),
				new Rotate(30,Rotate.Z_AXIS),
			    new Rotate(90,Rotate.Y_AXIS)
		);

		Reflektor reflektorDesni=new Reflektor(50,100);
		reflektorDesni.getTransforms().addAll(
				new Translate(LONG_WALL_WIDTH/2,-LONG_WALL_WIDTH/3),
				new Rotate(-30,Rotate.Z_AXIS),
				new Rotate(-90,Rotate.Y_AXIS)
		);

		levoSvetlo=new PointLight(Color.WHITE);
		levoSvetlo.getTransforms().addAll(
				new Translate(-LONG_WALL_WIDTH/2,-LONG_WALL_WIDTH/3)
		);

		desnoSvetlo=new PointLight(Color.WHITE);
		desnoSvetlo.getTransforms().addAll(
				new Translate(LONG_WALL_WIDTH/2,-LONG_WALL_WIDTH/3)
		);

		this.root.getChildren ( ).addAll ( reflektorLevi, this.levoSvetlo, reflektorDesni, this.desnoSvetlo, this.leftPaddle, this.rightPaddle );
		
		this.createCameras ( );
		scene.setCamera ( this.camera );
		
		this.timer = new Timer ( puck, upperWall, lowerWall, leftWall, rightWall, leftShortWall, rightShortWall, this.rightPaddle, this.leftPaddle );
		this.timer.start ( );
		
		scene.addEventHandler ( KeyEvent.KEY_PRESSED, this );
		scene.setPickOnBounds(true);
		scene.setFocusTraversable(true);

		Rectangle energijaR1=new Rectangle(5,HEIGHT/10+10, ENERGY_WIDTH,WIDTH/35);
		energijaR1.setStroke(Color.WHITE);
		energijaR1.setStrokeWidth(4);
		leviEnergija=new Rectangle(5,HEIGHT/10+10, ENERGY_WIDTH,WIDTH/35);
		leviEnergija.setFill(Color.RED);

		Rectangle energijaR2=new Rectangle(2*WIDTH/3 - 5,HEIGHT/10+10, ENERGY_WIDTH,WIDTH/35);
		energijaR2.setStroke(Color.WHITE);
		energijaR2.setStrokeWidth(4);
		desniEnergija=new Rectangle( 2*WIDTH/3 - 5,HEIGHT/10+10, ENERGY_WIDTH,WIDTH/35);
		desniEnergija.setFill(Color.RED);
		desniEnergija.setStroke(null);

		leviZivoti = new Group();
		desniZivoti = new Group();

		for (int i=0; i<5; i++){
			double recWidth=WIDTH/40;
			double recHeight=HEIGHT/10;

			Rectangle rec1 = new Rectangle(recWidth,recHeight, Color.YELLOW);
			Rectangle rec2 = new Rectangle(recWidth,recHeight, Color.YELLOW);

			rec1.getTransforms().add(
					new Translate(5+i*(5+recWidth),5)
			);
			rec2.getTransforms().add(
					new Translate(WIDTH-(5+recWidth+i*(5+recWidth)),5)
			);
			leviZivoti.getChildren().add(rec1);
			desniZivoti.getChildren().add(rec2);

		}

		stoperica=new Stoperica(60, this);
		Text text=stoperica.getText();
		text.setTranslateY(50);
		text.getTransforms().add(new Translate(WIDTH/2,0));
		text.setFill(Color.YELLOW);
		stoperica.start();
		pomocnaGrupa = new Group();
		pomocnaGrupa.getChildren().addAll(leviZivoti,desniZivoti,text, energijaR1, leviEnergija, energijaR2, desniEnergija);

		SubScene pomocnaScena=new SubScene(pomocnaGrupa,WIDTH,HEIGHT);
		pomocnaScena.setFocusTraversable(false);
		pomocnaScena.setMouseTransparent(true);

		Group grupaScena= new Group();
		grupaScena.getChildren().addAll(scene, pomocnaScena);
		Scene GlavnaScena = new Scene(grupaScena, WIDTH, HEIGHT,true,SceneAntialiasing.BALANCED);

		GlavnaScena.setFill(Color.BLACK);
		return GlavnaScena;

	}

	private Scene getMeni ( ) {
		Group g = new Group();
		Scene s = new Scene(g, WIDTH, HEIGHT);

		Rectangle background=new Rectangle(WIDTH,HEIGHT);
		background.setFill(new ImagePattern(new Image("Teren.png",false)));

		GridPane gp=new GridPane();
		gp.setPrefWidth(WIDTH);
		gp.setPrefHeight(HEIGHT);
		gp.setAlignment(Pos.CENTER);
		gp.setCenterShape(true);

		Text t1= new Text(0,0,"Izaberi Reket");
		t1.setFont(Font.font("Arial", FontWeight.BOLD, 20));

		gp.add(t1,1,0);
		gp.setHalignment(t1, HPos.CENTER);

		Image Paddle1img=new Image("Paddle.png");
		Dugme button1 = new Dugme(Paddle1img, 150, 180);
		gp.add(button1,0,1);

		Image Paddle2img=new Image("Paddle2.png");
		Dugme button2 = new Dugme(Paddle2img, 150, 180);
		gp.add(button2,1,1);

		Image Paddle3img=new Image("Paddle3.png");
		Dugme button3 = new Dugme(Paddle3img, 150, 180);
		gp.add(button3,2,1);

		Text t2= new Text(0,0,"Izaberi Pak");
		t2.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		gp.add(t2,1,2);
		gp.setHalignment(t2, HPos.CENTER);

		Image Puck1img=new Image("Pak1.jpg");
		Dugme buttonP1 = new Dugme(Puck1img, 150, 180);
		gp.add(buttonP1,0,3);

		Image Puck2img=new Image("Pak2.jpg");
		Dugme buttonP2 = new Dugme(Puck2img,150, 180);
		gp.add(buttonP2,1,3);

		Image Puck3img=new Image("Pak3.jpg");
		Dugme buttonP3 = new Dugme(Puck3img,150, 180);
		gp.add(buttonP3,2,3);

		Dugme button=new Dugme("Start");
		button.setScaleX(1.5);
		button.setScaleY(1.5);

		Rectangle r=new Rectangle(button.getBoundsInLocal().getWidth()*2+10,button.getBoundsInLocal().getHeight()*2+10);
		r.setFill(null);
		r.setStrokeWidth(0.00000001);
		r.setStroke(Color.WHITE);
		gp.add(r,1,4);
		gp.setHalignment(r, HPos.CENTER);

		gp.add(button,1,4);
		gp.setHalignment(button, HPos.CENTER);
		gp.setValignment(button, VPos.CENTER);


		button1.setOnMouseReleased(event -> {
			if (button2.getClicked()) button2.unclick();
			if (button3.getClicked()) button3.unclick();
		});

		button2.setOnMouseReleased(event -> {
			if (button1.getClicked()) button1.unclick();
			if (button3.getClicked()) button3.unclick();
		});

		button3.setOnMouseReleased(event -> {
			if (button2.getClicked()) button2.unclick();
			if (button1.getClicked()) button1.unclick();
		});

		buttonP1.setOnMouseReleased(event -> {
			if (buttonP2.getClicked()) buttonP2.unclick();
			if (buttonP3.getClicked()) buttonP3.unclick();
		});

		buttonP2.setOnMouseReleased(event -> {
			if (buttonP1.getClicked()) buttonP1.unclick();
			if (buttonP3.getClicked()) buttonP3.unclick();
		});

		buttonP3.setOnMouseReleased(event -> {
			if (buttonP2.getClicked()) buttonP2.unclick();
			if (buttonP1.getClicked()) buttonP1.unclick();
		});

		button.setOnMouseClicked(event -> {
			if ((!button1.getClicked() && !button2.getClicked() && !button3.getClicked()) || (!buttonP1.getClicked() && !buttonP2.getClicked() && !buttonP3.getClicked())) return;
			if (button1.getClicked()){
				this.leftPaddle = new Paddle( PADDLE_WIDTH, PADDLE_HEIGHT, this);
				this.rightPaddle = new Paddle( PADDLE_WIDTH, PADDLE_HEIGHT, this);
			}
			else if (button2.getClicked()){
				this.leftPaddle = new Paddle2( PADDLE_WIDTH, PADDLE_HEIGHT, this);
				this.rightPaddle = new Paddle2( PADDLE_WIDTH, PADDLE_HEIGHT, this);
			}
			else if (button3.getClicked()){
				this.leftPaddle = new Paddle3( PADDLE_WIDTH*3/4, PADDLE_HEIGHT, this);
				this.rightPaddle = new Paddle3( PADDLE_WIDTH*3/4, PADDLE_HEIGHT, this);
			}

			if (buttonP1.getClicked()){
				puck = new Puck1( PUCK_RADIUS, PUCK_HEIGHT );
			}
			else if (buttonP2.getClicked()){
				puck = new Puck2( PUCK_RADIUS, PUCK_HEIGHT );
			}
			else if (buttonP3.getClicked()){
				puck = new Puck3( PUCK_RADIUS, PUCK_HEIGHT );
			}

			this.primaryStage.setScene ( this.getScene ( ) );
			this.primaryStage.show ( );
		});

		g.getChildren().addAll(background, gp);

		return s;
	}

	@Override public void start ( Stage primaryStage ) throws Exception {
		this.primaryStage=primaryStage;
		primaryStage.setScene ( this.getMeni ( ) );
		primaryStage.setTitle ( "Vazdusni hokej" );
		primaryStage.show ( );
	}
	
	@Override public void stopGame (Wall.Type type) {
		if (brojPogodaka==0){ brojPogodaka++; random= (int) ((Math.random()*100)%3+1); }
		else if (brojPogodaka==random && !(puck instanceof Puck3)){
			brojPogodaka=0;
			root.getChildren().remove(puck);
			if (!(puck instanceof Puck3)) puck=new Puck3(PUCK_RADIUS,PUCK_HEIGHT);
			else puck=new Puck2(PUCK_RADIUS,PUCK_HEIGHT);
			root.getChildren().add(puck);
			this.timer.setMovableObject(puck);
		}
		else{ brojPogodaka++;}

		if (type== Wall.Type.LEFT) {
			int i=0;
			while (leviZivoti.getChildren().size()>0 && i<puck.getPower()){
			leviZivoti.getChildren().remove(leviZivoti.getChildren().size()-1);
			desniEnergija.setWidth(ENERGY_WIDTH);
			desniEnergija.setX(WIDTH-ENERGY_WIDTH-5);
			rightPaddle.fillEnergy();
			i++;
			}
		}
		else if (type== Wall.Type.RIGHT) {
			int i=0;
			while (desniZivoti.getChildren().size()>0 && i<puck.getPower()) {
				desniZivoti.getChildren().remove(desniZivoti.getChildren().size() - 1);
				leviEnergija.setWidth(ENERGY_WIDTH);
				leftPaddle.fillEnergy();
				i++;
			}
		}

		if (leviZivoti.getChildren().size()==0 || desniZivoti.getChildren().size()==0){
			stopGame();
		}
		puck.resetPosition();

	}

	@Override
	public void stopGame() {
		timer.stop();
		stoperica.stop();

		Text t=null;
		if (leviZivoti.getChildren().size()==0 || desniZivoti.getChildren().size()==0){ t= new Text(WIDTH/2,HEIGHT/2,(desniZivoti.getChildren().size()==0)? "Left wins":"Right wins");}
		else if (leviZivoti.getChildren().size()>desniZivoti.getChildren().size()) { t= new Text(WIDTH/2,HEIGHT/2, "Left wins");}
		else if (leviZivoti.getChildren().size()<desniZivoti.getChildren().size()) { t= new Text(WIDTH/2,HEIGHT/2, "Right wins");}
		else t= new Text(WIDTH/2,HEIGHT/2, "DRAW");

		t.setFont(Font.font(100));
		t.setTranslateX(-t.getBoundsInLocal().getWidth()/2);
		t.setTranslateY(-t.getBoundsInLocal().getHeight()/2);
		t.setStroke(Color.RED);
		t.setFill(Color.RED);
		pomocnaGrupa.getChildren().add(t);
	}

	@Override public void handle ( KeyEvent event ) {

		if ( event.getCode ( ).equals ( KeyCode.W ) ) {
			double newZ = this.leftPaddle.getZ ( ) + STEP;

			if ( ( newZ + leftPaddle.getBoundsInLocal().getWidth() / 2 ) <= SHORT_WALL_WIDTH / 2 ) {
				this.leftPaddle.move ( 0, 0, STEP );
				this.levaKameraPozicija.setZ(newZ);
			}
		} else if ( event.getCode ( ).equals ( KeyCode.S ) ) {
			double newZ = this.leftPaddle.getZ ( ) - STEP;


			if ( ( newZ - leftPaddle.getBoundsInLocal().getWidth() / 2 ) >= -SHORT_WALL_WIDTH / 2 ) {
				this.levaKameraPozicija.setZ(newZ);
				this.leftPaddle.move ( 0, 0, -STEP );
			}
		} else if ( event.getCode ( ).equals ( KeyCode.UP ) ) {
			double newZ = this.rightPaddle.getZ ( ) + STEP;

			if ( ( newZ + rightPaddle.getBoundsInLocal().getWidth() / 2 ) <= SHORT_WALL_WIDTH / 2 ) {
				this.desnaKameraPozicija.setZ(newZ);
				this.rightPaddle.move ( 0, 0, STEP );
			}
		} else if ( event.getCode ( ).equals ( KeyCode.DOWN ) ) {
			double newZ = this.rightPaddle.getZ ( ) - STEP;

			if ( ( newZ - rightPaddle.getBoundsInLocal().getWidth() / 2 ) >= -SHORT_WALL_WIDTH / 2 ) {
				this.desnaKameraPozicija.setZ(newZ);
				this.rightPaddle.move ( 0, 0, -STEP );
			}
		}
		else if ( event.getCode ( ).equals ( KeyCode.NUMPAD2 ) || event.getCode().equals(KeyCode.DIGIT2) ) {
			scene.setCamera(camera2);
		}
		else if ( event.getCode ( ).equals ( KeyCode.NUMPAD1 ) || event.getCode().equals(KeyCode.DIGIT1)) {
			scene.setCamera(camera);
		}
		else if ( event.getCode ( ).equals ( KeyCode.NUMPAD3 ) || event.getCode().equals(KeyCode.DIGIT3) ) {
			scene.setCamera(levaKamera);
		}
		else if ( event.getCode ( ).equals ( KeyCode.NUMPAD4 ) || event.getCode().equals(KeyCode.DIGIT4)) {
			scene.setCamera(desnaKamera);
		}
		else if ( event.getCode ( ).equals ( KeyCode.NUMPAD9 ) || event.getCode().equals(KeyCode.DIGIT9) ) {
			if (root.getChildren().contains(levoSvetlo)) root.getChildren().remove(levoSvetlo);
			else root.getChildren().add(levoSvetlo);
		}
		else if ( event.getCode ( ).equals ( KeyCode.NUMPAD0 ) || event.getCode().equals(KeyCode.DIGIT0)) {
			if (root.getChildren().contains(desnoSvetlo)) root.getChildren().remove(desnoSvetlo);
			else root.getChildren().add(desnoSvetlo);
		}
}

	@Override
	public void updateEnergy(Paddle p) {
		if (p==leftPaddle) leviEnergija.setWidth(ENERGY_WIDTH*p.getEnergy());
		else {
			double energyWidth=ENERGY_WIDTH*p.getEnergy();
			desniEnergija.setWidth(energyWidth);
			desniEnergija.setX(WIDTH-energyWidth-5);
		}
	}
}
