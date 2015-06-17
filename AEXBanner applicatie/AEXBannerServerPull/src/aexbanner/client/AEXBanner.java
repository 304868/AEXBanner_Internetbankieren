package aexbanner.client;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.util.Scanner;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Gijs en Nadiv
 */
public class AEXBanner extends Application {

    private Text txt;
    private Font font;
    private Pane root;
    private Scene scene;
    private Timer timer;
    private double width = 1000;
    private int koersUpdateCount = 150;
    private BannerController banner;
    private boolean correcteInvoer;

    @Override
    public void start(Stage stage) throws RemoteException {

        correcteInvoer = false;

        while (!correcteInvoer) {
            Scanner input = new Scanner(System.in);
            System.out.print("Client: Voer het IP-adres van de server in: ");
            String ipAdres = input.nextLine();

            System.out.print("Client: Voer het poortnummer in: ");
            int portNummer = input.nextInt();

            try {
                banner = new BannerController(ipAdres, portNummer);
                correcteInvoer = true;
            } catch (UnknownHostException e) {
                System.out.println("Kan geen host vinden bij dit ip-adres en poortnummer.");
                System.out.println("Check of de invoer wel klopt.");
            } catch (ConnectException e) {
                System.out.println("Het duurt te lang om een verbinding te maken.");
                System.out.println("Probeer het aub opnieuw.");
            } catch (RemoteException e) {
                Logger.getLogger(AEXBanner.class.getName()).log(Level.SEVERE, null, e);
                System.out.println("Fout bij ophalen van koersen: " + e.getMessage());
            }

        }

        timer = new Timer();

        txt = new Text(setKoersen());
        font = new Font("Arial", 80);
        txt.setFont(font);
        txt.setLayoutY(75);
        txt.setStroke(Color.BLACK);

        root = new Pane();
        root.getChildren().add(txt);

        scene = new Scene(root, 1000, 100);
        stage.setTitle("AEX Banner");
        stage.setScene(scene);
        stage.show();
        timer.schedule(new PeriodiekeActie(this), 0, 20);
    }

    public String setKoersen() {
        return banner.createString();
    }

    public void moveText() {
        if (width < -txt.getLayoutBounds().getMaxX()) {
            width = 1000;
        }

        if (koersUpdateCount == 0) {
            txt.setText(setKoersen());
            koersUpdateCount = 150;
        }

        width -= 2;
        txt.setLayoutX(width);
        koersUpdateCount--;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
