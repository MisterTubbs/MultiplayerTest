package com.nishu.mp.game;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import com.nishu.mp.Main;
import com.nishu.mp.Window;
import com.nishu.mp.game.entity.Player;
import com.nishu.mp.game.net.Packet;
import com.nishu.mp.game.net.Packet01Disconnect;
import com.nishu.mp.game.net.Packet02Message;
import com.nishu.mp.game.net.Packet03AddPlayer;

public class GameClient extends Thread {

	public InetAddress address;
	public DatagramSocket socket;
	private List<Player> players = new ArrayList<Player>();

	public int port = 1333;

	private String username;
	public String ip;

	public static boolean running = false;

	public GameClient(String ip, String username) {
		this.username = username;
		try {
			this.address = InetAddress.getByName(ip);
		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void initGL() {
		Window.createWindow(Main.WIDTH, Main.HEIGHT);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		glMatrixMode(GL_MODELVIEW);
		glOrtho(0, Main.WIDTH, Main.HEIGHT, 0, 1, -1);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public void init() {
		try{
			this.socket = new DatagramSocket();
		}catch(SocketException e){
			e.printStackTrace();
		}
		players.add(new Player((float) Math.random() * 32, (float) Math.random() * 32, 64));
		//Packet03AddPlayer addPlayer = new Packet03AddPlayer(String.valueOf(players.get(0).getPos()));
		//addPlayer.writeData(this);
	}

	public void updateWindow() {
		if (!Window.closed()) {
			update();
			render();
		}
		if (Window.closed()) {
			running = false;
			dispose();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				this.socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		switch (type) {
		case INVALID:
			break;
		case MESSAGE:
			Packet02Message packetMessage = new Packet02Message(data);
			System.out.println(packetMessage.getMessage());
			break;
		case ADDPLAYER:
			Packet03AddPlayer addPlayer = new Packet03AddPlayer(data);
			players.add(new Player(Float.valueOf(addPlayer.getPlayer().charAt(0)), Float.valueOf(addPlayer.getPlayer().charAt(1)), 64));
		default:
			break;
		}
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, this.address, this.port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		Window.update();
	}

	public void render() {
		for (int i = 0; i < players.size(); i++) {
			players.get(i).render();
		}
	}

	public String getUsername() {
		return username;
	}

	public void dispose() {
		Packet01Disconnect packet = new Packet01Disconnect(username);
		packet.writeData(this);
		for (int i = 0; i < players.size(); i++) {
			players.get(i).dispose();
		}
		socket.close();
		Window.dispose();
		System.exit(0);
	}
}