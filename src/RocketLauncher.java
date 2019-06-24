public class RocketLauncher {
    int time = 6144000;   // 5years 157784630
    CelestialBody earth;
    static CelestialBody rocket = new Rocket("rocket",5712, 0, 0, 0, 0, 0);
    protected double speed, startingDistance, catchSpeed, counter;
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
        System.out.println(speed);
    }

    private boolean speedReached = false; //is set to true once we reach our travel speed
    private boolean speedChanged = false; //is set to true once we change our speed to get into orbit
    private boolean catchSpeedSet = false;

    public void checkSpeedAndAngle(){
        counter++;
        if(Math.sqrt(rocket.velX*rocket.velX+ rocket.velY*rocket.velY) < speed && !speedReached){
            rocket.mainThruster(445);

            if (Math.sqrt(rocket.velX*rocket.velX + rocket.velY*rocket.velY) >= speed){
                System.out.println("travel speed reached");
                speedReached = true;
            }
        }

        Point rocketPosition = new Point(rocket.x, rocket.y);
        double distance = rocketPosition.getDistance(destination);

        if (distance < (startingDistance/2) && speedReached && !catchSpeedSet){
            catchSpeed = distance /((time/SolarSystem.timestep)-counter);
            System.out.println(catchSpeed);
            catchSpeedSet = true;
        }
        if((Math.sqrt(rocket.velX*rocket.velX+ rocket.velY*rocket.velY) < catchSpeed) && catchSpeedSet){
            rocket.mainThruster(500);
        }

        if (distance <= 42000){
            if (!speedChanged){
                System.out.println("speed = 947");
                speed = 947;
                speedChanged = true;
            }
            if(Math.sqrt(rocket.velX * rocket.velX + rocket.velY *rocket.velY) > speed){
                System.out.println("Front thrust");
                rocket.frontThruster(200);
            }
        }
    }
    public void setDestination(Point destination){
        this.destination = destination;
    }
}
