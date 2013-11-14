package com.nishu.mp;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.opengl.Display;

import com.nishu.mp.game.GameClient;
import com.nishu.mp.game.GameServer;

public class Main {
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	private boolean running = false;
	
	GameClient client;
	GameServer server;

	public Main(){
		running = false;
		
		initGL();
		init();
	}

	private void initGL(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glMatrixMode(GL_MODELVIEW);
		glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	private void init(){
		client = new GameClient("localhost");
		client.start();
		client.sendData("Ping".getBytes());
	}
	
	private void start(){
		if(running) return;
		running = true;
		run();
	}
	
	private void run(){
		while(!Window.closed() && running){
			update();
			render();
		}
		stop();
	}
	
	private void update(){
		Window.update();
	}

	private void render(){
	}
	
	private void stop(){
		if(!running) return;
		running = false;
		dispose();
	}

	private void dispose(){
		Window.dispose();
		client.dispose();
	}
	
	public static void main(String[] args){
		Window.createWindow(WIDTH, HEIGHT);
		Main main = new Main();
		main.start();
	}
}
