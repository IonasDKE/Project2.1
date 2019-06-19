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

import java.sql.SQLOutput;
import java.util.ArrayList;
import javafx.scene.control.Label;

public class gang extends Application{
    int offX=600, offY=600;
    int oldOffX=600, oldOffY=600;
    double scale=10*Math.pow(10,-10);
    int fstClickX, fstClickY;
    SolarSystem s = new SolarSystem();
    ArrayList<CelestialBody> planetaryObjects=s.getObjects();
    int zoommax=0;
    double diameter=3;
    ArrayList<Sphere> planets = new ArrayList<Sphere>();
    ArrayList<Label> names = new ArrayList<Label>();
    static boolean launched = false;
    int counter =0;
    RocketLauncher launcher = new RocketLauncher();

    @Override
    public void start(Stage primaryStage) {

        makePlanets();
		/*Polygon rocket = new Polygon();
		double xModule = offX-diameter/2+planetaryObjects.get(17).x*scale;
		double yModule = offY-diameter/2+planetaryObjects.get(17).y*scale;
		double xModuleLeft = offX-diameter/2+planetaryObjects.get(17).x*scale+10;
		double yModuleLeft = offY-diameter/2+planetaryObjects.get(17).y*scale+20;
		double xModuleRight = offX-diameter/2+planetaryObjects.get(17).x*scale+20;
		double yModuleRight = offY-diameter/2+planetaryObjects.get(17).y*scale+10;
		System.out.println(yModuleLeft);
		System.out.println(yModuleRight);


        rocket.getPoints().addAll(new Double[]{
            xModule, yModule,
            xModuleRight, yModuleRight,
            xModuleLeft, yModuleLeft });
        rocket.setFill(Color.RED);

        rocket.setTranslateY(10);
*/

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(20), t -> {
                    //Put here the code which is supposed to be repeated(or check the x,y coordinates and decide when to thrust)

                    for(int j=0;j<planets.size();j++) {
                        counter++;
                        s.updatePositions();
                        planets.get(j).setTranslateX(offX - diameter / 2 + planetaryObjects.get(j).x * scale);
                        planets.get(j).setTranslateY(offY - diameter / 2 + planetaryObjects.get(j).y * scale);
                        names.get(j).setTranslateX(offX - diameter / 2 + planetaryObjects.get(j).x * scale);
                        names.get(j).setTranslateY(offY - diameter / 2 + planetaryObjects.get(j).y * scale);

                        if (planetaryObjects.get(j).name.equals("rocket")) {
                            launcher.checkSpeed(planetaryObjects.get(j));
                        }
                    }
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Group root = new Group();
        root.getChildren().addAll(planets);
        //root.getChildren().addAll(rocket);
        root.getChildren().addAll(names);


        Scene scene = new Scene(root, 1200, 1200);
        scene.setFill(Color.BLACK);

        Camera camera = new PerspectiveCamera(false);

        scene.setCamera(camera);

        camera.translateZProperty().set(-500);
        //camera.setNearClip(1);
        camera.setFarClip(5000);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case W:
                    s.timestep += 10;
                    break;
                case Q: //Zooming in
                    if (zoommax<1930){
                        camera.translateZProperty().set(camera.getTranslateZ() + 500);
                        zoommax=zoommax+500;}
                    break;
                case A: //Zooming out
                    camera.translateZProperty().set(camera.getTranslateZ() - 500);
                    zoommax=zoommax-500;
                    break;
                case T: // get's to titan
                    scale=10*Math.pow(10,-8);
                    camera.translateXProperty().set((int)(diameter/2+planetaryObjects.get(11).x*scale));
                    camera.translateYProperty().set((int)(diameter/2+planetaryObjects.get(11).y*scale));

                    camera.translateZProperty().set(500);
                    break;

                case S: // get's to the sun
                    scale=10*Math.pow(10,-10);
                    camera.translateXProperty().set(0);
                    camera.translateYProperty().set(0);
                    scale=10*Math.pow(10,-10);
                    camera.translateZProperty().set(500);
                    break;
                case E: //Get's to earth
                    scale=10*Math.pow(10,-8);
                    camera.translateXProperty().set((int)(diameter/2+planetaryObjects.get(4).x*scale));
                    camera.translateYProperty().set((int)(diameter/2+planetaryObjects.get(4).y*scale));
                    camera.translateZProperty().set(500);
                    break;
                case L: //Launch the rocket
                    if (!launched){
                        SolarSystem system = new SolarSystem();
                        for (int i = 0; i < counter+(int)(6144000/SolarSystem.timestep); i ++){
                            system.updatePositions();
                        }
                        //RocketLauncher launcher = new RocketLauncher();
                        launcher.launchToTitan(s, system.getPlanetList().get(17));
                        launched = true;
                        s.planetaryObjects.add(RocketLauncher.rocket);
                        addRocket(RocketLauncher.rocket);
                        root.getChildren().add(planets.get(planets.size()-1));
                        root.getChildren().add(names.get(names.size()-1));
                    }
                    break;
                case B:
                    SolarSystem system = new SolarSystem();
                    for (int i = 0; i < counter+(int)(6144000/SolarSystem.timestep); i ++){
                        system.updatePositions();
                    }
                    launcher.launchToEarth(system.getPlanetList().get(4), s.getPlanetList().get(22));
                    s.timestep = 500f;

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

        for(CelestialBody p : planetaryObjects)
        {
            if(p.name!="Lander") {
                Sphere planet = new Sphere();
                planet.setTranslateX((int) (offX - diameter / 2 + p.x * scale));
                planet.setTranslateY((int) (offY - diameter / 2 + p.y * scale));
                planet.setRadius(diameter);
                Label text = new Label(p.name);
                text.setTranslateX((int) (offX - diameter / 2 + p.x * scale));
                text.setTranslateY((int) (offY - diameter / 2 + p.y * scale));
                text.setTextFill(Color.WHITE);
                //System.out.println(planet.getCenterX() + " " +  planet.getCenterY());
                names.add(text);
                planets.add(planet);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);

    }

    public void addRocket(CelestialBody p){
        //System.out.println("Rocket added");
        Sphere planet = new Sphere();
        planet.setTranslateX((int) (offX - diameter / 2 + p.x * scale));
        planet.setTranslateY((int) (offY - diameter / 2 + p.y * scale));
        planet.setRadius((diameter));
        Label text = new Label(p.name);
        text.setTranslateX((int) (offX - diameter / 2 + p.x * scale));
        text.setTranslateY((int) (offY - diameter / 2 + p.y * scale));
        text.setTextFill(Color.WHITE);
        //System.out.println(planet.getCenterX() + " " +  planet.getCenterY());
        names.add(text);
        planets.add(planet);
    }
}
