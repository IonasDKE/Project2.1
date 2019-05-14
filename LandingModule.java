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
    public void leftThrust()
    {
        //add fuel consumption
        angle +=1;

        //rotation around the barycenter (in this case the point where the gravitational forces and thrusts are applied)
        //rocket.transform(new Affine(new Rotate(s.planetaryObjects.get(22).leftThrust, xModule, yModule)));
    }

    public void rightThrust()
    {
        //add fuel consumption
        angle -= 1;
    }

    public void mainThrust()
    {
        //add fuel consumption
        double a =440/mass;
        this.velY= this.velY+a*timeStep;
    }

    final double timestep = 0.1;
    public void thrust(){

        int toThrust = check();

        if (toThrust == 1){
            rightThrust();

        }else if (toThrust == 2){
            leftThrust();
        }
        reduceSpeedForLanding();
    }

    public int check(){
        final Point TitanPosition = new Point(0,0);
        final double DERIVATION = 1;
        final double STARTING_ANGLE = 0;
        Point modulePosition = new Point(this.x, this.y);

        if (modulePosition.getAngle(TitanPosition) > STARTING_ANGLE + DERIVATION) {
            //Need to thrust rigth
            return 1;
        }
        else if (modulePosition.getAngle(TitanPosition) < STARTING_ANGLE + DERIVATION) {
            //Need to thrust left
            return 2;
        }
        else {
            return 0;
        }
    }

    public void reduceSpeedForLanding(){
        final double LANDING_SPEED = 0.5;
        final double DISTANCE_WHEN_NEED_TO_REDUCE_SPEED = 300;

        //thrust the main thruster to reduce speed, does not change the angle
        if (this.x < DISTANCE_WHEN_NEED_TO_REDUCE_SPEED){
            if (this.velY() > LANDING_SPEED){
                this.velY = this.velY+a*timestep;
            }
        }
    }
}
