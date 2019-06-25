import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;
/*
- Modelling the planetary system using the 4th order Yoshida integrator (an upgraded version of the leapfrog method).
- The scale: distance = m, mass = kg, time = s, velocity = m/s, acceleration = m/s^2

// Change the scale at line 247 to Math.pow(10,-10) to zoom out.

*/
public class SolarSystem {
	private static final double G = 6.67408 * Math.pow(10,-11);;//universal gravitational constant

	// Below are some constants needed for this method
	private static final double w0 = (-Math.pow(2,1/3))/(2-Math.pow(2,1/3));
	private static final double w1 = 1/(2-Math.pow(2,1/3));
	private static final double c1 = w1/2;
	private static final double c2 = (w0+w1)/2;
	private ArrayList<CelestialBody> planetaryObjects = new ArrayList<CelestialBody>();
	private float timestep=5000f;
	private double lastPosition = 0; //keeps track of the smallest distance between the spacecraft and Titan
	private boolean test = true;	// Used to calculate the smallest distance between the rocket and Titan
	private boolean timeStepFirstChange=false; //The following boolean variables are used to
	private boolean timeStepSecndChange=false; //slow down progressively the timestep with respect to the distance bewteen rocket and Titan
	private boolean timeStepThirdChange=false;
	private boolean timeStepFourthChange=false;
	private boolean timeStepFifthChange=false;
	private boolean timeStepSixthChange=false;
	private boolean timeStepFinalChange=false;
	private static boolean closeToTitan= false;
	//private float countTimestep=0f;
	//private final float TRAVEL_TIME=126144000f; //4 years in seconds

	public SolarSystem(){

		CelestialBody Sun = new Planet("Sun",1.989 *Math.pow(10,30),0,0,0,0);

		CelestialBody Earth = new Planet("Earth", 5.972*Math.pow(10,24), -149010862150.01596069, -2126396301.1637141705, -62.711922803909892821, -29884.912428149531479);
		CelestialBody Moon = new Planet("Moon", 7.34767309 *Math.pow(10,22), -149362685990.11404419, -2212378435.2487487793, 154.04965501127904304, -30946.618778578722413);
		CelestialBody Mars = new Planet("Mars",6.39 *Math.pow(10,23),23242872211.678588867,231499512113.57745361, -23192.796815354045975, 4479.3215975889943365);
		CelestialBody Phobos = new Planet("Phobos", 1.0659*Math.pow(10,16), 23236713780.293193817, 231493148759.69039917, -21904.085359357279231, 2922.1678946024862853);
		CelestialBody Deimos = new Planet("Deimos", 1.4762*Math.pow(10,15), 23260708397.058792114, 231513037031.97982788, -23865.346667643512774,5577.8591509278139711);

		CelestialBody Jupiter = new Planet("Jupiter",1.898 *Math.pow(10,27),-235672845845.29763794,-761001269458.0333252, 12333.612635551397034, -3252.7828483488401616);
		CelestialBody Io = new Planet("Io", 8.9319*Math.pow(10,22), -235534753847.68914795, -761398018637.74267578, 28776.872256098788057, 2415.5909830461550882);
		CelestialBody Europa = new Planet("Europa", 4.799844*Math.pow(10,22), -235707989708.32266235, -760337695952.91906738, -1510.9909273330456472, -3932.491101951766268);
		CelestialBody Ganymede = new Planet("Ganymede", 1.4819*Math.pow(10,23), -235779309153.56542969, -762063853242.70800781, 23178.548350762626796, -4323.7334045707193582);
		CelestialBody Callisto = new Planet("Callisto", 1.075938*Math.pow(10,23), -234831550521.73205566, -762679789117.9362793, 19662.909081447385688, 476.03519297275602185);

		CelestialBody Saturn = new Planet("Saturn",5.683 *Math.pow(10,26),354759353240.08221436,-1461948830848.2719727, 8867.8273592403966177, 2247.0444129401830651);
		CelestialBody Tethys = new Planet("Tethys", 6.17449*Math.pow(10,20), 354785138137.6774292, -1461689707592.3195801, -2377.8867427412342295, 3679.1153207793140609);
		CelestialBody Mimas = new Planet("Mimas", 3.7493*Math.pow(10,19) , 354929590317.0680542, -1461896423417.3325195, 3225.8538016469460672, 14125.074214980997567);
		CelestialBody Enceladus = new Planet("Enceladus", 1.08022*Math.pow(10,20), 354799787341.86053467, -1462158801299.2456055, 21245.580993242823752, 3606.8459847855569933);
		CelestialBody Dione = new Planet("Dione", 1.095452*Math.pow(10,21) , 354460272013.53613281, -1462138333984.7424316, 14914.964853148958355, -5087.5637844324646721);
		CelestialBody Rhea = new Planet("Rhea", 2.306518*Math.pow(10,21) , 354440944762.03717041, -1461564720465.1967773, 2140.5542062106133017, -2053.6060807788026068);
		CelestialBody Titan = new Planet("Titan", 1.3452*Math.pow(10,23), 353742492774.33044434, -1462539028125.2316895, 12081.93089270526616, -1813.839579262785719);

		CelestialBody Uranus = new Planet("Uranus",8.681 *Math.pow(10,25) ,2520721625280.1430664,1570265330931.7612305, -3638.6056156374625061, 5459.4683505725070063);
		CelestialBody Neptune = new Planet("Neptune",1.024 *Math.pow(10,26), 4344787551365.7446289,-1083664718815.0178223, 1292.6328876547368054, 5305.0241404888956822);

		CelestialBody Mercury = new Planet("Mercury", 3.3011*Math.pow(10,23),-58432374622.839942932,-21437816633.49621582, 6693.4979641187965171, -43627.083379485586192);
		CelestialBody Venus = new Planet("Venus",4.8675*Math.pow(10,24),-2580458154.9969267845,-108701123911.93000793, 34777.284216476567963, -961.21239989254672764);

		CelestialBody rocket = new Rocket("rocket",30000, -149010862150.01596069+6371000, -2126396301.1637141705, 11714.96540719167, -6.075583325887161e+04 );
																										//11101.36840719167, -6.075583325887161e+03
		// From the Escape velocity of the earth, reach anywhere at (h(2000km)+r(radius of titan)) of the surface of Titan, with the velocity decreasing until 5043Km/h
		planetaryObjects = new ArrayList<CelestialBody>();
		planetaryObjects.add(Sun);
		planetaryObjects.add(Mars);
		planetaryObjects.add(Phobos);
		planetaryObjects.add(Deimos);

		planetaryObjects.add(Earth);
		planetaryObjects.add(Moon);

		planetaryObjects.add(Jupiter);
		planetaryObjects.add(Io);
		planetaryObjects.add(Europa);
		planetaryObjects.add(Ganymede);
		planetaryObjects.add(Callisto);

		planetaryObjects.add(Saturn);
		planetaryObjects.add(Tethys);
		planetaryObjects.add(Mimas);
		planetaryObjects.add(Enceladus);
		planetaryObjects.add(Dione);
		planetaryObjects.add(Rhea);
		planetaryObjects.add(Titan);


		planetaryObjects.add(Uranus);

		planetaryObjects.add(Neptune);

		planetaryObjects.add(Mercury);

		planetaryObjects.add(Venus);

		planetaryObjects.add(rocket);



	}

	void firstUpdate() {
		for (int i=0;i<planetaryObjects.size();i++) {
			// x_i_1 = x_i + c1*v_i*timestep
			planetaryObjects.get(i).oldX =planetaryObjects.get(i).x+ timestep * c1*planetaryObjects.get(i).velX;
			planetaryObjects.get(i).oldY =planetaryObjects.get(i).y+ timestep * c1*planetaryObjects.get(i).velY;

		}
	}
	void secondUpdate() {

		// a -> a(x_i_1)
		double dx, dy, dz, D, A;

		for (int i = 0; i < planetaryObjects.size(); i++) {

			planetaryObjects.get(i).accX = 0;
			planetaryObjects.get(i).accY = 0;

				for (int j = 0; j < planetaryObjects.size(); j++) {
					if (i != j) {
						dx = planetaryObjects.get(j).oldX - planetaryObjects.get(i).oldX;
						dy = planetaryObjects.get(j).oldY - planetaryObjects.get(i).oldY;
						D = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
						A = G * planetaryObjects.get(j).mass / Math.pow(D, 2);
						planetaryObjects.get(i).accX += dx * A / D;
						planetaryObjects.get(i).accY += dy * A / D;
					}
				}

		}

	}
	void thirdUpdate()
			// v_i_1 = v_i+ w1*a*timestep
	{
		for(int i=0; i<planetaryObjects.size();i++)
		{
			planetaryObjects.get(i).velX+= w1*planetaryObjects.get(i).accX*timestep;
			planetaryObjects.get(i).velY+= w1*planetaryObjects.get(i).accY*timestep;
		}
	}

	void fourthUpdate() {
		for (int i=0;i<planetaryObjects.size();i++) {
			// x_i_2 = x_i_1 + c2*v_i_1*timestep
			planetaryObjects.get(i).oldX += timestep * c2*planetaryObjects.get(i).velX;
			planetaryObjects.get(i).oldY += timestep * c2*planetaryObjects.get(i).velY;

		}
	}

	void fifthUpdate() {

		// update a -> a(x_i_2)
		double dx, dy, dz, D, A;

		for (int i = 0; i < planetaryObjects.size(); i++) {

			planetaryObjects.get(i).accX = 0;
			planetaryObjects.get(i).accY = 0;

			for (int j = 0; j < planetaryObjects.size(); j++) {
				if (i != j) {
					dx = planetaryObjects.get(j).oldX - planetaryObjects.get(i).oldX;
					dy = planetaryObjects.get(j).oldY - planetaryObjects.get(i).oldY;
					D = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
					A = G * planetaryObjects.get(j).mass / Math.pow(D, 2);
					planetaryObjects.get(i).accX += dx * A / D;
					planetaryObjects.get(i).accY += dy * A / D;
				}
			}

		}
	}

	void sixthUpdate() {

		// v_i_2 = v_i_1+ w0*a*timestep
		for(int i=0; i<planetaryObjects.size();i++)
		{
			planetaryObjects.get(i).velX+= w0*planetaryObjects.get(i).accX*timestep;
			planetaryObjects.get(i).velY+= w0*planetaryObjects.get(i).accY*timestep;
		}
	}

	void seventhUpdate() {
		for (int i=0;i<planetaryObjects.size();i++) {
			// x_i_3 = x_i_2 + c2*v_i_2*timestep
			planetaryObjects.get(i).oldX += timestep * c2*planetaryObjects.get(i).velX;
			planetaryObjects.get(i).oldY += timestep * c2*planetaryObjects.get(i).velY;

		}
	}
	void eightUpdate() {
		// a -> a(x_i_3)
		double dx, dy, dz, D, A;

		for (int i = 0; i < planetaryObjects.size(); i++) {

			planetaryObjects.get(i).accX = 0;
			planetaryObjects.get(i).accY = 0;

			for (int j = 0; j < planetaryObjects.size(); j++) {
				if (i != j) {
					dx = planetaryObjects.get(j).oldX - planetaryObjects.get(i).oldX;
					dy = planetaryObjects.get(j).oldY - planetaryObjects.get(i).oldY;
					D = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
					A = G * planetaryObjects.get(j).mass / Math.pow(D, 2);
					planetaryObjects.get(i).accX += dx * A / D;
					planetaryObjects.get(i).accY += dy * A / D;
				}
			}

		}

	}
	void ninethUpdate() {
		// v_i_3 = v_i_2 + w1*a*timestep
		for(int i=0; i<planetaryObjects.size();i++)
		{
			planetaryObjects.get(i).velX+= w1*planetaryObjects.get(i).accX*timestep;
			planetaryObjects.get(i).velY+= w1*planetaryObjects.get(i).accY*timestep;
		}
	}

	void move() {
		// x_(i+1) = x_i_3 + c1*v_i_3*timestep;
		for (int i=0;i<planetaryObjects.size();i++) {
			planetaryObjects.get(i).x = planetaryObjects.get(i).oldX+ timestep * c1*planetaryObjects.get(i).velX;
			planetaryObjects.get(i).y = planetaryObjects.get(i).oldY+ timestep * c1*planetaryObjects.get(i).velY;


		}
	}

	//Check the distance between Titan and the rocket and slows down the timestep as the rocket gets close to it.
	public void check(){
		Point T = new Point(planetaryObjects.get(17).x, planetaryObjects.get(17).y);
		Point R = new Point(planetaryObjects.get(22).x, planetaryObjects.get(22).y);

		if (R.checkDistance(T)/1000 <= 40000000 && !timeStepFirstChange)
		{
			timestep = 2500;
			System.out.println("Timestep=2500");
			timeStepFirstChange=true;
		}
		if (R.checkDistance(T)/1000 <= 20000000 && !timeStepSecndChange)
		{
			timestep = 1000f;
			System.out.println("Timestep=1000");
			timeStepSecndChange=true;
		}
		if (R.checkDistance(T)/1000 <= 10000000 && !timeStepThirdChange)
		{
			timestep=500f;
			System.out.println("Timestep=500");
			timeStepThirdChange=true;
		}
		if(R.checkDistance(T)/1000 <= 5000000 && !timeStepFourthChange)
		{
			timestep=100f;
			System.out.println("Timestep=100");
			timeStepFourthChange=true;
			closeToTitan= true;
		}
		if(R.checkDistance(T)/1000 <= 2500000 && !timeStepFifthChange)
		{
			timestep=50f;
			timeStepFifthChange=true;
			System.out.println("Timestep=50");
		}

		if(R.checkDistance(T)/1000 <= 500000 && !timeStepSixthChange)
		{
			timestep=10f;
			timeStepSixthChange=true;
			System.out.println("Timestep=10");
			closeToTitan= true;
		}
		if(R.checkDistance(T)/1000 <= 100000 && !timeStepFinalChange)
		{
			timestep=5f;
			timeStepFinalChange=true;
			System.out.println("Timestep=5");

		}
	 }
	void updatePositions(){

		if(!timeStepFinalChange)
			check();

		/* //Change instantly speed of the rocket to enter into orbit
		if(timeStepFinalChange)
		{
			Point T = new Point(planetaryObjects.get(17).x, planetaryObjects.get(17).y);
			Point R = new Point(planetaryObjects.get(22).x, planetaryObjects.get(22).y);
			if(R.checkDistance(T)/1000 <=25000)
			{
				Rocket r=(Rocket) planetaryObjects.get(22);
				planetaryObjects.get(22).velX=planetaryObjects.get(17).velX;
				planetaryObjects.get(22).velY=0;
			}
		}*/

		 //print the smallest distance between Titan and Rocket during journey
	 	else if(test)
		{
			Point T = new Point(planetaryObjects.get(17).x, planetaryObjects.get(17).y);
			Point R = new Point(planetaryObjects.get(22).x, planetaryObjects.get(22).y);
			double position = R.checkDistance(T);
			if (lastPosition < position && test && lastPosition != 0)
			{
				System.out.println();
				System.out.println("Last position: " +lastPosition/1000);
				System.out.println("Next position: "+position/1000);
				test = false;
			}
			lastPosition = position;
		}

		firstUpdate();
		secondUpdate();
		thirdUpdate();
		fourthUpdate();
		fifthUpdate();
		sixthUpdate();
		seventhUpdate();
		eightUpdate();
		ninethUpdate();
		move();


		/* get the position of titan after a certain time t
		----------------------------------------------------
		countTimestep+=timestep;
		if(countTimestep==TRAVEL_TIME)
		{
			System.out.println(planetaryObjects.get(17).name + "'s x position after 4years: "+ planetaryObjects.get(17).x);
			System.out.println(planetaryObjects.get(17).name + "'s y position after 4years: "+planetaryObjects.get(17).y);
		}*/
	}

	ArrayList<CelestialBody> getObjects()
	{
		return planetaryObjects;
	}

	float getTimestep()
	{
		return timestep;
	}

	void setTimeStep(float nStep)
	{
		timestep=nStep;
	}

	public boolean getCloseToTitan()
	{
		return closeToTitan;
	}

	public void setCloseToTitan(boolean value)
	{
		closeToTitan=value;
	}
}
