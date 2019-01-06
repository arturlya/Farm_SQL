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
    private Kunde[] kunden;
    private int[] takenIDs;

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
        uiController.drawObject(new Player(this));
        uiController.drawObject(new Player(this));
        //Tier tier = new Tier("Kuh",farm.getId());
        //Pflanze pflanze = new Pflanze("Weizen",1);
        //uiController.drawObject(pflanze);
        for (int i = 0; i < 20; i++) {
            uiController.drawObject(new Pflanze("Weizen",1,this));
        }
        takenIDs = new int[3];
        kunden = new Kunde[3];
        for (int i = 0; i < 3; i++) {
            kunden[i] = new Kunde(farm,i);
            takenIDs[i] = kunden[i].getId();
            uiController.drawObject(kunden[i]);
        }
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                while (kunden[i].getId() == takenIDs[1] || kunden[i].getId() == takenIDs[2]) {
                    kunden[i].kundenErstellung();
                    takenIDs[i] = kunden[i].getId();
                }
            }else if (i == 1) {
                while (kunden[i].getId() == takenIDs[0] || kunden[i].getId() == takenIDs[2]) {
                    kunden[i].kundenErstellung();
                    takenIDs[i] = kunden[i].getId();
                }
            }else if (i == 2) {
                while (kunden[i].getId() == takenIDs[0] || kunden[i].getId() == takenIDs[1]) {
                    kunden[i].kundenErstellung();
                    takenIDs[i] = kunden[i].getId();
                }
            }
        }
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
        if (kunden[0].isNeuerKunde()) {
            kunden[0].kundenErstellung();
            while (kunden[0].getId() == takenIDs[1] || kunden[0].getId() == takenIDs[2]) {
                kunden[0].kundenErstellung();
                takenIDs[0] = kunden[0].getId();
            }
        }
        if (kunden[1].isNeuerKunde()){
            kunden[1].kundenErstellung();
            while (kunden[1].getId() == takenIDs[0] || kunden[1].getId() == takenIDs[2]) {
                kunden[1].kundenErstellung();
                takenIDs[1] = kunden[1].getId();
            }
        }
        if (kunden[2].isNeuerKunde()){
            kunden[2].kundenErstellung();
            while (kunden[2].getId() == takenIDs[0] || kunden[2].getId() == takenIDs[1]) {
                kunden[2].kundenErstellung();
                takenIDs[2] = kunden[2].getId();
            }
        }
    }

    public int getCurrentPanel() {
        return currentPanel;
    }

    public void setCurrentPanel(int currentPanel) {
        this.currentPanel = currentPanel;
    }
}
