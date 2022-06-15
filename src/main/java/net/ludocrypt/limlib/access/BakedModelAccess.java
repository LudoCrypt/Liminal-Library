package net.ludocrypt.limlib.access;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public interface BakedModelAccess {

	public Map<Identifier, List<Pair<BakedQuad, Optional<Direction>>>> getSubQuads();

}
