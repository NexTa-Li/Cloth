package cloth;

import java.util.ArrayList;
import java.util.List;

import view.Config;

public class Cloth {
    public int rows, cols, restLength;

    // maybe change to Lists
    public List<Point> points;
    public List<Integer> externalPoints;
    public List<Segment> springs;

    public double ks; // spring constant
    public double kd; // damping constant

    public Cloth(int x, int y, int rows, int cols, int restLength, double ks, double kd) {
        this.rows = rows;
        this.cols = cols;
        this.restLength = restLength;
        this.ks = ks;
        this.kd = kd;

        this.points = new ArrayList<Point>();
        this.externalPoints = new ArrayList<>();
        this.springs = new ArrayList<Segment>();
        createCloth(x, y);
    }

    // create points and springs
    void createCloth(int x, int y) {

        for (int i = 0; i < rows; i++) {
            double py = y + i * restLength;

            for (int j = 0; j < cols; j++) {
                double px = x + j * restLength;

                Point p = new Point(px, py, 0);

                // pin the top row
                // if (i == 0 && j == cols / 2) { //
                // p.pinned = true;
                // }

                if (i == 0 && j == 0 || i == 0 && j == cols - 1) {//
                    //
                    p.pinned = true;
                }

                // pin bottom two corners
                if (i == rows - 1 && j == 0 || i == rows - 1 && j == cols - 1) {

                    p.pinned = true;
                }
                // if (i == 0 && j % 30 == 0) {
                // p.pinned = true;
                // }
                points.add(p);

                // horizontal structural springs
                if (j > 0) {
                    Segment s = new Segment(i * cols + j, i * cols + j - 1, restLength);
                    springs.add(s);
                }

                // vertical structural springs
                if (i > 0) {
                    Segment s = new Segment(i * cols + j, (i - 1) * cols + j, restLength);
                    springs.add(s);
                }
            }
        }

        // pin the bottom row

        // add all edge point indicies to externalPoints in order from the left to the
        // top to the right to the bottom

        // top edge
        for (int i = 0; i < cols; i++) {
            externalPoints.add(i);
        }

        // right edge
        for (int i = 1; i < rows; i++) {
            externalPoints.add(i * cols + cols - 1);
        }

        // bottom edge
        for (int i = cols - 1; i >= 0; i--) {
            externalPoints.add((rows - 1) * cols + i);
        }

        // left edge
        for (int i = rows - 2; i > 0; i--) {
            externalPoints.add(i * cols);
        }
    }

    // create points and springs
    void createZCloth(int x, int y) {

        for (int i = 0; i < rows; i++) {
            double py = y + i * restLength;

            for (int j = 0; j < cols; j++) {
                double px = x + j * restLength;

                Point p = new Point(Config.PANEL_WIDTH / 2, py, px);

                // pin the top row
                // if (i == 0) {
                // p.pinned = true;
                // }

                if (i == 0 && j == 0 || i == 0 && j == cols - 1) { // || i == 0 && j == cols / 2
                    p.pinned = true;
                }

                // pin bottom two corners
                if (i == rows - 1 && j == 0 || i == rows - 1 && j == cols - 1) {
                    p.pinned = true;
                }

                points.add(p);

                // horizontal structural springs
                if (j > 0) {
                    Segment s = new Segment(i * cols + j, i * cols + j - 1, restLength);
                    springs.add(s);
                }

                // vertical structural springs
                if (i > 0) {
                    Segment s = new Segment(i * cols + j, (i - 1) * cols + j, restLength);
                    springs.add(s);
                }
            }
        }

        // pin the bottom row

        // add all edge point indicies to externalPoints in order from the left to the
        // top to the right to the bottom

        // top edge
        for (int i = 0; i < cols; i++) {
            externalPoints.add(i);
        }

        // right edge
        for (int i = 1; i < rows; i++) {
            externalPoints.add(i * cols + cols - 1);
        }

        // bottom edge
        for (int i = cols - 1; i >= 0; i--) {
            externalPoints.add((rows - 1) * cols + i);
        }

        // left edge
        for (int i = rows - 2; i > 0; i--) {
            externalPoints.add(i * cols);
        }
    }

    public int[] getXarr() {
        int[] xArr = new int[externalPoints.size()];

        for (int i = 0; i < externalPoints.size(); i++) {
            xArr[i] = (int) points.get(externalPoints.get(i)).position.x;
        }

        return xArr;
    }

    public int[] getYarr() {
        int[] yArr = new int[externalPoints.size()];

        for (int i = 0; i < externalPoints.size(); i++) {
            yArr[i] = (int) points.get(externalPoints.get(i)).position.y;
        }

        return yArr;
    }
}
