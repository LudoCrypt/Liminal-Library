package net.ludocrypt.limlib.render.skybox;

import java.util.function.Function;

import com.mojang.serialization.Codec;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.render.mixin.registry.BuiltinRegistriesAccessor;
import net.ludocrypt.limlib.render.mixin.registry.RegistryAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public abstract class Skybox {

	public static final RegistryKey<Registry<Skybox>> SKYBOX_KEY = RegistryAccessor.callCreateRegistryKey("limlib/skybox");
	public static final Registry<Skybox> SKYBOX = BuiltinRegistriesAccessor.callAddRegistry(SKYBOX_KEY, (registry) -> BuiltinRegistries.register(registry, RegistryKey.of(SKYBOX_KEY, new Identifier("limlib", "default")), new EmptySkybox()));

	@SuppressWarnings("unchecked")
	public static final SimpleRegistry<Codec<? extends Skybox>> SKYBOX_CODEC = (SimpleRegistry<Codec<? extends Skybox>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "limlib_skybox")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Codec<Skybox> CODEC = SKYBOX_CODEC.getCodec().dispatchStable(Skybox::getCodec, Function.identity());

	@Environment(EnvType.CLIENT)
	public abstract void renderSky(WorldRenderer worldRenderer, MinecraftClient client, MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta);

	public abstract Codec<? extends Skybox> getCodec();

}
