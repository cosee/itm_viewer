package itmviewer.task;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import itmviewer.ui.ITMViewerToolWindow;
import org.assertj.core.util.Arrays;
import tcl.client.TclTcpClient;
import tcl.commands.StartTraceCommand;
import tcl.itm.ITMDecoder;
import tcl.parser.TclBase;
import tcl.parser.TclDecoder;
import tcl.parser.TclEntity;

import java.io.IOException;
import java.util.List;


public class BackgroundClientTask implements Runnable {
    TclTcpClient tcpClient = null;
    private final String host;
    private final String port;

    final ITMViewerToolWindow toolWindow;

    public BackgroundClientTask(Project project, String host, String port) {
        this.toolWindow = ServiceManager.getService(project, ITMViewerToolWindow.class);
        this.host = host;
        this.port = port;
    }

    public boolean clientIsConnected() {
        if (tcpClient == null) {
            return false;
        }
        return tcpClient.isConnected();
    }

    public boolean initTclTraceData() throws IOException {
        tcpClient.setTimeout(3000);
        tcpClient.write(new StartTraceCommand().generateCommand());
        boolean receivedTerminator = tcpClient.readChar() == TclBase.field_terminator;
        tcpClient.setTimeout(0);
        return receivedTerminator;
    }

    public void closeConnection() throws IOException {
        if(tcpClient != null) {
            tcpClient.close();
            tcpClient = null;
            toolWindow.notifyOnServerClose();
        }
    }

    public void runTclServer() throws IOException {
        tcpClient = new TclTcpClient();
        tcpClient.open(host, Integer.parseInt(port));
        toolWindow.notifyOnConnect();
        boolean initSuccess = initTclTraceData();
        // TODO: add retry logic
        if (!initSuccess) {
            toolWindow.notifyOnTraceInitFail();
            return;
        }
        toolWindow.notifyOnTraceInitSuccess();
        while (!Thread.currentThread().isInterrupted()) {
            byte[] tclLine = tcpClient.read();
            if (tclLine == null) {
                continue;
            }
            byte[] itmLine = TclDecoder.parseTclData(tclLine);
            if (itmLine.length == 0) {
                continue;
            }
            List<TclEntity> entities = ITMDecoder.parseITMData(itmLine);
            if(entities != null) {
                toolWindow.addITMPackages(entities);
            }
        }
        closeConnection();
    }

    @Override
    public void run() {
        try {
            runTclServer();
        } catch (IOException e) {
            Thread.currentThread().interrupt();
        } finally {
            try {
                closeConnection();
            } catch (IOException e) {
                System.out.println("Encountered error when closing");
            }
        }
    }
}
