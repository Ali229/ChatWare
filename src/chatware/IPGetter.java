/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatware;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class IPGetter {
    public IPGetter(ComboBox cb, int max) {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws UnknownHostException {
                InetAddress localhost = InetAddress.getLocalHost();
                byte[] ip = localhost.getAddress();
                for (int i = 1; i <= max; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    try {
                        ip[3] = (byte) i;
                        InetAddress address = InetAddress.getByAddress(ip);

                        if (address.isReachable(100)) {
                            String output = address.toString().substring(1);
                            cb.getItems().add(output);
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    updateProgress(i, max);

                }
                return null;
            }
        };
        new Thread(task).start();
    }
}
