package net.ludocrypt.limlib.mixin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.access.BakedModelAccess;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

@Mixin(BakedModel.class)
public interface BakedModelMixin extends BakedModelAccess {

	@Override
	default Map<Identifier, List<Pair<BakedQuad, Optional<Direction>>>> getSubQuads() {
		return Maps.newHashMap();
	}

}
