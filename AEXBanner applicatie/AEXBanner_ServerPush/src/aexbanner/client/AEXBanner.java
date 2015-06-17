package aexbanner.client;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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

    private static final String bindingName = "ClientX";
    private static final int portNumber = 667;
    private Registry registry = null;

    @Override
    public void start(Stage stage) throws RemoteException, InterruptedException {

        try {
            banner = new BannerController();
        } catch (RemoteException exc) {
            System.out.println(exc.getMessage());
        }

        try {
            registry = LocateRegistry.createRegistry(portNumber);
        } catch (RemoteException ex) {
            Logger.getLogger(BannerController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            registry.rebind(bindingName, banner);
        } catch (RemoteException e) {
            Logger.getLogger(BannerController.class.getName()).log(Level.SEVERE, null, e);
        }

//        Thread.sleep(100000);
        timer = new Timer();

        txt = new Text("");
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
        if (banner.getMockBeurs() != null) {
            return banner.createString();
        } else {
            return "";
        }

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
