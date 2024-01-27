package controller;

import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    private static String username;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void exitButtonAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void loginButtonAction(ActionEvent actionEvent) throws IOException {

        String enteredUsername = usernameTextField.getText();
        String enteredPassword = passwordPasswordField.getText();

        UserDAO userDAO = new UserDAO();

        boolean isValidLogin = userDAO.validateLogin(enteredUsername, enteredPassword);


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
