package net.ludocrypt.limlib.render.access;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;

public interface BakedModelAccess {

	public List<Pair<Identifier, BakedModel>> getModels(@Nullable BlockState state);

	public void addModel(Identifier rendererId, @Nullable BlockState state, BakedModel model);

}
