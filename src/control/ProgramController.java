package control;

import control.framework.UIController;
import model.*;

/**
 * Ein Objekt der Klasse ProgramController dient dazu das Programm zu steuern. Die updateProgram - Methode wird
 * mit jeder Frame im laufenden Programm aufgerufen.
 */
public class ProgramController {

    // Attribute
    private int currentPanel = 0;

    // Referenzen
    private UIController uiController;  // diese Referenz soll auf ein Objekt der Klasse uiController zeigen. Über dieses Objekt wird das Fenster gesteuert.
    private Farm farm;
    private Kunde kunde1,kunde2,kunde3;

    /**
     * Konstruktor
     * Dieser legt das Objekt der Klasse ProgramController an, das den Programmfluss steuert.
     * Damit der ProgramController auf das Fenster zugreifen kann, benötigt er eine Referenz auf das Objekt
     * der Klasse UIController. Diese wird als Parameter übergeben.
     * @param uiController das UIController-Objekt des Programms
     */
    public ProgramController(UIController uiController){
        this.uiController = uiController;
    }

    /**
     * Diese Methode wird genau ein mal nach Programmstart aufgerufen.
     */
    public void startProgram(){
        uiController.createNewDrawingPanel();

        //new SQL_Demo();
        new SQL_Loader();

        farm = new Farm(uiController);
        uiController.drawObject(farm);
        uiController.drawObject(farm);
        Shop shop = new Shop(uiController,farm.getId(),this);
        uiController.drawObject(shop);
        uiController.drawObject(shop);
        uiController.drawObject(new Player(uiController,this));
        uiController.drawObject(new Player(uiController,this));
        //Tier tier = new Tier("Kuh",farm.getId());
        //Pflanze pflanze = new Pflanze("Weizen",1);
        //uiController.drawObject(pflanze);
        for (int i = 0; i < 20; i++) {
            uiController.drawObject(new Pflanze("Weizen",1,this));
        }
        kunde1 = new Kunde(farm,1);
        kunde2 = new Kunde(farm,2);
        kunde3 = new Kunde(farm,3);
        uiController.drawObject(kunde1);
        uiController.drawObject(kunde2);
        uiController.drawObject(kunde3);
        uiController.drawObject(new GameTime());
        Mitarbeiter mitarbeiter = new Mitarbeiter("Pflanze",farm.getId());
        uiController.drawObject(mitarbeiter);
        Mitarbeiter mitarbeiter1 = new Mitarbeiter("Tier",farm.getId());
        uiController.drawObject(mitarbeiter1);
    }

    /**
     * Diese Methode wird wiederholt automatisch aufgerufen und zwar für jede Frame einmal, d.h. über 25 mal pro Sekunde.
     * @param dt Die Zeit in Sekunden, die seit dem letzten Aufruf der Methode vergangen ist.
     */
    public void updateProgram(double dt){
        if (kunde1.isNeuerKunde()) kunde1.kundenErstellung();
        if (kunde2.isNeuerKunde()) kunde2 = new Kunde(farm,2);
        if (kunde3.isNeuerKunde()) kunde3 = new Kunde(farm,3);
    }

    public int getCurrentPanel() {
        return currentPanel;
    }

    public void setCurrentPanel(int currentPanel) {
        this.currentPanel = currentPanel;
    }
}
