package net.ludocrypt.limlib.effects.render.post;

import java.util.function.Function;

import com.google.common.base.Supplier;
import com.mojang.serialization.Codec;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.effects.mixin.BuiltinRegistriesAccessor;
import net.ludocrypt.limlib.effects.mixin.RegistryAccessor;
import net.ludocrypt.limlib.effects.render.post.holder.ShaderHolder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public abstract class PostEffect {

	public static final RegistryKey<Registry<PostEffect>> POST_EFFECT_KEY = RegistryAccessor.callCreateRegistryKey("limlib/post_effect");
	public static final Registry<PostEffect> POST_EFFECT = BuiltinRegistriesAccessor.callAddRegistry(POST_EFFECT_KEY, (registry) -> BuiltinRegistries.register(registry, RegistryKey.of(POST_EFFECT_KEY, new Identifier("limlib", "default")), new EmptyPostEffect()));

	@SuppressWarnings("unchecked")
	public static final SimpleRegistry<Codec<? extends PostEffect>> POST_EFFECT_CODEC = (SimpleRegistry<Codec<? extends PostEffect>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "limlib_post_effect")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Codec<PostEffect> CODEC = POST_EFFECT_CODEC.getCodec().dispatchStable(PostEffect::getCodec, Function.identity());

	public abstract Codec<? extends PostEffect> getCodec();

	public abstract boolean shouldRender();

	public abstract void beforeRender();

	public abstract Identifier getShaderLocation();

	@Environment(EnvType.CLIENT)
	public abstract Supplier<ShaderHolder> getMemoizedShaderEffect();
}
