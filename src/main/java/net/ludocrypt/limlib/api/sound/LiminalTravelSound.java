package net.ludocrypt.limlib.api.sound;

import java.util.Optional;

import org.apache.commons.lang3.mutable.Mutable;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;

public abstract class LiminalTravelSound {

	public abstract void hookSound(ServerWorld from, ServerWorld to, Mutable<Optional<SoundEvent>> mutable);

	public int priority() {
		return 1000;
	}

	public static class SimpleTravelSound extends LiminalTravelSound {

		private SoundEvent sound;

		public SimpleTravelSound(SoundEvent sound) {
			this.sound = sound;
		}

		@Override
		public void hookSound(ServerWorld from, ServerWorld to, Mutable<Optional<SoundEvent>> mutable) {
			mutable.setValue(Optional.of(sound));
		}

	}

}
