package com.nishu.mp.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.nishu.mp.Window;
import com.nishu.mp.game.net.Packet;
import com.nishu.mp.game.net.Packet00Login;
import com.nishu.mp.game.net.Packet01Disconnect;
import com.nishu.mp.game.net.Packet02Message;

public class GameServer extends Thread {
	private DatagramSocket socket;
	private List<GameClient> clients = new ArrayList<GameClient>();

	private boolean running = false;

	public GameServer() {
		try {
			this.socket = new DatagramSocket(1333);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		init();
	}

	public void run() {
		while (running) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				this.socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}

		dispose();
	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		switch (type) {
		case INVALID:
			break;
		case DISCONNECT:
			Packet01Disconnect disconnect = new Packet01Disconnect(data);
			System.out.println(disconnect.getUsername() + " Has Left");
			int index = getClientIndex(disconnect.getUsername());
			if (index != -1)
				clients.remove(index);
			else {
				System.err.println("Error Removing Client at: " + index);
			}
			Packet02Message disconnectMessage = new Packet02Message(disconnect.getUsername() + " Has Left");
			disconnectMessage.writeData(this);
			break;
		case LOGIN:
			Packet00Login login = new Packet00Login(data);
			System.out.println(login.getUsername() + " Has Connected");
			GameClient c = new GameClient(address.toString().substring(1), login.getUsername());
			clients.add(c);
			break;
		default:
			break;
		}
	}

	private int getClientIndex(String username) {
		int i;
		for (i = 0; i < clients.size(); i++) {
			if (clients.get(i).getUsername().equalsIgnoreCase(username))
				return i;
		}
		return -1;
	}

	public void sendDataToAllClients(byte[] data) {
		for (GameClient c : this.clients) {
			sendData(data, c.ip, c.port);
		}
	}

	public void sendData(byte[] data, InetAddress ip, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		if (running)
			return;
		running = true;
	}

	public void update() {
	}

	public void dispose() {
		socket.close();
		Window.dispose();
		System.exit(0);
	}

	public static void main(String[] args) {
		GameServer server = new GameServer();
		server.start();
	}
}