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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class ContestPrb implements Initializable {

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
    private Button acceptedbtn;

    @FXML
    private AnchorPane groupbtn;
    String id, users, txt, acceptedCode, inp, timelimit;


    String curUser = "";        //login user

    long slv = 0, pen = 0 , prepen = 0;

    startEndTime stend = startEndTime.getInstance();

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
        if (file != null) {
            codeBox.clear();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
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
        scene.getStylesheets().add(HelloApplication.class.getResource("java-keywords.css").toExternalForm());
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

        Connection connection1 = DBconnect.getConnect();
        String query1 = "SELECT * FROM `contest` WHERE contestName='" + stend.getContestName() + "'";
        PreparedStatement preparedStatement1 = connection1.prepareStatement(query1);
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        ArrayList<rankInfo> rankList = new ArrayList<>();
        while (resultSet1.next()) {
            String ranking = resultSet1.getString("ranking");
            Scanner sc = new Scanner(ranking);
            while(sc.hasNext()){
                String temp = encodeDecode.decode(sc.next());
                Scanner st = new Scanner(temp);
                String username = st.next();
                long solve = st.nextLong(), penalty = st.nextLong(), prepenalty = st.nextLong();
                rankList.add(new rankInfo(username, solve, penalty, prepenalty));
                if(Objects.equals(username, curUser)){
                    slv = solve;
                    pen = penalty;
                    prepen = prepenalty;
                }
            }
        }



        LocalDateTime tmm = LocalDateTime.now();
        DateTimeFormatter pat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String time = tmm.format(pat);
        String startTime = stend.getStart();

        System.out.println(startTime);

        Scanner sc1 = new Scanner(startTime);
        String year = sc1.next();
        String month = sc1.next();
        String day = sc1.next();
        String hour = sc1.next();
        String min = sc1.next();
        String sec = sc1.next();


        LocalDateTime date1 = LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(min), Integer.parseInt(sec));
        long penaccept = ChronoUnit.MINUTES.between(date1, tmm);

        File file = new File("assign.txt");
        String encodedCode = Base64.getEncoder().encodeToString(codeBox.getText().getBytes());
        int ac = 1;
        int mxTime = 0;
        int mxMemory = 0;
        Scanner sc = new Scanner(inp);
        while (sc.hasNext()) {
            String inp = sc.next();
            byte[] decodeInp = Base64.getDecoder().decode(inp);
            String decodedInp = new String(decodeInp);
            System.out.println(inp);
            System.out.println(decodedInp);
            Map<String, String> mapAc = CompilerOnline.compile(acceptedCode, inp, "cpp", timelimit);
            Map<String, String> mapUc = CompilerOnline.compile(encodedCode, inp, "cpp", timelimit);
            System.out.println(mapAc);
            if (!Objects.equals(mapUc.get("status"), "Accepted")) {
                String msg = mapUc.get("status") + "\n";
                outBox.setText(msg);
                ac = 0;
                break;
            }
            if (!Objects.equals(mapAc.get("stdout"), mapUc.get("stdout"))) {
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
                prepen += 20;
                break;
            }
            double t = parseDouble(mapUc.get("time")) * 1000;
            int tm = (int) t;
            mxTime = Math.max(mxTime, tm);
            mxMemory = Math.max(mxMemory, parseInt(mapUc.get("memory")));
        }
        if (ac == 1) {
            slv++;
            prepen += penaccept;
            pen = prepen;
            System.out.println("penalty " + slv + " " + pen);
            String msg = time + "\nAccepted\n" + "Time: " + Integer.toString(mxTime) + "ms\n" + "Memory: " + Integer.toString(mxMemory) + "KB\n";
            outBox.setText(msg);
            Connection connection = DBconnect.getConnect();
            String query = "SELECT * FROM `conProb` WHERE problemid='" + id + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String usrs = resultSet.getString("users");
                String encodedMsg = Base64.getEncoder().encodeToString(msg.getBytes());
                file = new File("userinfo.txt");
                sc = new Scanner(file);
                usrs += " " + sc.nextLine() + " " + encodedMsg + " " + encodedCode;
                System.out.println(encodedMsg);
                query = "UPDATE conProb SET users='" + usrs + "' WHERE problemid='" + id + "';";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.executeUpdate();
            }
        }

        LocalDateTime chk = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy MM dd HH mm ss");
        String nowstr = chk.format(fmt);
        String start = stend.getStart(), end = stend.getEnd();
        if (start.compareTo(nowstr) > 0 || end.compareTo(nowstr) < 0) {
            System.out.println("Time End");
            return;
        }

        int flag = 0;
        for(rankInfo i:rankList){
            String usr = i.username;
            if(Objects.equals(usr, curUser)){
                i.solve = slv;
                i.penalty = pen;
                i.prepenalty = prepen;
                flag = 1;
                break;
            }
        }
        if(flag == 0){
            rankList.add(new rankInfo(curUser, slv, pen, prepen));
        }
        rankList.sort(rankInfo::comp);
        StringBuilder ranking = new StringBuilder();
        for(rankInfo i:rankList){
            String temp = i.username + " " + Long.toString(i.solve) + " " + Long.toString(i.penalty) + " " + Long.toString(i.prepenalty);
            temp = encodeDecode.encode(temp);
            ranking.append(temp).append(" ");
        }
        query1 = "UPDATE `contest` SET ranking='" + ranking + "' WHERE contestName='" + stend.getContestName() + "'";
        preparedStatement1 = connection1.prepareStatement(query1);
        preparedStatement1.executeUpdate();
    }

    @FXML
    void Status(MouseEvent event) throws IOException {
        Stage stage = (Stage) statusbtns.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("status-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LatticeLine");
        stage.setScene(scene);
    }

    @FXML
    void accepted(MouseEvent event) throws IOException {
        Stage stage = (Stage) acceptedbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("accepted-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LatticeLine");
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        text.wrappingWidthProperty().bind(borderText.widthProperty());
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        outBox.wrappingWidthProperty().bind(borderText1.widthProperty());
        File usFile = new File("userinfo.txt");
        Scanner usSc = null;
        try {
            usSc = new Scanner(usFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String usn = usSc.nextLine();
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
            timelimit = sc.nextLine();
            text.setText("Time Limit: " + timelimit + "s\n\n" + decodedString);
            while(sc.hasNext()){
                users = sc.next();
                if(Objects.equals(users, usn)){
                    acceptedbtn.setVisible(true);
                    String temp = sc.next();
                    String atext = sc.next();
                    byte[] atextDec = Base64.getDecoder().decode(atext);
                    String atextStr = new String(atextDec);
                    byte[] tempDec = Base64.getDecoder().decode(temp);
                    String tempStr = new String(tempDec);
//                    System.out.println(atextStr);
                    String t = tempStr + "\n\n-------------\n\n" + atextStr;
                    FileWriter fileWriter = new FileWriter("acceptedinfo.txt");
                    fileWriter.write(t);
                    fileWriter.close();
                    acceptedbtn.setVisible(true);
                }
            }

//            String fname;
//            String temp = "";
//            String atext = "";
//            int flag = 0;
//            usSc = new Scanner(users);
//            String abc = usSc.next();
//            while(usSc.hasNext()){
//                fname = usSc.next();
//                temp = usSc.next();
//                atext = usSc.next();
//                System.out.println(fname + "\n" + temp + "\n" + atext);
//                if(Objects.equals(fname, usn)){
//                    flag = 1;
//                    break;
//                }
//            }
//            if(flag == 1){
//                submitbtn.setBackground(Background.fill(Color.GREEN));
//                byte[] atextDec = Base64.getDecoder().decode(atext);
//                String atextStr = new String(atextDec);
//                byte[] tempDec = Base64.getDecoder().decode(temp);
//                String tempStr = new String(tempDec);
//                System.out.println(atextStr);
//                String t = tempStr + "\n\n-------------\n\n" + atextStr;
//                FileWriter fileWriter = new FileWriter("acceptedinfo.txt");
//                fileWriter.write(t);
//                fileWriter.close();
//                acceptedbtn.setVisible(true);
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
            File fl = new File("isteacher.txt");
            Scanner scT = null;
            try {
                scT = new Scanner(fl);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            String fndTch = scT.next();
            if (Objects.equals(fndTch, "teacher")) {
                statusbtns.setVisible(true);
            } else {
                statusbtns.setVisible(false);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File file3 = new File("userinfo.txt");
        Scanner cuSc = null;
        try {
            cuSc = new Scanner(file3);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        curUser = cuSc.next();

    }
}
