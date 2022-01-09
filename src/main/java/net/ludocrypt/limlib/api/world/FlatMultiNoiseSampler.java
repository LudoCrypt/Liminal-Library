package net.ludocrypt.limlib.api.world;

import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.NoiseValuePoint;

public class FlatMultiNoiseSampler implements MultiNoiseSampler {

	private final float temperatureNoise, humidityNoise, continentalnessNoise, erosionNoise, depth, weirdnessNoise;

	public FlatMultiNoiseSampler(float temperatureNoise, float humidityNoise, float continentalnessNoise, float erosionNoise, float depth, float weirdnessNoise) {
		this.temperatureNoise = temperatureNoise;
		this.humidityNoise = humidityNoise;
		this.continentalnessNoise = continentalnessNoise;
		this.erosionNoise = erosionNoise;
		this.depth = depth;
		this.weirdnessNoise = weirdnessNoise;
	}

	public FlatMultiNoiseSampler(float all) {
		this(all, all, all, all, all, all);
	}

	@Override
	public NoiseValuePoint sample(int x, int y, int z) {
		return MultiNoiseUtil.createNoiseValuePoint(temperatureNoise, humidityNoise, continentalnessNoise, erosionNoise, depth, weirdnessNoise);
	}

}
