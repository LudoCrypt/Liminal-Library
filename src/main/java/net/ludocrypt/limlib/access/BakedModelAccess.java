package net.ludocrypt.limlib.access;

import java.util.Map;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;

public interface BakedModelAccess {

	public Map<Identifier, BakedModel> getSubModels();

}
