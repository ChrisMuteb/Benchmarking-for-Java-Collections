package codejava;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.sql.DriverManager;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final int size = 100;//insert a million key and md5 hash value
	private static long startTime, endTime, timeElapsed;
	
	private static String url = "jdbc:mysql://localhost:3306/manyrecords";
	private static String username = "root";
	private static String password = "65412";

	private static String keyStr, valueStr;
	private static PreparedStatement statement = null;
    public static void main( String[] args )
    {
        mysqlFnInsert();
		mysqlFnFetch();
        mysqlFnDelete();
    }

    public static long mysqlFnInsert() {
        int rows = 0;
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database");
            startTime = System.currentTimeMillis();
            for(int i = 0; i < size; i++) {
                String sql = "INSERT INTO many (keyStr, valueStr) VALUES(?, ?)";
                statement = connection.prepareStatement(sql);
                keyStr = getRandomString();
                valueStr = getMd5(keyStr);
                statement.setString(1, keyStr);
                statement.setString(2, valueStr);
                rows = statement.executeUpdate();
            }
            endTime = System.currentTimeMillis();
            timeElapsed = endTime - startTime;
            if(rows > 0) {
                System.out.println(" row has been inserted.");
            }
            System.out.println("Time to insert into mysql db: " + timeElapsed + "ms");
            
            connection.close();
        } catch (SQLException e) {
            System.out.println("Oops, error!");
            e.printStackTrace();
        }
        
        return timeElapsed;
    }

    public static long mysqlFnDelete() {
		int rows = 0;
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
				
			System.out.println("Connected to the database");
			
			startTime = System.currentTimeMillis();
			for(int i = 1; i <= size; i++) {
				String sql = "DELETE FROM many WHERE id=?";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, i);
				
				rows = statement.executeUpdate();
				
			}
			endTime = System.currentTimeMillis();
			timeElapsed = endTime - startTime;
			
				
				if(rows > 0) {
					System.out.println("rows has been deleted.");
				}
			
			System.out.println("Execution time in milliseconds: " + timeElapsed);
			connection.close();
				
		} catch (SQLException e) {
				
			System.out.println("Oops, error!");
			e.printStackTrace();
		}
		
		timeElapsed = endTime - startTime;
		
		return timeElapsed;
	}
	
	
	public static long mysqlFnFetch() {
		int rows = 0;
		String key = null, value = null;
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			System.out.println("Connected to the database");
			startTime = System.currentTimeMillis();
			String sql = "SELECT * FROM many LIMIT 100";
			statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery(sql);
				
			while(rs.next()) {
				key = rs.getString("keyStr");
				value = rs.getString("valueStr");
				System.out.println(key + " " + value);
			}

			endTime = System.currentTimeMillis();
			timeElapsed = endTime - startTime;
			if(rows > 0) {
				System.out.println("rows has been selected.");
			}
			
			System.out.println("Execution time in milliseconds: " + timeElapsed);
			connection.close();
				
		} catch (SQLException e) {
				
			System.out.println("Oops, error!");
			e.printStackTrace();
		}
		
		return timeElapsed;
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
