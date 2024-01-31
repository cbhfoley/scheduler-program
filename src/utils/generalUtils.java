package utils;

import javafx.stage.Screen;
import javafx.stage.Stage;

public class generalUtils {

    public static void centerOnScreen(Stage stage) {
        double centerX = Screen.getPrimary().getVisualBounds().getWidth() / 2.0;
        double centerY = Screen.getPrimary().getVisualBounds().getHeight() / 2.0;

        stage.setX(centerX - stage.getWidth() / 2.0);
        stage.setY(centerY - stage.getHeight() / 2.0);
    }
}
