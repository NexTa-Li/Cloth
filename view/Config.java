package view;

import geometry.Vector3D;

public interface Config {

    static final int PANEL_WIDTH = 1500;// 1620
    static final int PANEL_HEIGHT = 1000; // 900

    static final int TICKRATE = 10060;
    static final float timestep = 0.004f;
    static final Vector3D GRAVITY = new Vector3D(0, 1, 0);
}
