package codejava;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Key: " + getRandomString() );
        System.out.println("Value: " + getMd5(getRandomString()));
        // getRandomString();
    }

    public static String getRandomString() {
		Random rand = new Random();
		
		String digitAlphabet = "ABCDEFGRHIJKLMNOPQRSTUVWXYZ0123456789";
		String generatedString = "";
		
		for(int i = 0; i < 6; i++) {
			generatedString += digitAlphabet.charAt(rand.nextInt(35));
		}
		
		//System.out.println("Generated string: " + generatedString);
		
	    return generatedString;
	}

    public static String getMd5(String input) {
		String hashtext = null;
		
		try {
			//Static getInstance method is called with hashing MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			//digest() method is called to calculate message digest of an input digest() return array of byte
			byte[] messageDigest = md.digest(input.getBytes());
			
			// convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);
			
			// convert message digest into hex value
			hashtext = no.toString(16);
			while(hashtext.length() < 32)
				hashtext = "0" + hashtext;
			
			return hashtext;
			
		}catch(NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
