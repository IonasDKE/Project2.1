public class LandingModule extends Planet{

    //Simulation of the motion of a spacecraft with only yaw rotation considered

    //the angle of rotation of the spaceship
    private double angle=0;

    public LandingModule(String name, double mass, double x, double y, double velX, double velY)
    {
        super(name, mass, x, y, velX, velY);
    }

    //reference for the power of the thrusters
    public void leftThrust()
    {
        //add fuel consumption
        angle += Math.PI / 180;

        //rotation around the barycenter (in this case the point where the gravitational forces and thrusts are applied)
        //rocket.transform(new Affine(new Rotate(s.planetaryObjects.get(22).leftThrust, xModule, yModule)));

    }

    public void rightThrust()
    {
        //add fuel consumption
        angle -= Math.PI / 180;

    }

    public void mainThrust()
    {
        //add fuel consumption
        double a = 44000/super.getMass();
        double vel=a*SolarSystem.timestep;
        double distance = vel*SolarSystem.timestep;
        double dX = Math.sin(-angle)*distance;
        double dY = Math.cos(angle)*distance;
        super.setVelX(super.getVelX() + dX/SolarSystem.timestep);
        super.setVelY(super.getVelY() + dX/SolarSystem.timestep);
    }
    public void mainThrusthalf()
    {
        //add fuel consumption
        double a = 22000/super.getMass();
        double vel=a*SolarSystem.timestep;
        double distance = vel*SolarSystem.timestep;
        double dX = Math.sin(-angle)*distance;
        double dY = Math.cos(angle)*distance;
        super.setVelX(super.getVelX() + dX/SolarSystem.timestep);
        super.setVelY(super.getVelY() + dX/SolarSystem.timestep);
    }

    public double getAngle()
    {
        return angle;
    }
}
