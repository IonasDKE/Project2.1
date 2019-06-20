public class RocketLauncher {
    int time = 6144000;   // 6years 189216000
    CelestialBody earth;
    static CelestialBody rocket = new Rocket("rocket",5712, 0, 0, 0, 0, 0);
    double speed, startingDistance;
    Point destination;

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
        //System.out.println("set angle : " +rocket.angle);

        startingDistance = rocketPosition.getDistance(destination);
        //System.out.println("distance : "+distance);
        speed = startingDistance/time;
        //System.out.println("Speed = "+speed);
    }

    private boolean speedReached = false; //is set to true once we reach our travel speed
    private boolean speedChanged = false; //is set to true once we change our speed to get into orbit

    public void checkSpeedAndAngle(){

        //System.out.println("Rocket speed: " + Math.abs(rocket.velX + rocket.velY));
        if(Math.abs(rocket.velX + rocket.velY) < speed && !speedReached){
            //System.out.println("MainThrust");
            rocket.mainThruster(5);
        }

        if (Math.abs(rocket.velX + rocket.velY) >= speed && !speedReached){
            System.out.println("travel speed reached");
            speedReached = true;
        }

        Point rocketPosition = new Point(rocket.x, rocket.y);
        double distance = rocketPosition.getDistance(destination);

        if (distance <= 42000){
            if (!speedChanged){
                speed = 947;
                speedChanged = true;
            }
            if(rocket.velX + rocket.velY > speed){
                System.out.println("Front thrust");
                rocket.frontThruster(5);
            }
        }

        double checkAngle = rocketPosition.getAngle(destination);
        if(checkAngle >= rocket.angle+1){
            rocket.rightThruster();
            System.out.println("correct angle to the right");

        }else if(checkAngle <= rocket.angle-1){
            rocket.leftThruster();
            System.out.println("correct angle to the left");

        }
    }
    public void setDestination(Point destination){
        this.destination = destination;
    }
}
