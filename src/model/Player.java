package model;

import control.ProgramController;
import control.framework.UIController;
import model.framework.GraphicalObject;
import view.framework.DrawTool;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class Player extends GraphicalObject {

    private Rectangle2D.Double pflanze,tier;
    private UIController uiController;
    private boolean clicked;
    private ProgramController pc;

    public Player(UIController uiController, ProgramController pc){
        this.uiController = uiController;
        this.pc = pc;
        pflanze = new Rectangle2D.Double(300,740,90,30);
        tier = new Rectangle2D.Double(410,740,90,30);
    }

    @Override
    public void draw(DrawTool drawTool) {
        drawTool.setCurrentColor(0,0,0,255);
        drawTool.fill(pflanze);
        drawTool.fill(tier);
        drawTool.setCurrentColor(255,255,255,255);
        drawTool.drawText(200,200,""+pc.getCurrentPanel());
        drawTool.drawText(320,760,"Pflanzen");
        drawTool.drawText(430,760,"Tiere");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (pflanze.intersects(e.getX(), e.getY(), 1, 1) && !clicked && pc.getCurrentPanel() == 1) {
            clicked = true;
            pc.setCurrentPanel(0);
        }else if (tier.intersects(e.getX(), e.getY(), 1, 1) && !clicked && pc.getCurrentPanel() == 0) {
            clicked = true;
            pc.setCurrentPanel(1);
        }else{
            clicked = false;
        }
    }

    private void switchDrawingPanel(){

    }
}
