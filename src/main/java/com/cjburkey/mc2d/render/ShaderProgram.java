package com.cjburkey.mc2d.render;

import org.lwjgl.opengl.GL20;
import com.cjburkey.mc2d.MC2D;

public class ShaderProgram {
	
	private final int programId;
	private int vertexShaderId;
	private int fragmentShaderId;

	public ShaderProgram() {
		programId = GL20.glCreateProgram();
		if(programId == 0) {
			MC2D.getLogger().err("Could not create Shader");
			MC2D.INSTANCE.stopGame();
		}
	}

	public void createVertexShader(String shaderCode) {
		vertexShaderId = createShader(shaderCode, GL20.GL_VERTEX_SHADER);
	}

	public void createFragmentShader(String shaderCode) {
		fragmentShaderId = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
	}

	protected int createShader(String shaderCode, int shaderType) {
		int shaderId = GL20.glCreateShader(shaderType);
		if(shaderId == 0) {
			MC2D.getLogger().err("Error creating shader. Type: " + shaderType);
			MC2D.INSTANCE.stopGame();
		}

		GL20.glShaderSource(shaderId, shaderCode);
		GL20.glCompileShader(shaderId);

		if(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == 0) {
			MC2D.getLogger().err("Error compiling Shader code: " + GL20.glGetShaderInfoLog(shaderId, 1024));
			MC2D.INSTANCE.stopGame();
		}

		GL20.glAttachShader(programId, shaderId);

		return shaderId;
	}

	public void link() {
		GL20.glLinkProgram(programId);
		if(GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
			MC2D.getLogger().err("Error linking Shader code: " + GL20.glGetProgramInfoLog(programId, 1024));
			MC2D.INSTANCE.stopGame();
		}

		if(vertexShaderId != 0) {
			GL20.glDetachShader(programId, vertexShaderId);
		}
		
		if(fragmentShaderId != 0) {
			GL20.glDetachShader(programId, fragmentShaderId);
		}

		GL20.glValidateProgram(programId);
		if(GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) == 0) {
			MC2D.getLogger().err("Warning validating Shader code: " + GL20.glGetProgramInfoLog(programId, 1024));
		}

	}

	public void bind() {
		GL20.glUseProgram(programId);
	}

	public static void unbind() {
		GL20.glUseProgram(0);
	}

	public void cleanup() {
		unbind();
		if(programId != 0) {
			GL20.glDeleteProgram(programId);
		}
	}
}