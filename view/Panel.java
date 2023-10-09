package view;

import javax.sound.sampled.Line;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import cloth.Cloth;
import cloth.ClothSolver;
import cloth.Point;
import cloth.Spring;
import geometry.Point3D;
import geometry.Vector3D;

public class Panel extends JPanel implements Runnable, Config {
    Cloth cloth;
    ClothSolver clothSolver;

    int rows;
    int cols;

    int sx;
    int sy;
    int restLength;

    int shift;
    Thread thread;
    Vector3D light;
    Point3D camera;
    int count = 0;
    Color color = new Color(255, 215, 0);

    public Panel(Main main) {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.requestFocus();

        this.rows = main.rows;
        this.cols = main.cols;
        this.restLength = main.restLength;
        this.sx = main.sx;
        this.sy = main.sy;

        this.shift = sx - 150;
        this.cloth = main.cloth;
        this.clothSolver = main.clothSolver;
        this.light = main.light;
        this.camera = main.camera;

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        long cnt = 0;
        double drawInterval = 1000000000 / TICKRATE;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (thread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            // Update the game logic when enough time has passed for a frame
            if (delta >= 1.0) {
                revalidate();
                repaint();

                if (cnt % 100 == 0) {
                    color = generateRainbowSequenceColor();
                }
                cnt++;
                delta--;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics;

        // g.setStroke(new BasicStroke(1));

        int shade = 0;
        for (int i = 0; i < cloth.rows; i++) {
            for (int j = 0; j < cloth.cols; j++) {

                if (j == cloth.cols - 1 || i == cloth.rows - 1) {
                    break;
                }

                Point p = cloth.points.get(i * cloth.cols + j);
                Point p2 = cloth.points.get((i + 1) * cloth.cols + j);
                Point p3 = cloth.points.get(i * cloth.cols + j + 1);
                Point p4 = cloth.points.get((i + 1) * cloth.cols + j + 1);

                int[] xPoints = { (int) p.position.x, (int) p2.position.x, (int) p4.position.x,
                        (int) p3.position.x };

                int[] yPoints = { (int) p.position.y, (int) p2.position.y, (int) p4.position.y,
                        (int) p3.position.y };

                int[] zPoints = { (int) (p.position.z + shift), (int) (p2.position.z +
                        shift), (int) (p4.position.z + shift), (int) (p3.position.z + shift) };

                // double avgY = (p.position.y + p2.position.y + p3.position.y + p4.position.y)
                // / 4;

                // double avgZ = (p.position.z + p2.position.z + p3.position.z + p4.position.z)
                // / 4;

                shade = 0;

                // vector from camera to to point 1 on the plane = p - camera

                Vector3D v = p.position.sub(camera);
                // v.normalize();
                Vector3D v1 = p2.position.sub(p.position);
                Vector3D v2 = p3.position.sub(p.position);

                // v1.normalize();
                // v2.normalize();

                Vector3D normal = v1.cross(v2); // normal vector of plane

                normal.normalize();

                // reflect vector from camera to point across normal

                Vector3D reflected = v.subV(normal.scaleV(2 * v.dot(normal)));

                // calculate the angle between the reflected vector and the vector from the
                // camera to the light source

                double reflectedLength = reflected.distance();
                double lightLength = light.distance();

                if (reflectedLength > 0 && lightLength > 0) {
                    double cosAngle = reflected.dot(light) / (reflectedLength * lightLength);
                    cosAngle = Math.max(-1.0, Math.min(1.0, cosAngle)); // Clamp the value to[-1, 1] to handle precision
                                                                        // issues.
                    double angle = Math.acos(cosAngle);
                    shade = (int) Math.round(angle * 255 / Math.PI);

                    // scale based on distance from camera
                    double distance = p.position.distance(camera);

                    shade = (int) (shade * (1 - distance / 1000));
                    g.setColor(new Color((int) (color.getRed() * angle / Math.PI),
                            (int) (color.getGreen() * angle / Math.PI), (int) (color.getBlue() * angle / Math.PI)));
                } else {
                    // Handle division by zero or other exceptional cases.
                    shade = 0; // Set a default shade or handle the error as appropriate.
                    g.setColor(new Color(shade, shade, shade));
                }

                Polygon points = new Polygon(xPoints, yPoints, 4);

                // Polygon points2 = new Polygon(zPoints, yPoints, 4);
                // g.fill(points2);

                g.fill(points);

            }
        }

        // g.setColor(new Color(255, 255, 255, 50));
        // for (Point p : cloth.points) {
        // Ellipse2D.Double points = new Ellipse2D.Double(p.position.x - 1.0,
        // p.position.y - 1.0, 2, 2);
        // g.fill(points);
        // }

        // // // draw springs
        // g.setColor(new Color(255, 255, 255, 20));

        // for (Spring s : cloth.springs) {

        // Line2D.Double line = new Line2D.Double(cloth.points.get(s.p1).position.x,
        // cloth.points.get(s.p1).position.y, cloth.points.get(s.p2).position.x,
        // cloth.points.get(s.p2).position.y);

        // }

    }

    public Color generateRainbowSequenceColor() {
        // Generate a random hue in the range of 0.0 to 1.0 (representing the entire
        // color spectrum)
        float hue = (count % 5000) / 5000.0f;

        // Set saturation and lightness to create vibrant colors
        float saturation = 1.0f; // Full saturation
        float lightness = 1.0f; // You can adjust lightness as needed

        count++;
        // Convert HSL color to RGB
        return Color.getHSBColor(hue, saturation, lightness);
    }
}
