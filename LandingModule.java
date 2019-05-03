public class LandingModule extends Planet{

    //Simulation of the motion of a spacecraft with only yaw rotation considered

    //the angle of rotation of the spaceship
    private int angle=0;

    //torque from side thrusters
    private double v;

    //mass of the landing module(same as Huygens)
    private double mass=309;

    public LandingModule(String name, double mass, double x, double y, double velX, double velY)
    {
        super(name, mass, x, y, velX, velY);
    }

    //reference for the power of the thrusters
    public static int leftThrust()
    {
        //add fuel consumption
        int angle=5;
        return angle;

        //rotation around the barycenter (in this case the point where the gravitational forces and thrusts are applied)
        //rocket.transform(new Affine(new Rotate(s.planetaryObjects.get(22).leftThrust, xModule, yModule)));

    }

    public static int rightThrust()
    {
        //add fuel consumption
        int angle=5;
        return angle;
    }

    public void mainThrust()
    {
        //add fuel consumption
        double a =440/mass;
        velX=velX+a*SolarSystem.timestep;
    }
}
