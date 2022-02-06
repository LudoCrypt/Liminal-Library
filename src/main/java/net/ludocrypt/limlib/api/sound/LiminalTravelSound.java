package net.ludocrypt.limlib.api.sound;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.sound.SoundEvent;

public class LiminalTravelSound {

	private List<TravelRule> toTravelRules;
	private List<TravelRule> fromTravelRules;

	public static final Codec<LiminalTravelSound> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.list(TravelRule.CODEC).fieldOf("to_rules").stable().forGetter((travelSound) -> {
			return travelSound.getToTravelRules();
		}), Codec.list(TravelRule.CODEC).fieldOf("from_rules").stable().forGetter((travelSound) -> {
			return travelSound.getFromTravelRules();
		})).apply(instance, instance.stable(LiminalTravelSound::new));
	});

	public LiminalTravelSound(List<TravelRule> toTravelRules, List<TravelRule> fromTravelRules) {
		this.toTravelRules = toTravelRules;
		this.fromTravelRules = fromTravelRules;
	}

	public List<TravelRule> getToTravelRules() {
		return toTravelRules;
	}

	public List<TravelRule> getFromTravelRules() {
		return fromTravelRules;
	}

	public static class TravelRule {

		public static final Codec<TravelRule> CODEC = RecordCodecBuilder.create((instance) -> {
			return instance.group(Codec.STRING.fieldOf("regex").stable().forGetter((travelRule) -> {
				return travelRule.getRegex();
			}), SoundEvent.CODEC.fieldOf("result").stable().forGetter((travelRule) -> {
				return travelRule.getResult();
			})).apply(instance, instance.stable(TravelRule::new));
		});

		private String regex;
		private SoundEvent result;

		public TravelRule(String regex, SoundEvent result) {
			this.regex = regex;
			this.result = result;
		}

		public String getRegex() {
			return regex;
		}

		public SoundEvent getResult() {
			return result;
		}

	}

}
