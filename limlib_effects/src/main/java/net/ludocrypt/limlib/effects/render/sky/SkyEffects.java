package net.ludocrypt.limlib.effects.render.sky;

import java.util.function.Function;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.effects.mixin.BuiltinRegistriesAccessor;
import net.ludocrypt.limlib.effects.mixin.RegistryAccessor;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.Holder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.dimension.DimensionType;

/**
 * A non-client-side clone of {@link SkyProperties}
 */
public abstract class SkyEffects {

	public static final RegistryKey<Registry<SkyEffects>> SKY_EFFECTS_KEY = RegistryAccessor.callCreateRegistryKey("limlib/sky_effects");
	public static final Registry<SkyEffects> SKY_EFFECTS = BuiltinRegistriesAccessor.callAddRegistry(SKY_EFFECTS_KEY, (registry) -> BuiltinRegistries.register(registry, RegistryKey.of(SKY_EFFECTS_KEY, new Identifier("limlib", "default")), new EmptySkyEffects()));

	@SuppressWarnings("unchecked")
	public static final SimpleRegistry<Codec<? extends SkyEffects>> SKY_EFFECTS_CODEC = (SimpleRegistry<Codec<? extends SkyEffects>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "limlib_sky_effects")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Codec<SkyEffects> CODEC = SKY_EFFECTS_CODEC.getCodec().dispatchStable(SkyEffects::getCodec, Function.identity());

	/**
	 * Temporary static variable to tell {@link SkyProperties} what registry holder
	 * the DimensionType came from. Do not use in production.
	 */
	@Nullable
	public static Holder<DimensionType> tempHolder = null;

	public abstract Codec<? extends SkyEffects> getCodec();

	@Environment(EnvType.CLIENT)
	public abstract SkyProperties getSkyProperties();

	public abstract float getSkyShading();

}
