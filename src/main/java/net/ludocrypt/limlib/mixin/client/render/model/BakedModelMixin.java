package net.ludocrypt.limlib.mixin.client.render.model;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;

import com.google.common.collect.Maps;

import net.ludocrypt.limlib.access.BakedModelAccess;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;

@Mixin(BakedModel.class)
public interface BakedModelMixin extends BakedModelAccess {

	@Override
	default Map<Identifier, BakedModel> getSubModels() {
		return Maps.newHashMap();
	}

}
