package chatware;
//========================== Imports =========================================//
import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ComboBox;
//========================== Class ===========================================//
public class IPGetter {
    //========================== Constructor =================================//
    public void getIP(ComboBox cb, int min, int max) {
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
