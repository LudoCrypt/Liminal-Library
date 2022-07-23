package net.ludocrypt.limlib.mixin.client.render.model;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.ludocrypt.limlib.access.ModelAccess;
import net.minecraft.util.Identifier;

@Mixin(targets = { "net/minecraft/client/render/model/BasicBakedModel", "net/minecraft/client/render/model/BuiltinBakedModel", "net/minecraft/client/render/model/MultipartBakedModel", "net/minecraft/client/render/model/WeightedBakedModel", "net/fabricmc/fabric/api/renderer/v1/model/ForwardingBakedModel" })
public class GeneralBakedModelMixin implements ModelAccess {

	@Unique
	private Optional<Identifier> liminalQuadRenderer = Optional.empty();

	@Override
	public Optional<Identifier> getLiminalQuadRenderer() {
		return liminalQuadRenderer;
	}

	@Override
	public void setLiminalQuadRenderer(Identifier id) {
		liminalQuadRenderer = Optional.ofNullable(id);
	}

}
