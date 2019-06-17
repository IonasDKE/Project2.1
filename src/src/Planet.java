import java.util.LinkedList;


class Point{
	private double x,y;
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	public double getAngle(Point p) {
		double xDiff = p.x - this.x;
		double yDiff = p.y - this.y;
		return Math.toDegrees(Math.atan2(yDiff, xDiff));
	}

	public void setX(double x)
	{
		this.x=x;
	}
	public double getX()
	{return this.x;}

	public void setY(double y)
	{
		this.y=y;
	}
	public double getY()
	{return this.y;}

}

public class Planet {
	private String name;
	private double mass, x, y, velX, velY, accX, accY, oldAccX, oldAccY,oldX,oldY;
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
	public Planet(double mass, double x, double y){
		this.mass = mass;
		this.x = x;
		this.y = y;
		this.velX =0;
		this.velY = 0;
		this.accX=0;
		this.accY=0;
		this.oldAccX=0;
		this.oldAccY=0;
	}
	public Planet(String name, double mass, double x, double y){
		this.name = name;
		this.mass = mass;
		this.x = x;
		this.y = y;
		this.velX =0;
		this.velY = 0;
		this.accX=0;
		this.accY=0;
		this.oldAccX=0;
		this.oldAccY=0;
	}
	public void setPosition(double x, double y){
		this.x = x;
		this.y = y;
		positions.addFirst(new Point(x,y));
		if(positions.size()>MAX_NUM_POINTS)
			positions.removeLast();
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getName()
	{return this.name;}

	public void setMass(double mass)
	{
		this.mass=mass;
	}
	public double getMass()
	{return this.mass;}

	public void setX(double x)
	{
		this.x=x;
	}
	public double getX()
	{return this.x;}

	public void setY(double y)
	{
		this.y=y;
	}
	public double getY()
	{return this.y;}

	public void setVelX(double velX)
	{
		this.velX=velX;
	}
	public double getVelX()
	{return this.velX;}

	public void setVelY(double velY)
	{
		this.velY=velY;
	}
	public double getVelY()
	{return this.velY;}

	public void setAccX(double accX)
	{
		this.accX=accX;
	}
	public double getAccX()
	{return this.accX;}

	public void setAccY(double accY)
	{
		this.accY=accY;
	}
	public double getAccY()
	{return this.accY;}

	public void setOldAccX(double oldAccX)
	{
		this.oldAccX=oldAccX;
	}
	public double getOldAccX()
	{return this.oldAccX;}

	public void setOldAccY(double oldAccY)
	{
		this.oldAccY=oldAccY;
	}
	public double getOldAccY()
	{return this.oldAccY;}

	public void setOldX(double oldX)
	{
		this.oldX=oldX;
	}
	public double getOldX()
	{return this.oldX;}

	public void setOldY(double oldY)
	{
		this.oldY=oldY;
	}
	public double getOldY()
	{return this.oldY;}

}

















