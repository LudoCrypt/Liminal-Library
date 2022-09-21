package net.ludocrypt.limlib.effects.sound.distortion;

import java.util.function.Function;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;

/**
 * A Distortion effect controls
 */
public abstract class DistortionEffect {

	@SuppressWarnings("unchecked")
	public static final SimpleRegistry<Codec<? extends DistortionEffect>> DISTORTION_EFFECT_CODEC = (SimpleRegistry<Codec<? extends DistortionEffect>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "limlib_distortion_effect")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Codec<DistortionEffect> CODEC = DISTORTION_EFFECT_CODEC.getCodec().dispatchStable(DistortionEffect::getCodec, Function.identity());

	public abstract Codec<? extends DistortionEffect> getCodec();

	/**
	 * Whether or not a Sound Event should be ignored
	 * 
	 * @param identifier the Identifier of the Sound Event
	 */
	public abstract boolean shouldIgnore(Identifier identifier);

	public abstract boolean isEnabled(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getEdge(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getGain(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getLowpassCutoff(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getEQCenter(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getEQBandWidth(MinecraftClient client, SoundInstance soundInstance);

}
