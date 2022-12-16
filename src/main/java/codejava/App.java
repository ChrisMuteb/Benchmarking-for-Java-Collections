package codejava;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import redis.clients.jedis.Jedis;

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

    private static HashMap<String, String> map = new HashMap<String, String>();
	
	private static ArrayList<String> keyArr = new ArrayList<String>();
	private static ArrayList<String> valueArr = new ArrayList<String>();
	
	private static Vector<String> keyVec = new Vector<String>();
	private static Vector<String> valueVec = new Vector<String>();
	
	private static List<String> keyRedisList = new ArrayList<String>();
	private static List<String> keyHashMapList = new ArrayList<String>();

    private static Jedis jedis = new Jedis("redis://default:redispw@localhost:49153");
    
    public static void main( String[] args )
    {
        // mysqlFnInsert();
		// mysqlFnFetch();
        // mysqlFnDelete();

        /*---------------HashMap-----------------*/
		// hashMapInsert();
		// hashMapFetch();
		// hashMapDelete();

        /*--------------ArrayList-----------*/
		// arrayListInsert();
		// arrayListFetch();
		// arrayListDelete();

        /*--------------vector-----------*/
		// vectorInsert();
        // vectorFetch();
        // vectorDelete();

        /*--------------Redis-----------*/
		redisInsert();
        redisFetch();
        redisDelete();
    }

    public static long redisDelete() {
		startTime = System.currentTimeMillis();
		
		// Jedis jedis = new Jedis("redis://default:redispw@localhost:49156");
		jedis.auth("redispw");
		System.out.println("Connected to Redis database");
		
		for(int i = 0; i < size; i++) {
			String v = keyRedisList.get(i);
			jedis.del(v);
		}
		
		endTime = System.currentTimeMillis();
		timeElapsed = endTime - startTime;
		
		System.out.println("Time To delete from the Redis: " + timeElapsed + "ms");
		return timeElapsed;
	}
	
	public static long redisFetch() {
		startTime = System.currentTimeMillis();
		
		jedis.auth("redispw");
		System.out.println("Connected to Redis database");
		
		for(int i = 0; i < 100; i++) {
			String v = keyRedisList.get(i);
			System.out.println(v + " -> " + jedis.get(v));
		}
		
		endTime = System.currentTimeMillis();
		timeElapsed = endTime - startTime;
		// jedis.close();
		System.out.println("Time To fetch from the Redis: " + timeElapsed + "ms");
		return timeElapsed;
	}
	
	public static long redisInsert() {
		startTime = System.currentTimeMillis();
		
		// Jedis jedis = new Jedis("redis://default:redispw@localhost:49153");
		jedis.auth("redispw");
		System.out.println("Connected to Redis database");
		
		for(int i = 0; i < size; i++) {
			keyStr = getRandomString();
			valueStr = getMd5(keyStr);
			
			jedis.set(keyStr, valueStr);
			keyRedisList.add(i, keyStr);
		}
		System.out.println("Last item in redis: " + keyStr);
		
		endTime = System.currentTimeMillis();
		timeElapsed = endTime - startTime;
		
		System.out.println("Time To insert into the Redis: " + timeElapsed + "ms");
		return timeElapsed;
	}


    public static long vectorDelete() {
		startTime = System.currentTimeMillis();
		for(int i = size - 1; i >= 0; i--) {
			keyVec.remove(i);
			valueVec.remove(i);
		}
		endTime = System.currentTimeMillis();
		timeElapsed = endTime - startTime;
		
		System.out.println("Time To delete from a Vector: " + timeElapsed + "ms");
		return timeElapsed;
	}
	
	public static long vectorFetch() {
		startTime = System.currentTimeMillis();
		for(int i = 0; i < size; i++) {
			System.out.println(keyVec.get(i) + " -> " + valueVec.get(i));
			
			if(i == 100)
				break;
		}
		endTime = System.currentTimeMillis();
		timeElapsed = endTime - startTime;
		
		System.out.println("Time To fetch from a Vector: " + timeElapsed + "ms");
		return timeElapsed;
	}
	
	public static long vectorInsert() {
		startTime = System.currentTimeMillis();
		for(int i = 0; i < size; i++) {
			keyStr = getRandomString();
			keyVec.addElement(keyStr);
			valueVec.addElement(getMd5(keyStr));
		}
		endTime = System.currentTimeMillis();
		timeElapsed = endTime - startTime;
		
		System.out.println("Time To insert into an Vector: " + timeElapsed + "ms");
		return timeElapsed;
	}

    public static long arrayListDelete() {//check this fn later on
		startTime = System.currentTimeMillis();
		
		for(int i = size-1; i >= 0; i--) {
			
				keyArr.remove(i);
				valueArr.remove(i);
		}
		
		endTime = System.currentTimeMillis();
		timeElapsed = endTime - startTime;
		
		System.out.println("Time To delete from an ArrayList: " + timeElapsed + "ms");
		return timeElapsed;
	}
	
	public static long arrayListFetch() {
		startTime = System.currentTimeMillis();
		
		for(int i = 0; i < size; i++) {
			System.out.println(keyArr.get(i) + " -> " + valueArr.get(i));
			
			if(i == 100)
				break;
		}
		endTime = System.currentTimeMillis();
		timeElapsed = endTime - startTime;
		
		System.out.println("Time To fetch from an ArrayList: " + timeElapsed + "ms");
		return timeElapsed;
	}
	
	public static long arrayListInsert() {
		
		startTime = System.currentTimeMillis();
		
		for(int i = 0; i < size; i++) {
			keyStr = getRandomString();
			keyArr.add(keyStr);
			valueArr.add(getMd5(keyStr));
		}
		endTime = System.currentTimeMillis();
		timeElapsed = endTime - startTime;
		
		System.out.println("Time To insert into an ArrayList: " + timeElapsed + "ms");
		return timeElapsed;
	}

    public static long hashMapInsert() {
		startTime = System.currentTimeMillis();
		
		for(int i = 0; i < size; i++) {
			keyStr = getRandomString();
			valueStr = getMd5(keyStr);
			map.put(keyStr, valueStr);
			keyHashMapList.add(i, keyStr);
		}
		endTime = System.currentTimeMillis();
		timeElapsed = endTime - startTime;
		
		System.out.println("Time To insert into a HashMap: " + timeElapsed + "ms");
		return timeElapsed;
	}
	
	public static long hashMapFetch() {
		startTime = System.currentTimeMillis();
		int inc = 0;
		for(Map.Entry<String, String> set : map.entrySet()) {
			System.out.println(set.getKey() + " -> " + set.getValue());
			inc++;
			
			if(inc == 100)
				break;
		}
		
		endTime = System.currentTimeMillis();
		timeElapsed = endTime - startTime;
		System.out.println("Time To fetch from a HashMap: " + timeElapsed + "ms");
		return timeElapsed;
	}
	
	public static long hashMapDelete() {
		startTime = System.currentTimeMillis();
		int inc = 0;
		
		for(int i = size - 1; i > 0; i--) {
			String v = keyHashMapList.get(i);
			map.remove(v);
		}
		
		endTime = System.currentTimeMillis();
		timeElapsed = endTime - startTime;
		System.out.println("Time To delete from a HashMap: " + timeElapsed + "ms");
		return timeElapsed;
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
