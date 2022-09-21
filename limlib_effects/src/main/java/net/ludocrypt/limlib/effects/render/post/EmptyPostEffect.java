package net.ludocrypt.limlib.effects.render.post;

import com.google.common.base.Supplier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import ladysnake.satin.api.managed.ManagedShaderEffect;

public class EmptyPostEffect extends StaticPostEffect {

	public static final Codec<EmptyPostEffect> CODEC = RecordCodecBuilder.create((instance) -> instance.stable(new EmptyPostEffect()));

	public EmptyPostEffect() {
		super(null);
	}

	@Override
	public Codec<? extends PostEffect> getCodec() {
		return CODEC;
	}

	@Override
	public boolean shouldRender() {
		return false;
	}

	@Override
	public Supplier<ManagedShaderEffect> getMemoizedShaderEffect() {
		return () -> null;
	}

}
