import java.util.*;
public class Enigma {
	
	//II III IV L O B MN BV CX AS LK JH GF ZD RE PO HELLOWORLD
	// Rotors	Shifts		10 Plugboard Values		Cipher
	
	//GLOBAL VARIABLES
	private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String rotorI = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
	private static final String rotorII = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
	private static final String rotorIII = "BDFHJLCPRTXVZNYEIWGAKMUSQO";
	private static final String rotorIV = "ESOVPZJAYQUIRHXLNFTGKDCMWB";
	private static final String rotorV = "VZBRGITYUPSDNHLXAWMJQOFECK";
	private static final String reflector = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
	
	// MAIN METHOD
	public static void main(String[] args) {
		System.out.print("Welcome to Enigma!\n: ");
		Scanner console = new Scanner(System.in);
		String phrase = console.nextLine();
		while(!phrase.equalsIgnoreCase("quit")) {
			Scanner userInput = new Scanner(phrase);
			String type = userInput.next();
			String input = userInput.nextLine();
			input = input.substring(1);
			//Identifying a basic Caesar Cipher
			if ((type.equals(">C") || type.equals("<C")) && type.length() == 2) {
				caesarCipher(input, type);
			}
			//Identifying an Advanced Caesar Cipher
			else if ((type.contains(">C") || type.contains("<C")) && type.length() > 2) {
				int shift = Integer.parseInt(type.substring(type.indexOf("C") + 1));
				System.out.print(advCaesarCipher(input, type, shift));
			}
			//Identifying an Affine Cipher
			else if (type.contains(">A") || type.contains("<A")) {
				System.out.print(affineCipher(input, type, rotorI));
			}
			//Identifying a Rotor Cipher
			else if (type.contains(">R") || type.contains("<R")) {
				rotorCipher(input, type);
			}
			// Enigma!!!
			else {
				decipher(phrase);
			}
			System.out.print("\n: ");
			phrase = console.nextLine();
		}
	}
	
	//HELPER METHODS
	
	//CHECKPOINTS 1 - 4
	//Basic Caesar Cipher
	public static void caesarCipher(String input, String crypt) {
		String result = "";
		//Encrypting the input
		if (crypt.equals(">C")) {
			for (int i = 0; i < input.length(); i++) {
				if(input.charAt(i) == ' ') {
					result += " ";
				}else if ( input.charAt(i) != 'Z') {
					result += (char)(input.charAt(i) + 1);
				}else {
					result += "A";
				}
			}
			System.out.print(result);
		}
		//Decrypting the input
		else {
			for (int i = 0; i < input.length(); i++) {
				if(input.charAt(i) == ' ') {
					result += " ";
				}else if ( input.charAt(i) != 'A') {
					result += (char)(input.charAt(i) - 1);
				}else {
					result += "Z";
				}
			}
			System.out.print(result);
		}
	}
	//Advanced Caesar Cipher
	public static String advCaesarCipher (String input, String crypt, int shift) {
		crypt = crypt.substring(0, crypt.indexOf("C") + 1);
		String result = "";
		//Encrypting the input
		if (crypt.equals(">C")) {
			for (int i = 0; i < input.length(); i++) {
				if(input.charAt(i) == ' ') {
					result += " ";
				}else if ((input.charAt(i) + shift) <= 'Z') {
					result += (char)(input.charAt(i) + shift);
				}else {
					int startshift = (input.charAt(i) + shift) - ((int)'Z');
					result += (char)(('A' - 1) + startshift);
				}
			}
		}
		//Decrypting the input
		else {
			for (int i = 0; i < input.length(); i++) {
				if(input.charAt(i) == ' ') {
					result += " ";
				}else if ((input.charAt(i)  - shift) >= 'A') {
					result += (char)(input.charAt(i) - shift);
				}else {
					int endshift = (int)'A' - (input.charAt(i) - shift);
					result += (char)(((int)'Z' + 1) - endshift);
				}
			}
		}
		return result;
	}
	//Affine Cipher
	public static String affineCipher (String input, String crypt, String cipher) {
		String result = "";
		if (crypt.equals(">A")) {
			for (int i = 0; i < input.length(); i++) {
				if (input.charAt(i) == ' ') {
					result += " ";
					
				}
				else {
					int index = alphabet.indexOf(input.charAt(i));
					result += cipher.charAt(index) + "";
				}
			}
		}else {
			for (int i = 0; i < input.length(); i++) {
				if (input.charAt(i) == ' ') {
					result += " ";
					
				}
				else {
					int index = cipher.indexOf(input.charAt(i));
					result += alphabet.charAt(index) + "";
				}
			}
		}
		return result;
	}
	//Rotor cipher
	public static void rotorCipher(String input, String crypt) {
		String result = " ";
		if (crypt.equals(">R")){
			result = affineCipher(input, ">A", rotorI);
			result = affineCipher(result, ">A", rotorII);
			result = affineCipher(result, ">A", rotorIII);
		}else {
			result = affineCipher(input, "<A", rotorIII);
			result = affineCipher(result, "<A", rotorII);
			result = affineCipher(result, "<A", rotorI);
		}
		System.out.print(result);
	}
	
	//CHECKPOINTS 5 - 6
	// ENCRYPTING/DECRYPTING THE INPUT
	public static void decipher(String input) {
		String[] rotors = new String[3];
		int[] shifts = new int[3];
		String[] plugboard = new String[10];
		String newInput = sepInput(input, rotors, shifts, plugboard);
		String finalInput = "";
		for (int i = 0; i < newInput.length(); i++) {
			String temp = "";
			temp = plugSwitch(plugboard, newInput.charAt(i) + "");
			temp = rotorFrontOrder(temp, shifts, rotors);
			temp = reflect(temp);
			temp = rotorReverseOrder(temp, shifts, rotors);
			temp = plugSwitch(plugboard, temp);
			finalInput += temp;
			shifts[2]++;
			if (shifts[2] == 27) {
				shifts[2] = 0;
				shifts[1]++;
			}if (shifts[1] == 27) {
				shifts[1] = 0;
				shifts[0]++;
			}if (shifts[0] == 27) {
				shifts[0] = 0;
				shifts[1] = 0;
				shifts[2] = 0;
			}
		}
		System.out.print(finalInput);
	}
	
	//Going through the rotors
	//Front order of rotors - right to left
	public static String rotorFrontOrder(String input, int[] rotorshift, String[] rotor) {
		String result = "";
		//ROTOR 3
		result = advCaesarCipher(input, ">C", rotorshift[2]);
		result = affineCipher(result, ">A", findRotor(rotor[2]));
		result = advCaesarCipher(result, "<C", rotorshift[2]);
		//ROTOR 2
		result = advCaesarCipher(result, ">C", rotorshift[1]);
		result = affineCipher(result, ">A", findRotor(rotor[1]));
		result = advCaesarCipher(result, "<C", rotorshift[1]);
		//ROTOR 1
		result = advCaesarCipher(result, ">C", rotorshift[0]);
		result = affineCipher(result, ">A", findRotor(rotor[0]));
		result = advCaesarCipher(result, "<C", rotorshift[0]);
		return result;
	}
	//Front order of rotors - left to right
	public static String rotorReverseOrder(String input, int[] rotorshift, String[] rotor) {
		String result = "";
		//ROTOR 1
		result = advCaesarCipher(input, ">C", rotorshift[0]);
		result = affineCipher(result, "<A", findRotor(rotor[0]));
		result = advCaesarCipher(result, "<C", rotorshift[0]);
		//ROTOR 2
		result = advCaesarCipher(result, ">C", rotorshift[1]);
		result = affineCipher(result, "<A", findRotor(rotor[1]));
		result = advCaesarCipher(result, "<C", rotorshift[1]);
		//ROTOR 3
		result = advCaesarCipher(result, ">C", rotorshift[2]);
		result = affineCipher(result, "<A", findRotor(rotor[2]));
		result = advCaesarCipher(result, "<C", rotorshift[2]);
		return result;
	}
	
	//Finds the rotor provided in the input and finds the alphabet order
	public static String findRotor(String rotor) {
		if (rotor.equals("I")) {
			return rotorI;
		}else if (rotor.equals("II")) {
			return rotorII;
		}else if (rotor.equals("III")) {
			return rotorIII;
		}else if (rotor.equals("IV")) {
			return rotorIV;
		}else if (rotor.equals("V")) {
			return rotorV;
		}
		return "Rotor type not present";
	}
	
	//plugboard
	public static String plugSwitch(String[] plugboard, String input) {
		String result = "";		
		for(int i = 0; i < input.length(); i++) {
			char value = input.charAt(i);
			for(int j = 0; j < plugboard.length; j++) {
				if (plugboard[j].contains(input.charAt(i) + "")) {
					if (input.charAt(i) == plugboard[j].charAt(0)) {
						value = plugboard[j].charAt(1);
					}else {
						value = plugboard[j].charAt(0);
					}
				}
			}
			result += value + "";
		}
		return result;		
	}
	
	//reflector
	public static String reflect(String input) {
		String result = "";
		for(int i = 0; i < input.length(); i++) {
			if (input.charAt(i) != ' ') {
				int index = alphabet.indexOf(input.charAt(i));
				result += reflector.charAt(index);
			}else {
				result += input.charAt(i);
			}
		}
		return result;
	}
	
	//separates the input into rotors, shift, plugboard and the phrase to encrypt
	public static String sepInput(String input, String[] rotors, int[] shifts, String[] plugboard) {
		String[] parts = input.split(" ");
		//Rotors
		for (int i = 0; i < 3; i++) {
			rotors[i] = parts[i];
		}
		//Shifts
		int count = 0;
		for (int i = 3; i < 6; i++) {
			shifts[count] = (parts[i].charAt(0)) - 'A';
			count++;
		}
		shifts[2] ++;
		//Plugboard
		count = 0;
		for (int i = 6; i < 16; i++) {
			plugboard[count] = parts[i];
			count++;
		}
		String result = parts[16];
		for (int i = 17; i < parts.length; i++) {
			result += " " + parts[i];
		}
		return result;		
	}
}


