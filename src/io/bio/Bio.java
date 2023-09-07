package io.bio;

public class Bio {
	
	private static final char SMALLEST_SEED_STRING_CHARACTER_ID = 32;
	private static final char LARGEST_SEED_STRING_CHARACTER_ID = 126;
	
	private static final char EMPTY_STRING_SEED = 0;
	private static final char MINIMAL_SEED_STRING_LENGTH = 1;
	private static final char DEFAULT_SEED_STRING_CHARACTER = 'O';
	private static final char SEED_STRING_CHARACTER_WEIGHT_FACTOR = 31;
	private static final char LAST_SEED_STRING_CHARACTER_WEIGHT = 1;
	
	public static String simplifySeedString(String seedString) {
		int seed = seed(seedString);
		return seedString(seed);
	}
	
	public static String seedString(int seed, String seedStringPrefix) {
		int s = seed(seedStringPrefix);
		int length = MINIMAL_SEED_STRING_LENGTH;
		
		while(true) {
			
			s *= SEED_STRING_CHARACTER_WEIGHT_FACTOR;
			
			String seedString = seedString(seed - s, length);
			if(seedString != null) return seedStringPrefix + seedString;
			
			length++;
		}
	}
	
	public static String seedString(int seed) {
		int length = MINIMAL_SEED_STRING_LENGTH;
		
		while(true) {
			
			String seedString = seedString(seed, length);
			if(seedString != null) return seedString;
			
			length++;
		}
	}
	
	private static String seedString(int seed, int length) {
		char[] characters = new char[length];
		int[] characterWeights = new int[length];
		
		int s = EMPTY_STRING_SEED;
		int weight = LAST_SEED_STRING_CHARACTER_WEIGHT;
		
		for(int characterIndex = length - 1; characterIndex >= 0; characterIndex--) {
			
			characters[characterIndex] = DEFAULT_SEED_STRING_CHARACTER;
			s += DEFAULT_SEED_STRING_CHARACTER * weight;
			
			characterWeights[characterIndex] = weight;
			weight *= SEED_STRING_CHARACTER_WEIGHT_FACTOR;
		}
		
		boolean seedMatches = s == seed;
		if(!seedMatches) {
			
			for(int characterIndex = 0; characterIndex < length; characterIndex++) {
				
				char c = characters[characterIndex];
				weight = characterWeights[characterIndex];
				
				char character = enhanceSeedStringCharacter(c, weight, s, seed);
				int enhancedSeed = s - c * weight + character * weight;
				
				s = enhancedSeed;
				characters[characterIndex] = character;
				
				if(s == seed) {

					seedMatches = true;
					break;
				}
			}
		}
		
		if(!seedMatches) return null; 
		
		StringBuilder builder = new StringBuilder();
		for(char character : characters) builder.append(character);
		
		return builder.toString();
	}
	
	private static char enhanceSeedStringCharacter(char character, int characterWeight, int seed, int targetSeed) {
		int characterID = (targetSeed - (seed - character * characterWeight)) / characterWeight;
		if(characterID >= SMALLEST_SEED_STRING_CHARACTER_ID && characterID <= LARGEST_SEED_STRING_CHARACTER_ID) {
			
			int s = seed - character * characterWeight + characterID * characterWeight;
			if(s == targetSeed) return (char) characterID;
		}
		
		seed -= character * characterWeight;
		int bestCharacterID = character;
		long bestSeedDistance = Integer.MAX_VALUE;
		
		for(int id = SMALLEST_SEED_STRING_CHARACTER_ID; id <= LARGEST_SEED_STRING_CHARACTER_ID; id++) {
			
			seed += id * characterWeight;
			long seedDistance = seedDistance(seed, targetSeed);
			seed -= id * characterWeight;
			
			if(seedDistance < bestSeedDistance) {
				
				bestCharacterID = id;
				bestSeedDistance = seedDistance;
			}
		}
		
		return (char) bestCharacterID;
	}
	
	private static long seedDistance(int seed1, int seed2) {
		long distance1 = Math.abs(seed1 - seed2);
		long distance2 = Math.abs((long) Math.min(seed1, seed2) - Integer.MIN_VALUE) + Math.abs((long) Math.max(seed1, seed2) - Integer.MAX_VALUE) + 1;
		
		return Math.min(distance1, distance2);
	}
	
	public static int seed(String seedString) {
		int seed = 0;
		int weight = LAST_SEED_STRING_CHARACTER_WEIGHT;
		char[] characters = seedString.toCharArray();
		
		for(int i = characters.length - 1; i >= 0; i--) {
			char character = characters[i];

			seed += character * weight;
			weight *= SEED_STRING_CHARACTER_WEIGHT_FACTOR;
		}
		
		return seed;
	}
	
}
