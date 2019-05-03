import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;

public class gang extends Application{
	int offX=600, offY=600;
	int oldOffX=600, oldOffY=600;
	double scale=10*Math.pow(10,-10);
	int fstClickX, fstClickY;
	SolarSystem s = new SolarSystem();
	int zoommax=0;
	double diameter=3;
	ArrayList<Sphere> planets = new ArrayList<Sphere>();

	@Override
	public void start(Stage primaryStage) {

		makePlanets();

		Polygon rocket = new Polygon();
		double xModule = offX-diameter/2+s.planetaryObjects.get(22).x*scale;
		double yModule = offY-diameter/2+s.planetaryObjects.get(22).y*scale;
		double xModuleLeft = xModule+5;
		double yModuleLeft = yModule+7;
		double xModuleRight = xModule+7;
		double yModuleRight = yModule+5;
		System.out.println(yModuleLeft);
		System.out.println(yModuleRight);
		System.out.println(s.planetaryObjects.get(22).x + " " + s.planetaryObjects.get(22).y + " " + s.planetaryObjects.get(22).name);


		rocket.getPoints().addAll(new Double[]{
				xModule, yModule,
				xModuleRight, yModuleRight,
				xModuleLeft, yModuleLeft });
		rocket.setFill(Color.RED);

		rocket.setTranslateY(10);


		Timeline timeline = new Timeline(
				new KeyFrame(Duration.millis(20), t -> {
					//Put here the code which is supposed to be repeated(or check the x,y coordinates and decide when to thrust)
					s.updatePositions();
					rocket.setTranslateX(offX-diameter/2+s.planetaryObjects.get(22).x*scale);
					rocket.setTranslateY(offY-diameter/2+s.planetaryObjects.get(22).y*scale);
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
		root.getChildren().addAll(rocket);


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
					oldOffX=offX;
					oldOffY=offY;
					break;

				case S:
					scale=10*Math.pow(10,-10);
					offX=600;
					offY=600;
					camera.translateZProperty().set(500);
					oldOffX=offX;
					oldOffY=offY;
					break;
				case E:
					scale=10*Math.pow(10,-8);
					offX=600-(int)(diameter/2+s.planetaryObjects.get(4).x*scale);
					offY=600-(int)(diameter/2+s.planetaryObjects.get(4).y*scale);
					camera.translateZProperty().set(500);
					oldOffX=offX;
					oldOffY=offY;
					break;
			}
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

	public void makePlanets () {

		planets.clear();

		for(Planet p : s.planetaryObjects)
		{
			if(p.name!="Lander") {
				Sphere planet = new Sphere();
				planet.setTranslateX((int) (offX - diameter / 2 + p.x * scale));
				planet.setTranslateY((int) (offY - diameter / 2 + p.y * scale));
				planet.setRadius(diameter);
				//System.out.println(planet.getCenterX() + " " +  planet.getCenterY());

				planets.add(planet);
			}
		}
	}


	public static void main(String[] args) {
		launch(args);

	}

}