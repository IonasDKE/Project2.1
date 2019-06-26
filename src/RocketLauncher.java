public class RocketLauncher {
    static int time = 63113904 ;   // 5years 157784630
    CelestialBody earth;
    static CelestialBody rocket = new Rocket("rocket",5712, 0, 0, 0, 0, 0, 0);
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
        speed = Math.abs(startingDistance/time);
        System.out.println("travel speed: " + speed);
    }

    private boolean speedReached = false; //is set to true once we reach our travel speed
    private boolean speedChanged = false; //is set to true once we change our speed to get into orbit
    private boolean rotate = false;
    boolean speedReduced = false;
    boolean stopIncrease = false;
    double lastSpeed;

    public void reset(){
        speedChanged = false;
        speedChanged = false;
        rotate = false;
        speedReduced = false;
        stopIncrease = false;
        counter = 0;
    }
    public void checkSpeedAndAngle(){
        counter++;
        //System.out.println("Rocket current speed" + Math.sqrt(rocket.velX*rocket.velX+ rocket.velY*rocket.velY));
        Point rocketPosition = new Point(rocket.x, rocket.y);
        double distance = rocketPosition.getDistance(destination);

        if(Math.sqrt(rocket.velX*rocket.velX+ rocket.velY*rocket.velY) < speed && !speedReached){
            rocket.mainThruster(445);
            if (Math.sqrt(rocket.velX*rocket.velX + rocket.velY*rocket.velY) >= speed){
                //System.out.println("travel speed reached");
                speedReached = true;
            }
        }

        if (speedReached && !speedChanged){
            newSpeed = Math.abs(startingDistance /(((time/SolarSystem.timestep)-counter)*SolarSystem.timestep));
            speedChanged = true;
            System.out.println("new speed: "+newSpeed);
        }

        if((Math.sqrt(rocket.velX*rocket.velX+ rocket.velY*rocket.velY) < newSpeed) && speedChanged && !stopIncrease){
            rocket.mainThruster(445);
            if (Math.sqrt(rocket.velX*rocket.velX+ rocket.velY*rocket.velY) > newSpeed){
                stopIncrease = true;
            }
        }

        if (distance <= startingDistance/4 && !rotate){
            brakeDistance = getDistance();
            rotate = true;
            rocket.setAngle();
        }

        if(rotate && distance <= brakeDistance) {
            lastSpeed = Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY);
            //System.out.println("rocket speed: "+Math.sqrt(rocket.velX*rocket.velX + rocket.velY*rocket.velY));
            if (Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY) > 3000 && !speedReduced) {
                rocket.frontThruster(445);

                if (Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY) <= 354) {
                    //System.out.println("limite reached");
                    speedReduced = true;
                }
            } else if (Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY) > 2000 && !speedReduced && Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY) < 3000) {
                rocket.frontThruster(300);

                if (Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY) <= 354) {
                    //System.out.println("limite reached");
                    speedReduced = true;
                }
            } else if (Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY) > 1000 && !speedReduced && Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY) < 2000) {
                rocket.frontThruster(50);

                if (Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY) <= 354) {
                    //System.out.println("limite reached");
                    speedReduced = true;
                }
            } else if (Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY) > 500 && !speedReduced && Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY) < 1000) {
                rocket.frontThruster(25);

                if (Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY) <= 354) {
                    //System.out.println("limite reached");
                    speedReduced = true;
                }
            }else if (Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY) > 500 && !speedReduced) {
                rocket.frontThruster(10);

                if (Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY) <= 354) {
                    //System.out.println("limite reached");
                    speedReduced = true;
                }
            }

            if (lastSpeed < Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY)) {
                System.out.println("bigger");
                double thrustPower = lastSpeed - Math.sqrt(rocket.velX * rocket.velX + rocket.velY * rocket.velY);
                System.out.println(thrustPower);
                rocket.frontThruster(Math.abs(thrustPower));
                speedReduced = true;
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