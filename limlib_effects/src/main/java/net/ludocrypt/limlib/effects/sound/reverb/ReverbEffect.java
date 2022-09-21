package net.ludocrypt.limlib.effects.sound.reverb;

import java.util.function.Function;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;

/**
 * A Reverb effect controls
 */
public abstract class ReverbEffect {

	@SuppressWarnings("unchecked")
	public static final SimpleRegistry<Codec<? extends ReverbEffect>> REVERB_EFFECT_CODEC = (SimpleRegistry<Codec<? extends ReverbEffect>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "limlib_reverb_effect")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Codec<ReverbEffect> CODEC = REVERB_EFFECT_CODEC.getCodec().dispatchStable(ReverbEffect::getCodec, Function.identity());

	public abstract Codec<? extends ReverbEffect> getCodec();

	/**
	 * Whether or not a Sound Event should be ignored
	 * 
	 * @param identifier the Identifier of the Sound Event
	 */
	public abstract boolean shouldIgnore(Identifier identifier);

	public abstract boolean isEnabled(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getAirAbsorptionGainHF(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getDecayHFRatio(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getDensity(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getDiffusion(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getGain(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getGainHF(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getLateReverbGainBase(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getDecayTime(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getReflectionsGainBase(MinecraftClient client, SoundInstance soundInstance);

	public abstract int getDecayHFLimit(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getLateReverbDelay(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getReflectionsDelay(MinecraftClient client, SoundInstance soundInstance);

}
