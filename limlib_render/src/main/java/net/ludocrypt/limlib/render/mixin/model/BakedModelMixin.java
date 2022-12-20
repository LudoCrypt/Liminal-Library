package net.ludocrypt.limlib.render.mixin.model;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.render.access.BakedModelAccess;
import net.ludocrypt.limlib.render.special.SpecialModelRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;

@Mixin(BakedModel.class)
public interface BakedModelMixin extends BakedModelAccess {

	@Override
	default List<Pair<SpecialModelRenderer, BakedModel>> getModels(@Nullable BlockState state) {
		return Lists.newArrayList();
	}

	@Override
	default void addModel(SpecialModelRenderer modelRenderer, @Nullable BlockState state, BakedModel model) {
	}

}
