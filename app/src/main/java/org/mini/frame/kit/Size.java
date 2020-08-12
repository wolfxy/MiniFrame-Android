package org.mini.frame.kit;

/**
 * Created by Wuquancheng on 2018/8/24.
 */

public class Size {
    public int width;
    public int height;

    public Size() {
        this(0, 0);
    }

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
