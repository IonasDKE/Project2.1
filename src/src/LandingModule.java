public class LandingModule extends Planet{

    //Simulation of the motion of a spacecraft with only yaw rotation considered

    //the angle of rotation of the spaceship
    private double angle=0;
    private double burnedFuel=0;

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

    public void mainThrust(double ft)
    {
        if(ft<=0)
        {
            ft=0;
        }
        if(ft>=44000)
        {
            ft=44000;
        }
        burnedFuel += (Math.abs(ft)/3000)*0.1;
        double a = ft/super.getMass();
        double vel=a*SolarSystem.timestep;
        double distance = vel*SolarSystem.timestep;
        double dX = Math.sin(-angle)*distance;
        double dY = Math.cos(angle)*distance;
        super.setVelX(super.getVelX() + dX/SolarSystem.timestep);
        super.setVelY(super.getVelY() + dY/SolarSystem.timestep);
    }

    public double getAngle()
    {
        return this.angle;
    }
    public double getBurnedFuel()
    {
        return this.burnedFuel;
    }
}
