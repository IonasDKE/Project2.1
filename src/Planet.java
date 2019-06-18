import java.util.LinkedList;


class Point{
    double x,y;
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    //New
    public double getDistance (Point T){
        double deltaX=Math.abs(this.x-T.x);
        double deltaY=Math.abs(this.y-T.y);
        return Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));

    }
    //new
    public double getAngle(Point p) {
        double xDiff = p.x - this.x;
        double yDiff = p.y - this.y;
        double degree = Math.toDegrees(Math.atan2(yDiff, xDiff));
        if (degree < 0){
            degree += 360;
        }

        //return radians
        return Math.toRadians(degree);
    }
}

public class Planet extends CelestialBody {
    LinkedList<Point> positions = new LinkedList<Point>();
    final int MAX_NUM_POINTS = 1000;

    public Planet(String name, double mass, double x, double y, double velX, double velY){
        this.name = name;
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.velX =velX;
        this.velY = velY;
        this.accX=0;
        this.accY=0;
        this.oldAccX=0;
        this.oldAccY=0;
        this.oldX = 0;
        this.oldY=0;
        positions.add(new Point(x,y));
    }

    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
        positions.addFirst(new Point(x,y));
        if(positions.size()>MAX_NUM_POINTS)
            positions.removeLast();
    }

    public double getX() {
        return this.x;}

    public double getY() {
        return this.y;
    }

}
