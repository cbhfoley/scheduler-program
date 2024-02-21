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
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Controller class for handling logging into the application. Dynamically updates based on the region/language preferences
 * on the user's local machine. Handles logging in by validating credentials.
 *
 */
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

    /**
     * Initializes the Login controller. Updates the region label to the region based on the user preferences.
     * Also applies the language bundle file based on the user preferences. This translates between English or French.
     *
     * @param url
     * @param resourceBundle
     */
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
     * adding a method in to initialize to set the Locale to French.
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
        // Below code allows bypassing login check to make testing easier. Will be commented out or removed for final production.
        //isValidLogin = true;

        generalUtils.logLoginAttempt(enteredUsername, isValidLogin);

        if (isValidLogin) {
            setUsername(enteredUsername);
            Parent parent = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            generalUtils.centerOnScreen(stage);
            stage.show();
        } else {
            alertDisplay(bundle);
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
     * This could be added to alertUtils. I decided to leave it here since I created my alertUtils fairly late
     * into development.
     *
     * @param bundle
     */
    private void alertDisplay(ResourceBundle bundle) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString("errorTitle"));
        alert.setHeaderText(bundle.getString("errorHeader"));
        alert.setContentText(bundle.getString("errorContent"));
        alert.showAndWait();
    }
}
