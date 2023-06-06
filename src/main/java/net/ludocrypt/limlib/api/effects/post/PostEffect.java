package net.ludocrypt.limlib.api.effects.post;

import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;

import net.ludocrypt.limlib.impl.mixin.RegistriesAccessor;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public abstract class PostEffect {

	public static final RegistryKey<Registry<Codec<? extends PostEffect>>> POST_EFFECT_CODEC_KEY = RegistryKey.ofRegistry(new Identifier("limlib/codec/post_effect"));
	public static final Registry<Codec<? extends PostEffect>> POST_EFFECT_CODEC = RegistriesAccessor.callRegisterSimple(POST_EFFECT_CODEC_KEY, Lifecycle.stable(),
			(registry) -> StaticPostEffect.CODEC);
	public static final Codec<PostEffect> CODEC = POST_EFFECT_CODEC.getCodec().dispatchStable(PostEffect::getCodec, Function.identity());
	public static final RegistryKey<Registry<PostEffect>> POST_EFFECT_KEY = RegistryKey.ofRegistry(new Identifier("limlib/post_effect"));

	public abstract Codec<? extends PostEffect> getCodec();

	public abstract boolean shouldRender();

	public abstract void beforeRender();

	public abstract Identifier getShaderLocation();

}
