package net.ludocrypt.limlib.api.render;

import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormat;

public class LiminalCoreShader {

	private Shader shader;
	private VertexFormat vertexFormat;

	public LiminalCoreShader(VertexFormat vertexFormat) {
		this.vertexFormat = vertexFormat;
	}

	public Shader getShader() {
		return shader;
	}

	public void setShader(Shader shader) {
		this.shader = shader;
	}

	public VertexFormat getVertexFormat() {
		return vertexFormat;
	}

}
