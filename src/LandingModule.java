public class LandingModule extends Planet{

    //Simulation of the motion of a spacecraft with only yaw rotation considered

    //the angle of rotation of the spaceship
    private static double oldAngle=0;
    private static double angle=10;

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
        oldAngle=angle;
        angle += Math.PI / 180;

        //rotation around the barycenter (in this case the point where the gravitational forces and thrusts are applied)
        //rocket.transform(new Affine(new Rotate(s.planetaryObjects.get(22).leftThrust, xModule, yModule)));

    }

    public static void rightThrust()
    {
        //add fuel consumption
        oldAngle=angle;
        angle -= Math.PI / 180;
    }

    public void leftThrustAndMove()
    {
        //add fuel consumption
        double a = 22/mass;
        double vel=a*SolarSystem.timestep;
        double distance = vel*SolarSystem.timestep;
        double dX = Math.sin(-(angle + (Math.PI/2)))*distance;
        double dY = Math.cos(angle + Math.PI/2)*distance;
        velX += dX/SolarSystem.timestep;
        velY += dY/SolarSystem.timestep;
    }
    public void rightThrustAndMove()
    {
        //add fuel consumption
        double a = 22/mass;
        double vel=a*SolarSystem.timestep;
        double distance = vel*SolarSystem.timestep;
        double dX = Math.sin(-(angle - (Math.PI/2)))*distance;
        double dY = Math.cos(angle - Math.PI/2)*distance;
        velX += dX/SolarSystem.timestep;
        velY += dY/SolarSystem.timestep;
    }

    public void mainThrust()
    {
        //add fuel consumption
        double a = 440/mass;
        double vel=a*SolarSystem.timestep;
        double distance = vel*SolarSystem.timestep;
        double dX = Math.sin(-angle)*distance;
        double dY = Math.cos(angle)*distance;
        velX += dX/SolarSystem.timestep;
        velY += dY/SolarSystem.timestep;


    }
    // this boolean if for testing the thrusting
    boolean a = true;
    public void thrust(){
        if (a) {
           // velX -= 2;
            angle += Math.PI;
        }
       // a = false;

        int toThrustForAngle = check(true );
        int toThrustForPosition = check(false);

        if (toThrustForAngle == 1){
            rightThrust();

        }else if (toThrustForAngle == 2){
            leftThrust();
        }

        if (toThrustForPosition == 1){
            rightThrustAndMove();
        }
        else if (toThrustForPosition == 2){
            leftThrustAndMove();
        }

        reduceSpeedForLanding();
    }

    public int check(boolean a){
        final Point TitanPosition = new Point(0,0);
        final double ANGLE_DERIVATION = 1;
        final double STARTING_POSITION = 0;
        final double POSITION_DERIVATION = 1;
        final double STARTING_ANGLE = 0;
        Point modulePosition = new Point(this.x, this.y);


        if(a) {
            if (modulePosition.getAngle(TitanPosition) > STARTING_ANGLE + ANGLE_DERIVATION) {
                //Need to thrust right
                return 1;
            } else if (modulePosition.getAngle(TitanPosition) < STARTING_ANGLE + ANGLE_DERIVATION) {
                //Need to thrust left
                return 2;
            } else {
                return 0;
            }
        }else{
            if (modulePosition.x > STARTING_POSITION + POSITION_DERIVATION){
                //Need to thrust right
                return 1;
            } else if (modulePosition.x < STARTING_POSITION - POSITION_DERIVATION) {
                //Need to thrust left
                return 2;
            } else {
                return 0;
            }
        }

    }

    public void reduceSpeedForLanding(){
        final double LANDING_SPEED = 0.5;
        final double DISTANCE_WHEN_NEED_TO_REDUCE_SPEED = 0.5;

        //thrust the main thruster to reduce speed, does not change the angle
        if (this.x < DISTANCE_WHEN_NEED_TO_REDUCE_SPEED){
            if (this.velY > LANDING_SPEED){
                mainThrust();
            }
        }
    }

    public static double getAngle()
    {
        return angle;
    }
    public static double getOldAngle()
    {
        return oldAngle;
    }
}
