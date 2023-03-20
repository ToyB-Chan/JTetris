import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
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
	private List<NetworkMessage> unconfirmedMessages;
	private List<Integer> handeldMessageIds;

	public NetworkManager() {
		this.receivedMessages = new ArrayList<NetworkMessage>();
		this.unconfirmedMessages = new ArrayList<NetworkMessage>();
		this.handeldMessageIds = new ArrayList<Integer>();
	}

	public boolean host(int port) throws IOException {
		if (this.active()) {
			this.close();
		}

		try {
			this.serverSocket = new ServerSocket(port);
			this.serverSocket.setSoTimeout(2000);
			this.socket = this.serverSocket.accept();
		} catch (Exception e) {
			this.serverSocket = null;
			this.socket = null;
			return false;
		}

		this.socketInStream = new DataInputStream(this.socket.getInputStream());
		this.socketOutStream = new DataOutputStream(this.socket.getOutputStream());
		this.socket.setSoTimeout(10000);
		return true;
	}

	public boolean connect(String hostname, int port) throws IOException {
		if (this.active()) {
			this.close();
		}

		try {
			this.socket = new Socket();
			this.socket.connect(new InetSocketAddress(hostname, port), 250);
		} catch (Exception e) {
			this.socket = null;
			return false;
		}

		this.socketInStream = new DataInputStream(this.socket.getInputStream());
		this.socketOutStream = new DataOutputStream(this.socket.getOutputStream());
		this.socket.setSoTimeout(10000);
		return true;
	}

	public void update() throws IOException {
		if (!this.active()) {
			return;
		}

		while (this.socketInStream.available() > 0) {
			NetworkMessage msg = new NetworkMessage(this.socketInStream.readUTF());

			if (msg.type() == NetworkMessage.MESSAGE_RECEIVED) {
				for (int i = 0; i < this.unconfirmedMessages.size(); i++) {
					if (msg.contentInt() == this.unconfirmedMessages.get(i).id()) {
						this.unconfirmedMessages.remove(i);
						break;
					}
				}

				continue;
			}

			if (msg.type() == NetworkMessage.CLOSE_CONNECTION) {
				this.closeInternal();
			}

			if (!this.handeldMessageIds.contains(msg.id())) {
				receivedMessages.add(msg);
				handeldMessageIds.add(msg.id());

				// limit size of handled messages to 300
				if (handeldMessageIds.size() > 300) {
					handeldMessageIds.remove(0);
				}
			}

			this.sendUnreliable(new NetworkMessage(NetworkMessage.MESSAGE_RECEIVED, msg.id()));
		}

		// resend first unconfirmed message
		if (this.unconfirmedMessages.size() > 0) {
			this.sendUnreliable(this.unconfirmedMessages.get(0));
		}

		/*
		if (this.getNumOpenMessages() > 300) {
			this.close();
		}
		*/
	}

	public void sendReliable(NetworkMessage msg) throws IOException {
		if (!this.active()) {
			return;
		}

		this.socketOutStream.writeUTF(msg.toTransmitString());
		this.unconfirmedMessages.add(msg);
	}

	public void sendUnreliable(NetworkMessage msg) throws IOException {
		if (!this.active()) {
			return;
		}

		this.socketOutStream.writeUTF(msg.toTransmitString());
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

	public int numUnconfirmedMessages() {
		return this.unconfirmedMessages.size();
	}

	public boolean active() {
		return this.socket != null && !this.socket.isClosed();
	}

	public void close() throws IOException {
		NetworkMessage msg = new NetworkMessage(NetworkMessage.CLOSE_CONNECTION);
		this.sendUnreliable(msg);
		this.closeInternal();
	}

	private void closeInternal() throws IOException {
		this.receivedMessages.clear();
		this.unconfirmedMessages.clear();
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
