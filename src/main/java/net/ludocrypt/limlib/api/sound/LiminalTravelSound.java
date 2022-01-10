package net.ludocrypt.limlib.api.sound;

import java.util.Optional;

import org.apache.commons.lang3.mutable.Mutable;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public abstract class LiminalTravelSound {

	public abstract void hookSound(ServerWorld from, ServerWorld to, Mutable<Optional<SoundEvent>> mutable);

	public int priority() {
		return 1000;
	}

	public static class SimpleTravelSound extends LiminalTravelSound {

		private final RegistryKey<World> world;
		private final SoundEvent sound;

		public SimpleTravelSound(RegistryKey<World> world, SoundEvent sound) {
			this.world = world;
			this.sound = sound;
		}

		@Override
		public void hookSound(ServerWorld from, ServerWorld to, Mutable<Optional<SoundEvent>> mutable) {
			if (to.getRegistryKey().equals(world)) {
				mutable.setValue(Optional.of(sound));
			}
		}

	}

}
