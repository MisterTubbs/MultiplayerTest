package com.nishu.mp.game.net;

import com.nishu.mp.game.GameClient;
import com.nishu.mp.game.GameServer;

public abstract class Packet {

	public static enum PacketTypes {
		INVALID(-1), LOGIN(00), DISCONNECT(01), MESSAGE(02);

		private int ID;

		private PacketTypes(int ID) {
			this.ID = ID;
		}

		public int getID() {
			return this.ID;
		}
	}
	
	public byte ID;

	public Packet(int ID) {
		this.ID = (byte) ID;
	}

	public abstract void writeData(GameClient client);

	public abstract void writeData(GameServer client);

	public String readData(byte[] data) {
		String message = new String(data).trim();
		return message.substring(2);
	}

	public abstract byte[] getData();

	public static PacketTypes lookupPacket(String ID) {
		try {
			return lookupPacket(Integer.parseInt(ID));
		} catch (NumberFormatException e) {
		}
		return PacketTypes.INVALID;
	}

	public static PacketTypes lookupPacket(int ID) {
		for (PacketTypes p : PacketTypes.values()) {
			if (p.getID() == ID)
				return p;
		}
		return PacketTypes.INVALID;
	}
}