package net.ludocrypt.limlib.access;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;

public interface ModelAccess {

	public Optional<Identifier> getLiminalQuadRenderer();

	public void setLiminalQuadRenderer(@Nullable Identifier id);

}
