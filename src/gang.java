import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;

public class gang extends Application{
	int offX=950, offY=600;
	int oldOffX=500, oldOffY=500;
	double scale=Math.pow(10,-4);
	int fstClickX, fstClickY;
	SolarSystem wholeSystem = new SolarSystem("wholeSystem");
	SolarSystem landingSystem = new SolarSystem("landingSystem");
	int zoommax=0;
	double diameter=694;

	int secondsPassed=0;

	ArrayList<Sphere> planets = new ArrayList<Sphere>();

	@Override
	public void start(Stage primaryStage) {

		makePlanets();
		LandingModule rocketControler = (LandingModule)landingSystem.planetaryObjects.get(1);
		Polygon rocket = new Polygon();
		double xModule = offX-diameter/2+landingSystem.planetaryObjects.get(1).getX()*scale;
		double yModule = offY-diameter/2+landingSystem.planetaryObjects.get(1).getY()*scale;
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

					landingSystem.updatePositions();

					rocket.setTranslateX(landingSystem.planetaryObjects.get(1).getX()*scale);
					rocket.setTranslateY(landingSystem.planetaryObjects.get(1).getY()*scale);

					for(int j=0;j<planets.size();j++)
					{
						planets.get(j).setTranslateX(offX-diameter/2+landingSystem.planetaryObjects.get(j).getX()*scale);
						planets.get(j).setTranslateY(offY-diameter/2+landingSystem.planetaryObjects.get(j).getY()*scale);
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
                	camera.translateXProperty().set((int)(diameter/2+landingSystem.planetaryObjects.get(11).getX()*scale));
                	camera.translateYProperty().set((int)(diameter/2+landingSystem.planetaryObjects.get(11).getY()*scale));

					camera.translateZProperty().set(500);
					break;

				case S:
                	scale=10*Math.pow(10,-10);
                	camera.translateXProperty().set(0);
                	camera.translateYProperty().set(0);
					scale=10*Math.pow(10,-10);
					camera.translateZProperty().set(500);
					break;
				case E:
                	scale=10*Math.pow(10,-8);
                	camera.translateXProperty().set((int)(diameter/2+landingSystem.planetaryObjects.get(4).getX()*scale));
                	camera.translateYProperty().set((int)(diameter/2+landingSystem.planetaryObjects.get(4).getY()*scale));
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


		primaryStage.setTitle("SolarSystem");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void makePlanets(){

		planets.clear();

		for(Planet p : landingSystem.planetaryObjects)
		{
			if(p.getName()!="Lander") {
				Sphere planet = new Sphere();
				planet.setTranslateX((int) (offX - diameter / 2 + p.getX() * scale));
				planet.setTranslateY((int) (offY - diameter / 2 + p.getY() * scale));

				planet.setRadius(diameter);


				if (p.getName().equals("centerTitan")) {

					PhongMaterial material = new PhongMaterial();
					material.setDiffuseMap(new Image("/titan.jpg"));
					planet.setMaterial(material);
				}

				planets.add(planet);
			}
		}
	}

	public static void main(String[] args) {
		launch(args);

	}

	public void wind(LandingModule rocket, double D)
	{
		double distanceToSurface = D-2500000;
		//Apollo 16 dimensions: 23 feet 1 inch (7.04 m) high; 31 feet (9.4 m) wide; 31 feet (9.4 m) deep https://en.wikipedia.org/wiki/Apollo_Lunar_Module
		double S=7*9;
		double Am=0;
		double windSpeed=0;
		double dX=0;
		double dY=0;
		//F = S*Am*a^2 - S is the area "hit by the wind", Am is air density, a^2 speed of the wind in m/s
		if(distanceToSurface>1000000)
		{
			Am=Math.pow(10,-10);
			windSpeed = Math.random()*10;
		}
		if(distanceToSurface>500000 && distanceToSurface<1000000){
			Am=Math.pow(10,-5);
			windSpeed = 20+Math.random()*10;
		}
		if(distanceToSurface>100000 && D<500000) {
			Am = Math.pow(10, 0);
			windSpeed = 50+Math.random()*20;
		}

		double windForce = S*Am*windSpeed*windSpeed;

		double acc = windForce/rocket.getMass();
		double vel=acc*SolarSystem.timestep;
		double distance = vel*SolarSystem.timestep;

		dX = Math.sin(-(rocket.getAngle() + (Math.PI / 2))) * distance;
		dY = Math.cos(rocket.getAngle() + Math.PI / 2) * distance;

		rocket.setVelX(rocket.getVelX() + dX/SolarSystem.timestep);
		rocket.setVelY(rocket.getVelY() + dY/SolarSystem.timestep);
	}
}