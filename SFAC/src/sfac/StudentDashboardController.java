/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfac;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Faizun
 */
public class StudentDashboardController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
     @FXML
    private Button logoutButton;

    @FXML
    private Pane display;
    
    Stack<String> stack = new Stack<String>();

    Statement statement;
    ResultSet rs;
    PreparedStatement ps;
    int res;

    final ScrollPane sp = new ScrollPane();
    final VBox vb = new VBox();
    Connection connection;

    static String org = "";
    static String posi = "";
    static String descr = "";
    static String ti = "";
    static String da = "";
    static String tiSt = "";
    static String alu = "";

    @FXML
    void loggingOut(MouseEvent event) throws IOException {
        Parent home;
        home = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        Scene homeScene = new Scene(home);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Dashboard");
        window.setScene(homeScene);
        window.show();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int rowCount = 0;
        String query = "";
        VBox box = new VBox();
        box.getChildren().add(sp);
        try {
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myDatabase", "app", "app");
            query = "SELECT * FROM POST";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                stack.push(rs.getString(6));
                rowCount++;
            }

        } catch (SQLException e) {
        }

        // adding grid pane too
        PreparedStatement ps;
        try {
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myDatabase", "app", "app");
            int i = 0;
            while (!stack.empty()) {
                query = "SELECT * FROM POST WHERE TIMESTAMP = '" + stack.peek() + "'";
                stack.pop();
                ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    GridPane grid = new GridPane();
                    grid.setPadding(new Insets(35, 100, 10, 10));
                    grid.setMinSize(200, 150);
                    grid.setHgap(5);
                    grid.setVgap(5);
                    org = rs.getString(1);
                    posi = rs.getString(2);
                    descr = rs.getString(3);
                    ti = rs.getString(4);
                    da = rs.getString(5);
                    tiSt = rs.getString(6);
                    alu = rs.getString(7);

                    Text username = new Text(alu);
                    username.setStyle("-fx-font-weight: bold");
                    grid.add(username, 0, 0);

                    Text time = new Text(tiSt);
                    time.setStyle("-fx-font-style: italic");
                    grid.add(time, 0, 1);

                    Text oName = new Text(org);
                    oName.setStyle("-fx-font-weight: regular");
                    grid.add(oName, 0, 2);

                    Text posit = new Text(posi);
                    posit.setStyle("-fx-font-weight: regular");
                    grid.add(posit, 0, 3);

                    Text post = new Text(descr);
                    post.setStyle("-fx-font-weight: regular");
                    grid.add(post, 0, 4);

                    if (i % 2 == 0) {
                        grid.setStyle("-fx-background-color: #d1c4e9;");
                    } else {
                        grid.setStyle("-fx-background-color: #90caf9;");
                    }

                    vb.getChildren().add(grid);
                    i++;
                }

            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {

        }

        sp.setContent(vb);
        sp.setFitToHeight(true);
        sp.setPrefSize(447, 364);
        sp.setVmax(200);
        sp.setHmax(3);
        sp.setHvalue(1);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        display.getChildren().add(sp);
    }    
    
}