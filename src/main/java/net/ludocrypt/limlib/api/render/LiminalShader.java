package net.ludocrypt.limlib.api.render;

import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public abstract class LiminalShader {

	public abstract boolean shouldRender(MinecraftClient client, float tickdelta);

	public abstract ManagedShaderEffect getShader(MinecraftClient client, float tickdelta);

	public static class SimpleShader extends LiminalShader {

		private final ManagedShaderEffect shader;

		public SimpleShader(Identifier shader) {
			this(ShaderEffectManager.getInstance().manage(shader));
		}

		public SimpleShader(ManagedShaderEffect shader) {
			this.shader = shader;
		}

		@Override
		public boolean shouldRender(MinecraftClient client, float tickdelta) {
			return true;
		}

		@Override
		public ManagedShaderEffect getShader(MinecraftClient client, float tickdelta) {
			return shader;
		}

	}

}
