/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfac;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Faizun
 */
public class HomeController implements Initializable {

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
    
}
