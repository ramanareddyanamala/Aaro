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
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;

@WebServlet(urlPatterns = { "/receiverdeviceinsert" })
public class AaroReceiverDeviceInfoInsert extends HttpServlet{	
	private static final long serialVersionUID = 1L;
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost:3306/aaro";
	   static final String USER = "root";
	   static final String PASS = "vedas";	
	public void init(ServletConfig config) {  
}	
public AaroReceiverDeviceInfoInsert() {
  super();
  // TODO Auto-generated constructor stub
}
Connection conn=null;
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
	response.setContentType("application/json; charset=UTF-8");
	PrintWriter out=response.getWriter();
	request.setCharacterEncoding("UTF-8");	
	String detailes=request.getParameter("receiverdevicedata");
	System.out.println("inserted receiverdevice data ...."+detailes);
	
	String ssid = null;
	String deviceid = null;
	String devicetoken = null;
	String devicename  = null;
	String timestamp = null;
	Boolean status = false ;
	String serverkey = "AIzaSyBOEYBodORKsGnyFUVDTw1wJH-qk_iApnk";
	try{		
		final JSONObject obj = new JSONObject(detailes);
		ssid = obj.getString("ssid");
		deviceid = obj.getString("deviceid");
		devicetoken = obj.getString("devicetoken");
		devicename = obj.getString("devicename");
		timestamp = obj.getString("timestamp");
		status = obj.getBoolean("status");
		
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
		String selectquery = "SELECT * FROM receiverdeviceinfo where deviceid = ? ";
		
		PreparedStatement pstmt = conn.prepareStatement(selectquery);
		
		pstmt.setString(1,deviceid);
		//pstmt.setString(2, ssid);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			
				if(rs.getString(6).equals("1") && rs.getString(1).equals(ssid)) {
					System.out.println("connected state..");
					
					String sql = "select * from deviceinformation where deviceid = ?";
					PreparedStatement ps1 = conn.prepareStatement(sql);
					ps1.setString(1, deviceid);
					ResultSet rs1 = ps1.executeQuery();
					if(rs1.next()) {		
					JSONObject jsonobj = new JSONObject();
					String msg1 = "2";
					jsonobj.put("response", msg1);
					jsonobj.put("username", rs1.getString(1));
					out.println(jsonobj);
					}else {
						String msg1 = "0";
						JSONObject jsonobj = new JSONObject();
						jsonobj.put("response", msg1);
						jsonobj.put("message", "Device Already Available");
						out.println(jsonobj);
					}
				}else {
					
			    String updatequery = "UPDATE receiverdeviceinfo set ssid = ?, devicetocken = ?, status = ?, timestamp = ? where deviceid = ?";
				PreparedStatement pstmt1 = conn.prepareStatement(updatequery);
				pstmt1.setString(1, ssid);
				pstmt1.setString(2, devicetoken);
				pstmt1.setString(3, "0");
				pstmt1.setString(4, timestamp);
				pstmt1.setString(5, deviceid);

				int rs1 = pstmt1.executeUpdate();
				System.out.println("updated rows..."+rs1);
				
				
				if(rs1>0) {
					System.out.println("device id..."+rs.getString(2));
					String push = "select * from deviceinformation where deviceid = ?";
					PreparedStatement p = conn.prepareStatement(push);
					p.setString(1, deviceid);
					ResultSet r = p.executeQuery();
					if(r.next()) {
						System.out.println("username..."+r.getString(1));
						PreparedStatement px = conn.prepareStatement("select * from receiverdeviceinfo where deviceid=?");
						px.setString(1, deviceid);
						ResultSet rsp = px.executeQuery();
						if(rsp.next()){
						if((r.getString(2).equals(rs.getString(2)) && rsp.getString(6).equals("0"))) {
							
							String push1 = "select * from pushnotification where username = ? and hardwareid = ? and status = ?";
							PreparedStatement ps2 = conn.prepareStatement(push1);
							ps2.setString(1, r.getString(1));
							ps2.setString(2, deviceid);
							ps2.setString(3, "1");
							ResultSet rs2 = ps2.executeQuery();
							while(rs2.next()) {
								String device_id = rs2.getString(2);
		                		System.out.println("device id..."+device_id);
		                		String device_token = rs2.getString(3);
		                		System.out.println("device token..."+device_token);
		                		try {
		                		 PushNotificationPayload payload = PushNotificationPayload.complex();
		                         payload.addAlert("Your Device is Disabled");
		                         payload.addBadge(1);
		                         payload.addSound("default");
		                         payload.addCustomDictionary("id", "1");
		                         System.out.println(payload.toString());
		                         System.out.println("final payload.."+payload);
		                         List < PushedNotification > NOTIFICATIONS = Push.payload(payload, "C:\\Users\\wave\\Desktop\\AAro1DevCertificate.p12","abcd1234@#", false, device_token);
		                         for (PushedNotification NOTIFICATION: NOTIFICATIONS) {
		                             if (NOTIFICATION.isSuccessful()) {
		                                 /* APPLE ACCEPTED THE NOTIFICATION AND SHOULD DELIVER IT */
		                                 System.out.println("PUSH NOTIFICATION SENT SUCCESSFULLY TO:" +
		                                     NOTIFICATION.getDevice().getToken());
		                                 /* STILL NEED TO QUERY THE FEEDBACK SERVICE REGULARLY */
		                             } else {
		                                 String INVALIDTOKEN = NOTIFICATION.getDevice().getToken();
		                                 /* ADD CODE HERE TO REMOVE INVALIDTOKEN FROM YOUR DATABASE */
		                                 /* FIND OUT MORE ABOUT WHAT THE PROBLEM WAS */
		                                 Exception THEPROBLEM = NOTIFICATION.getException();
		                                 THEPROBLEM.printStackTrace();
		                                 /* IF THE PROBLEM WAS AN ERROR-RESPONSE PACKET RETURNED BY APPLE, GET IT */
		                                 ResponsePacket THEERRORRESPONSE = NOTIFICATION.getResponse();
		                                 if (THEERRORRESPONSE != null) {
		                                     System.out.println(THEERRORRESPONSE.getMessage());
		                                 }
		                             }
		                         }
		                		 } catch (CommunicationException e) {
		                	            // TODO Auto-generated catch block
		                	            e.printStackTrace();
		                	        } catch (KeystoreException e) {
		                	            // TODO Auto-generated catch block
		                	            e.printStackTrace();
		                	        } catch (JSONException e) {
		                	            // TODO Auto-generated catch block
		                	            e.printStackTrace();
		                	        }
		                		
		                		
		                		String title = "Aaro Notification";
								
						        String pushMessage = "{\"notification\":{\"title\":\"" +
						                title +
						                "\",\"body\":\"" +
						                "Your Device is Disabled"+
						                "\"},\"to\":\"" +
						                device_token +
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
							}

						}
					}
					}
				}
				JSONObject jsonobj = new JSONObject();
				String msg1 = "3";
				jsonobj.put("response", msg1);
				out.println(jsonobj);
				}
			}else {
				String insertpin="INSERT INTO receiverdeviceinfo VALUES(?,?,?,?,?,?)";
				PreparedStatement ps=conn.prepareStatement(insertpin);
				ps.setString(1, ssid);
				ps.setString(2, deviceid );
				ps.setString(3, devicetoken);
				ps.setString(4, devicename);
				ps.setString(5, timestamp);
				ps.setBoolean(6, status);
				int rowinsert = ps.executeUpdate();
				System.out.println(rowinsert);
			    
		if(rowinsert>0) {
			JSONObject jsonobj = new JSONObject();
			String msg1 = "3";
			jsonobj.put("response", msg1);
			jsonobj.put("message", "Device Added Successfully");
			out.println(jsonobj);
		}else {
			JSONObject jsonobj = new JSONObject();
			String msg1 = "0";
			jsonobj.put("response", msg1);
			jsonobj.put("message", "Device Already Available");
			out.println(jsonobj);
		}
		
	}
	}catch(SQLException  e) {
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
