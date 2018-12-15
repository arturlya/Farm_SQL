package control;

import control.framework.UIController;
import model.*;

/**
 * Ein Objekt der Klasse ProgramController dient dazu das Programm zu steuern. Die updateProgram - Methode wird
 * mit jeder Frame im laufenden Programm aufgerufen.
 */
public class ProgramController {

    // Attribute
    public static int currentPanel = 0;

    // Referenzen
    private UIController uiController;  // diese Referenz soll auf ein Objekt der Klasse uiController zeigen. Über dieses Objekt wird das Fenster gesteuert.

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

        Farm farm = new Farm();
        uiController.drawObjectOnPanel(farm,0);
        uiController.drawObjectOnPanel(farm,1);
        Shop shop = new Shop(uiController,farm.getId());
        uiController.drawObjectOnPanel(shop,0);
        uiController.drawObjectOnPanel(shop,1);
        uiController.drawObjectOnPanel(new Player(uiController),0);
        uiController.drawObjectOnPanel(new Player(uiController),1);
        //Tier tier = new Tier("Kuh",farm.getId());
        //Pflanze pflanze = new Pflanze("Weizen",1);
        //uiController.drawObject(pflanze);
        for (int i = 0; i < 20; i++) {
            uiController.drawObjectOnPanel(new Pflanze("Weizen",1),0);
        }

        Lager lager = new Lager(1);
        lager.storageResource(new Resource("Semelsalad", 35));
        lager.storageResource(new Resource("Weizen",18));
        lager.removeResource("Semelsalad",9);
        lager.removeResource("Weizen",11);
        //farm.kill(tier,lager);

        uiController.drawObject(new GameTime());
    }

    /**
     * Diese Methode wird wiederholt automatisch aufgerufen und zwar für jede Frame einmal, d.h. über 25 mal pro Sekunde.
     * @param dt Die Zeit in Sekunden, die seit dem letzten Aufruf der Methode vergangen ist.
     */
    public void updateProgram(double dt){
        // Hier passiert noch nichts, das Programm läuft friedlich vor sich hin
    }


}
