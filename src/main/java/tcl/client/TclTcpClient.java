package tcl.client;

import org.jetbrains.annotations.NotNull;
import tcl.parser.TclBase;

import java.io.*;
import java.net.Socket;


public class TclTcpClient {
    private Socket client = null;
    private OutputStream out;
    private InputStreamReader in;

    public TclTcpClient(@NotNull String host, @NotNull int port) throws IOException {
        open(host, port);
    }

    public TclTcpClient() {

    }

    public void open(@NotNull String host, @NotNull int port) throws IOException {
        client = new Socket(host, port);
        out = client.getOutputStream();
        in = new InputStreamReader(client.getInputStream());
    }

    public byte[] read() throws IOException {
        StringBuilder sb = new StringBuilder();

        int ch = in.read();
        sb.append((char) ch);
        while (ch != 0) {
            ch = in.read();
            sb.append((char) ch);
            if (ch == TclBase.field_terminator) {
                break;
            }
        }
        return sb.toString().getBytes();
    }

    public byte readChar() throws IOException {
        return (byte) in.read();
    }

    public void write(byte[] data) throws IOException {
        if (client == null) {
            return;
        }
        out.write(data);
    }

    public boolean isConnected() {
        return client != null;
    }

    public void close() throws IOException {
        if (client != null) {
            client.close();
            client = null;
        }
    }
}
