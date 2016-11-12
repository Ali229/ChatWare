package chatware;
//========================== Imports =========================================//

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
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
    Thread t1;

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
    public void checkLoop() {
        Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
           // Platform.runLater(() -> connectionBox.hide());
            Platform.runLater(() -> connectionBox.getItems().add("Item"));
            //Platform.runLater(() -> connectionBox.show());
            Platform.runLater(() -> connectionBox.setVisibleRowCount(30));
        }
    }, 0, 2000);
    }
    public void connectionBoxOnShown(){
        connectionBox.hide();
        connectionBox.show();
    }
    //========================== Intialize For XML ===========================//
    @FXML
    protected void initialize() throws UnknownHostException, SocketException {
        
        checkLoop();
        //loop2();
        //IPGetter ipg = new IPGetter(connectionBox, 10);
        //getIP();
    }
    static final int max = 20;
    ObservableList<String> strings1 = FXCollections.observableArrayList();

    public void getIP() throws UnknownHostException {
        ObservableList<String> ips = connectionBox.getItems();
        Task task = new Task<Void>() {
            @Override
            public Void call() throws UnknownHostException {
                InetAddress localhost = InetAddress.getLocalHost();
                byte[] ip = localhost.getAddress();
                for (int i = 10; i <= max; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    try {
                        ip[3] = (byte) i;
                        InetAddress address = InetAddress.getByAddress(ip);
                        if (address.isReachable(100)) {
                            String s = address.getHostName();
                            Platform.runLater(() -> connectionBox.getItems().add(s));
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    updateProgress(i, max);
                }
                return null;
            }
        };
        indicator.progressProperty().bind(task.progressProperty());
        //indicator.progressProperty().bind(task.progressProperty().multiply(1).add(task1.progressProperty().multiply(0.5)));
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        //new Thread(task).start();
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
