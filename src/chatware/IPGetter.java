/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatware;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class IPGetter {
    //static final int max = 20;
    @FXML
    static ComboBox connectionBox;
    public void getIP(ComboBox cb, int min, int max) {
        
        //bservableList<String> ips = Client.connectionBox.getItems();
        Task task = new Task<Void>() {
            @Override
            public Void call() throws UnknownHostException {
                InetAddress localhost = InetAddress.getLocalHost();
                byte[] ip = localhost.getAddress();
                for (int i = min; i <= max; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    try {
                        ip[3] = (byte) i;
                        InetAddress address = InetAddress.getByAddress(ip);
                        if (address.isReachable(1000)) {
                            
                            String s = address.getHostName();
                            Platform.runLater(() -> cb.getItems().add(s));
                       }
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    updateProgress(i, max);
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
}
}
