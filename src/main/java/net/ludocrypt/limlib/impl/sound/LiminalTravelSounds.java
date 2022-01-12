package net.ludocrypt.limlib.impl.sound;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableObject;

import com.google.common.collect.Lists;

import net.ludocrypt.limlib.api.sound.LiminalTravelSound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class LiminalTravelSounds {

	public static final HashMap<Identifier, LiminalTravelSound> TRAVEL_SOUND_REGISTRY = new HashMap<Identifier, LiminalTravelSound>();

	public static LiminalTravelSound register(Identifier id, LiminalTravelSound travelSound) {
		return TRAVEL_SOUND_REGISTRY.put(id, travelSound);
	}

	public static Optional<SoundEvent> getCurrent(ServerWorld from, ServerWorld to) {
		MutableObject<Optional<SoundEvent>> mutableSound = new MutableObject<Optional<SoundEvent>>(Optional.of(SoundEvents.BLOCK_PORTAL_TRAVEL));
		List<LiminalTravelSound> list = Lists.newArrayList(TRAVEL_SOUND_REGISTRY.values());
		Collections.sort(list, (a, b) -> Integer.compare(a.priority(), b.priority()));
		for (LiminalTravelSound sound : list) {
			sound.hookSound(from, to, mutableSound);
		}
		return mutableSound.getValue();
	}

}
