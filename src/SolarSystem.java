import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class SolarSystem {
    static final double G = 0.000667;           //universal gravitational constant
    ArrayList<Planet> list;
    float timestep=0.05f;

    public SolarSystem(){

        Planet Sun = new Planet("Sun",19.89,0,0);

        Planet Mercury = new Planet("Mercury",0.00000328,- 5.7,0);
        //Planet Earth = new Planet("Earth",0.0000597, 14.9,0);


        list = new ArrayList<>();
        list.add(Sun);
        list.add(Mercury);
        //list.add(Earth);

        for(Planet p : list){
            if(p.name!="Sun")
                p.velY = Math.sqrt(G * Sun.mass / Math.abs(p.x));  //????????????
            System.out.println("v = "+p.velY);
            if (p.x > Sun.x)
                p.velY = -p.velY;
        }
    }

    void updatePositions(){

        double ax[] = new double[list.size()];
        double ay[] = new double[list.size()];
        double dx, dy, dz;          //distance between particles along axes
        double D;                   //resultant distance between particles
        double A;                   //resultant acceleration

        int i=0;
        for(Planet p1 : list){
            if(p1.name!="Sun") {
                ax[i] = ay[i] = 0;
                for (Planet p2 : list) {
                    if (p1 != p2) {
                        dx = p2.x - p1.x;
                        dy = p2.y - p1.y;
                        D = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                        A = G * p2.mass / Math.pow(D, 2);
                        ax[i] += dx * A / D;
                        ay[i] += dy * A / D;
                    }
                }
            }
            i++;
        }

        i=0;
        for(Planet p1 : list){
//			if(p1!=lista.get(0)){
            p1.velX += ax[i] * timestep;                            //accelerate
            p1.velY += ay[i] * timestep;
            //p1.x += p1.velX * timestep;                             //move
            //p1.y += p1.velY * timestep;
            p1.setPosition(p1.x+p1.velX * timestep, p1.y+p1.velY * timestep);
            System.out.println(p1.name+": "+p1.velY+" y: "+p1.y);
//			}
            i++;
        }
    }
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
        int diameter=10;
        for(Planet p : s.list){
            if(i!=0) diameter=5;
            int x = (int)(offX-diameter/2+p.x*scale);
            int y = (int)(offY-diameter/2+p.y*scale);
            g.fillOval(x, y,diameter,diameter);
            i++;
			/*for(int k=1; k<p.positions.size(); k++){
				Point pt0 = p.positions.get(k-1);
				Point pt1 = p.positions.get(k);
				g.drawLine((int)(offX+pt0.x*scale), (int)(offY+pt0.y*scale), (int)(offX+pt1.x*scale), (int)(offY+pt1.y*scale));
			}*/
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(true){
            s.timestep+=0.005;
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
        f.setBounds(0, 0, 800, 800);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SolarGUI p = new SolarGUI();
        f.add(p);
        new Thread(p).start();
        f.setVisible(true);
    }
}


