import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkManager {
	private Socket socket;
	private DataInputStream socketInStream;
	private DataOutputStream socketOutStream;
	private ServerSocket serverSocket;
	private List<NetworkMessage> receivedMessages;
	private int numOpenMessages;

	public NetworkManager() {
		this.receivedMessages = new ArrayList<NetworkMessage>();
		this.numOpenMessages = 0;
	}

	public void host(int port) throws IOException {
		if (this.active()) {
			this.close();
		}

		this.serverSocket = new ServerSocket(port);
		this.socket = this.serverSocket.accept();
		this.socketInStream = new DataInputStream(this.socket.getInputStream());
		this.socketOutStream = new DataOutputStream(this.socket.getOutputStream());
		this.socket.setSoTimeout(10000);
	}

	public void connect(String hostname, int port) throws IOException {
		if (this.active()) {
			this.close();
		}

		this.socket = new Socket(hostname, port);
		this.socketInStream = new DataInputStream(this.socket.getInputStream());
		this.socketOutStream = new DataOutputStream(this.socket.getOutputStream());
		this.socket.setSoTimeout(10000);
	}

	public void update() throws IOException {
		if (!this.active()) {
			return;
		}

		while (this.socketInStream.available() > 0) {
			NetworkMessage msg = new NetworkMessage(this.socketInStream.readUTF());

			if (msg.type() == NetworkMessage.MESSAGE_RECEIVED) {
				this.numOpenMessages--;
				continue;
			}

			if (msg.type() == NetworkMessage.CLOSE_CONNECTION) {
				this.closeInternal();
			}

			receivedMessages.add(msg);
			this.send(new NetworkMessage(NetworkMessage.MESSAGE_RECEIVED));
		}

		if (this.numOpenMessages > 300) {
			this.close();
		}
	}

	public void send(NetworkMessage msg) throws IOException {
		if (!this.active()) {
			return;
		}

		this.socketOutStream.writeUTF(msg.toTransmitString());
		this.numOpenMessages++;
	}

	public int available() {
		return this.receivedMessages.size();
	}

	public NetworkMessage nextMessage() {
		if (this.available() <= 0) {
			return null;
		}

		NetworkMessage msg = this.receivedMessages.get(0);
		this.receivedMessages.remove(0);
		return msg;
	}

	public int getNumOpenMessages() {
		return this.numOpenMessages;
	}

	public boolean active() {
		return this.socket != null && !this.socket.isClosed();
	}

	public void close() throws IOException {
		NetworkMessage msg = new NetworkMessage(NetworkMessage.CLOSE_CONNECTION);
		this.send(msg);
		this.closeInternal();
	}

	private void closeInternal() throws IOException {
		this.numOpenMessages = 0;
		this.socketInStream.close();
		this.socketOutStream.close();
		this.socket.close();
		this.socket = null;
		
		if (this.serverSocket != null) {
			this.serverSocket.close();
			this.serverSocket = null;
		}
	}
}
