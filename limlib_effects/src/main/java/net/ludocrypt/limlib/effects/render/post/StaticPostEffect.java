package net.ludocrypt.limlib.effects.render.post;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

public class StaticPostEffect extends PostEffect {

	public static final Codec<StaticPostEffect> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Identifier.CODEC.fieldOf("shader_name").stable().forGetter((postEffect) -> {
			return postEffect.shaderName;
		})).apply(instance, instance.stable(StaticPostEffect::new));
	});

	private final Identifier shaderName;

	@Environment(EnvType.CLIENT)
	private final Supplier<ManagedShaderEffect> memoizedShaderEffect = Suppliers.memoize(() -> ShaderEffectManager.getInstance().manage(this.getShaderLocation()));

	public StaticPostEffect(Identifier shaderLocation) {
		this.shaderName = shaderLocation;
	}

	@Override
	public Codec<? extends PostEffect> getCodec() {
		return CODEC;
	}

	@Override
	public boolean shouldRender() {
		return true;
	}

	@Override
	public void beforeRender() {

	}

	@Override
	public Identifier getShaderLocation() {
		return new Identifier(shaderName.getNamespace(), "shaders/post/" + shaderName.getPath() + ".json");
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Supplier<ManagedShaderEffect> getMemoizedShaderEffect() {
		return memoizedShaderEffect;
	}

}
