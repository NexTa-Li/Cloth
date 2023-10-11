package cloth;

import geometry.Point3D;
import geometry.Vector3D;

public class Point {
    public Point3D position;
    public Point3D oldPosition;
    public Vector3D velocity;
    public Vector3D force;
    public boolean pinned = false;

    public Point() {
        this(0, 0, 0);
    }

    public Point(double x, double y, double z) {
        this.position = new Point3D(x, y, z);
        this.oldPosition = new Point3D(x, y, z);
        this.force = new Vector3D(0, 0, 0);
        this.velocity = new Vector3D();
    }

    // verlet integration
    public void update(double dt) {
        if (pinned)
            return;

        double vx = position.x - oldPosition.x;
        double vy = position.y - oldPosition.y;
        double vz = position.z - oldPosition.z;

        this.velocity = new Vector3D(vx, vy, vz);

        this.oldPosition = new Point3D(position);

        double dtSq = dt * dt;

        double dx = velocity.x + force.x * dtSq;
        double dy = velocity.y + force.y * dtSq;
        double dz = velocity.z + force.z * dtSq;

        this.position.add(dx, dy, dz);

        this.force.reset(); // reset force
    }

    public static boolean checkcirclecollide(double x1, double y1, double z1, double r1, double x2, double y2,
            double z2,
            double r2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2) < (r1 + r2) * (r1 + r2);
    }

    public static boolean checkcirclecollide(Point3D p1, double r1, Point3D p2, double r2) {
        return checkcirclecollide(p1.x, p1.y, p1.z, r1, p2.x, p2.y, p2.z, r2);
    }
}
