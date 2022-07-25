package net.ludocrypt.limlib.access;

import java.util.Map;

import net.minecraft.util.Identifier;

public interface UnbakedModelAccess {

	public Map<Identifier, Identifier> getSubModels();

}
