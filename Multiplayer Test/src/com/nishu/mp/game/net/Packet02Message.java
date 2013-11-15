package com.nishu.mp.game.net;

import com.nishu.mp.game.GameClient;
import com.nishu.mp.game.GameServer;

public class Packet02Message extends Packet{
	
	private String message;

	public Packet02Message(byte[] message) {
		super(02);
		this.message = readData(message);
	}
	
	public Packet02Message(String message) {
		super(02);
		this.message = message;
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
		return ("02" + this.message).getBytes();
	}
	
	public String getMessage(){
		return this.message;
	}

}
