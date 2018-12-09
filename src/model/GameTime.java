package model;

import model.framework.GraphicalObject;
import view.framework.DrawTool;

public class GameTime extends GraphicalObject {

    private double gameTime;
    private int tag;

    public GameTime(){

    }

    @Override
    public void draw(DrawTool drawTool) {
        drawTool.setCurrentColor(0,0,0,255);
        drawTool.drawText(15,15,"Tag "+tag+".");
    }

    @Override
    public void update(double dt) {
        gameTime += dt;
        tag = (int)(gameTime/15)+1;
    }

    public int getTag() {
        return tag;
    }
}