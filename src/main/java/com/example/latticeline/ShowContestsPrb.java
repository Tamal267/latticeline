package com.example.latticeline;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ShowContestsPrb implements Initializable {
    @FXML
    private AnchorPane compilerbtn;

    @FXML
    private AnchorPane groupbtn;

    @FXML
    private WebView webview;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane tilePane;

    @FXML
    private Text extime;

    long h= 0, m= 0, s= 0;

    startEndTime stend = startEndTime.getInstance();

    @FXML
    private Button rankbtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tilePane.setMaxWidth(Region.USE_PREF_SIZE);
        scrollPane.setFitToWidth(true);
        WebEngine webengine = webview.getEngine();

        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Fireworks</title>\n" +
                "    <style>\n" +
                "    \tbody{\n" +
                "    \tbackground:#000;\n" +
                "    \tmargin:0;\n" +
                "    \twidth:100%;\n" +
                "    \toverflow:hidden;\n" +
                "    \t}\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    \n" +
                "    <canvas></canvas>\n" +
                "\n" +
                "    <script>\n" +
                "        var t = 0;\n" +
                "        var c = document.querySelector(\"canvas\");     \n" +
                "        var $ = c.getContext('2d');\n" +
                "        c.width = window.innerWidth;\n" +
                "        c.height = window.innerHeight;\n" +
                "        $.fillStyle = 'hsla(0,0%,0%,1)';\n" +
                "\n" +
                "        window.addEventListener('resize', function() {\n" +
                "          c.width = window.innerWidth;\n" +
                "          c.height = window.innerHeight;\n" +
                "        }, false);\n" +
                "\n" +
                "        function draw() {\n" +
                "          $.globalCompositeOperation = 'source-over';\n" +
                "          $.fillStyle = 'hsla(0,0%,0%,.1)';\n" +
                "          $.fillRect(0, 0, c.width, c.height);\n" +
                "          var foo, i, j, r;\n" +
                "          foo = Math.sin(t) * 0.1 * Math.PI;\n" +
                "          for (i=0; i<400; ++i) {\n" +
                "            r = 800 * Math.sin(i * foo);\n" +
                "            $.globalCompositeOperation = '';\n" +
                "            $.fillStyle = 'hsla(' + i + 12 + ',100%, 60%,1)';\n" +
                "            $.beginPath();\n" +
                "            $.arc(Math.sin(i) * r + (c.width / 2), \n" +
                "                  Math.cos(i) * r + (c.height / 2), \n" +
                "                  1.5, 0, Math.PI * 2);\n" +
                "            $.fill();\n" +
                "\n" +
                "          }\n" +
                "          t += 0.000005;\n" +
                "          return t %= 2 * Math.PI;\n" +
                "\n" +
                "        };\n" +
                "\n" +
                "        function run() {\n" +
                "          window.requestAnimationFrame(run);\n" +
                "          draw();\n" +
                "        }\n" +
                "        run();\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";

        webengine.loadContent(htmlContent);


        File file = new File("userinfo.txt");
        Scanner usinf = null;
        try {
            usinf = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String usname = usinf.next();
        String username = new String();
        String connect = new String();
        File file1 = new File("groupname.txt");
        Scanner gpsc = null;
        try {
            gpsc = new Scanner(file1);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String gpname = gpsc.next();
        String statement = new String();
        String code = new String();
        String input = new String();


        File file2 = new File("problem.txt");
        Scanner findPrb = null;
        try {
            findPrb = new Scanner(file2);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        String problemsIds = new String();
        String ranking = new String();
        String users = new String();
        String timelimit = new String();
        while(findPrb.hasNext()) {
            problemsIds = encodeDecode.decode(findPrb.next());
            try {
                Connection connection = DBconnect.getConnect();
                String query = "SELECT * FROM `conProb` WHERE problemid='" + problemsIds + "';";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    statement = resultSet.getString("statement");
                    code = resultSet.getString("code");
                    input = resultSet.getString("input");
                    users = resultSet.getString("users");
                    timelimit = resultSet.getString("timeLimit");

                    String showText = problemsIds;
                    BorderPane borderPane = new BorderPane();
                    Text txt = new Text();
                    txt.setText(showText);
                txt.setStyle("-fx-font-size: 30");
//                txt.setWrappingWidth(250);
                    borderPane.setId(showText);
                    StackPane stackPane = new StackPane();
                    String finalProblemsIds = problemsIds;
                    String finalStatement = statement;
                    String finalCode = code;
                    String finalInput = input;
                    String finalUsers = users;
                    String finalTimelimit = timelimit;
                    stackPane.setOnMouseClicked(e -> {
                        try {
                            FileWriter fileWriter = new FileWriter("assign.txt");
                            fileWriter.write(finalProblemsIds);
                            fileWriter.write("\n");
                            fileWriter.write(finalStatement);
                            fileWriter.write("\n");
                            fileWriter.write(finalCode);
                            fileWriter.write("\n");
                            fileWriter.write(finalInput);
                            fileWriter.write("\n");
                            fileWriter.write(finalTimelimit);
                            fileWriter.write("\n");
                            fileWriter.write(finalUsers);
                            fileWriter.close();
                            Stage stage = (Stage) borderPane.getScene().getWindow();
                            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("contestprb-view.fxml"));
                            Scene scene = new Scene(fxmlLoader.load());
                            scene.getStylesheets().add(HelloApplication.class.getResource("java-keywords.css").toExternalForm());
                            stage.setTitle("LatticeLine");
                            stage.setScene(scene);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    stackPane.getChildren().add(txt);
                    stackPane.setMaxSize(300, 300);
                    stackPane.setMinSize(300, 300);
                    txt.setTextAlignment(TextAlignment.CENTER);
                    txt.wrappingWidthProperty().bind(stackPane.widthProperty());
                    txt.setFill(Color.WHITE);
                    stackPane.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-width: 2; -fx-border-color: WHITE;");
                    stackPane.setOnMouseEntered(e -> {
                        stackPane.setStyle("-fx-background-color: rgba(219, 245, 39, 0.45);-fx-background-radius: 10 10 50 10; -fx-border-radius: 10 10 50 10; -fx-border-width: 2; -fx-border-color: YELLOW;");
                    });
                    stackPane.setOnMouseExited(e -> {
                        stackPane.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-width: 2; -fx-border-color: WHITE;");
                    });
                    BorderPane.setMargin(stackPane, new Insets(20));
                    borderPane.setCenter(stackPane);
                    borderPane.setMaxSize(320, 320);
                    borderPane.setMinSize(320, 320);
                    tilePane.getChildren().add(borderPane);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        String duration = stend.getEnd();

        Scanner sc = new Scanner(duration);
        String year = sc.next();
        String month = sc.next();
        String day = sc.next();
        String hour = sc.next();
        String min = sc.next();
        String sec = sc.next();


        LocalDateTime date1 = LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(min), Integer.parseInt(sec));


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e->{
            LocalDateTime date2 = LocalDateTime.now();
            System.out.println(date1 + " " + date2);
            h = ChronoUnit.HOURS.between(date2, date1);
            m = ChronoUnit.MINUTES.between(date2, date1);
            s = ChronoUnit.SECONDS.between(date2, date1);
            m = m % 60;
            s = s - h * 3600 - m * 60;
            if(h < 0 || s < 0 || m < 0)  extime.setText("00:00:00");
            else extime.setText(Long.toString(h) + ":" + Long.toString(m) + ":" + Long.toString(s));
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
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
    private AnchorPane problemsbtn;
    @FXML
    void problems(MouseEvent event) throws IOException {
        Stage stage = (Stage) problemsbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LatticeLine");
        stage.setScene(scene);
    }

    @FXML
    void group(MouseEvent event) throws IOException {
        Stage stage = (Stage) groupbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("groups-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LatticeLine");
        stage.setScene(scene);
    }

    @FXML
    private Button backbtn;

    @FXML
    void back(MouseEvent event) throws IOException {
        Stage stage = (Stage) backbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("showcontests-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LatticeLine");
        stage.setScene(scene);
    }


    @FXML
    void rank(MouseEvent event) throws IOException {
        Stage stage = (Stage) rankbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("showrank-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LatticeLine");
        stage.setScene(scene);
    }

}