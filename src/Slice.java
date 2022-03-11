import java.awt.*;
import java.awt.geom.*;

public class Slice extends Path2D.Double {

    Point center;
    int insideRadius;
    int outsideRadius;
    double startAngle;
    double endAngle;
    SliceType type;
    String country;
    int count;

    public enum SliceType {
        COUNTRY,
        CREMATED,
        OTHER
    }

    public Slice(Point center, int insideRadius, int outsideRadius, double startAngle, double endAngle, SliceType t, String country, int count) {

        this.center = center;
        this.insideRadius = insideRadius;
        this.outsideRadius = outsideRadius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.type = t;
        this.country = country;
        this.count = count;
        build();
    }



    public void build() {

        Arc2D arc1 = new Arc2D.Double(center.x - outsideRadius, center.y - outsideRadius, 2 * outsideRadius, 2 * outsideRadius, startAngle, endAngle - startAngle, Arc2D.OPEN);
        Point2D start1 = arc1.getStartPoint();
        Point2D end1 = arc1.getEndPoint();
        //TODO draw outside curve
        Arc2D arc2 = new Arc2D.Double(center.x - insideRadius, center.y - insideRadius, 2 * insideRadius, 2 * insideRadius, endAngle, - (endAngle - startAngle), Arc2D.OPEN);
        Point2D start2 = arc2.getStartPoint();
        Point2D end2 = arc2.getEndPoint();
        Line2D.Double l1 = new Line2D.Double(end1, start2);
        Line2D.Double l2 = new Line2D.Double(start1, end2);
        append(arc1, true);
        append(l1, true);
        append(arc2, true);
        append(l2, true);

    }

    public Point2D getStringLocation() {

        Rectangle2D r = getBounds();

        Arc2D a = new Arc2D.Double(

                center.x - (insideRadius + ((outsideRadius - insideRadius)/2)),
                center.y - (insideRadius + ((outsideRadius - insideRadius)/2)),
                2 * (insideRadius + ((outsideRadius - insideRadius)/2)),
                2 * (insideRadius + ((outsideRadius - insideRadius)/2)),
                startAngle + ((endAngle - startAngle)/2),
                0, Arc2D.OPEN);

        return a.getEndPoint();
    }
}
