package net.ludocrypt.limlib.api.world;

import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.NoiseValuePoint;

public class SimpleMultiNoiseSampler implements MultiNoiseSampler {

	private final DoublePerlinNoiseSampler temperatureNoise, humidityNoise, continentalnessNoise, erosionNoise, depth, weirdnessNoise;

	public SimpleMultiNoiseSampler(DoublePerlinNoiseSampler temperatureNoise, DoublePerlinNoiseSampler humidityNoise, DoublePerlinNoiseSampler continentalnessNoise, DoublePerlinNoiseSampler erosionNoise, DoublePerlinNoiseSampler depth, DoublePerlinNoiseSampler weirdnessNoise) {
		this.temperatureNoise = temperatureNoise;
		this.humidityNoise = humidityNoise;
		this.continentalnessNoise = continentalnessNoise;
		this.erosionNoise = erosionNoise;
		this.depth = depth;
		this.weirdnessNoise = weirdnessNoise;
	}

	@Override
	public NoiseValuePoint sample(int x, int y, int z) {
		return MultiNoiseUtil.createNoiseValuePoint((float) this.temperatureNoise.sample(x, y, z), (float) this.humidityNoise.sample(x, y, z), (float) this.continentalnessNoise.sample(x, y, z), (float) this.erosionNoise.sample(x, y, z), (float) this.depth.sample(x, y, z), (float) this.weirdnessNoise.sample(x, y, z));
	}

}
