package control;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StaticData {

    public static final String farm = "MarArtFarm_Farm";
    public static final String pflanze = "MarArtFarm_Pflanze";
    public static final String mitarbieter = "MarArtFarm_Mitarbeiter";
    public static final String lager = "MarArtFarm_Lager";
    public static final String tier = "MarArtFarm_Tier";
    public static final String kunde = "MarArtFarm_Kunde";
    public static final String shop = "MarArtFarm_Shop";
    public static final String kuemmert_sich = "MarArtFarm_kümmert_sich";
    public static final String fuettert = "MarArtFarm_fuettert";

    public static BufferedImage huhn;
    public static BufferedImage weizen;

    static {
        try {
            weizen = ImageIO.read(new File("assets/Weizen.png"));
            huhn = ImageIO.read(new File("assets/Huhn.png"));
        } catch (IOException e) {
            if ( Config.INFO_MESSAGES) System.out.println("Laden eines Bildes fehlgeschlagen.");
        }
    }
}
