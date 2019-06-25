public class RocketLauncher {
    static int time = 6144000;   // 5years 157784630
    CelestialBody earth;
    static CelestialBody rocket = new Rocket("rocket",5712, 0, 0, 0, 0, 0);
    protected double speed, startingDistance, newSpeed, counter, brakeDistance;
    private Point destination;

    public void launchToTitan(SolarSystem system, CelestialBody titan){

        earth = system.getPlanetList().get(4);

        double distanceDifference = -6371000.0; // distance from the center of earth and his surface
        rocket.x = earth.x + distanceDifference;
        rocket.y = earth.y;
        setSpeedAndAngle(titan);
    }

    public void launchToEarth(CelestialBody destination){
        setSpeedAndAngle(destination);
    }

    public void setSpeedAndAngle(CelestialBody body) {

        Point destination = new Point(body.x, body.y);
        setDestination(destination);
        Point rocketPosition = new Point(rocket.x, rocket.y);

        rocket.angle = rocketPosition.getAngle(destination);

        startingDistance = rocketPosition.getDistance(destination);
        speed = startingDistance/time;
        System.out.println("Starting speed: "+speed);
    }

    private boolean speedReached = false; //is set to true once we reach our travel speed
    private boolean speedChanged = false; //is set to true once we change our speed to get into orbit
    private boolean rotate = false;

    public void checkSpeedAndAngle(){
        counter++;

        Point rocketPosition = new Point(rocket.x, rocket.y);
        double distance = rocketPosition.getDistance(destination);

        if(Math.sqrt(rocket.velX*rocket.velX+ rocket.velY*rocket.velY) < speed && !speedReached){
            rocket.mainThruster(445);

            if (Math.sqrt(rocket.velX*rocket.velX + rocket.velY*rocket.velY) >= speed){
                System.out.println("travel speed reached");
                speedReached = true;
            }
        }

        if (speedReached && !speedChanged){
            newSpeed = startingDistance /(((time/SolarSystem.timestep)-counter)*SolarSystem.timestep);
            speedChanged = true;
            System.out.println("new speed: "+newSpeed);
        }

        if((Math.sqrt(rocket.velX*rocket.velX+ rocket.velY*rocket.velY) < newSpeed) && speedChanged){
            rocket.mainThruster(445);
        }

        if (distance <= startingDistance/4 && !rotate){
            System.out.println("rotate");
            brakeDistance = getDistance();
            System.out.println("braking distance: "+ brakeDistance);
            rotate = true;
            rocket.rotateRocket();
        }

        if(rotate){
            System.out.println("distance: "+ distance);
        }

        if(rotate && distance <= brakeDistance){
            System.out.println("rocket speed: "+Math.sqrt(rocket.velX*rocket.velX + rocket.velY*rocket.velY));
            if(Math.sqrt(rocket.velX*rocket.velX + rocket.velY*rocket.velY) > 947) {
                System.out.println("brake");
                rocket.frontThruster(445);
            }
        }
    }

    public double getDistance(){
        double a = (445/rocket.mass);
        double t = (Math.sqrt(rocket.velX*rocket.velX + rocket.velY*rocket.velY) - 947)/a;
        double distance = Math.sqrt(rocket.velX*rocket.velX + rocket.velY*rocket.velY) + (a * Math.pow(t, 2))/2;

        return distance;
    }

    public void setDestination(Point destination){
        this.destination = destination;
    }
}