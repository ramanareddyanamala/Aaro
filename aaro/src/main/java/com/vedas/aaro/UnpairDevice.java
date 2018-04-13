package com.vedas.aaro;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(urlPatterns = { "/unpair" }) 

	public class UnpairDevice extends HttpServlet {
		private static final long serialVersionUID = 1L;
	       
	   public void init(ServletConfig config) {
		   
	   }
	    public UnpairDevice() {
	        super();
	        // TODO Auto-generated constructor stub
	    }
	    Connection conn=null;
	   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			String details = request.getParameter("unpairdevice"); 
			System.out.println("request parameter..."+details);
			
			String deviceid = null;
			String serverkey = "AIzaSyBOEYBodORKsGnyFUVDTw1wJH-qk_iApnk";
			
			try{
				final JSONObject obj = new JSONObject(details);
				
				deviceid = obj.getString("deviceid");
				
		    		     
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
				e.printStackTrace();
			}		
			
			try {
				
				String unpair = "select * from deviceinformation where deviceid = ?";
				PreparedStatement ps = conn.prepareStatement(unpair);
				ps.setString(1, deviceid);
				ResultSet rs = ps.executeQuery();
				if(rs.next())
				       {
						String unpair1 = "select * from receiverdeviceinfo where deviceid = ?";
						PreparedStatement ps1 = conn.prepareStatement(unpair1);
						ps1.setString(1, deviceid);
						ResultSet rs1 = ps1.executeQuery();
						if(rs1.next()) {
							     String id = rs1.getString(2);
							     if(id.equalsIgnoreCase(deviceid)) {
							    	 System.out.println("you can update status..");
							    	 try {
							    		 
							    		 String update = "update receiverdeviceinfo set status = ? where deviceid = ?";
							    		 PreparedStatement ps2 = conn.prepareStatement(update);
							    		 ps2.setString(1, "0");
							    		 ps2.setString(2, deviceid);
							    		 int rowupdate = ps2.executeUpdate();
							    		 if(rowupdate>0) {
							    			 String delete = "delete from deviceinformation where deviceid = ?";
							    			 PreparedStatement ps3 = conn.prepareStatement(delete);
							    			 ps3.setString(1, deviceid);
							    			 int rowdelete = ps3.executeUpdate();
							    			 System.out.println("row deleted..."+rowdelete);
							    		 if(rowdelete>0) {
							    			 
							    			 String device_tocken = rs1.getString(3);
							    			 System.out.println("Device Token..."+device_tocken);
											 
											 String title = "Aaro Notification";
												
										        String pushMessage = "{\"notification\":{\"title\":\"" +
										                title +
										                "\",\"body\":\"" +
										                "Device Unpaired"+
										                "\"},\"to\":\"" +
										                device_tocken +
											         "\"}";
												
										        URL url = new URL("https://fcm.googleapis.com/fcm/send");
										        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
										        conn.setRequestProperty("Authorization", "key=" +serverkey);
										        conn.setRequestProperty("Content-Type", "application/json");
										        conn.setRequestMethod("POST");
										        conn.setDoOutput(true);

										        // Send FCM message content.
										        OutputStream outputStream = conn.getOutputStream();
										        outputStream.write(pushMessage.getBytes());

										        System.out.println(conn.getResponseCode());
										        //System.out.println(conn.getResponseMessage());
										        
											    
							    			 String msg = "3";
							    			 JSONObject jb = new JSONObject();
							    			 jb.put("response", msg);
							    			 jb.put("message", "Device Unpaired Successfully");
							    			 System.out.println("json object..."+jb);
							    			 out.println(jb);
							    			 
							    		 }else {
							    			 String e = "0";
							    			 JSONObject jb1 = new JSONObject();
							    			 jb1.put("response", e);
							    			 jb1.put("message", "Device Not Unpaired");
							    			 System.out.println("json object..."+jb1);
							    			 out.println(jb1);
							    		 }
							    	  }else{
											String s = "0";
											JSONObject jb = new JSONObject();
											jb.put("response", s);
											
											out.println(jb);
										}
							    		 
							    	 }catch (Exception e) {
							    		 e.printStackTrace();
							    	 }
							     }else {
							    	 System.out.println("Invalid Device Id...");
							     }
						}else{
							String s = "0";
							JSONObject jb = new JSONObject();
							jb.put("response", s);
							
							out.println(jb);
						}
				      }else{
							String s = "0";
							JSONObject jb = new JSONObject();
							jb.put("response", s);
							
							out.println(jb);
						}
			}catch(Exception e) {
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