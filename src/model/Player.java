package model;

import control.ProgramController;
import control.framework.UIController;
import model.framework.GraphicalObject;
import view.framework.DrawTool;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class Player extends GraphicalObject {

    private Rectangle2D.Double rec;
    private UIController uiController;
    private boolean clicked;

    public Player(UIController uiController){
        this.uiController = uiController;
        rec = new Rectangle2D.Double(350,740,100,30);
    }

    @Override
    public void draw(DrawTool drawTool) {
        drawTool.setCurrentColor(0,0,0,255);
        drawTool.fill(rec);
        drawTool.setCurrentColor(255,255,255,255);
        if (ProgramController.currentPanel == 0) drawTool.drawText(380,760,"Pflanzen");
        if (ProgramController.currentPanel == 1) drawTool.drawText(370,760,"Tiere");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (rec.intersects(e.getX(),e.getY(),1,1) && !clicked) {
            clicked = true;
            switch (ProgramController.currentPanel) {
                case 0:
                    ProgramController.currentPanel = 1;
                    uiController.selectDrawingPanel(1);
                    break;
                case 1:
                    ProgramController.currentPanel = 0;
                    uiController.selectDrawingPanel(0);
                    break;
            }
        }else{
            clicked = false;
        }
    }

    private void switchDrawingPanel(){

    }
}
