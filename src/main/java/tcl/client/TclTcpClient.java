package tcl.client;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import tcl.parser.TclBase;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

@NoArgsConstructor
public class TclTcpClient {
    private Socket client = null;
    private OutputStream out;
    private InputStreamReader in;

    public TclTcpClient(@NotNull String host, int port) throws IOException {
        open(host, port);
    }

    public void setTimeout(int timeout) throws SocketException {
        if(client != null) {
            client.setSoTimeout(timeout);
        }
    }

    public void open(@NotNull String host, int port) throws IOException {
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
            in.close();
            client = null;
        }
    }
}
