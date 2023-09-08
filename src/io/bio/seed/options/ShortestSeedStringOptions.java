package io.bio.seed.options;

import io.bio.seed.SeedStringSearchOptions;
import io.bio.seed.SeedStringSearchStrategy;

public class ShortestSeedStringOptions extends SeedStringSearchOptions {
	
	private static final String CHARACTER_SUBSTITUTES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
	private static final char START_STRING_CHARACTER = 'O';
	
	public ShortestSeedStringOptions() {
		super(SeedStringSearchStrategy.FORWARD);
	}
	
	@Override
	public String characterSubstitutes(char character) {
		return CHARACTER_SUBSTITUTES;
	}
	
	@Override
	public char startStringCharacter(int index) {
		return START_STRING_CHARACTER;
	}
	
	@Override
	public boolean validStringLength(int length) {
		return true;
	}
	
}
