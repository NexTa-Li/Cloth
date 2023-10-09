package cloth;

import java.util.List;
import java.util.Vector;

import geometry.Vector3D;
import view.Config;

public class ClothSolver {
    List<Point> points;
    List<Spring> springs;

    public double averageZ = 0;
    public double lastAvgZ = 0;
    Cloth cloth;

    int currCol = 0;

    public ClothSolver(List<Point> points, List<Spring> springs, Cloth cloth) {
        this.points = points;
        this.springs = springs;
        this.cloth = cloth;
    }

    public void update(int substeps, double dt, Vector3D wind) {
        final double sub_dt = dt / substeps;

        for (int i = 0; i < substeps; i++) {
            update(sub_dt, wind);
        }

        // keep track of average z
        for (int i = 0; i < points.size(); i++) {
            averageZ += (int) points.get(i).position.z;
        }

        averageZ /= points.size();

    }

    public void update(double dt, Vector3D wind) {
        maintainLength();
        accelerate(Config.GRAVITY);
        accelerateRand(wind);
        accumulateSpringForce();

        for (int i = 0; i < points.size(); i++) {

            points.get(i).update(dt);

        }

    }

    void accumulateSpringForce() {
        Vector3D forceVector = new Vector3D(0, 0, 0);
        int p1, p2;
        double x1, x2, y1, y2, z1, z2, distance, force, vX, vY, vZ;

        for (int i = 0; i < springs.size(); i++) {
            p1 = springs.get(i).p1;
            p2 = springs.get(i).p2;

            x1 = points.get(p1).position.x;
            x2 = points.get(p2).position.x;
            y1 = points.get(p1).position.y;
            y2 = points.get(p2).position.y;
            z1 = points.get(p1).position.z;
            z2 = points.get(p2).position.z;

            // calculate distance, This is current length of spring
            distance = points.get(p1).position.distance(points.get(p2).position);

            // System.out.println(distance);

            // calculate force
            if (distance != 0) { // skip 0 to avoid division by 0
                vX = points.get(p1).velocity.x - points.get(p2).velocity.x;
                vY = points.get(p1).velocity.y - points.get(p2).velocity.y;
                vZ = points.get(p1).velocity.z - points.get(p2).velocity.z;

                force = (distance - springs.get(i).restLen) * cloth.ks +
                        (vX * (x1 - x2) + vY * (y1 - y2) + vZ * (z1 - z2)) * cloth.kd / distance;

                // calculate force vector
                forceVector.setX(force * ((x1 - x2) / distance));
                forceVector.setY(force * ((y1 - y2) / distance));
                forceVector.setZ(force * ((z1 - z2) / distance));

                // add force to points
                points.get(p1).force.sub(forceVector);
                points.get(p2).force.add(forceVector);
            }
        }
    }

    public void accelerate(Vector3D acceleration) {
        for (int i = 0; i < points.size(); i++) {
            points.get(i).force.add(acceleration);

            // keep track of average z

        }
    }

    public void accelerateRand(Vector3D wind) {

        Vector3D wind2 = new Vector3D(wind.x, wind.y, wind.z);

        // generate random multiplier for wind
        double randMult = Math.random() * (2 * Math.PI);

        // apply wind to each point in the column

        for (int i = 0; i < cloth.rows; i++) {
            for (int j = 0; j < cloth.cols; j++) {

                double windX = wind.x * Math.sin(randMult);
                wind2.setX(windX);

                double windZ = wind.z * Math.sin(randMult); // wind.z * Math.sin(((j + i) % (Math.PI * 2)) / (Math.PI -
                // 180));
                wind2.setZ(windZ);

                wind2.scale(randMult);

                points.get(i * cloth.cols + j).force.add(wind2);
            }
        }

    }

    void maintainLength() {
        int p1, p2;
        double x1, x2, y1, y2, z1, z2, distance;

        for (int i = 0; i < springs.size(); i++) {

            p1 = springs.get(i).p1;
            p2 = springs.get(i).p2;

            x1 = points.get(p1).position.x;
            x2 = points.get(p2).position.x;
            y1 = points.get(p1).position.y;
            y2 = points.get(p2).position.y;
            z1 = points.get(p1).position.z;
            z2 = points.get(p2).position.z;

            // calculate distance, This is current length of spring
            distance = points.get(p1).position.distance(points.get(p2).position);

            // check if distance != than rest length

            if (distance != springs.get(i).restLen) {
                // calculate force

                // find the difference in positions between the two points
                double diffX = x1 - x2;
                double diffY = y1 - y2;
                double diffZ = z1 - z2;
                double moveAmount = (springs.get(i).restLen - distance) / 2;
                // find the unit vector of the vector between the two points
                Vector3D unitVector = new Vector3D(diffX, diffY, diffZ);
                unitVector.normalize();

                // find the amount to move each point

                // scale the unit vector by the move amount
                unitVector.scale(moveAmount);

                // move the points
                if (points.get(springs.get(i).p2).pinned && !points.get(springs.get(i).p1).pinned) {
                    unitVector.scale(2);
                    points.get(p1).position.add(unitVector);
                    break;
                }

                if (points.get(springs.get(i).p1).pinned && !points.get(springs.get(i).p2).pinned) {
                    unitVector.scale(2);
                    points.get(p2).position.sub(unitVector);
                    break;
                }

                points.get(p1).position.add(unitVector);
                points.get(p2).position.sub(unitVector);
            }
        }
    }
}
