public abstract class CelestialBody implements Cloneable {

    String name;
    double mass, x, y, velX, velY, accX, accY, oldAccX, oldAccY,oldX,oldY, angle;

    public void mainThruster(double ft){
        // ft in kN
        //System.out.println("thruster : "+this.angle);

        double a = ft/this.mass;
        double vel = a*SolarSystem.timestep;
        double dX = Math.cos(this.angle)*vel;
        double dY = Math.sin(this.angle)*vel;

        this.velX += dX;
        this.velY += dY;
    }
    public void frontThruster(double ft){

        double a = ft/this.mass;
        double vel = a*SolarSystem.timestep;
        double dX = Math.cos(this.angle)*vel;
        double dY = Math.sin(this.angle)*vel;

        this.velX -= dX;
        this.velY -= dY;
    }

    public void leftThruster(){
        this.angle += 5*Math.PI/180;
    }

    public void rightThruster(){
        this.angle += 5*Math.PI/180;
    }
}