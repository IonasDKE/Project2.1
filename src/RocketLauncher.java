public class RocketLauncher {
    static int time = 126227808;   // 5years 157784630
    CelestialBody earth;
    static CelestialBody rocket = new Rocket("rocket",5712, 0, 0, 0, 0, 0);
    protected double speed, startingDistance, newSpeed, counter;
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
            rocket.mainThruster(rocket.reduceSpeedPower(distance));

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
            rocket.mainThruster(rocket.reduceSpeedPower(distance));
        }

        if (distance <= startingDistance/10 && !rotate){
            System.out.println("rotate");
            rotate = true;
            rocket.rotateRocket();
        }

        if(rotate){
            rocket.reduceSpeedPower(distance);
            if(rocket.velX + rocket.velY > 947) {
                System.out.println("speed: " + Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY));
                rocket.mainThruster(rocket.reduceSpeedPower(distance));
                //System.out.println("Slow down");
            }
        }
    }

    public void setDestination(Point destination){
        this.destination = destination;
    }
}