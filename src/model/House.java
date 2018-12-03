package model;

import model.framework.GraphicalObject;
import view.framework.DrawTool;

/**
 * BEISPIELKLASSE:
 * Es wird ein Haus gezeichnet
 */
public class House extends GraphicalObject {

    //Attribute
    private int hausnummer;

    //Referenzen

    //Konstruktor: Diese Methode wird bei Erzeugung eines Hauses aufgerufen
    public House(){
        hausnummer = 7;
        this.createAndSetNewImage("assets/images/dog_down.png");
    }

    @Override
    /**
     * Diese Methode wird wiederholt, über 25 mal pro Sekunde aufgerufen und zeichnet
     * in schneller Folge immer wieder ihren Inhalt, bevor er vom nächsten Bild ersetzt
     * wird.
     */
    public void draw(DrawTool drawTool) {
        drawTool.drawImage(getMyImage(),100,100);
        //Zeichne den Hauskörper mit Rahmen
        drawTool.setCurrentColor(200,200,200,255);
        drawTool.drawFilledRectangle(300,300,100,100);
        drawTool.setCurrentColor(0,0,0,255);
        drawTool.drawRectangle(300,300,100,100);
        //Zeichne das Dach mit Rahmen
        drawTool.setCurrentColor(255,0,0,255);
        drawTool.drawFilledTriangle(300,300,400,300,350, 250);
        drawTool.setCurrentColor(0,0,0,255);
        drawTool.drawTriangle(300,300,400,300,350,250);
        //Zeichne das Fenster mit Rahmen
        drawTool.setCurrentColor(0,120,200,255);
        drawTool.drawFilledRectangle(360,310,35,40);
        drawTool.setCurrentColor(0,0,0,255);
        drawTool.drawRectangle(360,310,35,40);
        //Zeichne die Tür
        drawTool.setCurrentColor(0,0,0,255);
        drawTool.drawFilledRectangle(320,340,30,60);
        //Zeichne die Hausnummer
        drawTool.drawText(355,365,"Nr."+hausnummer);
        //Schreibe etwas unter das Haus
        drawTool.setCurrentColor(0,0,255,255);
        drawTool.drawText(265,415,"Dies ist ein sehr hübsches Haus.");
    }

}
