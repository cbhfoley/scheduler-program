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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Below code commented out, allows for testing to make sure language is being properly set to French.
        //Locale.setDefault(new Locale("fr"));
        String region = TimeZone.getDefault().getID();
        updateRegionLabel(region);

        // Load the language property files
        ResourceBundle bundle = ResourceBundle.getBundle("resources.language");
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

    private void updateRegionLabel(String region) {
        regionLabel.setText(region);
    }

    public void exitButtonAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void loginButtonAction(ActionEvent actionEvent) throws IOException {

        String enteredUsername = usernameTextField.getText();
        String enteredPassword = passwordPasswordField.getText();

        UserDAO userDAO = new UserDAO();

        boolean isValidLogin = userDAO.validateLogin(enteredUsername, enteredPassword);
        //isValidLogin = true;


        if (isValidLogin) {
            setUsername(enteredUsername);
            Parent parent = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            alertDisplay(1);
            usernameTextField.clear();
            passwordPasswordField.clear();
        }
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    private void alertDisplay(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Action invalid");
                alert.setContentText("Incorrect Login Credentials");
                alert.showAndWait();

            }
        }
    }
}
