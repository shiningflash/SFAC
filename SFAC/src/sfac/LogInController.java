/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfac;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Faizun
 */
public class LogInController implements Initializable {

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myDatabase", "app", "app");
        } catch (SQLException e) {
        }
    }
    @FXML
    private Label login;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private Button forgotPassword;
    @FXML
    private Button join;

    Connection connection;
    int type=0;
    

    @FXML
    void addEmail(ActionEvent event) {
    }

    @FXML
    void addPass(ActionEvent event) {
    }

    private void showError(String err) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(err);
        alert.showAndWait();
    }

    private boolean checkEmail(String email) throws SQLException {
        if (email.isEmpty()) {
            showError("Enter your email");
            return true;
        }
        String wrongEmailCheck;
        if(email.endsWith("@g.bracu.ac.bd")){
            wrongEmailCheck = "SELECT * FROM STUDENT_DATA WHERE EMAIL_ID = '" + email + "'";
        }else if(email.endsWith("@bracu.ac.bd")){
            wrongEmailCheck = "SELECT * FROM FACULTY_DATA WHERE EMAIL_ID = '" + email + "'";
        }else{
            wrongEmailCheck = "SELECT * FROM ALUMNI_DATA WHERE EMAIL = '" + email + "'";
        }
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myDatabase", "app", "app");
        PreparedStatement ps = connection.prepareStatement(wrongEmailCheck);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            showError("Invalid email");
            return true;
        }
        return false;
    }

    private boolean checkPassword(String email, String pass) throws SQLException, NoSuchAlgorithmException, Exception {
        if (pass.isEmpty()) {
            showError("Enter your password");
            return true;
        }
        String extractEmail;
        if(email.endsWith("@g.bracu.ac.bd")){
            extractEmail = "SELECT * FROM STUDENT_DATA WHERE EMAIL_ID = '" + email + "'";
        }else if(email.endsWith("@bracu.ac.bd")){
            extractEmail = "SELECT * FROM FACULTY_DATA WHERE EMAIL_ID = '" + email + "'";
        }else{
            extractEmail = "SELECT * FROM ALUMNI_DATA WHERE EMAIL = '" + email + "'";
        }
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myDatabase", "app", "app");
        PreparedStatement ps = connection.prepareStatement(extractEmail);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            String passwordDB = rs.getString(4);
            Encoding ed = new Encoding();
            String password = ed.getHash(pass);
            System.out.println(pass);
            System.out.println(password);
            if (!password.equals(passwordDB)) {
                showError("Wrong password");
                return true;
            }
        }
        return false;
    }

    //if false then it will work
    @FXML
    void loginToAccount(MouseEvent event) throws SQLException, IOException, NoSuchAlgorithmException, Exception {
        String Email_ID = email.getText();
        if (checkEmail(Email_ID)) {
            return;
        }
        String Password = password.getText();
        if (checkPassword(Email_ID, Password)) {
            return;
        }
        Parent home;
        String extractEmail="";
        if(Email_ID.endsWith("@g.bracu.ac.bd")){
            home = FXMLLoader.load(getClass().getResource("Student_Dashboard.fxml"));
        }else if(Email_ID.endsWith("@bracu.ac.bd")){
            home = FXMLLoader.load(getClass().getResource("Faculty_Dashboard.fxml"));
        }else{
            home = FXMLLoader.load(getClass().getResource("Alumni_Dashboard.fxml"));
        }
        Scene homeScene = new Scene(home);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Dashboard");
        window.setScene(homeScene);
        window.show();
    }
    @FXML
    void goToSignUp(MouseEvent event) throws SQLException, IOException {
        Parent signUp = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        Scene signUpScene = new Scene(signUp);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Sign Up");
        window.setScene(signUpScene);
        window.show();
    }
}