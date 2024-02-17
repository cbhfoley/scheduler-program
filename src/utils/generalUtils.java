package utils;

import controller.Login;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class generalUtils {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Used in some instances where the different sized windows were shifting around the screen in an annoying way when
     * scenes were changed.
     *
     * @param stage
     */
    public static void centerOnScreen(Stage stage) {
        double centerX = Screen.getPrimary().getVisualBounds().getWidth() / 2.0;
        double centerY = Screen.getPrimary().getVisualBounds().getHeight() / 2.0;

        stage.setX(centerX - stage.getWidth() / 2.0);
        stage.setY(centerY - stage.getHeight() / 2.0);
    }

    /**
     * Retrieves the login username for use in adding that to the various SQL statements so the database correctly
     * indicates who created or updated a customer/appointment.
     *
     * @return
     */
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

    /**
     * Method to track login activity. It creates a .txt file in the root folder of the application with the
     * time (both local and UTC), username, and success/failure of the login attempt.
     *
     * @param enteredUsername
     * @param success
     */
    public static void logLoginAttempt(String enteredUsername, boolean success) {
        String logFileName = "login_activity.txt";
        LocalDateTime now = LocalDateTime.now();
        String nowString = now.format(dateTimeFormatter);
        String nowStringUtc = dateTimeUtils.convertToUTC(nowString);
        String status = success ? "SUCCESS" : "FAILURE";

        try {
            FileWriter writer = new FileWriter(logFileName, true);
            String logEntry = String.format("%s UTC | %s LOCAL | Username: %s | Status: %s\n", nowStringUtc, nowString, enteredUsername, status);
            writer.write(logEntry);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
