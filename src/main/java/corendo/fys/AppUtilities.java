/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corendo.fys;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Gabriel & ricardopolenta
 */
public class AppUtilities {

    public static Parent loadScreen(String schermenFileName) throws IOException {
        String resource = "/corendo/fys/" + schermenFileName;
        URL url = AppUtilities.class.getResource(resource);
        ResourceBundle bundle = ResourceBundle.getBundle("message", new Locale("nl"));
        return FXMLLoader.load(url, bundle);
    }

    public static Parent loadScreen(String schermenFileName, String language) throws IOException {
        String resource = "/corendo/fys/" + schermenFileName;
        URL url = AppUtilities.class.getResource(resource);
        ResourceBundle bundle = ResourceBundle.getBundle("message", new Locale(language));
        return FXMLLoader.load(url, bundle);
    }
}
