package io.bio.seed;

import io.bio.seed.options.ShortestSeedStringOptions;

public abstract class SeedStringSearchOptions {
	
	public static final SeedStringSearchOptions SHORTEST = new ShortestSeedStringOptions();
	
	private final SeedStringSearchStrategy strategy;
	
	protected SeedStringSearchOptions(SeedStringSearchStrategy strategy) {
		this.strategy = strategy;
	}
	
	public abstract String characterSubstitutes(char character);
	public abstract char startStringCharacter(int index);
	public abstract boolean validStringLength(int length);
	
	public SeedStringSearchStrategy getStrategy() {
		return strategy;
	}
	
}
