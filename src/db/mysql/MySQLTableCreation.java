package db.mysql;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;

public class MySQLTableCreation {
	// Run this as Java application to reset db schema.
	public static void main(String[] args) {
		try {
			// Step 1 Connect to MySQL.
			System.out.println("Connecting to " + MySQLDBUtil.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			// reflection -- runtime create object, number of drivers
			Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);
			
			if (conn == null) {
				return;
			}
			
			// Step 2 Drop tables in case they exist.
			Statement statement = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS categories";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS history";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS items";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS users";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS creatures";
			statement.executeUpdate(sql);
			
			// Step 3 Create new tables
			sql = "CREATE TABLE items ("
					+ "item_id VARCHAR(255) NOT NULL,"
					+ "name VARCHAR(255),"
					+ "rating FLOAT,"
					+ "address VARCHAR(255),"
					+ "image_url VARCHAR(255),"
					+ "url VARCHAR(255),"
					+ "distance FLOAT,"
					+ "PRIMARY KEY (item_id)"
					+ ")";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE users ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "password_hash CHAR(128) NOT NULL,"
					+ "first_name VARCHAR(255),"
					+ "last_name VARCHAR(255),"
					+ "salt CHAR(128) NOT NULL,"
					+ "PRIMARY KEY (user_id)"					
					+ ")";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE categories ("
					+ "item_id VARCHAR(255) NOT NULL,"
					+ "category VARCHAR(255) NOT NULL,"
					+ "PRIMARY KEY (item_id, category),"
					+ "FOREIGN KEY (item_id) REFERENCES items(item_id)"
					+ ")";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE history ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "item_id VARCHAR(255) NOT NULL,"
					+ "last_favor_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
					+ "PRIMARY KEY (user_id, item_id),"
					+ "FOREIGN KEY (user_id) REFERENCES users(user_id),"
					+ "FOREIGN KEY (item_id) REFERENCES items(item_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE creatures ("
					+ "creature_id VARCHAR(255) NOT NULL,"
					+ "lat DECIMAL(9,6) NOT NULL,"
					+ "lon DECIMAL(9,6) NOT NULL,"
					+ "geohash VARCHAR(255) NOT NULL,"
					+ "glow VARCHAR(255) NOT NULL," 
					+ "fig_url VARCHAR(255) NOT NULL,"
					+ "PRIMARY KEY (creature_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			// step 4: test insert
			sql = "INSERT INTO creatures VALUES (" 
					+ "'Red tide', '32.716', '-117.161', 'San Diego', 'bioluminescence', "
					+ "'https://www.mercurynews.com/wp-content/uploads/2018/05/bluewave.jpg?w=865')";
			statement.executeUpdate(sql);
			
			sql = "INSERT INTO creatures VALUES (" 
					+ "'Clusterwink snail', '32.716', '-117.161', 'San Diego', 'bioluminescence', "
					+ "'https://media.wired.com/photos/5b189b6699308e6c50e27698/master/w_660,c_limit/clusterwink.jpg')";
			statement.executeUpdate(sql);
			
			sql = "INSERT INTO creatures VALUES (" 
					+ "'Atolla', '36.600', '-121.895', 'Monterey Bay', 'bioluminescence', "
					+ "'https://media.wired.com/photos/5b189b677169727a89c69b99/master/w_660,c_limit/atolla.jpg')";
			statement.executeUpdate(sql);
			
			sql = "INSERT INTO creatures VALUES (" 
					+ "'Abraliopsis-squid', '36.600', '-121.895', 'Monterey Bay', 'bioluminescence', "
					+ "'https://media.wired.com/photos/5b189b68d09a372e164ceaea/master/w_660,c_limit/abraliopsis-squid.jpg')";
			statement.executeUpdate(sql);
			
			sql = "INSERT INTO creatures VALUES (" 
					+ "'Tomopteris', '36.600', '-121.895', 'Monterey Bay', 'bioluminescence', "
					+ "'https://media.wired.com/photos/5b189b691f816f6e082cce28/master/w_660,c_limit/tomopteris.jpg')";
			statement.executeUpdate(sql);
			
			sql = "INSERT INTO creatures VALUES (" 
					+ "'Glowing corals', '20.280', '38.513', 'Red sea', 'biofluorescence', "
					+ "'https://66.media.tumblr.com/tumblr_lpeuciiZYo1qb2upxo1_640.jpg')";
			statement.executeUpdate(sql);
			
			sql = "INSERT INTO creatures VALUES (" 
					+ "'Comb Jelly', '27.665', '-81.516', 'Florida', 'bioluminescence', "
					+ "'https://s3.amazonaws.com/static.organiclead.com/Site-e11ba9c7-af71-4afc-8f6a-32bce62a9a52/shutterstock_190676801.jpg')";
			statement.executeUpdate(sql);
			
			conn.close();
			System.out.println("Import done successfully");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
