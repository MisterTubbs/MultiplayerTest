package com.nishu.mp.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class GameServer extends Thread{
	
	private DatagramSocket socket;
	
	private boolean running = false;
	
	public GameServer(){
		try {
			this.socket = new DatagramSocket(1333);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		initGL();
		init();
	}
	
	public void run(){
		while(running){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String message = new String(packet.getData()).trim();
			System.out.println("Client: " + message);
			sendData("Pong".getBytes(), packet.getAddress(), packet.getPort());
		}
		dispose();
	}
	
	public void sendData(byte[] data, InetAddress ip, int port){
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initGL(){
	}

	private void init(){
		if(running) return;
		running = true;
	}

	public void update(){
	}
	
	public void render(){
	}

	public void dispose(){
		if(!running) return;
		running = false;
		socket.close();
	}
	
	public static void main(String[] args){
		GameServer server = new GameServer();
		server.start();
	}
	
}
