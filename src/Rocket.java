public class Rocket extends  CelestialBody{

    public final int ORBITAL_SPEED =5043; // in km/h
    public final double ORBITAL_VEL_X=1000;
    public final double ORBITAL_VEL_Y=4942.85838567144;
    //public final 1.4818E8;



    public Rocket(String name, double mass, double x, double y, double velX, double velY) {
        this.name = name;
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.velX =velX;
        this.velY = velY;
        this.accX=0;
        this.accY=0;
        this.oldAccX=0;
        this.oldAccY=0;
        this.oldX = 0;
        this.oldY=0;
    }

    public void xThrust(double x, double y)
    {

    }

    public void yThrust(double x,double y)
    {

    }

    public void distance(double x, double y)
    {
    }
}
