package org.rivierarobotics;

import static com.google.common.base.Preconditions.checkState;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class LidarPanel extends JPanel {

    private static final long serialVersionUID = 6510570479197032598L;

    private final List<Dot> dots = new ArrayList<>();

    public LidarPanel() {
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                dots.add(calculateToLidar(e.getX(), e.getY()));
                repaint();
            }

        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.RED);
        for (Dot dot : dots) {
            Vector2d vec = calculateFromLidar(dot);
            g.fillOval((int) vec.getX() - 3, (int) vec.getY() - 3, 6, 6);
        }
        double lx = getLidarX();
        double ly = getLidarY();
        g.setColor(Color.BLUE);
        g.fillOval((int) lx - 5, (int) ly - 5, 10, 10);
    }

    private Dot calculateToLidar(double x, double y) {
        Vector2d vec = new Vector2d(x, y);
        // make Lidar origin
        vec = vec.sub(getLidarX(), getLidarY());
        // negate Y to correct math
        vec = vec.scale(1, -1);
        double ang = vec.getAngle();
        double dist = vec.getDistance();
        return Dot.from(ang, dist);
    }

    private Vector2d calculateFromLidar(Dot dot) {
        double x = Math.cos(Math.toRadians(dot.angle()));
        double y = -Math.sin(Math.toRadians(dot.angle()));
        Vector2d vec = new Vector2d(x, y);
        vec = vec.scale(dot.distance());
        // offset to Lidar
        vec = vec.add(getLidarX(), getLidarY());
        return vec;
    }

    @Override
    public Dimension getPreferredSize() {
        if (getParent() != null) {
            return getParent().getSize();
        }
        return super.getPreferredSize();
    }

    public List<Dot> getDots() {
        return this.dots;
    }

    public double getLidarX() {
        checkState(getParent() != null,
                "Lidar is only positioned after displayed");
        return getParent().getWidth() / 2;
    }

    public double getLidarY() {
        checkState(getParent() != null,
                "Lidar is only positioned after displayed");
        return getParent().getHeight() / 2;
    }

}
