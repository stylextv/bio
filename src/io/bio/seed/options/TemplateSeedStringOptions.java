package io.bio.seed.options;

import io.bio.seed.SeedStringSearchOptions;
import io.bio.seed.SeedStringSearchStrategy;

public class TemplateSeedStringOptions extends SeedStringSearchOptions {
	
	private final String seedStringTemplate;
	
	public TemplateSeedStringOptions(String seedStringTemplate) {
		super(SeedStringSearchStrategy.FORWARD);
		
		this.seedStringTemplate = seedStringTemplate;
	}
	
	@Override
	public String characterSubstitutes(char character) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(Character.toLowerCase(character));
		builder.append(Character.toUpperCase(character));
		
		return builder.toString();
	}
	
	@Override
	public char startStringCharacter(int index) {
		return seedStringTemplate.charAt(index);
	}
	
	@Override
	public boolean validStringLength(int length) {
		return length == seedStringTemplate.length();
	}
	
	public String getSeedStringTemplate() {
		return seedStringTemplate;
	}
	
}
