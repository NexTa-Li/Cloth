package view;

import geometry.Vector3D;

public interface Config {

    static final int PANEL_WIDTH = 1920;// 1620
    static final int PANEL_HEIGHT = 1080; // 900

    static final int TICKRATE = 100000;
    static final float timestep = 0.005f;
    static final Vector3D GRAVITY = new Vector3D(0, 0, 0);
}
