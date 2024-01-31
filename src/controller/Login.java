package controller;

import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.generalUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class Login implements Initializable {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Label regionLabel;
    @FXML
    private Label regionTextLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button exitButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label headerLabel;
    @FXML
    private Label passwordLabel;


    private static String username;
    private ResourceBundle bundle;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Below code commented out, allows for testing to make sure language is being properly set to French.
        // Locale.setDefault(new Locale("fr"));
        String region = TimeZone.getDefault().getID();
        updateRegionLabel(region);

        // Load the language property files
        bundle = ResourceBundle.getBundle("resources.language");
        applyLanguage(bundle);
    }

    /**
     * Method that sets the various labels depending on the language preference on the machine. Tested by manually
     * adding a method in the intialize to set the Locale to French.
     *
     * @param bundle
     */
    private void applyLanguage(ResourceBundle bundle) {
        regionTextLabel.setText(bundle.getString("regionTextLabel"));
        passwordLabel.setText(bundle.getString("passwordLabel"));
        loginButton.setText(bundle.getString("loginButton"));
        exitButton.setText(bundle.getString("exitButton"));
        usernameLabel.setText(bundle.getString("usernameLabel"));
        headerLabel.setText(bundle.getString("headerLabel"));

    }

    /**
     * Method that updates the label depending on the region used by a users local machine.
     *
     * @param region
     */
    private void updateRegionLabel(String region) {
        regionLabel.setText(region);
    }

    /**
     * Allows the program to exit by clicking the exit button.
     *
     * @param actionEvent
     */
    public void exitButtonAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * Allows the user to login by validating credentials using the userDAO validateLogin method.
     * If it returns true, the user is logged in successfully, if incorrect (or no) information is entered,
     * an error is displayed in either English or French depending on the user's language preferences.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void loginButtonAction(ActionEvent actionEvent) throws IOException {

        String enteredUsername = usernameTextField.getText();
        String enteredPassword = passwordPasswordField.getText();

        UserDAO userDAO = new UserDAO();

        boolean isValidLogin = userDAO.validateLogin(enteredUsername, enteredPassword);
        // Below code allows use w/o validating login each time. Will be commented out or removed for final production.
        isValidLogin = true;


        if (isValidLogin) {
            setUsername(enteredUsername);
            Parent parent = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            generalUtils.centerOnScreen(stage);
            stage.show();
        } else {
            alertDisplay(1, bundle);
            usernameTextField.clear();
            passwordPasswordField.clear();
        }
    }

    /**
     * Setter for the username for later use.
     *
     * @param username
     */
    public void setUsername(String username) {
        Login.username = username;
    }

    /**
     * Getter for the username for later use.
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Alert displays depending on the error encountered.
     * In this instance there is only one case.
     *
     * @param alertType
     * @param bundle
     */
    private void alertDisplay(int alertType, ResourceBundle bundle) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (alertType == 1) {
            alert.setTitle(bundle.getString("errorTitle"));
            alert.setHeaderText(bundle.getString("errorHeader"));
            alert.setContentText(bundle.getString("errorContent"));
            alert.showAndWait();
        }
    }
}
