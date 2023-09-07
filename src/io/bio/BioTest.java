package io.bio;

public class BioTest {
	
	public static void main(String[] args) {
		int hash = 69420;
		
		String s = Bio.reverseHashString(hash);
		System.out.println(s);                 // prints 'FCI' 
		System.out.println(Bio.hashString(s)); // prints '69420', as expected
	}
	
}
