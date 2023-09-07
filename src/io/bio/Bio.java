package io.bio;

public class Bio {
	
	private static final char SMALLEST_HASH_STRING_CHARACTER_ID = 32;
	private static final char LARGEST_HASH_STRING_CHARACTER_ID = 126;
	
	private static final char DEFAULT_HASH_STRING_CHARACTER = 'O';
	private static final char EMPTY_STRING_HASH = 0;
	private static final char STRING_HASH_CHARACTER_WEIGHT_FACTOR = 31;
	private static final char LAST_STRING_HASH_CHARACTER_WEIGHT = 1;
	private static final char MINIMAL_REVERSE_HASH_STRING_LENGTH = 1;
	
	public static String reverseHashString(int hash, String hashStringPrefix) {
		int h = hashString(hashStringPrefix);
		int length = MINIMAL_REVERSE_HASH_STRING_LENGTH;
		
		while(true) {
			
			int h2 = h;
			for(int i = 0; i < length; i++) h2 *= STRING_HASH_CHARACTER_WEIGHT_FACTOR;
			h2 = hash - h2;
			
			String string = reverseHashString(h2, length);
			if(string != null) return hashStringPrefix + string;
			
			length++;
		}
	}
	
	public static String reverseHashString(int hash) {
		int length = MINIMAL_REVERSE_HASH_STRING_LENGTH;
		
		while(true) {
			
			String string = reverseHashString(hash, length);
			if(string != null) return string;
			
			length++;
		}
	}
	
	private static String reverseHashString(int hash, int length) {
		char[] characters = new char[length];
		int[] characterWeights = new int[length];
		
		int h = EMPTY_STRING_HASH;
		int weight = LAST_STRING_HASH_CHARACTER_WEIGHT;
		
		for(int characterIndex = length - 1; characterIndex >= 0; characterIndex--) {
			
			characters[characterIndex] = DEFAULT_HASH_STRING_CHARACTER;
			h += DEFAULT_HASH_STRING_CHARACTER * weight;
			
			characterWeights[characterIndex] = weight;
			weight *= STRING_HASH_CHARACTER_WEIGHT_FACTOR;
		}
		
		boolean hashMatches = h == hash;
		if(!hashMatches) {
			
			for(int characterIndex = 0; characterIndex < length; characterIndex++) {
				
				char c = characters[characterIndex];
				weight = characterWeights[characterIndex];
				
				char character = enhanceHashStringCharacter(c, weight, h, hash);
				int enhancedHash = h - c * weight + character * weight;
				
				h = enhancedHash;
				characters[characterIndex] = character;
				
				if(h == hash) {
					
					hashMatches = true;
					break;
				}
			}
		}
		
		if(!hashMatches) return null; 
		
		StringBuilder builder = new StringBuilder();
		for(char character : characters) builder.append(character);
		
		return builder.toString();
	}
	
	private static char enhanceHashStringCharacter(char character, int characterWeight, int hash, int targetHash) {
		int characterID = (targetHash - (hash - character * characterWeight)) / characterWeight;
		if(characterID >= SMALLEST_HASH_STRING_CHARACTER_ID && characterID <= LARGEST_HASH_STRING_CHARACTER_ID) {
			
			int h = hash - character * characterWeight + characterID * characterWeight;
			if(h == targetHash) return (char) characterID;
		}
		
		hash -= character * characterWeight;
		int bestCharacterID = character;
		int bestHashDistance = Integer.MAX_VALUE;
		
		for(int id = SMALLEST_HASH_STRING_CHARACTER_ID; id <= LARGEST_HASH_STRING_CHARACTER_ID; id++) {
			
			hash += id * characterWeight;
			int hashDistance = Math.abs(hash - targetHash);
			hash -= id * characterWeight;
			
			if(hashDistance < bestHashDistance) {
				
				bestCharacterID = id;
				bestHashDistance = hashDistance;
			}
		}
		
		return (char) bestCharacterID;
	}
	
	public static int hashString(String string) {
		int hash = 0;
		int weight = 1;
		char[] characters = string.toCharArray();

		for(int i = characters.length - 1; i >= 0; i--) {
			char character = characters[i];

			hash += character * weight;
			weight *= 31;
		}

		return hash;
	}
	
}
