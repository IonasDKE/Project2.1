import java.sql.SQLOutput;

public abstract class CelestialBody implements Cloneable {

    String name;
    double mass, x, y, velX, velY, accX, accY, oldAccX, oldAccY,oldX,oldY, angle;
    boolean rotated = false;

    public void mainThruster(double ft) {
        // ft in kN

        double a = ft / this.mass;
        double vel = a * SolarSystem.timestep;

        double dX = Math.cos(this.angle) * vel;
        double dY = Math.sin(this.angle) * vel;
        this.velX += dX;
        this.velY += dY;

    }
    public void frontThruster(double ft){

        double a = ft / this.mass;
        double vel = a * SolarSystem.timestep;

        double dX = Math.cos(this.angle+Math.PI) * vel;
        double dY = Math.sin(this.angle+Math.PI) * vel;

        System.out.println("Velx: "+dX);
        System.out.println("Vely: "+dY);

        this.velX += dX;
        this.velY += dY;
    }

    public void rotateRocket(){
        rotated = true;
    }
}