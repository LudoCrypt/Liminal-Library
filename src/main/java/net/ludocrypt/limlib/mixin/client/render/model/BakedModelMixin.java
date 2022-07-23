package net.ludocrypt.limlib.mixin.client.render.model;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import net.ludocrypt.limlib.access.ModelAccess;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;

@Mixin(BakedModel.class)
public interface BakedModelMixin extends ModelAccess {

	@Override
	default Optional<Identifier> getLiminalQuadRenderer() {
		return Optional.empty();
	}

	@Override
	default void setLiminalQuadRenderer(@Nullable Identifier id) {
	}

}
