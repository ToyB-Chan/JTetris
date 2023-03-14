import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkManager {
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream socketInStream;
    private DataOutputStream socketOutStream;
    private List<NetworkMessage> receivedMessages;
    private int numOpenMessages;

    public NetworkManager() {
        this.receivedMessages = new ArrayList<NetworkMessage>();
        this.numOpenMessages = 0;
    }

    public void host(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.socket = this.serverSocket.accept();
        this.socketInStream = new DataInputStream(this.socket.getInputStream());
        this.socketOutStream = new DataOutputStream(this.socket.getOutputStream());
    }

    public void connect(String hostname, int port) throws IOException {
        this.socketInStream = new DataInputStream(this.socket.getInputStream());
        this.socketOutStream = new DataOutputStream(this.socket.getOutputStream());
    }

    public void update() throws IOException {
        while (this.socketInStream.available() > 0) {
            NetworkMessage msg = new NetworkMessage(this.socketInStream.readUTF());

            if (msg.type == NetworkMessage.MESSAGE_RECEIVED) {
                this.numOpenMessages--;
                continue;
            }

            receivedMessages.add(msg);
            this.send(new NetworkMessage(NetworkMessage.MESSAGE_RECEIVED));
        }
    }

    public void send(NetworkMessage msg) throws IOException {
        this.socketOutStream.writeUTF(msg.toTransmitString());
        this.numOpenMessages++;
    }

    public int available() {
        return this.receivedMessages.size();
    }

    public NetworkMessage nextMessage() {
        NetworkMessage msg = this.receivedMessages.get(0);
        this.receivedMessages.remove(0);
        return msg;
    }

    public int getNumOpenMessages() {
        return this.numOpenMessages;
    }
}
