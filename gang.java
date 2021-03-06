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
import javafx.scene.transform.*;
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
	int offX=950, offY=600;
	int oldOffX=500, oldOffY=500;
	double scale=Math.pow(10,-4);
	int fstClickX, fstClickY;
	SolarSystem s = new SolarSystem();
	int zoommax=0;
	double diameter=694;

	int secondsPassed=0;

	ArrayList<Sphere> planets = new ArrayList<Sphere>();

	@Override
	public void start(Stage primaryStage) {

		makePlanets();
		LandingModule rocketControler = (LandingModule)s.planetaryObjects.get(1);
		Polygon rocket = new Polygon();
		double xModule = offX-diameter/2+s.planetaryObjects.get(1).x*scale;
		double yModule = offY-diameter/2+s.planetaryObjects.get(1).y*scale;
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
					secondsPassed++;

					double dx = s.planetaryObjects.get(0).x - s.planetaryObjects.get(1).x;
					double dy = s.planetaryObjects.get(0).y - s.planetaryObjects.get(1).y;
					double D = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
					if(D<=2500000)
					{
						System.out.println("VelY " +/*s.planetaryObjects.get(1).velX + " " + */s.planetaryObjects.get(1).velY);
						System.out.println("x : " + s.planetaryObjects.get(1).x + "y : " + s.planetaryObjects.get(1).y);
						try
						{
							System.out.println("d = " +D);
							System.out.println("Second passed " +secondsPassed);
							Thread.sleep(20000);
						}
						catch(InterruptedException ex)
						{
							Thread.currentThread().interrupt();
						}
					}
					wind(rocketControler, D);

					openLoopController(rocketControler);
					//closeLoopController(rocketControler,rocket, xModule, yModule, D);


					rocket.setTranslateX(s.planetaryObjects.get(1).x*scale);
					rocket.setTranslateY(s.planetaryObjects.get(1).y*scale);

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
                	camera.translateXProperty().set((int)(diameter/2+s.planetaryObjects.get(11).x*scale));
                	camera.translateYProperty().set((int)(diameter/2+s.planetaryObjects.get(11).y*scale));

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
                	camera.translateXProperty().set((int)(diameter/2+s.planetaryObjects.get(4).x*scale));
                	camera.translateYProperty().set((int)(diameter/2+s.planetaryObjects.get(4).y*scale));
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

		for(Planet p : s.planetaryObjects)
		{
			if(p.name!="Lander") {
				Sphere planet = new Sphere();
				planet.setTranslateX((int) (offX - diameter / 2 + p.x * scale));
				planet.setTranslateY((int) (offY - diameter / 2 + p.y * scale));

				int a = (int) (offX - diameter / 2 + p.x * scale);
				int b = (int) (offY - diameter / 2 + p.y * scale);

				//System.out.println(a + " " + b);
				planet.setRadius(diameter);


				if (p.name == "centerTitan") {

					PhongMaterial material = new PhongMaterial();
					material.setDiffuseMap(new Image("/titan.jpg"));
					planet.setMaterial(material);
				}

				planets.add(planet);
			}
		}
	}

	public void openLoopController(LandingModule rocketControler)
	{
		if(secondsPassed>2525)
		{
			if(secondsPassed<4671) {
				rocketControler.mainThrust();
			}
			else  if(secondsPassed<=4850){
				rocketControler.mainThrusthalf();
			} else if(secondsPassed==4851)
			{
				rocketControler.mainThrust();
			}
		}
	}

	public static void main(String[] args) {
		launch(args);

	}

	public void closeLoopController(LandingModule rocket, Polygon guiRocket, double xModule, double yModule, double distanceToSurface){

		//System.out.println("x : "+rocket.x + " y = " + rocket.y);

		Point TitanPosition = new Point(s.planetaryObjects.get(0).x, s.planetaryObjects.get(0).y);
		final double STARTING_POSITION = 0;
		final double POSITION_DERIVATION = 1;

		final double STARTING_ANGLE = -90;
		final double ANGLE_DERIVATION = 1;

		Point modulePosition = new Point(rocket.x, rocket.y);
		//System.out.println("angle = " + modulePosition.getAngle(TitanPosition));
		System.out.println("velY : " + rocket.velY );

		if (modulePosition.getAngle(TitanPosition) < STARTING_ANGLE - ANGLE_DERIVATION && modulePosition.getAngle(TitanPosition) > STARTING_ANGLE) {
			//Need to adjust angle from right side
			//System.out.println("right angle correction");
			rightThrust(guiRocket, rocket, xModule, yModule);
		} else if (modulePosition.getAngle(TitanPosition) > STARTING_ANGLE + ANGLE_DERIVATION && modulePosition.getAngle(TitanPosition) < STARTING_ANGLE) {
			//Need to thrust left
			//System.out.println("left angle correction");
			leftThrust(guiRocket, rocket, xModule, yModule);
		}else{}

		if (rocket.x > STARTING_POSITION + POSITION_DERIVATION){
			//Need to thrust right
			rocket.leftThrustAndMove();
		} else if (rocket.x < STARTING_POSITION - POSITION_DERIVATION) {
			//Need to thrust left
			rocket.rightThrustAndMove();
		} else {}

		if (distanceToSurface  < 3500000 && distanceToSurface > 2600000){
			if (rocket.velY < -100){
				System.out.println("main thrust");
				rocket.mainThrust();
			}
		}else if(distanceToSurface < 2600000 && distanceToSurface > 2501000){
			if (rocket.velY < -50){
				System.out.println("main thrust 25 ");
				rocket.mainThrust();
			}
		}else if (distanceToSurface < 2501000 && distanceToSurface > 2500500){
			if (rocket.velY < -20){
				System.out.println("main thrust half ");
				rocket.mainThrust();
			}
		} else if(distanceToSurface < 2500500 && distanceToSurface > 2500150){
			if (rocket.velY < -10){
				System.out.println("main thrust half ");
				rocket.mainThrust();
			}
		} else if(distanceToSurface < 2500150 && distanceToSurface > 2500015){
			if (rocket.velY < -5){
				System.out.println("main thrust half ");
				rocket.mainThrust();
			}
		} else if(distanceToSurface < 2500015 && distanceToSurface > 2500007){
			if (rocket.velY < -1){
				System.out.println("main thrust half ");
				rocket.mainThrust();
			}
		} else if(distanceToSurface < 2500007){
			if (rocket.velY < -0.1){
				System.out.println(distanceToSurface);
				rocket.mainThrusthalf();
			}
		}


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
			windSpeed = 5+Math.random()*10;
		}
		if(distanceToSurface>100000 && distanceToSurface<500000) {
			Am = Math.pow(10, 0);
			windSpeed = 10+Math.random()*20;
		}

		double windForce = S*Am*windSpeed*windSpeed;

		double acc = windForce/rocket.getMass();
		double vel=acc*SolarSystem.timestep;
		double distance = vel*SolarSystem.timestep;

		dX = Math.sin(-(rocket.angle + (Math.PI / 2))) * distance;
		dY = Math.cos(rocket.angle + Math.PI / 2) * distance;

		rocket.velX += dX/SolarSystem.timestep;
		rocket.velY += dY/SolarSystem.timestep;
	}
	public void leftThrust(Polygon rocket, LandingModule rocketControler, double xModule, double yModule)
	{
		rocketControler.leftThrust();
		rocket.getTransforms().add(new Rotate(rocketControler.angle, xModule, yModule));
	}
	public void rightThrust(Polygon rocket, LandingModule rocketControler, double xModule, double yModule)
	{
		rocketControler.rightThrust();
		rocket.getTransforms().add(new Rotate(rocketControler.angle, xModule, yModule));
	}
}