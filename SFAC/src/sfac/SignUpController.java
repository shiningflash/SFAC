/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfac;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Faizun
 */
public class SignUpController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    Connection connection;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myDatabase", "app", "app");
        } catch (SQLException e) {
        }
    }    
    @FXML
    private Label signup;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField cPassword;

    @FXML
    private Button joinButton;

    @FXML
    private Button backButton;

      @FXML
    private RadioButton facultyRButton;

    @FXML
    private ToggleGroup ChooseType;

    @FXML
    private RadioButton alumniRButton;

    @FXML
    private RadioButton studentRButton;

    int type =0;
    CheckRegex checkReg = new CheckRegex();
    
    private void showError(String err) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(err);
        alert.showAndWait();
    }
    
    @FXML
    void goToLoginByJoining(MouseEvent event) throws Exception  {
        
        // Checking whether the info provide by user is in correct form
        
        if(checkType()){
            return;
        }              
        String First_Name = firstName.getText();
        if(checkFirstName(First_Name)){
            return;
        }
        String Last_Name = lastName.getText();
        if(checkLastName(Last_Name)){
            return;
        }
        String Email_ID = email.getText();
        ///here i'll send 1 or 2 or 3 as parameter for faculty, alumni, student respectively
        if (checkEmail(Email_ID,type)) {
            return;
        }
         
        String Password = password.getText();
        if (checkPassword(Password)) {
            return;
        }
        String Confirm_Password = cPassword.getText();
        if (checkConfirmPassword(Password, Confirm_Password)) {
            return;
        }    
        // As everything is ok , so inserting every info in the database
        Encoding ed = new Encoding();
        Password = ed.encrypt(Password);
        String addingInfo="";
        if(type==1){
            addingInfo = "INSERT INTO FACULTY_DATA VALUES ("
                + "'" + Email_ID + "'," 
                + "'" + First_Name + "'," 
                + "'" + Last_Name + "',"
                + "'" + Password + "'" + ")";
        }else if(type==2){
            addingInfo = "INSERT INTO ALUMNI_DATA VALUES ("
                + "'" + Email_ID + "'," 
                + "'" + First_Name + "'," 
                + "'" + Last_Name + "',"
                + "'" + Password + "'" + ")";
        }else if(type==3){
            addingInfo = "INSERT INTO STUDENT_DATA VALUES ("
                + "'" + Email_ID + "'," 
                + "'" + First_Name + "'," 
                + "'" + Last_Name + "',"
                + "'" + Password + "'" + ")";
        }
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myDatabase", "app", "app");
        Statement stm = connection.createStatement();
        int r = stm.executeUpdate(addingInfo);
        if (r != 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("SignUp Successful");
            alert.setContentText("Please log in");
            alert.showAndWait();
            Parent  SignIn = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
            Scene signInScene = new Scene(SignIn);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Sign In");
        window.setScene(signInScene);
        window.show();
        }       
    }
    @FXML
    void typeSelected(ActionEvent event) {
        type = 0;
        if(facultyRButton.isSelected()){
            type = 1;
        }
        if(alumniRButton.isSelected()){
            type = 2;
        }
        if(studentRButton.isSelected()){
            type = 3;
        }

    }
    private boolean checkType() {
        if(type==0){
            showError("Choose type");
            return true;
        }
        return false;
    }
    
    
    private boolean checkFirstName(String fName) {
        if(fName.isEmpty()){
            showError("Enter your first name");
            return true;
        }
        return false;
    }
    
    private boolean checkLastName(String lName) {
        if(lName.isEmpty()){
            showError("Enter your last name");
            return true;
        }
        return false;
        
    }
    
    private boolean checkEmail(String email, int n) throws SQLException {
        if (email.isEmpty()) {
            showError("Enter your email");
            return true;
        }
        String wrongEmailCheck = null;
        switch (n) {
            case 1:
                wrongEmailCheck = "SELECT * FROM FACULTY_DATA WHERE EMAIL_ID = '" + email + "'";
                break;
            case 2:
                wrongEmailCheck = "SELECT * FROM ALUMNI_DATA WHERE EMAIL_ID = '" + email + "'";
                break;
            case 3:
                wrongEmailCheck = "SELECT * FROM STUDENT_DATA WHERE EMAIL_ID = '" + email + "'";
                break;
            default:
                break;
        }
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myDatabase", "app", "app");
        PreparedStatement ps = connection.prepareStatement(wrongEmailCheck);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            showError("This email already exists");
            return true;
        }
        if(checkReg.checkEmail(email,n)){
            showError("Incorrect email format");
            return true;
        }
        return false;
    }
     
    private boolean checkPassword(String pass) throws SQLException {
        if (pass.isEmpty()) {
            showError("Enter your password");
            return true;
        }if(checkReg.checkPassword(pass)){
            showError("Your password must contain 1 uppercase letter, 1 lowercase letter, 1 numerical number .");
            return true;
        }
        return false;
    }
    
    
    private boolean checkConfirmPassword(String pass, String cPass) throws SQLException {
        if (cPass.isEmpty()) {
            showError("Enter your confirm password");
            return true;
        }
        if(!pass.equals(cPass)){
            showError("Your password does not match with confirm password");
            return true;
        }
        return false;
    }
    
    
    @FXML
    void goToLoginPage(MouseEvent event) throws Exception  {
        Parent SignIn = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        Scene signInScene = new Scene(SignIn);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Sign In");
        window.setScene(signInScene);
        window.show();
    }
    
}
