package com.example.latticeline;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CrtContest implements Initializable{
    @FXML
    private AnchorPane compilerbtn;

    @FXML
    private AnchorPane problemsbtn;

    @FXML
    private Button addbtn;

    @FXML
    private Button backbtn;

    @FXML
    private TextArea caddprbbox;

    @FXML
    private TextArea cdurationbox;

    @FXML
    private TextArea cnamebox;

    @FXML
    private TextArea ctimebox;

    @FXML
    private AnchorPane groupsbtn;

    @FXML
    private Button submitbtn;

    @FXML
    private Text status;

    String contestName = "", startTime = "", duration = "", problemsId = "", gpname = "";

    contestInfo info = contestInfo.getInstance();

    @FXML
    void problems(MouseEvent event) throws IOException {
        Stage stage = (Stage) problemsbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(HelloApplication.class.getResource("java-keywords.css").toExternalForm());
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("eachgroup-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LatticeLine");
        stage.setScene(scene);
    }

    @FXML
    void add(MouseEvent event) throws IOException {
        contestName = cnamebox.getText();
        startTime = ctimebox.getText();
        duration = cdurationbox.getText();
        problemsId = caddprbbox.getText();
        info.setContestName(contestName);
        info.setStartTime(startTime);
        info.setDuration(duration);
        info.setProblemsIds(problemsId);
        info.setGroupName(gpname);
        System.out.println(contestName + " " + startTime + " " + duration + " " + problemsId);
        Stage stage = (Stage) addbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("crtconprb-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LatticeLine");
        stage.setScene(scene);
    }


    @FXML
    void submit(MouseEvent event) {
        status.setFill(Color.RED);
        String str = ctimebox.getText();
        Scanner sc = new Scanner(str);
        try {
            String year = sc.next(), month = sc.next(), day = sc.next(), hour = sc.next(), min = sc.next(), sec = sc.next();

            if (year.length() != 4 || month.length() != 2 || day.length() != 2 || hour.length() != 2 || min.length() != 2 || sec.length() != 2) {
                status.setText("Check Starting Time");
                return;
            }
        } catch (Exception e) {
            status.setText("Check Starting Time");
            return;
        }


        str = cdurationbox.getText();
        sc = new Scanner(str);
        try {
            String year = sc.next(), month = sc.next(), day = sc.next(), hour = sc.next(), min = sc.next(), sec = sc.next();

            if (year.length() != 4 || month.length() != 2 || day.length() != 2 || hour.length() != 2 || min.length() != 2 || sec.length() != 2) {
                status.setText("Check Ending Time");
                return;
            }
        } catch (Exception e) {
            status.setText("Check Ending Time");
            return;
        }


        Connection connection = null;
        try {
            connection = DBconnect.getConnect();
            String query = "INSERT INTO `contest`(`contestName`, `startTime`, `duration`, `problemsIds`, `groupName`, `ranking`) VALUES ('" + cnamebox.getText() + "', '" + ctimebox.getText() + "', '" + cdurationbox.getText() + "', '" + caddprbbox.getText() + "', '" + gpname + "', '" + "');";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            status.setFill(Color.GREEN);
            status.setText("Passed");
        } catch (SQLException e) {
            status.setText("An Error Occured");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File("groupname.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        gpname = sc.next();
        cnamebox.setText(info.getContestName());
        ctimebox.setText(info.getStartTime());
        cdurationbox.setText(info.getDuration());
        caddprbbox.setText(info.getProblemsIds());
        System.out.println(info.getRanking());
    }
}
