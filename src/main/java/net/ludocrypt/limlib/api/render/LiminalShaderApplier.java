package net.ludocrypt.limlib.api.render;

import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public abstract class LiminalShaderApplier {

	public static final Codec<LiminalShaderApplier> CODEC = LimlibRegistries.LIMINAL_SHADER_APPLIER.getCodec().dispatchStable(LiminalShaderApplier::getCodec, Function.identity());

	public abstract boolean shouldRender(MinecraftClient client, float tickdelta);

	public abstract ManagedShaderEffect getShader(MinecraftClient client, float tickdelta);

	public abstract Codec<? extends LiminalShaderApplier> getCodec();

	public static class SimpleShader extends LiminalShaderApplier {

		public static final Codec<SimpleShader> CODEC = RecordCodecBuilder.create((instance) -> {
			return instance.group(Identifier.CODEC.fieldOf("shader").stable().forGetter((shader) -> {
				return shader.getShaderId();
			})).apply(instance, instance.stable(SimpleShader::new));
		});

		private final ManagedShaderEffect shader;
		private final Identifier shaderId;

		public SimpleShader(Identifier shader) {
			this.shader = ShaderEffectManager.getInstance().manage(new Identifier(shader.getNamespace(), "shaders/post/" + shader.getPath() + ".json"));
			this.shaderId = shader;
		}

		@Override
		public boolean shouldRender(MinecraftClient client, float tickdelta) {
			return true;
		}

		@Override
		public ManagedShaderEffect getShader(MinecraftClient client, float tickdelta) {
			return shader;
		}

		public Identifier getShaderId() {
			return shaderId;
		}

		@Override
		public Codec<? extends LiminalShaderApplier> getCodec() {
			return SimpleShader.CODEC;
		}

	}

}
