package chatware;
//========================== Imports =========================================//
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
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
    private TextArea messageArea;
    @FXML
    private TextArea inputArea;
    @FXML
    private Button sendButton;
    @FXML
    private ProgressIndicator indicator;
    private String ipConnected;
    private String Something;
    Thread t1;
    String something1;
    int nothing;
    //========================== Start =======================================//    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent p = FXMLLoader.load(getClass().getResource("ClientDesign.fxml"));
        Scene mainScene = new Scene(p);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Chat Client");
        primaryStage.show();   
        System.out.println("start ran");
    }
    
    //========================== Main Method =================================// 
    public static void main(String[] args) {
        System.out.println("main ran");
        launch(args);
    }
    
    //========================== Intialize For XML ===========================//
    @FXML
    protected void initialize() throws UnknownHostException, SocketException {
//        Task<Integer> task = new Task<Integer>() {
//    @Override 
//    protected Integer call() throws Exception {
//        
//    }
//};
//indicator.progressProperty().bind(t1.run());
   t1 = new Thread(){
            @Override
            public void run(){
                
                System.out.println("Initialize ran");
        try {
            connectionBox.getEditor().setText(Inet4Address.getLocalHost().getHostAddress());
            Enumeration e = NetworkInterface.getNetworkInterfaces();
while(e.hasMoreElements())
{
    NetworkInterface n = (NetworkInterface) e.nextElement();
    Enumeration ee = n.getInetAddresses();
    while (ee.hasMoreElements())
    {
        InetAddress i = (InetAddress) ee.nextElement();
        connectionBox.getItems().add(i);
        System.out.println(i.getHostName());
    }
}
        } catch (Exception e) {
            
        }
        indicator.setVisible(false);
                }
           
   };
   t1.start();
        
            

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
