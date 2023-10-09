package geometry;

public class Vector3D {
    public double x, y, z;

    public Vector3D() {
        this(0, 0, 0);
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector3D v) {
        set(v.x, v.y, v.z);
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
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

    public Vector3D subV(Vector3D v) {
        return new Vector3D(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    public void sub(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    public void reset() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public double distanceSq() {
        return x * x + y * y + z * z;
    }

    public double distance() {
        return (double) Math.sqrt(x * x + y * y + z * z);
    }

    // scale
    public void scale(double s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
    }

    // scale
    public Vector3D scaleV(double s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;

        return this;
    }

    public void divide(double s) {
        this.x /= s;
        this.y /= s;
        this.z /= s;
    }

    // normalize

    public void normalize() {
        double length = distance();

        if (length != 0) {
            this.x /= length;
            this.y /= length;
            this.z /= length;
        }
    }

    // cross
    public Vector3D cross(Vector3D v) {
        double x = this.y * v.z - this.z * v.y;
        double y = this.z * v.x - this.x * v.z;
        double z = this.x * v.y - this.y * v.x;

        return new Vector3D(x, y, z);
    }

    // dot product
    public double dot(Vector3D v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }
}
