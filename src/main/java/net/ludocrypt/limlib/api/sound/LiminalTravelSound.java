package net.ludocrypt.limlib.api.sound;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.apache.commons.lang3.mutable.Mutable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.ludocrypt.limlib.access.DimensionTypeAccess;
import net.ludocrypt.limlib.impl.world.LiminalSoundRegistry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public abstract class LiminalTravelSound {

	public static final Codec<LiminalTravelSound> CODEC = LiminalSoundRegistry.LIMINAL_TRAVEL_SOUND.getCodec().dispatchStable(LiminalTravelSound::getCodec, Function.identity());

	public abstract void hookSound(ServerWorld from, ServerWorld to, Mutable<Optional<SoundEvent>> mutable);

	public abstract Codec<? extends LiminalTravelSound> getCodec();

	public int priority() {
		return 1000;
	}

	public static class SimpleTravelSound extends LiminalTravelSound {

		public static final Codec<SimpleTravelSound> CODEC = RecordCodecBuilder.create((instance) -> {
			return instance.group(RegistryKey.createCodec(Registry.WORLD_KEY).fieldOf("world").stable().forGetter((travelSound) -> {
				return travelSound.world;
			}), SoundEvent.CODEC.fieldOf("sound").stable().forGetter((travelSound) -> {
				return travelSound.sound;
			})).apply(instance, instance.stable(SimpleTravelSound::new));
		});

		private final RegistryKey<World> world;
		private final SoundEvent sound;

		public SimpleTravelSound(RegistryKey<World> world, SoundEvent sound) {
			this.world = world;
			this.sound = sound;
		}

		@Override
		public void hookSound(ServerWorld from, ServerWorld to, Mutable<Optional<SoundEvent>> mutable) {
			if (from.getRegistryKey().equals(world) || to.getRegistryKey().equals(world)) {
				mutable.setValue(Optional.of(sound));
			}
		}

		@Override
		public Codec<? extends LiminalTravelSound> getCodec() {
			return CODEC;
		}

	}

	public static class RegexTravelSound extends LiminalTravelSound {

		private List<TravelRule> toTravelRules;
		private List<TravelRule> fromTravelRules;

		public static final Codec<RegexTravelSound> CODEC = RecordCodecBuilder.create((instance) -> {
			return instance.group(Codec.list(TravelRule.CODEC).fieldOf("to_rules").stable().forGetter((travelSound) -> {
				return travelSound.getToTravelRules();
			}), Codec.list(TravelRule.CODEC).fieldOf("from_rules").stable().forGetter((travelSound) -> {
				return travelSound.getFromTravelRules();
			})).apply(instance, instance.stable(RegexTravelSound::new));
		});

		public RegexTravelSound(List<TravelRule> toTravelRules, List<TravelRule> fromTravelRules) {
			this.toTravelRules = toTravelRules;
			this.fromTravelRules = fromTravelRules;
		}

		public List<TravelRule> getToTravelRules() {
			return toTravelRules;
		}

		public List<TravelRule> getFromTravelRules() {
			return fromTravelRules;
		}

		@Override
		public void hookSound(ServerWorld from, ServerWorld to, Mutable<Optional<SoundEvent>> mutable) {

			SoundEvent sound = null;
			Optional<LiminalTravelSound> toTravelSound = ((DimensionTypeAccess) to.getDimension()).getLiminalEffects().getTravel();
			Optional<LiminalTravelSound> fromTravelSound = ((DimensionTypeAccess) from.getDimension()).getLiminalEffects().getTravel();

			int prevPriority = 1000;
			if (fromTravelSound.isPresent()) {
				if (fromTravelSound.get()instanceof RegexTravelSound travel) {
					for (TravelRule rule : travel.getFromTravelRules()) {
						if (rule.getPriority() <= prevPriority && Pattern.matches(rule.getRegex(), from.getRegistryKey().getValue().toString())) {
							sound = rule.getResult();
							prevPriority = rule.getPriority();
						}
					}
				}
			}

			if (toTravelSound.isPresent()) {
				if (toTravelSound.get()instanceof RegexTravelSound travel) {
					for (TravelRule rule : travel.getToTravelRules()) {
						if (rule.getPriority() <= prevPriority && Pattern.matches(rule.getRegex(), to.getRegistryKey().getValue().toString())) {
							sound = rule.getResult();
							prevPriority = rule.getPriority();
						}
					}
				}
			}

			if (sound != null) {
				mutable.setValue(Optional.of(sound));
			}

		}

		@Override
		public Codec<? extends LiminalTravelSound> getCodec() {
			return CODEC;
		}

		public static class TravelRule {

			public static final Codec<TravelRule> CODEC = RecordCodecBuilder.create((instance) -> {
				return instance.group(Codec.STRING.fieldOf("regex").stable().forGetter((travelRule) -> {
					return travelRule.regex;
				}), SoundEvent.CODEC.fieldOf("result").stable().forGetter((travelRule) -> {
					return travelRule.result;
				}), Codec.INT.optionalFieldOf("priority", 1000).stable().forGetter((travelRule) -> {
					return travelRule.priority;
				})).apply(instance, instance.stable(TravelRule::new));
			});

			private String regex;
			private SoundEvent result;
			private int priority;

			public TravelRule(String regex, SoundEvent result, int priority) {
				this.regex = regex;
				this.result = result;
				this.priority = priority;
			}

			public String getRegex() {
				return regex;
			}

			public SoundEvent getResult() {
				return result;
			}

			public int getPriority() {
				return priority;
			}

		}

	}

}
