import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class gang extends Application{
	int offX=600, offY=600;
	int oldOffX=600, oldOffY=600;
	double scale=Math.pow(10,-5);
	int fstClickX, fstClickY;
	SolarSystem s = new SolarSystem();
	int zoommax=0;
	double diameter=10;

	ArrayList<Sphere> planets = new ArrayList<Sphere>();

	@Override
	public void start(Stage primaryStage) {

		makePlanets();
		LandingModule rocketControler = (LandingModule)s.planetaryObjects.get(0);
		Polygon rocket = new Polygon();
		double xModule = (int)(offX-diameter/2+s.planetaryObjects.get(0).x*scale);
		double yModule = (int)(offY-diameter/2+s.planetaryObjects.get(0).y*scale);
		double xModuleLeft = xModule+5;
		double yModuleLeft = yModule-5;
		double xModuleRight = xModule-5;
		double yModuleRight = yModule-5;


		rocket.getPoints().addAll(new Double[]{
				xModule, yModule,
				xModuleRight, yModuleRight,
				xModuleLeft, yModuleLeft });
		rocket.setFill(Color.RED);

		Timeline timeline = new Timeline(
				new KeyFrame(Duration.millis(2), t -> {
					//Put here the code which is supposed to be repeated(or check the x,y coordinates and decide when to thrust)

					s.updatePositions();

					//double dx = s.planetaryObjects.get(0).x - s.planetaryObjects.get(1).x;
					//double dy = s.planetaryObjects.get(0).y - s.planetaryObjects.get(1).y;
					//double D = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
					//if(D<=2500400)
					//{
					//	System.out.println(/*s.planetaryObjects.get(1).velX + " " + */s.planetaryObjects.get(1).velY);
						//System.out.println(s.planetaryObjects.get(1).x + " " + s.planetaryObjects.get(1).y);
					//	try
					//	{
					//		Thread.sleep(20000);
					//	}
					//	catch(InterruptedException ex)
					//	{
					//		Thread.currentThread().interrupt();
					//	}
					//}

					//rocketControler.rightThrust();
					//rocketControler.mainThrust();
					rocketControler.leftThrustAndMove();

					rocket.setTranslateX(s.planetaryObjects.get(0).x*scale);
					rocket.setTranslateY(s.planetaryObjects.get(0).y*scale);

					for(int j=0;j<planets.size();j++)
					{
						planets.get(j).setTranslateX(offX-diameter/2+s.planetaryObjects.get(j).x*scale);
						planets.get(j).setTranslateY(offY-diameter/2+s.planetaryObjects.get(j).y*scale);
					}
				})
		);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

		Group root = new Group();
		root.getChildren().addAll(planets);
		root.getChildren().add(rocket);


		Scene scene = new Scene(root, 1200, 1200);
		scene.setFill(Color.BLACK);

		Camera camera = new PerspectiveCamera(false);

		scene.setCamera(camera);

		camera.translateZProperty().set(-500);
		//camera.setNearClip(1);
		camera.setFarClip(5000);

		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			switch (event.getCode()) {
				case Q:
					if (zoommax<1930){
						camera.translateZProperty().set(camera.getTranslateZ() + 500);
						zoommax=zoommax+500;}
					break;
				case A:
					camera.translateZProperty().set(camera.getTranslateZ() - 500);
					zoommax=zoommax-500;
					break;
				case T:
					scale=10*Math.pow(10,-8);
					offX=600-(int)(diameter/2+s.planetaryObjects.get(11).x*scale);
					offY=600-(int)(diameter/2+s.planetaryObjects.get(11).y*scale);

					camera.translateZProperty().set(500);
					break;

				case S:
					scale=10*Math.pow(10,-10);
					camera.translateXProperty().set(0);
					camera.translateYProperty().set(0);
					scale=10*Math.pow(10,-10);
					offX=600;
					offY=600;
					camera.translateZProperty().set(500);
					break;
				case E:
					scale=10*Math.pow(10,-8);
					camera.translateXProperty().set((int)(diameter/2+s.planetaryObjects.get(4).x*scale));
					camera.translateYProperty().set((int)(diameter/2+s.planetaryObjects.get(4).y*scale));
					scale=10*Math.pow(10,-8);
					offX=600-(int)(diameter/2+s.planetaryObjects.get(4).x*scale);
					offY=600-(int)(diameter/2+s.planetaryObjects.get(4).y*scale);
					camera.translateZProperty().set(500);
					break;
			}
		});


		primaryStage.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			fstClickX=(int)(event.getX());
			System.out.println(fstClickX);
			fstClickY=(int)(event.getY());

		});


		primaryStage.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
			camera.translateXProperty().set(-(((int)(event.getX())-camera.getTranslateX())-fstClickX));
			camera.translateYProperty().set(-(((int)(event.getY())-camera.getTranslateY())-fstClickY));

		});

		primaryStage.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			fstClickX=(int)(event.getX());
			fstClickY=(int)(event.getY());

		});

		primaryStage.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
			oldOffX=offX;
			oldOffY=offY;

		});

		primaryStage.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
			offX=oldOffX+(int)(event.getX())-fstClickX;
			offY=oldOffY+(int)(event.getY())-fstClickY;

		});


		primaryStage.setTitle("SolarSystem");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void makePlanets(){

		planets.clear();

		for(Planet p : s.planetaryObjects)
		{
			if(p.name!="Lander") {
			Sphere planet = new Sphere();
			planet.setTranslateX((int) (offX - diameter / 2 + p.x * scale));
			planet.setTranslateY((int) (offY - diameter / 2 + p.y * scale));

			int a = (int) (offX - diameter / 2 + p.x * scale);
			int b = (int) (offY - diameter / 2 + p.y * scale);

			System.out.println(a + " " + b);
			planet.setRadius(diameter);


			if (p.name == "centerTitan") {

				PhongMaterial material = new PhongMaterial();
				material.setDiffuseMap(new Image("http://westciv.com/images/wdblogs/radialgradients/simpleclorstops.png"));
				planet.setMaterial(material);
			}

			planets.add(planet);
			}
		}
	}


	public static void main(String[] args) {
		launch(args);

	}

}