package com.cjburkey.mc2d.render;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import com.cjburkey.mc2d.MC2D;

public class ShaderProgram {
	
	private final Map<String, Integer> uniforms;
	
	private final int programId;
	private int vertexShaderId;
	private int fragmentShaderId;

	public ShaderProgram() {
		uniforms = new HashMap<>();
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
	
	public void createUniform(String name) {
		int location = GL20.glGetUniformLocation(programId, name);
		if(location < 0) {
			MC2D.getLogger().err("Couldn't find uniform: " + name);
			MC2D.INSTANCE.stopGame();
		}
		uniforms.put(name, location);
	}
	
	public void setUniform(String name, Matrix4f value) {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buff = stack.mallocFloat(16);
			value.get(buff);
			GL20.glUniformMatrix4fv(uniforms.get(name), false, buff);
		}
	}
	
	public void setUniform(String name, int value) {
		GL20.glUniform1i(uniforms.get(name), value);
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