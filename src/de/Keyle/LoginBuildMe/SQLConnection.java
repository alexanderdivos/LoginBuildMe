package de.Keyle.LoginBuildMe;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLConnection {

	public  Connection connect = null;
	
	public SQLConnection(ConfigBuffer cb)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch(Exception e) 
		{
			System.err.println("Exception: " + e.getMessage());
			System.err.println("Maybe you don't have the mysql-connector-java-bin.jar file in your server directory?");
		}
		try
		{
			connect = DriverManager.getConnection("jdbc:mysql://"+cb.DatabaseServer+":"+cb.DatabasePort+"/"+cb.Database+"?user="+cb.DatabaseUser+"&password="+cb.DatabasePassword+"&autoReconnect=true");
		}
		catch(Exception e)
		{
			System.out.println("Something went wrong, please check your MySQL login data and the databasename");
		}
	}
}
