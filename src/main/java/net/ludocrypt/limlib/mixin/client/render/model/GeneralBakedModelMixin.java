package net.ludocrypt.limlib.mixin.client.render.model;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.google.common.collect.Maps;

import net.ludocrypt.limlib.access.BakedModelAccess;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;

@Mixin(targets = { "net/minecraft/client/render/model/BasicBakedModel", "net/minecraft/client/render/model/BuiltinBakedModel", "net/minecraft/client/render/model/MultipartBakedModel", "net/minecraft/client/render/model/WeightedBakedModel", "net/fabricmc/fabric/api/renderer/v1/model/ForwardingBakedModel" })
public class GeneralBakedModelMixin implements BakedModelAccess {

	@Unique
	private Map<Identifier, BakedModel> subQuads = Maps.newHashMap();

	@Override
	public Map<Identifier, BakedModel> getSubModels() {
		return subQuads;
	}

}
