package chatware;
//========================== Imports =========================================//
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private Button button2;
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
//        scanButtonAction();
//        ipg.getIP(connectionBox, 1, 3);
//        ipg.getIP(connectionBox, 4, 7);
//        ipg.getIP(connectionBox, 8, 11);
//        ipg.getIP(connectionBox, 12, 15);
//        ipg.getIP(connectionBox, 16, 19);
//        ipg.getIP(connectionBox, 20, 29);
//        ipg.getIP(connectionBox, 30, 39);
//        ipg.getIP(connectionBox, 40, 49);
//        ipg.getIP(connectionBox, 50, 69);
//        ipg.getIP(connectionBox, 70, 99);
//        ipg.getIP(connectionBox, 100, 149);
//        ipg.getIP(connectionBox, 150, 199);
//        ipg.getIP(connectionBox, 200, 255);
    }
    public void scanButtonAction() {
        indicator.setVisible(true);
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
        connectionBox.setDisable(false);
        indicator.setVisible(false);
    }
    //========================== Connect Button ==============================//
    public void connectButtonAction() {
        try {
            System.out.println(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            sendButton.setDisable(false);
        } catch (Exception e) {
            System.err.println(e);
            messageArea.appendText("Connection Failed! Check computer name/IP..." + "\n");
            inputArea.setDisable(true);
            sendButton.setDisable(true);
        }
    }
}
