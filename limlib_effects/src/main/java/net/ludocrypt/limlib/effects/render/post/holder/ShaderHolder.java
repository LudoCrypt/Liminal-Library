package net.ludocrypt.limlib.effects.render.post.holder;

import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

public class ShaderHolder {

	@Environment(EnvType.CLIENT)
	private final ManagedShaderEffect shaderEffect;

	@Environment(EnvType.CLIENT)
	public ShaderHolder(Identifier id) {
		shaderEffect = ShaderEffectManager.getInstance().manage(id);
	}

	@Environment(EnvType.CLIENT)
	public ManagedShaderEffect getShader() {
		return shaderEffect;
	}

}
