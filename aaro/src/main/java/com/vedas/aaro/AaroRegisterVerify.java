package com.vedas.aaro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(urlPatterns = { "/regverify" })
public class AaroRegisterVerify extends HttpServlet   {
	private static final long serialVersionUID = 1L;   
    public AaroRegisterVerify() {
        super();
        // TODO Auto-generated constructor stub
    }    
    public void init(ServletConfig config) {       
    }
    Connection conn=null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out=response.getWriter();
		request.setCharacterEncoding("UTF-8");
		String details = request.getParameter("regdetailsverfy");
		String FirstName=null;
		String LastName=null;
		String EmailAddress=null;
		String Password=null;
		String Country=null;
		String UserName=null;
		String pin = null;
		String devicetoken = null;
		String deviceid = null;
		System.out.println("request parameter..."+details);
		
		String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		  int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			StringBuilder builder = new StringBuilder();
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
			int randomPIN = (int)(Math.random()*900)+100;
			builder.toString();
			builder.append(randomPIN);

		try{
			final JSONObject obj = new JSONObject(details);
			    FirstName	=	obj.getString("FirstName");
			    LastName 	=	obj.getString("LastName");
			    EmailAddress =	obj.getString("EmailAddress");
			    Password 	=	obj.getString("Password");
			    Country 	=	obj.getString("Country");
			    UserName 	=	obj.getString("UserName");
			    pin         =   obj.getString("Pin");
			    deviceid = obj.getString("DeviceId");
			    devicetoken = obj.getString("DeviceToken");
			  }catch(JSONException e){
				System.out.println(e);
			 }
	    try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/aaro", "root", "vedas");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		try {
			
			String querypin="select pinnumber  from pindetailes where EmailAddress=?";
			PreparedStatement ps1=conn.prepareStatement(querypin);
			ps1.setString(1, EmailAddress);
			ResultSet rs1=ps1.executeQuery();
			
			if(rs1.next()){
		 		String pinnumber=rs1.getString(1);
		 		
		 		if(pinnumber.equals(pin)){
		 			
		 			String query="INSERT INTO login (FirstName  ,LastName  ,EmailAddress, Password, Country, UserName ) values (?,?,?,?,?,?)";
		 			PreparedStatement ps2=conn.prepareStatement(query);
					
		 			ps2.setString(1, FirstName);
					ps2.setString(2, LastName);
					ps2.setString(3, EmailAddress);
					ps2.setString(4, Password);
					ps2.setString(5, Country);
					ps2.setString(6, UserName);
					
					int row=ps2.executeUpdate();
					
					
					String message1="3";
					JSONObject jsonobj1 = new JSONObject();
					jsonobj1.put("response", message1);
					jsonobj1.put("message", "Registration Verification Successfully Completed");
					out.println(jsonobj1);
					
					
				if(row>0){
						
								String deletequery="delete from pindetailes where EmailAddress=?";
								PreparedStatement ps3=conn.prepareStatement(deletequery);
								ps3.setString(1, EmailAddress);
								int rs3=ps3.executeUpdate();
								System.out.println(rs3);
								
								
								String deletequery1="delete from registration where EmailAddress=?";
								PreparedStatement ps13=conn.prepareStatement(deletequery1);
								ps13.setString(1, EmailAddress);
								int rs13=ps13.executeUpdate();
								System.out.println(rs13);
								

								String push = "select * from pushnotification where username=? and deviceid=?";
								PreparedStatement ps=conn.prepareStatement(push);
								ps.setString(1, EmailAddress);
								ps.setString(2, deviceid);
								ResultSet rs=ps.executeQuery();
								//System.out.println(rs);
								if(rs.next()){
									
										PreparedStatement ps20=conn.prepareStatement("update pushnotification set username=?, devicetoken = ?,status=? where deviceid = ? and username=?");
										ps20.setString(1, EmailAddress);
										ps20.setString(2, devicetoken);
										ps20.setString(3, "1");
										ps20.setString(4, deviceid);
										ps20.setString(5, EmailAddress);
										int r = ps20.executeUpdate();
										System.out.println("row update..."+r);
									
										
								}else{
									String push1 = "insert into pushnotification(username,deviceid,devicetoken,status) values (?,?,?,?)";
									PreparedStatement ps21=conn.prepareStatement(push1);
									ps21.setString(1, EmailAddress);
									ps21.setString(2, deviceid);
									ps21.setString(3, devicetoken);
									ps21.setString(4, "1");
									int row1 =ps21.executeUpdate();
									System.out.println("row inserted..."+row1);
								}
								
								
				
								
							}
		 		
		 		}
		
			else{
				String message="0";
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("response", message);
				jsonobj.put("message", "Wrong OTP");
				out.println(jsonobj);
			}
				
		
} 
		}catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
    if (conn != null) {
        // closes the database connection
        try {
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
	}
}
