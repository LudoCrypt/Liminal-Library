package net.ludocrypt.limlib.mixin.client.render.model;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.access.BakedModelAccess;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;

@Mixin(targets = { "net/minecraft/client/render/model/BasicBakedModel", "net/minecraft/client/render/model/BuiltinBakedModel", "net/minecraft/client/render/model/WeightedBakedModel", "net/fabricmc/fabric/api/renderer/v1/model/ForwardingBakedModel" })
public class GeneralBakedModelMixin implements BakedModelAccess {

	private final Map<BlockState, List<Pair<Identifier, BakedModel>>> modelsMap = Maps.newHashMap();
	private List<Pair<Identifier, BakedModel>> defaultModels = Lists.newArrayList();

	@Override
	public List<Pair<Identifier, BakedModel>> getModels(@Nullable BlockState state) {
		return modelsMap.getOrDefault(state, defaultModels);
	}

	@Override
	public void addModel(Identifier rendererId, @Nullable BlockState state, BakedModel model) {
		if (state == null) {
			defaultModels.add(Pair.of(rendererId, model));
		} else {
			List<Pair<Identifier, BakedModel>> list = modelsMap.get(state);
			if (list == null) {
				list = Lists.newArrayList();
				modelsMap.put(state, list);
			}

			list.add(Pair.of(rendererId, model));
		}
	}

}
