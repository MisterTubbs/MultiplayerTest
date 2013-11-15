package com.nishu.mp.game.net;

import com.nishu.mp.game.GameClient;
import com.nishu.mp.game.GameServer;

public class Packet03AddPlayer extends Packet{
	
	private String data;

	public Packet03AddPlayer(byte[] data) {
		super(03);
		this.data = readData(data);
	}
	
	public Packet03AddPlayer(String data) {
		super(03);
		this.data = data;
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("03" + data).getBytes();
	}
	
	public String getPlayer(){
		return data;
	}

}
