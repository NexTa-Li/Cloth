package view;

import cloth.Cloth;
import cloth.ClothSolver;
import geometry.Point3D;
import geometry.Vector3D;

public class Main implements Runnable, Config {
    public Cloth cloth;
    ClothSolver clothSolver;

    public int rows = 120;
    public int cols = 180;
    public int restLength = 2;
    public int sx = PANEL_WIDTH / 2 - (cols * restLength) / 2;
    public int sy = PANEL_HEIGHT / 2 - (rows * restLength) / 2;

    public Thread thread;
    public Vector3D light = new Vector3D(sx + (restLength * cols), (sx + rows * restLength), 15000);
    public Point3D camera = new Point3D(PANEL_WIDTH / 2, PANEL_HEIGHT / 2, -500); // min z = 255;

    public Main() {

        this.cloth = new Cloth(sx, sy, rows, cols, restLength, 0, 0);
        this.clothSolver = new ClothSolver(cloth.points, cloth.springs, cloth);

        thread = new Thread(this);
        thread.start();
    }

    int count = 0;
    int target = 200;
    double x = 0.00;
    double y = 0.0;
    double z = 0.00;
    int multiplier = 1;

    void idle() {

        if (count == target) {

            count = 0;
            target = (int) (Math.random() * 250 + 500);

            if (y != 0) {
                x = 0.00;
                y = 0.0;
                z = -0.0;
                target = 400;
            } else {
                x = 0;
                y = Math.random() * 6.0;
                z = Math.random() * 20.0;
            }

            // multiplier *= -1;
            x *= multiplier;
            z *= multiplier;
            y *= multiplier;
        }

        Vector3D wind = new Vector3D(x, y, z);
        // clothSolver.accelerate(new Vector3D(x, 0, 0));
        clothSolver.update(2, timestep, wind);
        count++;
    }

    @Override
    public void run() {

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
                idle();

                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
}
