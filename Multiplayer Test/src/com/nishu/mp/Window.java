package com.nishu.mp;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {
	
	public static void createWindow(int WIDTH, int HEIGHT){
		try {
			Display.setDisplayMode(new DisplayMode(Main.WIDTH, Main.HEIGHT));
			Display.setTitle("Nishu MP Test");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean closed(){
		return Display.isCloseRequested();
	}
	
	public static void update(){
		Display.sync(60);
		Display.update();
	}

	public static void dispose(){
		Display.destroy();
	}
}
