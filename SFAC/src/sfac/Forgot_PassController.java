package sfac;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Faizun
 */
public class Forgot_PassController implements Initializable {
    Statement statement;
    Connection connection;
    int res;
    public void initialize(URL url, ResourceBundle rb) {
        try {
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myDatabase", "app", "app");
        } catch (SQLException e) {
        }
    }
    @FXML
    private TextField email_id;

    @FXML
    private TextField passw;

    @FXML
    private TextField cPassw;

    @FXML
    private Button changePass;

    CheckRegex checkReg = new CheckRegex();

    @FXML
    void changePassword(MouseEvent event) throws SQLException, Exception {
        String Password = passw.getText();
        String Email = email_id.getText();

        if (checkPassword(Email, Password)) {
            return;
        }
        String Confirm_Password = cPassw.getText();

        if (checkConfirmPassword(Password, Confirm_Password)) {
            return;
        }
        Encoding ed = new Encoding();
        String encPass = ed.encrypt(Password);
        
        String query = "INSERT INTO PREVIOUS_PASSWORDS VALUES ("
                + "'" + Email + "',"
                + "'" + encPass + "'" + ")";
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myDatabase", "app", "app");
        statement = connection.createStatement();
        res = statement.executeUpdate(query);

        if(Email.endsWith("@g.bracu.ac.bd")){
            query = "UPDATE STUDENT_DATA SET PASSWORD = '" + encPass + "'WHERE EMAIL_ID = '" + Email + "'";
        }else if(Email.endsWith("@bracu.ac.bd")){
            query = "UPDATE FACULTY_DATA SET PASSWORD = '" + encPass + "'WHERE EMAIL_ID = '" + Email + "'";
        }else{
            query = "UPDATE ALUMNI_DATA SET PASSWORD = '" + encPass + "'WHERE EMAIL_ID = '" + Email + "'";
        }
        
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myDatabase", "app", "app");
        statement = connection.createStatement();
        res = statement.executeUpdate(query);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Password changed");
        alert.showAndWait();

        Parent login = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        Scene signinScene = new Scene(login);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Log In");
        window.setScene(signinScene);
        window.show();

    }

    private boolean prevPassCheck(String email, String pass) throws SQLException {
        String prevPasswordCheck = "SELECT * FROM PREVIOUS_PASSWORDS WHERE EMAIL_ID = '" + email + "'AND PASSWORD = '" + pass + "'";
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myDatabase", "app", "app");
        PreparedStatement ps = connection.prepareStatement(prevPasswordCheck);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            showError("You can not choose previous one. Choose another password");
            return true;
        }
        return false;
    }

    private boolean checkPassword(String email, String pass) throws SQLException, Exception {
        if (pass.isEmpty()) {
            showError("Enter your password");
            return true;
        }
        if (checkReg.checkPassword(pass)) {
            showError("Your password must contain 1 uppercase letter, 1 lowercase letter, 1 numerical number .");
            return true;
        }
        Encoding ed = new Encoding();
        pass = ed.encrypt(pass);
        if (prevPassCheck(email, pass)) {
            return true;
        }
        return false;
    }

    private boolean checkConfirmPassword(String pass, String cPass) throws SQLException {
        if (cPass.isEmpty()) {
            showError("Enter your confirm password");
            return true;
        }
        if (!pass.equals(cPass)) {
            showError("Your password does not match with confirm password");
            return true;
        }
        return false;
    }

    private void showError(String err) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(err);
        alert.showAndWait();
    }
}
