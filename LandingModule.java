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
    public static void leftThrust()
    {
        //add fuel consumption
        int angle=5;
        return angle;

        //rotation around the barycenter (in this case the point where the gravitational forces and thrusts are applied)
        //rocket.transform(new Affine(new Rotate(s.planetaryObjects.get(22).leftThrust, xModule, yModule)));

    }

    public static void rightThrust()
    {
        //add fuel consumption
        int angle=5;
        return angle;
    }

    public void mainThrust()
    {
        //add fuel consumption
        double a =440/mass;
        velY=velY+a*SolarSystem.timestep;
    }


    //thrust the side thruster if it's going out of is direction
    public void thrust(){

        int toThrust = check(module);
        double a =440/module.mass;

        if (toThrust == 1){
            rightThrust();

        }else if (toThrust == 2){
            leftThrust();
        }
    }


    //Need to get the position of Titan
    Point p = new Point(SolarSystem.Titan.x, SolarSystem.Titan.y);
    public int check(){
        final double DERIVATION = 2;
        final double STARTING_ANGLE = 0;
        Point modulePosition = new Point(module.x, module.y);
        if (this.getAngle(p) > STARTING_ANGLE + DERIVATION)
            //rigth side
            return 1;
        else if (this.getAngle(p) < STARTING_ANGLE + DERIVATION)
            //left side
            return 2;
        else
            return 0;
    }

    public void reduceSpeedForLanding(){
        final double LANDING_SPEED = 0.5;
        final double DISTANCE_WHEN_NEED_TO_REDUCE_SPEED = 300;
        double a =440/module.mass;

        //thrust the main thruster to reduce speed, don't change angle of landing
        if (module.getDistanceFromTitan() < DISTANCE_WHEN_NEED_TO_REDUCE_SPEED){
            if (module.velY() > LANDING_SPEED){
                module.velY= module.velY+a*SolarSystem.timestep;
            }
        }
    }
}
