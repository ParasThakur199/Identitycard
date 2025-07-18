package com.idcard.CommonData;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerate {
	private static final String LOWERCASE = "abcdefghijklmnpqrstuvwxyz";
	private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String DIGITS = "123456789";
	private static final String SPECIAL_CHARACTERS = "@";
	private static final SecureRandom RANDOM = new SecureRandom();

	public static String createPassword() {
		StringBuilder result = new StringBuilder();
		// 1 uppercase letter
		result.append(UPPERCASE.charAt(RANDOM.nextInt(UPPERCASE.length())));
		// 4 lowercase letters
		for (int i = 0; i < 4; i++) {
			result.append(LOWERCASE.charAt(RANDOM.nextInt(LOWERCASE.length())));
		}
		// 1 special character
		result.append(SPECIAL_CHARACTERS);
		// 2 digits
		for (int i = 0; i < 2; i++) {
			result.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
		}
		// Shuffle the characters
		List<Character> characters = new ArrayList<>();
		for (char c : result.toString().toCharArray()) {
			characters.add(c);
		}
		Collections.shuffle(characters);
		// Build the final string
		StringBuilder finalString = new StringBuilder();
		for (char c : characters) {
			finalString.append(c);
		}
		return finalString.toString();
	}
}
