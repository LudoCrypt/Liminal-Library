package net.ludocrypt.limlib.mixin.client.render.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.access.BakedModelAccess;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

@Mixin(targets = { "net/minecraft/client/render/model/BasicBakedModel", "net/minecraft/client/render/model/BuiltinBakedModel", "net/minecraft/client/render/model/MultipartBakedModel", "net/minecraft/client/render/model/WeightedBakedModel", "net/fabricmc/fabric/api/renderer/v1/model/ForwardingBakedModel" })
public class GeneralBakedModelMixin implements BakedModelAccess {

	@Unique
	private Map<Identifier, List<Pair<BakedQuad, Optional<Direction>>>> subQuads = Maps.newHashMap();

	@Override
	public Map<Identifier, List<Pair<BakedQuad, Optional<Direction>>>> getSubQuads() {
		return subQuads;
	}

}
