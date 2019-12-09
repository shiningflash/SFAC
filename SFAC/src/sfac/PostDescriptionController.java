package sfac;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PostDescriptionController {

    Connection connection;
    SimpleDateFormat formatter;

    Statement statement;
    ResultSet rs;
    int res;

    public void initialize(URL url, ResourceBundle rb) {
        try {
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myDatabase", "app", "app");
        } catch (SQLException e) {
        }
    }

    @FXML
    private TextField orgName;

    @FXML
    private TextField description;

    @FXML
    private TextField position;

    @FXML
    private Button postToDashboard;

    @FXML
    private Button backButton;

    @FXML
    void goToBack(MouseEvent event) throws IOException {
        Parent home;
        home = FXMLLoader.load(getClass().getResource("AlumniDashboard.fxml"));
        Scene homeScene = new Scene(home);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Dashboard");
        window.setScene(homeScene);
        window.show();
    }

    private void showError(String err) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(err);
        alert.showAndWait();
    }

    private boolean checkOrganizationName(String orgName) {
        if (orgName.isEmpty()) {
            showError("Enter organization name");
            return true;
        }
        return false;
    }

    private boolean checkPosition(String pos) {
        if (pos.isEmpty()) {
            showError("Enter position");
            return true;
        }
        return false;
    }

    private boolean checkDescription(String des) {
        if (des.isEmpty()) {
            showError("Enter description");
            return true;
        }
        return false;
    }

    @FXML
    void goToDashboardByPosting(MouseEvent event) throws IOException, SQLException {
        String oName = orgName.getText();
        if (checkOrganizationName(oName)) {
            return;
        }
        String pos = position.getText();
        if (checkPosition(pos)) {
            return;
        }
        String des = description.getText();
        if (checkDescription(des)) {
            return;
        }

        Date date = new Date();
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String d = formatter.format(date); // after 1 week date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(formatter.parse(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, 7);
        String d2 = formatter.format(c.getTime());
        
        formatter = new SimpleDateFormat("HH:mm:ss");
        String t = formatter.format(date);
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dt = formatter.format(date);

        LogInController lc = new LogInController();
        String e = lc.getEmail();
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myDatabase", "app", "app");
        String query = "SELECT FIRST_NAME FROM ALUMNI_DATA WHERE EMAIL_ID ='" + e + "'";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        String nameOfAuthor ="";
        if(rs.next()){
            nameOfAuthor = rs.getString(1);
        }
        
        String addingInfo = "INSERT INTO POST VALUES ("
                + "'" + oName + "',"
                + "'" + pos + "',"
                + "'" + des + "',"
                + "'" + t + "',"
                + "'" + d2 + "',"
                + "'" + dt + "',"
                + "'" + nameOfAuthor + "'" + ")";
        Statement stm = connection.createStatement();
        int r = stm.executeUpdate(addingInfo);
        if (r != 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Posted");
            alert.setContentText("Your post has been submitted");
            alert.showAndWait();
            Parent home = FXMLLoader.load(getClass().getResource("AlumniDashboard.fxml"));
            Scene homeScene = new Scene(home);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Job offerings");
            window.setScene(homeScene);
            window.show();
        } 
    }

}
