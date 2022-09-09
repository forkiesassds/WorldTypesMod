package me.icanttellyou.mods.worldtypes.terrain.noise;

import net.minecraft.src.NoiseGenerator;

import java.util.Random;

public final class NoiseGeneratorOctavesIndev extends NoiseGenerator {
	private final NoiseGeneratorPerlinIndev[] generatorCollection;
	private final int octaves;

	public NoiseGeneratorOctavesIndev(int i1) {
		this(new Random(), i1);
	}

	private NoiseGeneratorOctavesIndev(Random random1, int i2) {
		this.octaves = i2;
		this.generatorCollection = new NoiseGeneratorPerlinIndev[i2];

		for(int i3 = 0; i3 < i2; ++i3) {
			this.generatorCollection[i3] = new NoiseGeneratorPerlinIndev(random1);
		}

	}

	public double generateNoise(double d1, double d3) {
		double d5 = 0.0D;
		double d7 = 1.0D;

		for(int i9 = 0; i9 < this.octaves; ++i9) {
			d5 += this.generatorCollection[i9].generateNoise(d1 / d7, d3 / d7) * d7;
			d7 *= 2.0D;
		}

		return d5;
	}

	public double generateNoise(double d1, double d3, double d5) {
		double d7 = 0.0D;
		double d9 = 1.0D;

		for(int i11 = 0; i11 < this.octaves; ++i11) {
			d7 += this.generatorCollection[i11].generateNoiseD(d1 / d9, 0.0D / d9, d5 / d9) * d9;
			d9 *= 2.0D;
		}

		return d7;
	}
}