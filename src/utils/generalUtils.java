package utils;

import controller.Login;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class generalUtils {

    public static void centerOnScreen(Stage stage) {
        double centerX = Screen.getPrimary().getVisualBounds().getWidth() / 2.0;
        double centerY = Screen.getPrimary().getVisualBounds().getHeight() / 2.0;

        stage.setX(centerX - stage.getWidth() / 2.0);
        stage.setY(centerY - stage.getHeight() / 2.0);
    }

    public static String getLoginUsername() {
        FXMLLoader loader = new FXMLLoader(generalUtils.class.getResource("/view/login.fxml"));
        try {
            loader.load();
            Login loginController = loader.getController();
            return loginController.getUsername();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
