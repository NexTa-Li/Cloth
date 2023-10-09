package geometry;

public class Point3D {
    public double x, y, z;

    public Point3D() {
        this(0, 0, 0);
    }

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // copy
    public Point3D(Point3D p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public void add(Vector3D v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }

    public void add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    // sub
    public void sub(Vector3D v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
    }

    public void sub(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    public Vector3D sub(Point3D p) {
        return new Vector3D(this.x - p.x, this.y - p.y, this.z - p.z);
    }

    // dot product
    public double dot(Point3D p) {
        return this.x * p.x + this.y * p.y + this.z * p.z;
    }

    // distance
    public double distanceSq(Point3D p) {
        double dx = this.x - p.x;
        double dy = this.y - p.y;
        double dz = this.z - p.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double distance(Point3D p) {
        return (double) Math.sqrt(distanceSq(p));
    }
}
