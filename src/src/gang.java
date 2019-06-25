import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import javafx.scene.shape.Circle;

public class gang extends Application{
	int offX=600, offY=-1900;
	int oldOffX=600, oldOffY=600;
	double scale=10*Math.pow(10,-4);
	double landerScaleX=10*Math.pow(10,-1);
	double landerScaleY=10*Math.pow(10,-4);
	int fstClickX, fstClickY;
	SolarSystem wholeSystem = new SolarSystem("wholeSystem");
	SolarSystem landingSystem = new SolarSystem("landingSystem");
	int zoommax=0;
	double diameter=5148000*scale;
	PIDController pidController = new PIDController(0.6, 175);
	public static Group root = new Group();
	int iterationsPassed=0;
	boolean colorChangeForLanderTrajectory=false;

	ArrayList<Sphere> planets = new ArrayList<Sphere>();

	@Override
	public void start(Stage primaryStage) {

		makePlanets();
		LandingModule rocketControler = (LandingModule)landingSystem.planetaryObjects.get(1);
		Polygon rocket = new Polygon();
		double xModule = offX-diameter/2+landingSystem.planetaryObjects.get(1).getX()*scale;
		double yModule = offY-diameter/2+landingSystem.planetaryObjects.get(1).getY()*scale;
		double xModuleLeft = xModule+20;
		double yModuleLeft = yModule-20;
		double xModuleRight = xModule-20;
		double yModuleRight = yModule-20;


		rocket.getPoints().addAll(new Double[]{
				xModule, yModule,
				xModuleRight, yModuleRight,
				xModuleLeft, yModuleLeft });
		rocket.setFill(Color.RED);

		

		Timeline timeline = new Timeline(
				new KeyFrame(Duration.millis(2), t -> {
					//Put here the code which is supposed to be repeated(or check the x,y coordinates and decide when to thrust)
					double distanceToSurface = Math.sqrt(rocketControler.getX()*rocketControler.getX() + rocketControler.getY()*rocketControler.getY());
					//System.out.println("x " + rocketControler.getX());
					//System.out.println("y " + rocketControler.getY());
					/*if(rocketControler.getY()<1)
					//if(distanceToSurface<=1)
					{
						try
						{
							System.out.println("d = " + distanceToSurface);
							System.out.println("Iterations " + iterationsPassed);
							System.out.println("Seconds passed " + iterationsPassed*landingSystem.timestep);
							System.out.println("x " + rocketControler.getX());
							System.out.println("y " + rocketControler.getY());
							System.out.println("Vel x " + rocketControler.getVelX());
							System.out.println("Vel y " + rocketControler.getVelY());
							System.out.println("Angle " + rocketControler.getAngle());
							Thread.sleep(20000);
						}
						catch(InterruptedException ex)
						{
							Thread.currentThread().interrupt();
						}
					}
					*/


					if(iterationsPassed<38456) {
						landingSystem.updatePositions();

						controllerDescSystem(rocketControler, distanceToSurface, rocketControler.getX(), rocket, xModule, yModule);
						if(iterationsPassed==38455)
						{
							System.out.println(rocketControler.getBurnedFuel());
							colorChangeForLanderTrajectory=true;
						}
					}
					if(iterationsPassed>40000)
					{
						landingSystem.updatePositions();

						pidController.setFinalXValue(500);
						controllerAscSystem(rocketControler, distanceToSurface, rocketControler.getX(), rocket, xModule, yModule);
					}
					if(iterationsPassed==80000)
					{
						System.out.println(rocketControler.getBurnedFuel());
						colorChangeForLanderTrajectory=false;
					}

					iterationsPassed++;

					rocket.setTranslateX(landingSystem.planetaryObjects.get(1).getX()*landerScaleX+diameter/2);
					rocket.setTranslateY(landingSystem.planetaryObjects.get(1).getY()*landerScaleY+diameter/2);

					printTrajectory(rocket.getTranslateX()-diameter/2+600,rocket.getTranslateY()-diameter/2+100, colorChangeForLanderTrajectory);

					for(int j=0;j<planets.size();j++)
					{
						planets.get(j).setTranslateX(offX+landingSystem.planetaryObjects.get(j).getX()*scale);
						planets.get(j).setTranslateY(offY+landingSystem.planetaryObjects.get(j).getY()*scale-3315);
					}
				})
		);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

		
		root.getChildren().addAll(planets);
		root.getChildren().add(rocket);
		

		Scene scene = new Scene(root, 1200, 1200);
		scene.setFill(Color.BLACK);

		Camera camera = new PerspectiveCamera(false);

		scene.setCamera(camera);

		camera.translateZProperty().set(-4000);
		camera.translateYProperty().set(1000);
		//camera.setNearClip(1);
		camera.setFarClip(500000);

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
				planet.setTranslateX((int) (offX + diameter / 2 ));
				planet.setTranslateY((int) (offY + diameter / 2 ));

				planet.setRadius(diameter);


				if (p.getName().equals("centerTitan")) {

					PhongMaterial material = new PhongMaterial();
					//material.setDiffuseMap(new Image("/titan.jpg"));
					//planet.setMaterial(material);
				}

				planets.add(planet);
			}
		}
	}

	public static void main(String[] args) {
		launch(args);

	}

	public void controllerAscSystem(LandingModule rocket, double distanceToSurface, double currentXvalue, Polygon rocketGUI, double xModule, double yModule)
	{
		yAscControl(rocket, distanceToSurface);
		xControl(rocket, currentXvalue, rocketGUI, xModule, yModule);
	}

	public void controllerDescSystem(LandingModule rocket, double distanceToSurface, double currentXvalue, Polygon rocketGUI, double xModule, double yModule)
	{
		yDescControl(rocket, distanceToSurface);
		xControl(rocket, currentXvalue, rocketGUI, xModule, yModule);
	}

	public void yAscControl(LandingModule rocket, double distanceToSurface)
	{
		double thrustPower = -(2350*rocket.getVelY()) - (4*(rocket.getY()-2000000));
		if(distanceToSurface<30)
		{
			thrustPower+=12500;
			if(distanceToSurface<22)
			{
				thrustPower+=18000;
			}
		}
		rocket.mainThrust(thrustPower);
	}

	public void yDescControl(LandingModule rocket, double distanceToSurface)
	{
		double thrustPower = -(2350*rocket.getVelY()) - (4*rocket.getY());
		if(distanceToSurface<30)
		{
			thrustPower+=12500;
			if(distanceToSurface<22)
			{
				thrustPower+=18000;
			}
		}
		rocket.mainThrust(thrustPower);
	}


	public void xControl(LandingModule rocket, double currentXvalue, Polygon rocketGUI, double xModule, double yModule)
	{

		double thrust = pidController.executePID(currentXvalue);
		if(thrust>0 && rocket.getAngle()>(-Math.PI/12))
		{
			rightThrust(rocketGUI, rocket, xModule, yModule);
		}
		if(thrust<0  && rocket.getAngle()<(Math.PI/12))
		{
			leftThrust(rocketGUI, rocket, xModule, yModule);
		}
	}

	public void printTrajectory(double x, double y, boolean colorChanger){
		Circle trajectoryDot = new Circle();
		trajectoryDot.setCenterX(x);
		trajectoryDot.setCenterY(y);
		trajectoryDot.setRadius(2);
		if(colorChanger==false) {
			trajectoryDot.setFill(javafx.scene.paint.Color.RED);
		} else {
			trajectoryDot.setFill(javafx.scene.paint.Color.BLUE);
		}
		root.getChildren().add(trajectoryDot);
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

	public void leftThrust(Polygon rocket, LandingModule rocketControler, double xModule, double yModule)
	{
		rocketControler.leftThrust();
		rocket.getTransforms().add(new Rotate(rocketControler.getAngle(), xModule, yModule));
	}
	public void rightThrust(Polygon rocket, LandingModule rocketControler, double xModule, double yModule)
	{
		rocketControler.rightThrust();
		rocket.getTransforms().add(new Rotate(rocketControler.getAngle(), xModule, yModule));
	}
}