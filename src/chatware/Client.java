package chatware;
//========================== Imports =========================================//
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
//========================== Class ===========================================//
public class Client extends Application {
    //========================== Declerations ================================// 
    @FXML
    private ComboBox connectionBox;
    @FXML
    private Button connectButton;
    @FXML
    private Button scanButton;
    @FXML
    private TextArea messageArea;
    @FXML
    private TextArea inputArea;
    @FXML
    private Button sendButton;
    @FXML
    private ProgressIndicator indicator;
    private String ipConnected;
    //========================== Start ===L====================================//    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent p = FXMLLoader.load(getClass().getResource("ClientDesign.fxml"));
        Scene mainScene = new Scene(p);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Chat Client");
        primaryStage.show();
    }
    //========================== Main Method =================================// 
    public static void main(String[] args) {
        launch(args);
    }
    //========================== Intialize For XML ===========================//
    @FXML
    protected void initialize() {
        connectionBox.getEditor().setText("ALI-ASPIRE");
    }
    //========================== [Enter] Key To Send/Connect =================//
    public void inputAreaKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            e.consume();
            if (inputArea.getText().length() > 0) {
                sendButtonAction();
                inputArea.setText("");
            }
        } else {
        }
    }
    //========================== Disables and Enables Send Button ============//
    public void inputAreaKeyReleased() {
        if (inputArea.getText().isEmpty() == false) {
            sendButton.setDisable(false);
        } else {
            sendButton.setDisable(true);
        }
    }
    //========================== Scan Button =================================//
    public void scanButtonAction() {
        indicator.setVisible(true);
        scanButton.setDisable(true);
        connectButton.setDisable(true);
        connectionBox.setDisable(true);
        connectionBox.getItems().clear();
        IPGetter ipg = new IPGetter();
        for (int i = 1; i < 256; i++) {
            ipg.getIP(connectionBox, i, i + 1);
            i++;
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5000), ae -> checking()));
        timeline.play();
    }
    public void checking() {
        connectButton.setDisable(false);
        scanButton.setDisable(false);
        connectionBox.setDisable(false);
        indicator.setVisible(false);
    }
    //========================== Connection Outgoing =========================//
    public void connectButtonAction() {
        try {
            Socket s1 = new Socket(connectionBox.getEditor().getText(), 8000);
            OutputStream sout1 = s1.getOutputStream();
            PrintStream pout1 = new PrintStream(sout1);
            pout1.println(Inet4Address.getLocalHost().getHostAddress());
            pout1.flush();
            s1.close();
            messageArea.appendText("Connection Establised!" + "\n");
            inputArea.setDisable(false);
            inputArea.requestFocus();
            ipConnected = connectionBox.getEditor().getText();
        } catch (Exception e) {
            System.err.println(e);
            messageArea.appendText("Connection Failed! Check computer name/IP..." + "\n");
            inputArea.setDisable(true);
        }
    }
    //========================== Message Outgoing ============================//
    public void sendButtonAction() {
        try {
            DateFormat df = new SimpleDateFormat("hh:mm a");
            Date dateobj = new Date();
            messageArea.appendText("You: " + inputArea.getText() + "\n");
            messageArea.appendText(df.format(dateobj) + "\n");
            Socket s1 = new Socket(ipConnected, 8001);
            OutputStream sout = s1.getOutputStream();
            PrintStream pout = new PrintStream(sout);
            pout.println(inputArea.getText());
            pout.flush();
            s1.close();
            sendButton.setDisable(true);
            inputArea.setText(null);
            inputArea.requestFocus();
        } catch (Exception er) {
            messageArea.appendText("Message failed, check connection..." + "\n");
            inputArea.setText(null);
            connectButton.setDisable(false);
            sendButton.setDisable(true);
            inputArea.setEditable(false);
        }
    }
}
