package com.example.latticeline;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CrtAnnounce {

    @FXML
    private TextArea anbox;

    @FXML
    private Button anbtn;

    @FXML
    private Button backbtn;

    @FXML
    private AnchorPane compilerbtn;

    @FXML
    private AnchorPane groupsbtn;

    @FXML
    private AnchorPane problemsbtn;

    @FXML
    private Label status;

    @FXML
    void announce(MouseEvent event) throws FileNotFoundException, SQLException {
        File file = new File("groupname.txt");
        Scanner sc = new Scanner(file);
        String gpname = sc.next();
        Connection connection = DBconnect.getConnect();
        String query = "SELECT * FROM `gp` WHERE name='" + gpname + "';";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet =  preparedStatement.executeQuery();
        while(resultSet.next()){
            String ann = resultSet.getString("announce");
            String enc = encodeDecode.encode(anbox.getText());
            ann += " " + enc;
            query = "UPDATE  gp SET announce='" + ann + "' WHERE name='" + gpname + "';";
            preparedStatement.executeUpdate(query);
        }
    }

    @FXML
    void problems(MouseEvent event) throws IOException {
        Stage stage = (Stage) problemsbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LatticeLine");
        stage.setScene(scene);
    }

    @FXML
    void compiler(MouseEvent event) throws IOException {
        Stage stage = (Stage) compilerbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("compiler-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(HelloApplication.class.getResource("java-keywords.css").toExternalForm());
        stage.setTitle("LatticeLine");
        stage.setScene(scene);
    }

    @FXML
    void group(MouseEvent event) throws IOException {
        Stage stage = (Stage) groupsbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("groups-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LatticeLine");
        stage.setScene(scene);
    }

    @FXML
    void back(MouseEvent event) throws IOException {
        Stage stage = (Stage) backbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("announce-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LatticeLine");
        stage.setScene(scene);
    }

}
