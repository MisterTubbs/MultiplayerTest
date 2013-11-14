package com.nishu.mp.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.nishu.mp.Window;

public class GameClient extends Thread{
	
	private InetAddress ip;
	private DatagramSocket socket;
	
	public GameClient(String ip){
		try {
			this.socket = new DatagramSocket();
			this.ip = InetAddress.getByName(ip);
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		initGL();
		init();
	}

	private void initGL(){
	}

	private void init(){
	}
	
	public void run(){
		while(!Window.closed()){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String message = new String(packet.getData()).trim();
			System.out.println("Server: " + message);
		}
		dispose();
	}
	
	public void sendData(byte[] data){
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, 1333);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(){
	}
	
	public void render(){
	}

	public void dispose(){
		socket.close();
	}
	
}
