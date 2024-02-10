package utils;

import javafx.scene.control.Alert;

public class alertUtils {
    /**
     * Different alerts based on the various errors or confirmations that need to be displayed to the user.
     *
     * @param alertType
     */

    public static void alertDisplay(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Action invalid");
                alert.setContentText("Please enter text in ALL fields and select a start date/time, end date/time, contact and a customer.");
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
            case 9 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Action invalid");
                alert.setContentText("The start of the appointment has to be before the end of the appointment.");
                alert.showAndWait();
            }
            case 10 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Action invalid");
                alert.setContentText("The appointment must be scheduled between 8:00am and 10:00pm Eastern Time");
                alert.showAndWait();
            }
            case 11 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Action invalid");
                alert.setContentText("Please select an appointment to edit.");
                alert.showAndWait();
            }
            case 12 -> {
                alert.setTitle("Deleted");
                alert.setHeaderText("Appointment deleted");
                alert.setContentText("Appointment was successfully deleted.");
                alert.showAndWait();
            }
            case 13 -> {
                alert.setTitle("Deleted");
                alert.setHeaderText("Customer deleted");
                alert.setContentText("Customer was successfully deleted, along with any appointments they may have had.");
                alert.showAndWait();
            }
            case 14 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Appointment overlap");
                alert.setContentText("Appointment cannot be added due to existing appointment in this time frame for the selected customer.");
                alert.showAndWait();
            }
            case 15 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Appointment overlap");
                alert.setContentText("Appointment cannot be updated due to existing appointment in this time frame for the selected customer.");
                alert.showAndWait();
            }
        }
    }
}
