package itmviewer.task;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import itmviewer.ui.ITMViewerToolWindow;
import tcl.client.TclTcpClient;

import java.io.IOException;

public class BackgroundClientTask implements Runnable {
    TclTcpClient tcpClient = null;
    private String host;
    private String port;
    private Project project;

    public BackgroundClientTask(Project project, String host, String port) {
        this.project = project;
        this.host = host;
        this.port = port;
    }

    public boolean clientIsConnected() {
        if(tcpClient == null) {
            return false;
        }
        return tcpClient.isConnected();
    }

    @Override
    public void run() {
        try {
            tcpClient = new TclTcpClient();
            tcpClient.open(host, Integer.valueOf(port));
            for(int i = 0; i<100; i++)  {
                tcpClient.write("Hello".getBytes());
                ITMViewerToolWindow toolWindow = ServiceManager.getService(project, ITMViewerToolWindow.class);
                toolWindow.addMessage("Hello\n");
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if(tcpClient != null) {
                try {
                    tcpClient.close();
                } catch (IOException e) {
                    System.out.println("Encountered error when closing");
                }
            }
        }
    }
}
