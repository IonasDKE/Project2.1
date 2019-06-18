public class RocketLauncher {
    int time = 6144000 / (int)SolarSystem.timestep; // +/- 5.9 years 186062400
    CelestialBody earth;
    static CelestialBody rocket;
    double angle, speed;


    public void launch(SolarSystem system, CelestialBody body){
        earth = system.getPlanetList().get(4);
        rocket = new Rocket("rocket",5712, 0, 0, 0, 0, 0 );
        double distanceDifference = -6371000.0; // distance from the center of earth and his surface
        rocket.x = earth.x + distanceDifference;
        rocket.y = earth.y;

        //System.out.println(rocket.x + " " + rocket.y);
        //System.out.println(earth.x + " " + earth.y);

       double angleAndSpeed[] = getShootingAngle(system, body);
       angle = angleAndSpeed[0];
       speed = angleAndSpeed[1];
       rocket.velX = (speed * Math.cos(angle));
       rocket.velY = (speed * Math.sin(angle));
       //System.out.println(rocket.velX + " " + rocket.velY);

    }

    public double[] getShootingAngle(SolarSystem system, CelestialBody body) {
        /*
        ArrayList<CelestialBody> futurePosition = getCloneSolarSystem(system);

        for (int i = 0; i < time; i ++) {
            system.updatePositions(futurePosition);
        }
        */

        Point newTitanPosition = new Point(body.x, body.y);
        System.out.println("New Titan "+ system.getPlanetList().get(17).x + " " + system.getPlanetList().get(17).y);
        System.out.println("current titan "+body.x +" "+ body.y);
        Point earthPosition = new Point(earth.x, earth.y);

        double[] angleAndSpeed = new double[2];
        angleAndSpeed[0] = earthPosition.getAngle(newTitanPosition);

        double distance = earthPosition.getDistance(newTitanPosition);
        angleAndSpeed[1] = distance/(6144000.0);
        System.out.println("Speed : "+angleAndSpeed[1]);

        return angleAndSpeed;
    }
}
