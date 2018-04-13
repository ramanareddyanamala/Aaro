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

@WebServlet(urlPatterns = { "/deviceinsert" }) 

	public class AaroDeviceInformationInsert extends HttpServlet {
		private static final long serialVersionUID = 1L;
	       
	   public void init(ServletConfig config) {
		   
	   }
	    public AaroDeviceInformationInsert() {
	        super();
	        // TODO Auto-generated constructor stub
	    }
	    Connection conn=null;
	   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			String details = request.getParameter("devicedata"); 
			String username = null;
			String deviceid = null;
			String devicename  = null;
			String devicelocation = null;
			String DateAdded = null ;
			//String devicetoken = null;
			String serverkey = "AIzaSyBOEYBodORKsGnyFUVDTw1wJH-qk_iApnk";
			
			System.out.println("request parameter..."+details);
			try{
				final JSONObject obj = new JSONObject(details);
				username = obj.getString("username");
				deviceid = obj.getString("deviceid");
				devicename = obj.getString("devicename");
				devicelocation = obj.getString("devicelocation");
				DateAdded = obj.getString("DateAdded");
		    		     
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
				String selectquery = "SELECT * FROM receiverdeviceinfo where deviceid = ?";
				PreparedStatement pstmt = conn.prepareStatement(selectquery);
				
				pstmt.setString(1,deviceid);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					
					System.out.println("status value.."+rs.getString(6));
					
					if(rs.getString(2).equals(deviceid)) {
						if(rs.getString(6).equals("1")) {
						JSONObject jsonobj = new JSONObject();
						String msg1 = "2";
						jsonobj.put("response", msg1);
						jsonobj.put("message", "Device Unavailable To Pair");
						out.println(jsonobj);	
						}else {
							PreparedStatement np = conn.prepareStatement("select * from deviceinformation where username=?");
							np.setString(1, username);
							ResultSet rst = np.executeQuery();
							while(rst.next()) {
								System.out.println("device id...."+rst.getString(2));
								String statusconnection = "update receiverdeviceinfo set status=? where deviceid=?";  
								PreparedStatement ps12 = conn.prepareStatement(statusconnection);
								ps12.setString(1, "0");
								ps12.setString(2, rst.getString(2));
								int rp = ps12.executeUpdate();
								System.out.println("updated  rows"+rp);
								if(rp>0) {
									
									 String select1 = "select * from receiverdeviceinfo where deviceid = ? and status = ? ";
									 PreparedStatement ps3 = conn.prepareStatement(select1);
									 ps3.setString(1, rst.getString(2));
									 ps3.setString(2,"0");
									 ResultSet rs2 = ps3.executeQuery();
									 while(rs2.next()) {
										 String id1 = rs2.getString(2);
										 String device_token = rs2.getString(3);
										 System.out.println("Device Id..."+id1);
										 System.out.println("Device Token..."+device_token);
										 
										 String title = "Aaro Notification";
											
									        String pushMessage = "{\"notification\":{\"title\":\"" +
									                title +
									                "\",\"body\":\"" +
									                "Device Unpaired"+
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
							
							String insertpin="INSERT INTO deviceinformation VALUES(?,?,?,?,?)";
							PreparedStatement ps=conn.prepareStatement(insertpin);
							ps.setString(1, username);
							ps.setString(2, deviceid );
							ps.setString(3, devicename);
							ps.setString(4, devicelocation);
							ps.setString(5, DateAdded);
							int rowinsert = ps.executeUpdate();
							if(rowinsert>0){
								PreparedStatement px = conn.prepareStatement("update pushnotification set hardwareid = ? where username = ?");
								 px.setString(1, deviceid);
								 px.setString(2, username);
								 int row = px.executeUpdate();
								 System.out.println("row update..."+row);
								 
								PreparedStatement ps2 = conn.prepareStatement("update receiverdeviceinfo set status = ? where deviceid = ? ");
								ps2.setString(1, "1");
								ps2.setString(2, deviceid);
								int rowinsert1 = ps2.executeUpdate();
								System.out.println("row upadted..."+rowinsert1);
								
								if(rowinsert1>0) {
									
									 String select1 = "select * from receiverdeviceinfo where deviceid = ? and status = ? ";
									 PreparedStatement ps3 = conn.prepareStatement(select1);
									 ps3.setString(1, deviceid);
									 ps3.setString(2,"1");
									 ResultSet rs2 = ps3.executeQuery();
									 while(rs2.next()) {
										 String id1 = rs2.getString(2);
										 String device_token = rs2.getString(3);
										 System.out.println("Device Id..."+id1);
										 System.out.println("Device Token..."+device_token);
										 
										 String title = "Aaro Notification";
											
									        String pushMessage = "{\"notification\":{\"title\":\"" +
									                title +
									                "\",\"body\":\"" +
									                username+
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
								
							}else {
								
								
								System.out.println("Incorrect Deviceid");
							}
						
								JSONObject jb1 = new JSONObject();
								String msg1 = "3";
								jb1.put("response", msg1);
								jb1.put("message", "Device Paired Successfully");
								//jb1.put("connectionsatus", )
								System.out.println("row inserted..."+jb1);
							    out.print(jb1);
				      }
						
					}else {
						
							PreparedStatement np = conn.prepareStatement("select * from deviceinformation where username=?");
							np.setString(1, username);
							ResultSet rst = np.executeQuery();
							while(rst.next()) {
								System.out.println("device id...."+rst.getString(2));
								String statusconnection = "update receiverdeviceinfo set status=? where deviceid=?";  
								PreparedStatement ps12 = conn.prepareStatement(statusconnection);
								ps12.setString(1, "0");
								ps12.setString(2, rst.getString(2));
								int rp = ps12.executeUpdate();
								System.out.println("updated  rows"+rp);
								if(rp>0) {
									
									 String select1 = "select * from receiverdeviceinfo where deviceid = ? and status = ? ";
									 PreparedStatement ps3 = conn.prepareStatement(select1);
									 ps3.setString(1, deviceid);
									 ps3.setString(2,"0");
									 ResultSet rs2 = ps3.executeQuery();
									 while(rs2.next()) {
										 String id1 = rs2.getString(2);
										 String device_token = rs2.getString(3);
										 System.out.println("Device Id..."+id1);
										 System.out.println("Device Token..."+device_token);
										 
										 String title = "Aaro Notification";
											
									        String pushMessage = "{\"notification\":{\"title\":\"" +
									                title +
									                "\",\"body\":\"" +
									                "Device Unpaired"+
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

							
							
							PreparedStatement pp = conn.prepareStatement("select * from receiverdeviceinfo where deviceid = ?");
							pp.setString(1, deviceid);
							ResultSet res = pp.executeQuery();
							if(res.next()) {
						
						      if(res.getString(6)=="1") {
						    	  System.out.println("Device is in connected state");
						    	  String msg = "6";
									JSONObject jb = new JSONObject();
									jb.put("response", msg);
									jb.put("message", "Device Unavailable To Pair");
									out.println(jb);
						      }else {
									String insertpin="INSERT INTO deviceinformation VALUES(?,?,?,?,?)";
									PreparedStatement ps=conn.prepareStatement(insertpin);
									ps.setString(1, username);
									ps.setString(2, deviceid );
									ps.setString(3, devicename);
									ps.setString(4, devicelocation);
									ps.setString(5, DateAdded);
									int rowinsert = ps.executeUpdate();
									if(rowinsert>0){
										PreparedStatement px = conn.prepareStatement("update pushnotification set hardwareid = ? where username = ?");
										 px.setString(1, deviceid);
										 px.setString(2, username);
										 int row = px.executeUpdate();
										 System.out.println("row update..."+row);
										PreparedStatement ps2 = conn.prepareStatement("update receiverdeviceinfo set status = ? where deviceid = ? ");
										ps2.setString(1, "1");
										ps2.setString(2, deviceid);
										int rowinsert1 = ps2.executeUpdate();
										System.out.println("row upadted..."+rowinsert1);
										
										if(rowinsert1>0) {
											
											 String select1 = "select * from receiverdeviceinfo where deviceid = ? and status = ? ";
											 PreparedStatement ps3 = conn.prepareStatement(select1);
											 ps3.setString(1, deviceid);
											 ps3.setString(2,"1");
											 ResultSet rs2 = ps3.executeQuery();
											 while(rs2.next()) {
												 String id1 = rs2.getString(2);
												 String device_token = rs2.getString(3);
												 System.out.println("Device Id..."+id1);
												 System.out.println("Device Token..."+device_token);
												 
												 String title = "Aaro Notification";
													
											        String pushMessage = "{\"notification\":{\"title\":\"" +
											                title +
											                "\",\"body\":\"" +
											                username+
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
										
									}else {
										
										
										System.out.println("Incorrect Deviceid");
									}
								
										JSONObject jb1 = new JSONObject();
										String msg1 = "3";
										jb1.put("response", msg1);
										jb1.put("message", "Device Paired Successfully");
										//jb1.put("connectionsatus", )
										System.out.println("row inserted..."+jb1);
									    out.print(jb1);
						      }
								
								}else {
									String msg = "1";
									JSONObject jb = new JSONObject();
									jb.put("response", msg);
									jb.put("message", "Divice Id not available");
									out.println(jb);
								}
							
							}
						
							
				}
				else {
					String msg = "1";
					JSONObject jb = new JSONObject();
					jb.put("response", msg);
					jb.put("message", "Divice Id not available");
					out.println(jb);
				}
				/*
				else {
					try {
						PreparedStatement np = conn.prepareStatement("select * from deviceinformation where username=?");
						np.setString(1, username);
						ResultSet rst = np.executeQuery();
						while(rst.next()) {
							System.out.println("device id...."+rst.getString(2));
							String statusconnection = "update receiverdeviceinfo set status=? where deviceid=?";  
							PreparedStatement ps12 = conn.prepareStatement(statusconnection);
							ps12.setString(1, "0");
							ps12.setString(2, rst.getString(2));
							int rp = ps12.executeUpdate();
							System.out.println("updated  rows"+rp);
						}
						PreparedStatement pp = conn.prepareStatement("select * from receiverdeviceinfo where deviceid = ?");
						pp.setString(1, deviceid);
						ResultSet res = pp.executeQuery();
						if(res.next()) {
							

					     		String insertpin="INSERT INTO deviceinformation VALUES(?,?,?,?,?)";
								PreparedStatement ps=conn.prepareStatement(insertpin);
								ps.setString(1, username);
								ps.setString(2, deviceid );
								ps.setString(3, devicename);
								ps.setString(4, devicelocation);
								ps.setString(5, DateAdded);
								int rowinsert = ps.executeUpdate();
								if(rowinsert>0){
									JSONObject jb1 = new JSONObject();
									String msg1 = "3";
									jb1.put("response", msg1);
									System.out.println("row inserted..."+jb1);
								    out.print(jb1);
								
								String select = "select * from receiverdeviceinfo where deviceid = ?";
								PreparedStatement ps1 = conn.prepareStatement(select);
								ps1.setString(1, deviceid);
								ResultSet rs1 = ps1.executeQuery();
								if(rs1.next()) {
									String id = rs1.getString(2);
									if(id.equalsIgnoreCase(deviceid)) {
										
										PreparedStatement ps2 = conn.prepareStatement("update receiverdeviceinfo set status = ? where deviceid = ? ");
										ps2.setString(1, "1");
										ps2.setString(2, deviceid);
										int rowinsert1 = ps2.executeUpdate();
										System.out.println("row upadted..."+rowinsert1);
										
										if(rowinsert1>0) {
											
											 String select1 = "select * from receiverdeviceinfo where deviceid = ? and status = ? ";
											 PreparedStatement ps3 = conn.prepareStatement(select1);
											 ps3.setString(1, deviceid);
											 ps3.setString(2,"1");
											 ResultSet rs2 = ps3.executeQuery();
											 while(rs2.next()) {
												 String id1 = rs2.getString(2);
												 String device_token = rs2.getString(3);
												 System.out.println("Device Id..."+id1);
												 System.out.println("Device Token..."+device_token);
												 
												 String title = "Aaro Notification";
													
											        String pushMessage = "{\"notification\":{\"title\":\"" +
											                title +
											                "\",\"body\":\"" +
											                username+
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
										
									}else {
										
										
										System.out.println("Incorrect Deviceid");
									}
								}
								
						     		
									}else {
										JSONObject jb1 = new JSONObject();
										String msg1 = "0";
										jb1.put("response", msg1);
										jb1.put("message", "No devices found with this username");
										System.out.println("row inserted..."+jb1);
									    out.print(jb1);
									}
							}else {
								String msg = "1";
								JSONObject jb = new JSONObject();
								jb.put("response", msg);
								jb.put("message", "Divice Id not available");
								out.println(jb);
							}
						
						
						
						
						
						}catch (Exception e) {  
							 e.printStackTrace();
							    throw new RuntimeException(e);  
					    }
					
				}*/
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
