public class RocketLauncher {
    int time = 6144000;   // 6years 189216000
    CelestialBody earth;
    static CelestialBody rocket;
    double angle, speed;
    private Point destination;

    public void launchToTitan(SolarSystem system, CelestialBody titan){ // Titan is the obtained after the simulation
        earth = system.getPlanetList().get(4);
        rocket = new Rocket("rocket",5712, 0, 0, 0, 0);

        double distanceDifference = -6371000.0; // distance from the center of earth and his surface
        rocket.x = earth.x + distanceDifference;
        rocket.y = earth.y;

        //titan.x += 2000000+2574000; // position of titan + distance of the orbit and distance from the surface
        double angleAndSpeed[] = getShootingAngle(titan);
        angle = angleAndSpeed[0];
        speed = angleAndSpeed[1];
        rocket.velX = (speed * Math.cos(angle));
        rocket.velY = (speed * Math.sin(angle));
    }

    public void launchToEarth(CelestialBody destination, CelestialBody rocket){

        this.rocket = rocket;
        double angleAndSpeed[] = getShootingAngle(destination);
        angle = angleAndSpeed[0];
        speed = angleAndSpeed[1];
        rocket.velX = (speed * Math.cos(angle));
        rocket.velY = (speed * Math.sin(angle));
    }

    public double[] getShootingAngle(CelestialBody body) {

        Point destination = new Point(body.x, body.y);
        setDestination(destination);
        Point rocketPosition = new Point(rocket.x, rocket.y);

        double[] angleAndSpeed = new double[2];
        angleAndSpeed[0] = rocketPosition.getAngle(destination);

        double distance = rocketPosition.getDistance(destination);
        angleAndSpeed[1] = distance/time;

        return angleAndSpeed;
    }

    public void checkSpeed(CelestialBody rocket){
        Point rocketPosition = new Point(rocket.x, rocket.y);
        double distance = rocketPosition.getDistance(destination);
        if (distance <= 42000000){
            System.out.println("enter in sphere influence");
            speed = 947;
        }
        rocket.velX = speed * Math.cos(angle);
        rocket.velY = speed * Math.sin(angle);
    }

    private void setDestination(Point p){
        destination = p;
    }
}
