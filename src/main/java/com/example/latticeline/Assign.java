package com.example.latticeline;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class Assign implements Initializable {

    @FXML
    private AnchorPane problemsbtn;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Text text;


    @FXML
    private BorderPane borderText;


    @FXML
    private Text problemId;


    @FXML
    private TextArea codeBox;

    @FXML
    private Text outBox;

    @FXML
    private BorderPane borderText1;


    @FXML
    private Button submitbtn;

    @FXML
    private Button statusbtn;

    @FXML
    private HBox statusbtns;

    FileChooser fileChooser = new FileChooser();

    @FXML
    private Button backbtn;

    @FXML
    private AnchorPane compilerbtn;

    @FXML
    private AnchorPane groupbtn;
    String id, users, txt, acceptedCode, inp;

    Text t = new Text("Yusuf");


    @FXML
    void problems(MouseEvent event) throws IOException {
        Stage stage = (Stage) problemsbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LatticeLine");
        stage.setScene(scene);
    }

    @FXML
    void chooseFile(MouseEvent event) throws FileNotFoundException {
        File file = fileChooser.showOpenDialog(new Stage());
        if(file != null){
            codeBox.clear();
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                codeBox.appendText(scanner.nextLine() + "\n");
            }
        }
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
    void compiler(MouseEvent event) throws IOException {
        Stage stage = (Stage) compilerbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("compiler-view.fxml"));
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
    void run(MouseEvent event) throws IOException, SQLException {
//        outputBox.setWrapText(true);
//        String out = CppCompiler.compileAndRunFromFile(codeBox.getText(), inputBox.getText());
//        outputBox.clear();
//        outputBox.appendText(out);


        File file = new File("assign.txt");
        String encodedCode = Base64.getEncoder().encodeToString(codeBox.getText().getBytes());
        int ac = 1;
        int mxTime = 0;
        int mxMemory = 0;
        Scanner sc = new Scanner(inp);
        while(sc.hasNext()){
            String inp = sc.next();
            byte[] decodeInp = Base64.getDecoder().decode(inp);
            String decodedInp = new String(decodeInp);
            Map<String, String> mapAc = CompilerOnline.compile(acceptedCode, inp, "cpp", "1");
            Map<String, String> mapUc = CompilerOnline.compile(encodedCode, inp, "cpp", "1");
            if(!Objects.equals(mapUc.get("status"), "Accepted")){
                String msg = mapUc.get("status") + "\n";
                outBox.setText(msg);
                ac = 0;
                break;
            }
            if(!Objects.equals(mapAc.get("stdout"), mapUc.get("stdout"))){
                String msg = "Wrong Answer\n\n" +
                        "Input:\n" +
                        decodedInp +
                        "\n\n" +
                        "Accecpted Answer:\n" +
                        mapAc.get("stdout") +
                        "\n\n" +
                        "Your Answer:\n" +
                        mapUc.get("stdout") +
                        "\n";
                outBox.setText(msg);
                ac = 0;
                break;
            }
            double t = parseDouble(mapUc.get("time")) * 1000;
            int tm = (int) t;
            mxTime = Math.max(mxTime, tm);
            mxMemory = Math.max(mxMemory, parseInt(mapUc.get("memory")));
        }
        if(ac == 1){
            String msg = "Accepted\n" + "Time: " + Integer.toString(mxTime) + "ms\n" + "Memory: " + Integer.toString(mxMemory) + "KB\n";
            outBox.setText(msg);
            Connection connection = DBconnect.getConnect();
            String query = "SELECT * FROM `assignment` WHERE assignId='" + id + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet =  preparedStatement.executeQuery();
            while(resultSet.next()){
                String usrs = resultSet.getString("users");
                String encodedMsg = Base64.getEncoder().encodeToString(msg.getBytes());
                file = new File("userinfo.txt");
                sc = new Scanner(file);
                usrs += " " + sc.nextLine() + " " + encodedMsg + " " + encodedCode;
                System.out.println(encodedMsg);
                query = "UPDATE assignment SET users='" + usrs + "' WHERE assignId='" + id + "';";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.executeUpdate();
            }
        }
    }

    @FXML
    void Status(MouseEvent event) throws IOException {
        Stage stage = (Stage) statusbtns.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("status-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LatticeLine");
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        text.wrappingWidthProperty().bind(borderText.widthProperty());
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        outBox.wrappingWidthProperty().bind(borderText1.widthProperty());
        outBox.setText("");
        File file = new File("assign.txt");
        try {
            Scanner sc = new Scanner(file);
            id = sc.nextLine();
            System.out.println(id);
            problemId.setText(id);
            txt = sc.nextLine();
            byte[] decodedBytes = Base64.getDecoder().decode(txt);
            String decodedString = new String(decodedBytes);
            text.setText(decodedString);
            System.out.println(txt);
            acceptedCode = sc.nextLine();
            inp = sc.nextLine();
            users = sc.nextLine();
            File usFile = new File("userinfo.txt");
            Scanner usSc = new Scanner(usFile);
            String usn = usSc.nextLine();
            String fname;
            String temp = "";
            String atext = "";
            int flag = 0;
            usSc = new Scanner(users);
            String abc = usSc.next();
            while(usSc.hasNext()){
                fname = usSc.next();
                temp = usSc.next();
                atext = usSc.next();
                System.out.println(fname + "\n" + temp + "\n" + atext);
                if(Objects.equals(fname, usn)){
                    flag = 1;
                    break;
                }
            }
            if(flag == 1){
                submitbtn.setBackground(Background.fill(Color.GREEN));
                byte[] atextDec = Base64.getDecoder().decode(atext);
                String atextStr = new String(atextDec);
                byte[] tempDec = Base64.getDecoder().decode(temp);
                String tempStr = new String(tempDec);
                System.out.println(atextStr);
                codeBox.setText(atextStr);
                outBox.setText(tempStr);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        File fl = new File("isteacher.txt");
        Scanner scT = null;
        try {
            scT = new Scanner(fl);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String fndTch = scT.next();
        if(Objects.equals(fndTch, "teacher")){
            statusbtns.setVisible(true);
        }
        else{
            statusbtns.setVisible(false);
        }
    }
}
