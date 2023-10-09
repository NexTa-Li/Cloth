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

    Thread thread;

    public Panel() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        this.cloth = new Cloth(250, 150, 20, 60, 15, 100, 10000);
        this.clothSolver = new ClothSolver(cloth.points, cloth.springs, cloth);

        this.setBackground(Color.black);
        this.setFocusable(true);
        this.requestFocus();

        thread = new Thread(this);
        thread.start();
    }

    int count = 0;
    int target = 1400;
    double x = 0.00;
    double y = -0.0;
    double z = 2.01;

    void idle() {

        if (count == target) {
            x *= -1;

            z *= -1;
            y *= -1;

            // if (Math.abs(z) < 2) {
            // z *= 1.5;
            // } else {
            // z = 1.0;
            // }
            count = 0;
            // target = (int) (Math.random() * 500 + 1000);
        }

        Vector3D wind = new Vector3D(x, 0, z);
        // clothSolver.accelerate(new Vector3D(x, 0, 0));
        clothSolver.update(8, timestep, wind);
        count++;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        double drawInterval = 1000000000 / TICKRATE;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        long timer = 0;
        int drawCount = 0;

        while (thread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            // Update the game logic when enough time has passed for a frame
            if (delta >= 1.0) {
                revalidate();
                repaint();
                idle();

                delta--;
                drawCount++;
            }

            // Calculate the time to sleep in order to achieve the desired frame rate
            long sleepTime = (long) (drawInterval - (System.nanoTime() - currentTime));

            // if (sleepTime > 0) {
            // try {
            // Thread.sleep(sleepTime / 1000000); // Convert nanoseconds to milliseconds
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }
            // }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }

    Point3D camera = new Point3D(0, 600, -100);

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // System.out.println(clothSolver.averageZ);

        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(1));

        // draw cloth
        // g.setColor(new Color(0.5f, 0.5f, 0.5f));
        // g.fillPolygon(cloth.getXarr(), cloth.getYarr(), cloth.externalPoints.size());
        int shade = 50;
        double distance = 0;

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

                // double shift = PANEL_WIDTH / 2 + 350;

                // int[] zPoints = { (int) (p.position.z + shift), (int) (p2.position.z +
                // shift), (int) (p4.position.z + shift), (int) (p3.position.z + shift) };

                double avgY = (p.position.y + p2.position.y + p3.position.y + p4.position.y) / 4;

                double avgZ = (p.position.z + p2.position.z + p3.position.z + p4.position.z) / 4;

                shade = 0;
                Line2D.Double line = new Line2D.Double(p.position.x,
                        p.position.y, p2.position.x, p2.position.y);

                Line2D line2 = new Line2D.Double(p3.position.x,
                        p3.position.y, p.position.x, p.position.y);

                // calculation of distance from camera to point
                distance = camera.distance(new Point3D(0, avgY, avgZ));

                // the less the distance, the darker the rgb value from 0 to 255

                shade = (int) (distance);

                if (shade >= 255) {
                    shade = 255;
                } else if (shade <= 10) {
                    shade = 10;
                }

                // g.setColor(new Color(mapToGrayscale(distance), 1.0f, 1.0f));

                g.setColor(new Color(shade, shade, shade));

                // g.setColor(new Color((int) (distance % 255), (int) (distance % 255), (int)
                // (distance % 255)));

                // g.setColor(new Color((float) Math.abs(averageZ * 2), (float)
                // Math.abs(averageZ * 2),
                // (float) Math.abs(averageZ * 2)));

                Polygon points = new Polygon(xPoints, yPoints, 4);

                // Polygon points2 = new Polygon(zPoints, yPoints, 4);
                // g.fill(points2);
                g.fill(points);
                g.draw(line);
                // g.draw(line2);

                Vector3D v1 = p2.position.sub(p.position);
                Vector3D v2 = p3.position.sub(p.position);
                Vector3D normal = v1.cross(v2);

                // reflect vector from camera to point across normal

                Vector3D reflected = camera.sub(p.position);

            }
        }

        // int i = cloth.rows / 2;
        // int j = cloth.cols / 2;

        // Point p = cloth.points.get(i * cloth.cols + j);
        // // get the point in the row below
        // Point p2 = cloth.points.get((i + 1) * cloth.cols + j);
        // // get the point in the column to the right
        // Point p3 = cloth.points.get(i * cloth.cols + j + 1);
        // // get the point in the row below and column to the right
        // Point p4 = cloth.points.get((i + 1) * cloth.cols + j + 1);

        // averageZ = (p.position.z + p2.position.z + p3.position.z + p4.position.z) /
        // 4;

        // System.out.println(distance);
        // draw points

        g.setColor(new Color(255, 255, 255, 60));
        for (Point p : cloth.points) {
            Ellipse2D.Double points = new Ellipse2D.Double(p.position.x - 2.5, p.position.y - 2.5, 5, 5);
            g.fill(points);
        }

        // // draw springs
        g.setColor(new Color(255, 255, 255, 20));

        // for (Spring s : cloth.springs) {

        // Line2D.Double line = new Line2D.Double(cloth.points.get(s.p1).position.x,
        // cloth.points.get(s.p1).position.y, cloth.points.get(s.p2).position.x,
        // cloth.points.get(s.p2).position.y);

        // }

    }

    public static float mapToGrayscale(double value) {

        // Ensure that the input value is within the desired range
        float f = (float) ((value) / 20);

        // Ensure the grayscale value is within the valid range [0.0f, 1.0f]
        return (float) Math.max(0.0, Math.min(1.0, f));
    }

}
