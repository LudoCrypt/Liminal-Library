package net.ludocrypt.limlib.render.access;

import java.util.Map;

import net.ludocrypt.limlib.render.special.SpecialModelRenderer;
import net.minecraft.util.Identifier;

public interface UnbakedModelAccess {

	public Map<Identifier, SpecialModelRenderer> getSubModels();

}
