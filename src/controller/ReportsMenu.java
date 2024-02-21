package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.generalUtils;

import java.io.IOException;

/**
 * Controller class for the reports menu of the application. Allows for the user to navigate to the three pre-defined
 * reports within the application. Labels indicate what each report will do.
 *
 */
public class ReportsMenu {
    /**
     * Loads the Report1Menu when clicked.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void report1ButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/report1Menu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }

    /**
     * Loads the Report2Menu when clicked.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void report2ButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/report2Menu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }

    /**
     * Loads the Report3Menu when clicked.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void report3ButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/report3Menu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }

    /**
     * Loads the MainMenu when clicked.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void mainMenuButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }
}
