package com.nishu.mp;

import com.nishu.mp.game.GameClient;
import com.nishu.mp.game.net.Packet00Login;

public class Main {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	private boolean running = false;

	GameClient client;

	public Main() {
		this.running = false;
		init();
	}

	private void init() {
		client = new GameClient("localhost", "Mister Tubbs");
		client.initGL();
		client.init();
		GameClient.running = true;
		client.start();
		Packet00Login p = new Packet00Login("Mister Tubbs");
		p.writeData(client);
	}

	private void start() {
		if (running)
			return;
		running = true;
		run();
	}

	private void run() {
		while (running) {
			update();
			render();
		}
		stop();
	}

	private void update() {
		if (GameClient.running) {
			client.updateWindow();
		}
	}

	private void render() {
	}

	private void stop() {
		if (!running) return;
		running = false;
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.start();
	}
}