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
}
