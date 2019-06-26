public abstract class CelestialBody implements Cloneable {

    String name;
    double mass, x, y, velX, velY, accX, accY, oldAccX, oldAccY,oldX,oldY, angle, fuel;

    public void mainThruster(double ft) {
        // ft in kN

        double a = ft / this.mass;
        double vel = a * SolarSystem.timestep;

        double dX = Math.cos(this.angle) * vel;
        double dY = Math.sin(this.angle) * vel;
        this.velX += dX;
        this.velY += dY;
        consumeFuel(ft);

    }
    public void frontThruster(double ft){

        double a = ft / this.mass;
        double vel = a * SolarSystem.timestep;

        double dX = Math.cos(this.angle) * vel;
        double dY = Math.sin(this.angle) * vel;

        this.velX += dX;
        this.velY += dY;
        consumeFuel(ft);
    }

    public void setAngle(){
        this.angle = this.angle + Math.PI;
    }

    public void consumeFuel(double ft){
        // mass flow rate (m) in kg/s, g in m/s²
        final double G =  9.80665;
        double I = 300;
        double m = ft / (G * I);
        this.fuel += m*5000;
    }
}