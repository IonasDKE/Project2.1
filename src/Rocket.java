public class Rocket extends  CelestialBody{

    public Rocket(String name, double mass, double x, double y, double velX, double velY, double angle) {
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
        this.angle = angle;
    }
}
