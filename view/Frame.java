package view;

import javax.swing.JFrame;

public class Frame extends JFrame {
    Panel panel;
    Main main = new Main();

    public Frame() {
        this.setTitle("3D Cloth Simulation - 2D render");

        panel = new Panel(main);
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
