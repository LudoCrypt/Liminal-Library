package net.ludocrypt.limlib.effects.render.sky;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * A Sky effects controller
 * <p>
 * This is a completely empty, default setting version of
 * {@link StaticSkyEffects}
 */
public class EmptySkyEffects extends StaticSkyEffects {

	public static final Codec<EmptySkyEffects> CODEC = RecordCodecBuilder.create((instance) -> instance.stable(new EmptySkyEffects()));

	public EmptySkyEffects() {
		super(Optional.empty(), false, "NONE", false, false, false, 1.0F);
	}

	@Override
	public Codec<? extends SkyEffects> getCodec() {
		return CODEC;
	}

}
