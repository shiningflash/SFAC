package sfac;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PostDescriptionController {
    
    Connection connection;
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
    
    private void showError(String err) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(err);
        alert.showAndWait();
    }
    private boolean checkOrganizationName(String orgName) {
        if(orgName.isEmpty()){
            showError("Enter organization name");
            return true;
        }
        return false;
    }
    
    private boolean checkPosition(String pos) {
        if(pos.isEmpty()){
            showError("Enter position");
            return true;
        }
        return false;
    }
    private boolean checkDescription(String des) {
        if(des.isEmpty()){
            showError("Enter description");
            return true;
        }
        return false;
    }
    
    @FXML
    void goToDashboardByPosting(MouseEvent event) throws IOException {
        String oName = orgName.getText();
        if(checkOrganizationName(oName)){
            return;
        }
        String pos = position.getText();
        if(checkPosition(pos)){
            return;
        }
        String des = description.getText();
        if(checkDescription(des)){
            return;
        }
        
        Parent home;
        home = FXMLLoader.load(getClass().getResource("Alumni_Dashboard.fxml")); 
        Scene homeScene = new Scene(home);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Job offering");
        window.setScene(homeScene);
        window.show();
    }
}
