package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class alertUtils {

    public static void alertDisplay(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Action invalid");
                alert.setContentText("Please enter text in ALL fields and select a start and end time as well as a contact.");
                alert.showAndWait();
            }
            case 2 -> {
                alert.setTitle("Saved");
                alert.setHeaderText("Appointment Saved");
                alert.setContentText("Press OK to continue.");
                alert.showAndWait();
            }
            case 3 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Action invalid");
                alert.setContentText("Please enter text in ALL fields and select a country and region.");
                alert.showAndWait();
            }
            case 4 -> {
                alert.setTitle("Saved");
                alert.setHeaderText("Customer Saved");
                alert.setContentText("Press OK to continue.");
                alert.showAndWait();
            }
            case 5 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Action invalid");
                alert.setContentText("Please select a customer to edit.");
                alert.showAndWait();
            }
            case 6 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Action invalid");
                alert.setContentText("Please select a customer to delete.");
                alert.showAndWait();
            }
            case 7 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Action invalid");
                alert.setContentText("Please enter text in all fields.");
                alert.showAndWait();
            }
            case 8 -> {
                alert.setTitle("Updated");
                alert.setHeaderText("Customer Updated");
                alert.setContentText("Press OK to continue.");
                alert.showAndWait();
            }
        }
    }
}
