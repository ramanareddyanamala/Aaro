package com.vedas.aaro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(urlPatterns = { "/receiverdevicesfetch" })
public class AaroReceiverDeviceInfoFetching extends HttpServlet{	
	private static final long serialVersionUID = 1L;
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost:3306/aaro";
	   static final String USER = "root";
	   static final String PASS = "vedas";	
	public void init(ServletConfig config) {  
}	
public AaroReceiverDeviceInfoFetching() {
  super();
  // TODO Auto-generated constructor stub
}
Connection conn=null;
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
	response.setContentType("application/json; charset=UTF-8");
	PrintWriter out=response.getWriter();
	request.setCharacterEncoding("UTF-8");	
	String detailes=request.getParameter("ssidInfo");
	System.out.println(" SSID / Network Details...."+detailes);
	
	String ssid = null;
	JSONArray array = new JSONArray();
	
	try{		
		final JSONObject obj = new JSONObject(detailes);
		ssid = obj.getString("ssid");
	}catch(JSONException e){
		e.printStackTrace();
	}	
	
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}	
	
	try {
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	       try {
	  				String query="select * from receiverdeviceinfo where ssid=? and status = ? ";
	  				PreparedStatement ps=conn.prepareStatement(query);
	  				ps.setString(1, ssid);
	  				ps.setString(2, "0");
	  				ResultSet rs = ps.executeQuery();
	  				
	  				while(rs.next()){
	  					
	  					String deviceid = rs.getString(2);
	  					String devicename=rs.getString(4);
	  					
	  					JSONObject jsonobj2 = new JSONObject();
	  				
	  					jsonobj2.put("deviceid", deviceid);
	  					jsonobj2.put("devicename", devicename);
	  					
	  					array.put(jsonobj2);
	  				}
	  					if( rs.first()){
	  						 JSONObject jobj1= new JSONObject();
	  						 String success="3";
	  				         jobj1.put("response", success);
	  				         jobj1.put("devices", array);
	  				         out.println(jobj1);
	  						}else{
	  			 			String message="0";
	  						JSONObject jsonobj = new JSONObject();
	  						jsonobj.put("response", message);
	  						jsonobj.put("devicedata", "No Devices Found");

	  						out.println(jsonobj);
	  						}	
	  					
				}catch (SQLException e) {  
					 e.printStackTrace();
					    
			    } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
  finally{
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
