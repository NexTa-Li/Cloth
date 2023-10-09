package view;

import javax.swing.JFrame;

public class Frame extends JFrame {
    Panel panel = new Panel();

    public Frame() {
        this.setTitle("3D Cloth Simulation - 2D render");
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Frame();
    }

}
