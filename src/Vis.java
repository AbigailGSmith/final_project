import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;


public class Vis extends JPanel implements ActionListener, MouseInputListener {

    public static boolean collision;
    private Point mouseDown;
    private ColorScheme currentColor;
    private Hashtable<String, Country> countries;
    private String selected;
    private int currentYear;
    private Color crematedColor;
    private Color otherColor;
    private ArrayList<Slice> noodles;

    enum ColorScheme {

        PINKANDPURPLE, //#5F4B8B, #E69A8D
        CORALANDBLUE, //#FC766A, #5B84B1
        BLUEANDYELLOW //#00B1D2, #FDDB27
    }

    public Vis() {

        super();

        addMouseListener(this);
        addMouseMotionListener(this);

        changeColorScheme(ColorScheme.PINKANDPURPLE);

        noodles = new ArrayList<>();
        countries = new Hashtable<>();
        selected = null;
        currentYear = 2000;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        noodles.clear();

        int centerx = getWidth() / 2;
        int centery = getHeight() / 2;
        int proportionZoom = Math.min(getWidth(), getHeight()) / 6;
        int proportion = Math.min(getWidth(), getHeight()) / 8;
        //draws the text above the slider
        //change f
        Font f = new Font( "SansSerif", Font. PLAIN, 36);

        //draw key
        g.setColor(crematedColor);
        g.fillRect(5, getHeight() - 65, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString("Cremated", 30, getHeight() - 50);

        g.setColor(otherColor);
        g.fillRect(5, getHeight() - 40, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString("Other (embalming, composting, etc.)", 30, getHeight() - 25);

        g.setFont(f);
        FontRenderContext frc = new FontRenderContext(null, true, true);
        Rectangle2D r2d = f.getStringBounds(String.valueOf(currentYear), frc);

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(currentYear), (int) (getWidth() / 2 - (r2d.getWidth() / 2)), getHeight() - 10);

        if (selected == null) {

            Hashtable<String, DataPoint> pointsToDraw = new Hashtable<>();
            Set<String> countryNames = countries.keySet();
            for (String s: countryNames) {

                if (countries.get(s).getData(currentYear) != null) {

                    pointsToDraw.put(s, countries.get(s).getData(currentYear));
                }
            }

            g.setColor(Color.BLACK);
            g.drawOval(centerx - proportion, centery - proportion, 2 * proportion, 2 * proportion);

            Set<String> countryNamesToDraw = pointsToDraw.keySet();
            if (countryNamesToDraw.size() != 0) {

                double totalDeaths = 0;

                for (String country: countryNamesToDraw) {

                    //count up yer dead
                    totalDeaths += pointsToDraw.get(country).dead;
                }

                double startAngle = 0;
                for (String country: countryNamesToDraw) {

                    double dead = pointsToDraw.get(country).dead;
                    double cremated = pointsToDraw.get(country).cremated;

                    //draw the noods
                    Graphics2D g2 = (Graphics2D) g;
                    Slice s = new Slice(new Point(centerx, centery), proportion+1, 2 * proportion, startAngle, startAngle + (dead * 360d / totalDeaths), Slice.SliceType.COUNTRY, country, (int) dead);
                    g2.setPaint(Color.BLACK);
                    g2.draw(s);

                    noodles.add(s);

                    //ya know, font stuff, you wouldn't understand
                    Font f2 = new Font( "SansSerif", Font. PLAIN, proportionZoom / 8);
                    FontRenderContext frc2 = new FontRenderContext(null, true, true);

                    //draws the country name
                    TextLayout l = new TextLayout(country, f2, frc2);
                    Rectangle2D r = l.getBounds();
                    Point2D pointToDrawText = s.getStringLocation();
                    int x = (int) (pointToDrawText.getX() - (r.getWidth() / 2));
                    int y = (int) (((pointToDrawText.getY() * 2) - (r.getHeight() - l.getDescent()))/2);
                    y += l.getAscent() - l.getDescent();
                    l.draw((Graphics2D) g,(float) x,(float) y);

                    //cremated
                    double middleAngle = cremated * (dead * 360d / totalDeaths) / dead;
                    Slice k = new Slice(new Point(centerx, centery), 2 * proportion+1, 3 * proportion, startAngle, startAngle + middleAngle, Slice.SliceType.CREMATED, country, (int) cremated);
                    g2.setPaint(crematedColor);
                    g2.fill(k);

                    noodles.add(k);

                    //other
                    Slice o = new Slice(new Point(centerx, centery), 2 * proportion+1, 3 * proportion, startAngle + middleAngle, startAngle + (dead * 360 / totalDeaths), Slice.SliceType.OTHER, country, (int) (dead - cremated));
                    g2.setPaint(otherColor);
                    g2.fill(o);

                    noodles.add(o);

                    startAngle += (dead * 360 / totalDeaths);
                }
            }

        } else {

            if (countries.get(selected).getData(currentYear) != null) {


                g.drawOval(centerx - proportionZoom, centery - proportionZoom, 2 * proportionZoom, 2 * proportionZoom);

                //ya know, font stuff, you wouldn't understand
                Font f2 = new Font( "SansSerif", Font. PLAIN, proportionZoom / 2);
                FontRenderContext frc2 = new FontRenderContext(null, true, true);

                //draws the country name
                TextLayout l = new TextLayout(selected, f2, frc2);
                Rectangle2D r = l.getBounds();
                int x = (int) (centerx - (r.getWidth() / 2));
                int y = (int) ((getHeight() - (r.getHeight() - l.getDescent()))/2);
                y += l.getAscent() - l.getDescent();
                l.draw((Graphics2D) g, (float) x, (float) y);

                //draw the noodles
                int c = countries.get(selected).getData(currentYear).cremated;
                int d = countries.get(selected).getData(currentYear).dead;

                Graphics2D g2 = (Graphics2D) g;

                Slice s = new Slice(new Point(centerx, centery), proportionZoom+1, 2 * proportionZoom, 0, (c * 360 / d), Slice.SliceType.CREMATED, selected, c);
                g2.setPaint(crematedColor);
                g2.fill(s);

                noodles.add(s);

                Slice m = new Slice(new Point(centerx, centery), proportionZoom+1, 2 * proportionZoom, (c * 360 / d), 360, Slice.SliceType.OTHER, selected, d-c);
                g2.setPaint(otherColor);
                g2.fill(m);

                noodles.add(m);
            } else {

                //ya know, font stuff, you wouldn't understand
                Font f2 = new Font( "SansSerif", Font. PLAIN, proportionZoom / 4);
                FontRenderContext frc2 = new FontRenderContext(null, true, true);

                //draws the country name
                TextLayout l = new TextLayout("There is no data for " + selected + " for the selected year.", f2, frc2);
                Rectangle2D r = l.getBounds();
                int x = (int) (centerx - (r.getWidth() / 2));
                int y = (int) ((getHeight() - (r.getHeight() - l.getDescent()))/2);
                y += l.getAscent() - l.getDescent();
                l.draw((Graphics2D) g, (float) x, (float) y);
            }

        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();
        mouseDown = new Point(x,y);
        boolean set = false;

        for (Slice s: noodles) {

            if (s.contains(mouseDown)) {

                selected = s.country;
                set = true;
            }
        }
        if (set == false) {
            selected = null;
        }
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

        for (Slice s: noodles) {

            if (s.contains(e.getPoint())) {

                if (s.type == Slice.SliceType.COUNTRY) {

                    setToolTipText(s.country + "'s total number of deaths in " + currentYear + ": " + s.count);
                } else if (s.type == Slice.SliceType.CREMATED) {

                    setToolTipText(s.country + " cremations in " + currentYear + ": " + s.count);
                } else if (s.type == Slice.SliceType.OTHER) {

                    setToolTipText(s.country + " non-cremations in " + currentYear + ": " + s.count);
                }
            }
        }

        repaint();

        //TODO draw tooltip

    }

    public void reset() {

        selected = null;
    }

    public ColorScheme getColorScheme() {

        return currentColor;
    }

    public void changeColorScheme(ColorScheme c) {

        currentColor = c;
        if (currentColor == ColorScheme.PINKANDPURPLE) {

            crematedColor = new Color(90, 75, 139);
            otherColor = new Color(233,26,141);
        } else if (currentColor == ColorScheme.CORALANDBLUE) {

            crematedColor = new Color(252,118,106);
            otherColor = new Color(91, 132, 177);
        } else if (currentColor == ColorScheme.BLUEANDYELLOW) {

            crematedColor = new Color(0, 177, 210);
            otherColor = new Color(253, 219, 39);
        }

        repaint();
    }

    public void setCountries(Hashtable<String, Country> c) {

        countries = c;
    }

    public void setCurrentYear(int y) {

        currentYear = y;
    }
}

