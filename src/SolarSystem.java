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
	static final double G = 6.67408 * Math.pow(10,-11);;//universal gravitational constant

	// Below are some constants needed for this method
	static final double w0 = (-Math.pow(2,1/3))/(2-Math.pow(2,1/3));
	static final double w1 = 1/(2-Math.pow(2,1/3));
	static final double c1 = w1/2;
	static final double c2 = (w0+w1)/2;
	ArrayList<Planet> planetaryObjects = new ArrayList<Planet>();
	static float timestep=1f;

	public SolarSystem(){

		Planet Sun = new Planet("Sun",1.989 *Math.pow(10,30),0,0,0,0);

		Planet Earth = new Planet("Earth", 5.972*Math.pow(10,24), -149010862150.01596069, -2126396301.1637141705, -62.711922803909892821, -29884.912428149531479);
		Planet Moon = new Planet("Moon", 7.34767309 *Math.pow(10,22), -149362685990.11404419, -2212378435.2487487793, 154.04965501127904304, -30946.618778578722413);
		Planet Mars = new Planet("Mars",6.39 *Math.pow(10,23),23242872211.678588867,231499512113.57745361, -23192.796815354045975, 4479.3215975889943365);
		Planet Phobos = new Planet("Phobos", 1.0659*Math.pow(10,16), 23236713780.293193817, 231493148759.69039917, -21904.085359357279231, 2922.1678946024862853);
		Planet Deimos = new Planet("Deimos", 1.4762*Math.pow(10,15), 23260708397.058792114, 231513037031.97982788, -23865.346667643512774,5577.8591509278139711);

		Planet Jupiter = new Planet("Jupiter",1.898 *Math.pow(10,27),-235672845845.29763794,-761001269458.0333252, 12333.612635551397034, -3252.7828483488401616);
		Planet Io = new Planet("Io", 8.9319*Math.pow(10,22), -235534753847.68914795, -761398018637.74267578, 28776.872256098788057, 2415.5909830461550882);
		Planet Europa = new Planet("Europa", 4.799844*Math.pow(10,22), -235707989708.32266235, -760337695952.91906738, -1510.9909273330456472, -3932.491101951766268);
		Planet Ganymede = new Planet("Ganymede", 1.4819*Math.pow(10,23), -235779309153.56542969, -762063853242.70800781, 23178.548350762626796, -4323.7334045707193582);
		Planet Callisto = new Planet("Callisto", 1.075938*Math.pow(10,23), -234831550521.73205566, -762679789117.9362793, 19662.909081447385688, 476.03519297275602185);

		Planet Saturn = new Planet("Saturn",5.683 *Math.pow(10,26),354759353240.08221436,-1461948830848.2719727, 8867.8273592403966177, 2247.0444129401830651);
		Planet Tethys = new Planet("Tethys", 6.17449*Math.pow(10,20), 354785138137.6774292, -1461689707592.3195801, -2377.8867427412342295, 3679.1153207793140609);
		Planet Mimas = new Planet("Mimas", 3.7493*Math.pow(10,19) , 354929590317.0680542, -1461896423417.3325195, 3225.8538016469460672, 14125.074214980997567);
		Planet Enceladus = new Planet("Enceladus", 1.08022*Math.pow(10,20), 354799787341.86053467, -1462158801299.2456055, 21245.580993242823752, 3606.8459847855569933);
		Planet Dione = new Planet("Dione", 1.095452*Math.pow(10,21) , 354460272013.53613281, -1462138333984.7424316, 14914.964853148958355, -5087.5637844324646721);
		Planet Rhea = new Planet("Rhea", 2.306518*Math.pow(10,21) , 354440944762.03717041, -1461564720465.1967773, 2140.5542062106133017, -2053.6060807788026068);
		Planet Titan = new Planet("Titan", 1.3452*Math.pow(10,23), 353742492774.33044434, -1462539028125.2316895, 12081.93089270526616, -1813.839579262785719);

		Planet Uranus = new Planet("Uranus",8.681 *Math.pow(10,25) ,2520721625280.1430664,1570265330931.7612305, -3638.6056156374625061, 5459.4683505725070063);
		Planet Neptune = new Planet("Neptune",1.024 *Math.pow(10,26), 4344787551365.7446289,-1083664718815.0178223, 1292.6328876547368054, 5305.0241404888956822);

		Planet Mercury = new Planet("Mercury", 3.3011*Math.pow(10,23),-58432374622.839942932,-21437816633.49621582, 6693.4979641187965171, -43627.083379485586192);
		Planet Venus = new Planet("Venus",4.8675*Math.pow(10,24),-2580458154.9969267845,-108701123911.93000793, 34777.284216476567963, -961.21239989254672764);

		LandingModule Lander = new LandingModule("Lander", 309, 0, 4500000, 0, 0);
		Planet centerTitan = new Planet("centerTitan",1.3452*Math.pow(10,23),0,0,0,0);


		planetaryObjects = new ArrayList<Planet>();
		/*planetaryObjects.add(Sun);
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
		*/

		//planetaryObjects.add(centerTitan);
		planetaryObjects.add(Lander);

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
							//A = 1.352;
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
					//A = 1.352;
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
					//A = 1.352;
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
	void updatePositions(){
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
	}

}


