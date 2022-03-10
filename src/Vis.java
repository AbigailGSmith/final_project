import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;


public class Vis extends JPanel implements ActionListener, MouseInputListener {

    public static boolean collision;
    private Point mouseDown;

    public Vis() {

        super();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();
        mouseDown = new Point(x,y);
        //for (Entry m: Main.entries) {
          //  m.selected = false;
           // m.selection = true;
        //}
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        repaint();
    }

    public void checkCollision(int x, int y) {

        collision = false;
    }

    //adding tool tips to each point
    @Override
    public void mouseMoved(MouseEvent e) {

        repaint();

        //TODO draw tooltip

    }
}

