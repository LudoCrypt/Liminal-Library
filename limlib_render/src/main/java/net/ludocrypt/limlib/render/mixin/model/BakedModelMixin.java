package net.ludocrypt.limlib.render.mixin.model;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.render.access.BakedModelAccess;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;

@Mixin(BakedModel.class)
public interface BakedModelMixin extends BakedModelAccess {

	@Override
	default List<Pair<Identifier, BakedModel>> getModels(@Nullable BlockState state) {
		return Lists.newArrayList();
	}

	@Override
	default void addModel(Identifier rendererId, @Nullable BlockState state, BakedModel model) {
	}

}
