package com.vedas.aaro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailValidate {
	
	public static boolean checkUser(String UserName,String Password) 
    {
	      boolean st =false;
	      Connection con=null;
	      
	      try {
			Class.forName("com.mysql.jdbc.Driver");
		   } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
	      
	      try {
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/aaro", "root", "vedas");
			
			PreparedStatement ps =con.prepareStatement
                  ("select username,password from login where (username=? and password=?) or (emailaddress=? and password=?)");
						
			ps.setString(1, UserName);
			ps.setString(2, Password);
			ps.setString(3, UserName);
			ps.setString(4, Password);
			ResultSet rs =ps.executeQuery();
			while( rs.next()) {
			if(rs.getString(2).equals(Password)) {
				st =true;
			}else {
				st = false;
			}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		return st;
	
    }
}