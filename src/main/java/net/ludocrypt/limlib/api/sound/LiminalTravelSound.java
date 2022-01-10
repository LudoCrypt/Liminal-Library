package net.ludocrypt.limlib.api.sound;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;

public abstract class LiminalTravelSound {

	public abstract SoundEvent getSound(ServerWorld from, ServerWorld to);

	public static class SimpleTravelSound extends LiminalTravelSound {

		private SoundEvent sound;

		public SimpleTravelSound(SoundEvent sound) {
			this.sound = sound;
		}

		@Override
		public SoundEvent getSound(ServerWorld from, ServerWorld to) {
			return this.sound;
		}

	}

}
