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

public abstract class LiminalShaderApplier {

	public static final Codec<LiminalShaderApplier> CODEC = LimlibRegistries.LIMINAL_SHADER_APPLIER.getCodec().dispatchStable(LiminalShaderApplier::getCodec, Function.identity());

	@Environment(EnvType.CLIENT)
	public abstract boolean shouldRender(MinecraftClient client, float tickdelta);

	@Environment(EnvType.CLIENT)
	public abstract ManagedShaderEffect getShader(MinecraftClient client, float tickdelta);

	public abstract Codec<? extends LiminalShaderApplier> getCodec();

	public static class SimpleShader extends LiminalShaderApplier {

		public static final Codec<SimpleShader> CODEC = RecordCodecBuilder.create((instance) -> {
			return instance.group(Identifier.CODEC.fieldOf("shader").stable().forGetter((shader) -> {
				return shader.getShaderId();
			})).apply(instance, instance.stable(SimpleShader::new));
		});

		private final Identifier shaderId;

		@Environment(EnvType.CLIENT)
		private ManagedShaderEffect managedShader = null;

		public SimpleShader(Identifier shader) {
			this.shaderId = shader;
		}

		@Override
		@Environment(EnvType.CLIENT)
		public boolean shouldRender(MinecraftClient client, float tickdelta) {
			return true;
		}

		@Override
		@Environment(EnvType.CLIENT)
		public ManagedShaderEffect getShader(MinecraftClient client, float tickdelta) {
			if (this.managedShader == null) {
				this.managedShader = ShaderEffectManager.getInstance().manage(new Identifier(shaderId.getNamespace(), "shaders/post/" + shaderId.getPath() + ".json"));
			}

			return this.managedShader;
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
