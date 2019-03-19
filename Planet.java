import java.util.LinkedList;
import java.util.ArrayList;


class Point{
    double x,y;
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
}

public class Planet {
    String name;
    double mass, x, y, velX, velY,accX, accY, FX,FY,a,b;
    LinkedList<Point> positions = new LinkedList<Point>();
    final int MAX_NUM_POINTS = 1000;
    ArrayList<Planet>  childrenPlanets = new ArrayList<Planet>();

    public Planet(String name, double mass, double x, double y, double a, double b){
        this.name = name;
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;
        positions.add(new Point(x,y));
    }

    public Planet(double mass, double x, double y){
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.velX =0;
        this.velY = 0;
        this.accX = 0;
        this.accY = 0;
        this.FX = 0;
        this.FY = 0;
    }
    public Planet(String name, double mass, double x, double y){
        this.name = name;
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.velX =0;
        this.velY = 0;
        this.accX = 0;
        this.accY = 0;
        this.FX = 0;
        this.FY = 0;
    }
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
        positions.addFirst(new Point(x,y));
        if(positions.size()>MAX_NUM_POINTS)
            positions.removeLast();
    }

    public void addChild(Planet child) {
        this.childrenPlanets.add(child);
    }

}


