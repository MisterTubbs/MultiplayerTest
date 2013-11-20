package com.nishu.mp;

import java.util.Scanner;

import com.nishu.mp.game.GameClient;
import com.nishu.mp.game.net.Packet00Login;

public class Main {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	private boolean running = false;

	GameClient client;

	public Main(String name) {
		this.running = false;
		init(name);
	}

	private void init(String name) {
		client = new GameClient("localhost", name);
		client.initGL();
		client.init();
		GameClient.running = true;
		client.start();
		Packet00Login p = new Packet00Login(name);
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
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter a name: ");
		String name = scan.nextLine();
		scan.close();
		Main main = new Main(name);
		main.start();
	}
}