package io.bio;

public class BioTest {
	
	public static void main(String[] args) {
		String seedString = "TotallyAwesomeButPrettyLongSeed";
		
		seedString = Bio.simplifySeedString(seedString);
		System.out.println(seedString); // prints 'MKCMEO'
	}
	
}
