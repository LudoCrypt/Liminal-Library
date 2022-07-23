package net.ludocrypt.limlib.access;

import java.util.List;
import java.util.Map;

import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.util.Identifier;

public interface UnbakedModelAccess {

	public Map<Identifier, List<ModelElement>> getSubElements();

	public void putSubElements(Map<Identifier, List<ModelElement>> map);

	public JsonUnbakedModel getParent();

}
