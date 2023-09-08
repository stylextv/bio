package io.bio.seed;

import io.bio.seed.options.TemplateSeedStringOptions;

import java.util.Arrays;
import java.util.Random;

public class SeedStringSearch {
	
	public static final SeedStringSearch DEFAULT = new SeedStringSearch();
	
	private static final int START_SEARCH_LENGTH = 1;
	private static final int RANDOM_SEARCH_ITERATIONS = 10000;
	private static final int INVALID_STRING_INDEX = -1;
	
	private static final char EMPTY_SEED_STRING_SEED = 0;
	private static final char SEED_STRING_CHARACTER_WEIGHT_FACTOR = 31;
	private static final char LAST_SEED_STRING_CHARACTER_WEIGHT = 1;
	
	private static final Random RANDOM = new Random();
	
	private final SeedStringSearchOptions options;
	
	public SeedStringSearch() {
		this(SeedStringSearchOptions.SHORTEST);
	}
	
	public SeedStringSearch(SeedStringSearchOptions options) {
		this.options = options;
	}
	
	public String search(String targetSeedString) {
		int targetSeed = 0;
		int weight = LAST_SEED_STRING_CHARACTER_WEIGHT;
		char[] characters = targetSeedString.toCharArray();
		
		for(int i = characters.length - 1; i >= 0; i--) {
			char character = characters[i];

			targetSeed += character * weight;
			weight *= SEED_STRING_CHARACTER_WEIGHT_FACTOR;
		}
		
		return search(targetSeed);
	}
	
	public String search(int targetSeed) {
		int length = START_SEARCH_LENGTH - 1;
		boolean searched = false;
		
		while(true) {
			length++;
			
			if(!options.validStringLength(length)) {
				
				if(searched) return null;
				continue;
			}
			
			String string = search(targetSeed, length);
			searched = true;
			
			if(string != null) return string;
		}
	}
	
	private String search(int targetSeed, int length) {
		char[] characters = new char[length];
		int[] characterWeights = new int[length];
		
		int seed = EMPTY_SEED_STRING_SEED;
		int weight = LAST_SEED_STRING_CHARACTER_WEIGHT;
		
		for(int i = length - 1; i >= 0; i--) {
			
			char character = options.startStringCharacter(i);
			
			characters[i] = character;
			seed += character * weight;
			
			characterWeights[i] = weight;
			weight *= SEED_STRING_CHARACTER_WEIGHT_FACTOR;
		}
		
		if(seed != targetSeed) {
			
			SeedStringSearchStrategy strategy = options.getStrategy();
			switch(strategy) {
				case FORWARD -> {
					
					for(int i = 0; i < length; i++) {
						
						char character = characters[i];
						int characterWeight = characterWeights[i];
						
						char c = enhanceStringCharacter(character, characterWeight, seed, targetSeed);
						
						characters[i] = c;
						seed = seed - character * characterWeight + c * characterWeight;
						
						if(seed == targetSeed) break;
					}
					
				}
				case RANDOM -> {
					
					int iterations = 0;
					
					while(iterations < RANDOM_SEARCH_ITERATIONS) {
						
						int i = RANDOM.nextInt(length);
						
						char character = characters[i];
						int characterWeight = characterWeights[i];
						
						char c = enhanceStringCharacter(character, characterWeight, seed, targetSeed);
						
						characters[i] = c;
						seed = seed - character * characterWeight + c * characterWeight;
						
						if (seed == targetSeed) break;
						
						iterations++;
					}
					
				}
				case OPTIMAL -> {
					
					int index = INVALID_STRING_INDEX;
					
					while(true) {
						
						long distance = seedDistance(seed, targetSeed);
						int i = length - 1;
						
						while(distance >= SEED_STRING_CHARACTER_WEIGHT_FACTOR) {
							distance /= SEED_STRING_CHARACTER_WEIGHT_FACTOR;

							i--;
						}
						
						if(i <= index) i = index + 1;
						if(i < 0 || i >= length) break;
						index = i;
						
						char character = characters[i];
						int characterWeight = characterWeights[i];
						
						char c = enhanceStringCharacter(character, characterWeight, seed, targetSeed);
						
						characters[i] = c;
						seed = seed - character * characterWeight + c * characterWeight;
						System.out.println(i);
						System.out.println(Arrays.toString(characters));
						System.out.println(seed);
						
						if(seed == targetSeed) break;
					}
					
				}
			}
		}
		
		if(seed != targetSeed) return null;
		
		StringBuilder builder = new StringBuilder();
		for(char character : characters) builder.append(character);
		
		return builder.toString();
	}
	
	private char enhanceStringCharacter(char character, int characterWeight, int seed, int targetSeed) {
		long distance = seedDistance(seed, targetSeed);
		seed -= character * characterWeight;
		
		String characterSubstitutes = options.characterSubstitutes(character);
		for(char c : characterSubstitutes.toCharArray()) {
			
			seed += c * characterWeight;
			long d = seedDistance(seed, targetSeed);
			seed -= c * characterWeight;
			
			if(d < distance) {
				
				distance = d;
				character = c;
			}
		}
		
		return character;
	}
	
	public SeedStringSearchOptions getOptions() {
		return options;
	}
	
	private static long seedDistance(int seed1, int seed2) {
		long distance1 = Math.abs(seed1 - seed2);
		long distance2 = Math.abs((long) Math.min(seed1, seed2) - Integer.MIN_VALUE) + Math.abs((long) Math.max(seed1, seed2) - Integer.MAX_VALUE) + 1;
		
		return Math.min(distance1, distance2);
	}
	
	public static String searchSeedString(String targetSeedString, String seedStringTemplate) {
		SeedStringSearchOptions options = new TemplateSeedStringOptions(seedStringTemplate);
		SeedStringSearch search = new SeedStringSearch(options);
		
		return search.search(targetSeedString);
	}
	
	public static String searchSeedString(int targetSeed, String seedStringTemplate) {
		SeedStringSearchOptions options = new TemplateSeedStringOptions(seedStringTemplate);
		SeedStringSearch search = new SeedStringSearch(options);
		
		return search.search(targetSeed);
	}
	
	public static String searchSeedString(String targetSeedString) {
		return DEFAULT.search(targetSeedString);
	}
	
	public static String searchSeedString(int targetSeed) {
		return DEFAULT.search(targetSeed);
	}
	
}
