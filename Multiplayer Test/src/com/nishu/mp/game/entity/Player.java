package com.nishu.mp.game.entity;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;

import com.nishu.shaderutils.Shader;
import com.nishu.shaderutils.ShaderProgram;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

public class Player {

	private Vector2f pos;
	private ShaderProgram program;

	private int vID, size;

	public Player(float x, float y, int size) {
		this.pos = new Vector2f(x, y);
		this.size = size;
		
		initGL();
		init();
	}

	public void initGL(){
		Shader geomShader = new Shader("/player.vert", "/player.frag");
		program = new ShaderProgram(geomShader.getvShader(), geomShader.getfShader());
		
		FloatBuffer verts = BufferUtils.createFloatBuffer(2 * 4);
		verts.put(new float[]{
				pos.x, pos.y,
				pos.x, pos.y + size,
				pos.x + size, pos.y + size, 
				pos.x + size, pos.y
		});
		verts.rewind();
		
		vID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vID);
		glBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void init() {
	}

	public void update() {
	}

	public void render() {
		glColor3f(0, 0, 1);
		program.use();
		glBindBuffer(GL_ARRAY_BUFFER, vID);
		glVertexPointer(2, GL_FLOAT, 0, 0);
		
		glEnableClientState(GL_VERTEX_ARRAY);
		
		glDrawArrays(GL_QUADS, 0, 4);
		
		glDisableClientState(GL_VERTEX_ARRAY);
		program.release();
	}

	public void dispose() {
		glDeleteBuffers(vID);
	}

}
