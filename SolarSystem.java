import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class SolarSystem {
    static final double G = 0.000667;           //universal gravitational constant
    ArrayList<Planet> list = new ArrayList<Planet>();
    float timestep = 0.0f;

    public SolarSystem(){

        Planet Sun = new Planet("Sun",19.89,0,0);

        Planet Mercury = new Planet("Mercury",0.00000328,-4.6,0, 5.8,5.7);

        Planet Venus = new Planet("Venus",0.0000487,-10.725,0,10.8,10.799);

        Planet Earth = new Planet("Earth",0.0000597, -14.745,0,15,14.99);

        Planet Mars = new Planet("Mars",0.00000642,-20.66,0 ,22.8,22.7 );
        Planet Jupiter = new Planet ("Jupiter", 0.019, -74.1,0,77.9,77.8);
        Planet Saturn = new Planet("Saturn",0.00568, -134.8,0,143,48.8);
        Planet Uranus = new Planet ("Uranus",0.000866, -273.8,0,287,286.7 );
        Planet Neptune = new Planet ("Neptune",0.00103,-445,0,450,449.9);


        list.add(Sun);
        Sun.addChild(Mercury);
        Sun.addChild(Venus);
        Sun.addChild(Earth);
        Sun.addChild(Mars);
        Sun.addChild(Jupiter);
        Sun.addChild(Saturn);
        Sun.addChild(Uranus);
        Sun.addChild(Neptune);

        Planet Moon = new Planet("Moon", 0.00005, Earth.x-0.0051,0,0.0384,0.0192);
        Earth.addChild(Moon);
        Planet Phobos = new Planet("Phobos",0,Mars.x-0.0009,0,0.0009,0.0009);
        Planet Deimos = new Planet("Deimos",0,Mars.x-0.0023,0,0.0023,0.0023);
        Mars.addChild(Phobos);
        Mars.addChild(Deimos);
        Planet Io = new Planet("Io",0,Jupiter.x-0.042,0,0.042,0.042);
        Planet Europa = new Planet("Europa",0,Jupiter.x-0.066,0,0.067,0.067);
        Planet Ganymede = new Planet("Ganymede",0,Jupiter.x-0.1,0,0.1,0.1);
        Planet Callisto = new Planet("Callisto", 0,Jupiter.x-0.187,0,0.188,0.188);
        Jupiter.addChild(Io);
        Jupiter.addChild(Europa);
        Jupiter.addChild(Ganymede);
        Jupiter.addChild(Callisto);
        Planet Mimas = new Planet("Mimas", 0,Saturn.x-0.0185,0,0.0185,0.0185);
        Planet Enceladus = new Planet("Enceladus",0,Saturn.x-0.0237,0,0.0238,0.0238);
        Planet Tethys = new Planet("Tethys",0,Saturn.x-0.0295,0,0.0295,0.0295);
        Planet Dion = new Planet("Dion",0,Saturn.x-0.0377,0,0.0377,0.0377);
        Planet Rhea = new Planet("Rhea",0,Saturn.x-0.0526,0,0.0527,0.0527);
        Planet Titan = new Planet("Titan",0,Saturn.x-0.12,0,0.12,0.12);
        Planet Rapetus = new Planet("Rapetus",0,Saturn.x-0.35,0,0.36,0.36);
        Saturn.addChild(Mimas);
        Saturn.addChild(Enceladus);
        Saturn.addChild(Tethys);
        Saturn.addChild(Dion);
        Saturn.addChild(Rhea);
        Saturn.addChild(Titan);
        Saturn.addChild(Rapetus);

        /*
        for (Planet i: list.get(0).childrenPlanets) {
                i.x *= 10;
                i.y *= 10;
                i.a *= 10;
                i.b *= 10;
                for (Planet c: i.childrenPlanets) {
                    c.x *=10;
                    c.y *= 10;
                    c.a *= 10;
                    c.b *= 10;
                }

        } */

    }

    void updatePositions () {
        for (Planet i: list) {
            for(Planet child: i.childrenPlanets) {
                if(!child.equals(null)) {

            double n = Math.sqrt(G*(child.mass+i.mass)/(Math.pow(child.a,3)));
            double M = n* timestep;
            double e = Math.sqrt(1-child.b*child.b/(child.a*child.a));
            double E = M;
            for (int j = 0; j<10;j++) {
                E = E+e*Math.sin(E);
            }
            child.x = child.a*(Math.cos(E)-e);
            child.y = child.b*Math.sin(E);

            for (Planet grandChild : child.childrenPlanets) {
                if(!grandChild.equals(null)) {
                    double nChild = Math.sqrt(G*(grandChild.mass+child.mass)/(Math.pow(grandChild.a,3)));
                    double MChild = nChild* timestep;
                    double eChild = Math.sqrt(1-grandChild.b*grandChild.b/(grandChild.a*grandChild.a));
                    double EChild = MChild;
                    for (int k = 0; k<10;k++) {
                        EChild = EChild+eChild*Math.sin(EChild);
                    }
                    grandChild.x = grandChild.a*(Math.cos(EChild)-eChild);
                    grandChild.y = grandChild.b*Math.sin(EChild);
                    //System.out.println(grandChild.name);
                    //System.out.println(grandChild.x);
                    //System.out.println(grandChild.y);
                }

            }
                }

        }

        }

    }


        /*
    void updatePositions(){
        for (Planet p:list) {
            if (p.name!= Sun.name) {
        for (Planet i: list) {
            if (i.name != p.name) {
        p.FX += G*(p.mass*i.mass)*(i.x-p.x)/((Math.pow((Math.sqrt(Math.pow(p.x-i.x,2)+Math.pow(p.y-i.y,2))),3)));
        p.FY += G*(p.mass*i.mass)*(i.y-p.y)/((Math.pow((Math.sqrt(Math.pow(p.x-i.x,2)+Math.pow(p.y-i.y,2))),3)));
            }
        }
        p.accX = p.FX/p.mass;
        p.accY = p.FY/p.mass;
        System.out.println(p.accX);
        System.out.println(p.accY);
        p.x += p.velX*timestep;
        p.y +=p.velY*timestep;
        p.velX+= p.accX*timestep;
        p.velY+= p.accY*timestep;
        p.FX = 0;
        p.FY = 0; } }

} */


}


class SolarGUI extends JPanel implements Runnable{
    int offX=400, offY=400;
    double scale=20;
    SolarSystem s;
    public SolarGUI(){
        s = new SolarSystem();
        setFocusable(true);
        requestFocus();
    }

    public void paintComponent(Graphics g) {
        int i=0;
        int diameter=20;
        double centraY = s.list.get(0).y;
        double centraX = s.list.get(0).x;
        int xSun = (int)(offX-diameter/2+centraX*scale);
        int ySun = (int)(offY-diameter/2+centraY*scale);
        g.fillOval(xSun, ySun,diameter,diameter);
        for(Planet p : s.list.get(0).childrenPlanets){

            diameter=10;
            int x = (int)(offX-diameter/2+p.x*scale);
            int y = (int)(offY-diameter/2+p.y*scale);
            g.fillOval(x, y,diameter,diameter);
            //g.drawString(p.name, x, y);

			/*for(int k=1; k<p.positions.size(); k++){
				Point pt0 = p.positions.get(k-1);
				Point pt1 = p.positions.get(k);
				g.drawLine((int)(offX+pt0.x*scale), (int)(offY+pt0.y*scale), (int)(offX+pt1.x*scale), (int)(offY+pt1.y*scale));
			}*/

			for (Planet c: p.childrenPlanets) {
			    if(!c.equals(null)) {
                    diameter = 0;
                    x = (int) (offX - diameter / 2 + c.x * scale);
                    y = (int) (offY - diameter / 2 + c.y * scale);
                    g.fillOval(x, y, diameter, diameter);

                }
            }
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(true){
            s.timestep+=20;
            s.updatePositions();
            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        JFrame f = new JFrame("Solar system");
        f.setBounds(0, 0, 8000, 8000);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SolarGUI p = new SolarGUI();
        f.add(p);
        new Thread(p).start();
        f.setVisible(true);
    }
}

