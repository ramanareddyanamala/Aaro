package com.vedas.aaro;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.jcodec.api.JCodecException;
import org.json.JSONException;
import org.json.JSONObject;
@WebServlet(urlPatterns = { "/sending" })
@MultipartConfig(maxFileSize = 2024288000)
public class ImageFileSharing extends HttpServlet {
private static final long serialVersionUID = 1L;
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost:3306/aaro";

	   static final String USER = "root";
	   static final String PASS = "vedas";
	
	public void init(ServletConfig config) {
}
	
public ImageFileSharing() {
super();
// TODO Auto-generated constructor stub
}

Connection conn=null;
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException,IOException {
	
	response.setContentType("application/json; charset=UTF-8");
	PrintWriter out=response.getWriter();
	request.setCharacterEncoding("UTF-8");
	 InputStream inputStream1 = null,inputStream2 = null,inputStream3 = null,inputStream4 = null,inputStream5 = null,inputStream6 = null,inputStream7 =null, inputStream = null,uname,tstamp,tnail = null;
	 Collection<Part> parts = request.getParts();
	 System.out.println("parts size...."+parts.size());
	 Part imagePart1 = null;
	 Part thumbnail = null;
	 Part videoPart = null;
	 Part timestamp = null;
	 Part imagePart2 = null;
	 Part imagePart3 = null;
	 Part imagePart4 = null;
	 Part imagePart5 = null;
	 Part username = null;
	 String serverkey = "AIzaSyBOEYBodORKsGnyFUVDTw1wJH-qk_iApnk";
	 //String deviceid = null;
	 String Path ="C:\\images\\";
	 username = request.getPart("username");
	 timestamp= request.getPart("timestamp");
	 
	    uname = username.getInputStream();
	    String theString1 = IOUtils.toString(uname, StandardCharsets.UTF_8.name()); 
	    System.out.println("user name..."+theString1);
	    
	    tstamp = timestamp.getInputStream();
	    String theString2 = IOUtils.toString(tstamp, StandardCharsets.UTF_8.name()); 
	    System.out.println("timestamp..."+theString2);
	    
	   
	    
	    String text2 = "";
        String possible2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 10; i++) {
            text2 += possible2.charAt((int) Math.floor(Math.random() * possible2.length()));
        }
        System.out.println("pin:" + text2);
        
        String text1 = "";
        String possible1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 10; i++) {
            text1 += possible1.charAt((int) Math.floor(Math.random() * possible1.length()));
        }
        System.out.println("pin:" + text1);
        
        String text3 = "";
        String possible3 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 10; i++) {
            text3 += possible3.charAt((int) Math.floor(Math.random() * possible3.length()));
        }
        System.out.println("pin:" + text3);
        
        String text4 = "";
        String possible4 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 10; i++) {
            text4 += possible4.charAt((int) Math.floor(Math.random() * possible4.length()));
        }
        System.out.println("pin:" + text4);
        
        String text5 = "";
        String possible5 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 10; i++) {
            text5 += possible5.charAt((int) Math.floor(Math.random() * possible5.length()));
        }
        System.out.println("pin:" + text5);
        
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
        
	   // String pin = String.valueOf((int)(((Math.random())*1000)+1990));
	 if(parts.size() == 3){
		 int insertrow = 0;
		System.out.println("parts size is 3");
		
		if(request.getPart("image1")!=null) {
		
		 imagePart1 = request.getPart("image1");
			System.out.println("image data..."+imagePart1.getSubmittedFileName());
			 
			if (imagePart1 != null) {
		        inputStream1 = imagePart1.getInputStream(); 
		        
			}
		        
		        byte[] bytes1 = IOUtils.toByteArray(inputStream1);  
			    try{
			    FileUtils.writeByteArrayToFile(new File(Path+text1+".png"), bytes1);
			   
			    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
			    PreparedStatement ps = conn.prepareStatement(insert);
			   
			    ps.setString(1, theString1);
			    ps.setString(2, Path+text1+".png");
			    ps.setString(3, theString2);
			    
			    insertrow = ps.executeUpdate();
			    System.out.println("row inserted..."+insertrow);
			    
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
			    System.out.println("bytearray..."+bytes1);
			    System.out.println("image part1 data...."+imagePart1);
			    
			    String message = "3";
		    	JSONObject jobj = new JSONObject();
		    	try {
					jobj.put("response",message);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    	out.println(jobj);
		}
		    	
		    if(request.getPart("video")!=null) {
			    videoPart = request.getPart("video");
				System.out.println("video format ..."+videoPart.getSubmittedFileName());
				
		 
		 if(videoPart != null) {
			        inputStream6 = videoPart.getInputStream();   
		 }
		 
		 byte[] bytes14 = IOUtils.toByteArray(inputStream6);  
		    try{
		    	 String ext1 = FilenameUtils.getExtension(videoPart.getSubmittedFileName());
	    		  System.out.println("file extension.."+ext1);
			    FileUtils.writeByteArrayToFile(new File(Path+text1+"."+ext1), bytes14);
			 
			    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
			    PreparedStatement ps = conn.prepareStatement(insert);
			   
			    ps.setString(1, theString1);
			    ps.setString(2, Path+text1+"."+ext1);
			    ps.setString(3, theString2);
			   
			    insertrow = ps.executeUpdate();
			    System.out.println("row inserted..."+insertrow);
			   

			    System.out.println("path of file.."+Path);
			    VideoFrameExtracter videoFrameExtracter = new VideoFrameExtracter();
			      File file = Paths.get(Path+text1+"."+ext1).toFile();
			      try {
			          File imageFrame = videoFrameExtracter.createThumbnailFromVideo(file, 2);
			          System.out.println("input file name : " + file.getAbsolutePath());
			          System.out.println("output video frame file name  : " + imageFrame.getAbsolutePath());
			      } catch (Exception e) {
			          System.out.println("error occurred while extracting image : " + e.getMessage());
			      }
			    /*
			   if(request.getPart("thumbnail")!=null) {

				    thumbnail = request.getPart("thumbnail");
			   
			   if(thumbnail !=null) {
				   inputStream7 = thumbnail.getInputStream();
			   }
			   byte[] bytes145 = IOUtils.toByteArray(inputStream7);  
			    try{
			    	 String ext12 = FilenameUtils.getExtension(thumbnail.getSubmittedFileName());
		    		  System.out.println("file extension.."+ext12);
				    FileUtils.writeByteArrayToFile(new File(Path+text1+".png"), bytes145);
				 
				    
			   }catch(Exception e){
			    	e.printStackTrace();
			    }
			   }*/
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
		    System.out.println("bytearray..."+bytes14);
		   
		 
		    String message1 = "3";
	    	JSONObject jobj1 = new JSONObject();
	    	try {
				jobj1.put("response",message1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	out.println(jobj1);
	    	
	 }
		    if(insertrow>0) {
		    	try {
		    	PreparedStatement np = conn.prepareStatement("select * from deviceinformation where username=?");
				np.setString(1, theString1);
				ResultSet rst = np.executeQuery();
				while(rst.next()) {
				 String select1 = "select * from receiverdeviceinfo where deviceid = ? and status = ? ";
				 PreparedStatement ps3 = conn.prepareStatement(select1);
				 ps3.setString(1, rst.getString(2));
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
				                "Files Added"+
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
		    	}catch(Exception e){
			    	e.printStackTrace();
			    }
		    }		 
	 }else{
		 int insertrow = 0;
		 if(parts.size() == 4){
			 System.out.println("size is 4");
			if(request.getPart("image1")!=null) {
			 imagePart1 = request.getPart("image1");
			 if (imagePart1 != null) {
			        inputStream1 = imagePart1.getInputStream();    
			    }
			    byte[] bytes1 = IOUtils.toByteArray(inputStream1);  
			    System.out.println("bytearray..."+bytes1);
			    try{
				    FileUtils.writeByteArrayToFile(new File(Path+text1+".png"), bytes1);
				    
				    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				    
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text1+".png");
				    ps.setString(3, theString2);
				     insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				    
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			  
			    imagePart2 = request.getPart("image2");
			    if (imagePart2 != null) {
			        inputStream2 = imagePart2.getInputStream();    
			    }
			    byte[] bytes2 = IOUtils.toByteArray(inputStream2);  
			    System.out.println("bytearray..."+bytes2);
			    
			    try{
				    FileUtils.writeByteArrayToFile(new File(Path+text2+".png"), bytes2);
				   
				    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				   
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text2+".png");
				    ps.setString(3, theString2);
				    insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				   
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			    
			    String message = "3";
		    	JSONObject jobj = new JSONObject();
		    	try {
					jobj.put("response",message);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	out.println(jobj);
			}
		    	if(request.getPart("video")!=null) {
				    videoPart = request.getPart("video");
					System.out.println("video format ..."+videoPart.getSubmittedFileName());
					
			 
			 if(videoPart != null) {
				        inputStream6 = videoPart.getInputStream();   
			 }
			 
			 byte[] bytes14 = IOUtils.toByteArray(inputStream6);  
			    try{
			    	 String ext1 = FilenameUtils.getExtension(videoPart.getSubmittedFileName());
		    		  System.out.println("file extension.."+ext1);
				    FileUtils.writeByteArrayToFile(new File(Path+text1+"."+ext1), bytes14);
				 
				    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				   
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text1+"."+ext1);
				    ps.setString(3, theString2);
				   
				     insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				   if(request.getPart("thumbnail")!=null) {

					    thumbnail = request.getPart("thumbnail");
				   
				   if(thumbnail !=null) {
					   inputStream7 = thumbnail.getInputStream();
				   }
				   byte[] bytes145 = IOUtils.toByteArray(inputStream7);  
				    try{
				    	 String ext12 = FilenameUtils.getExtension(thumbnail.getSubmittedFileName());
			    		  System.out.println("file extension.."+ext12);
					    FileUtils.writeByteArrayToFile(new File(Path+text1+".png"), bytes145);
					 
					    String insert1 = "insert into thumbnail (id,username,path) values (?,?,?)";
					    PreparedStatement ps1 = conn.prepareStatement(insert1);
					   
					    ps1.setString(1, "1");
					    ps1.setString(2, theString1);
					    ps1.setString(3, Path+text1+".png");
					   
					     insertrow = ps1.executeUpdate();
					    System.out.println("row inserted..."+insertrow);
				   }catch(Exception e){
				    	e.printStackTrace();
				    }
				   }
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			    System.out.println("bytearray..."+bytes14);
			    String message1 = "3";
		    	JSONObject jobj1 = new JSONObject();
		    	try {
					jobj1.put("response",message1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    	out.println(jobj1);
		}
		    	 if(insertrow>0) {
				    	try {
				    	PreparedStatement np = conn.prepareStatement("select * from deviceinformation where username=?");
						np.setString(1, theString1);
						ResultSet rst = np.executeQuery();
						while(rst.next()) {
						 String select1 = "select * from receiverdeviceinfo where deviceid = ? and status = ? ";
						 PreparedStatement ps3 = conn.prepareStatement(select1);
						 ps3.setString(1, rst.getString(2));
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
						                "Files Added"+
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
				    	  }catch(Exception e){
						    	e.printStackTrace();
						    }
				    }
  	
		 }else if(parts.size() == 5){
			 System.out.println("size is 5");
			 imagePart1 = request.getPart("image1");
			 if (imagePart1 != null) {
			        inputStream1 = imagePart1.getInputStream();    
			    }
			    byte[] bytes1 = IOUtils.toByteArray(inputStream1);  
			    System.out.println("bytearray..."+bytes1);
			    try{
				    FileUtils.writeByteArrayToFile(new File(Path+text1+".png"), bytes1);
				    
				    				    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				    
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text1+".png");
				    ps.setString(3, theString2);
				    insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				    
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			    imagePart2 = request.getPart("image2");
			    if (imagePart2 != null) {
			        inputStream2 = imagePart2.getInputStream();    
			    }
			    byte[] bytes2 = IOUtils.toByteArray(inputStream2);  
			    System.out.println("bytearray..."+bytes2);
			    
			    try{
				    FileUtils.writeByteArrayToFile(new File(Path+text2+".png"), bytes2);
				    
				    				    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				   
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text2+".png");
				    ps.setString(3, theString2);
				    insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				   
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			    
			    imagePart3 = request.getPart("image3");
			    if (imagePart3 != null) {
			        inputStream3 = imagePart3.getInputStream();    
			    }
			    byte[] bytes3 = IOUtils.toByteArray(inputStream3);  
			    System.out.println("bytearray..."+bytes3);
			    
			    try{
				    FileUtils.writeByteArrayToFile(new File(Path+text3+".png"), bytes3);
				   
				    				    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				   
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text3+".png");
				    ps.setString(3, theString2);
				    insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				   
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			    if(insertrow>0) {
			    	try {
			    	PreparedStatement np = conn.prepareStatement("select * from deviceinformation where username=?");
					np.setString(1, theString1);
					ResultSet rst = np.executeQuery();
					while(rst.next()) {
					 String select1 = "select * from receiverdeviceinfo where deviceid = ? and status = ? ";
					 PreparedStatement ps3 = conn.prepareStatement(select1);
					 ps3.setString(1, rst.getString(2));
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
					                "Files Added"+
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
			    	  }catch(Exception e){
					    	e.printStackTrace();
					    }
			    }

			    String message = "3";
		    	JSONObject jobj = new JSONObject();
		    	try {
					jobj.put("response",message);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	out.println(jobj);
			    
		 }else if(parts.size() == 6){
			 System.out.println("size is 6");
			 
			 imagePart1 = request.getPart("image1");
			 if (imagePart1 != null) {
			        inputStream1 = imagePart1.getInputStream();    
			    }
			    byte[] bytes1 = IOUtils.toByteArray(inputStream1);  
			    System.out.println("bytearray..."+bytes1);
			    try{
				    FileUtils.writeByteArrayToFile(new File(Path+text1+".png"), bytes1);
				   
				    				    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				    
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text1+".png");
				    ps.setString(3, theString2);
				    insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				    
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			    
			    imagePart2 = request.getPart("image2");
			    if (imagePart2 != null) {
			        inputStream2 = imagePart2.getInputStream();    
			    }
			    byte[] bytes2 = IOUtils.toByteArray(inputStream2);  
			    System.out.println("bytearray..."+bytes2);
			    
			    try{
				    FileUtils.writeByteArrayToFile(new File(Path+text2+".png"), bytes2);
				    
				    				    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				   
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text2+".png");
				    ps.setString(3, theString2);
				    insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				    
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			    
			    imagePart3 = request.getPart("image3");
			    if (imagePart3 != null) {
			        inputStream3 = imagePart3.getInputStream();    
			    }
			    byte[] bytes3 = IOUtils.toByteArray(inputStream3);  
			    System.out.println("bytearray..."+bytes3);
			    
			    try{
				    FileUtils.writeByteArrayToFile(new File(Path+text3+".png"), bytes3);
				    
				    				    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				   
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text3+".png");
				    ps.setString(3, theString2);
				    insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				    
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			    
			    imagePart4 = request.getPart("image4");
			    if (imagePart4 != null) {
			        inputStream4 = imagePart4.getInputStream();    
			    }
			    byte[] bytes4 = IOUtils.toByteArray(inputStream4);  
			    System.out.println("bytearray..."+bytes4);
			    
			    try{
				    FileUtils.writeByteArrayToFile(new File(Path+text4+".png"), bytes4);
				   
				    				    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				   
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text4+".png");
				    ps.setString(3, theString2);
				    insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				    
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			    if(insertrow>0) {
			    	try {
			    	PreparedStatement np = conn.prepareStatement("select * from deviceinformation where username=?");
					np.setString(1, theString1);
					ResultSet rst = np.executeQuery();
					while(rst.next()) {
					 String select1 = "select * from receiverdeviceinfo where deviceid = ? and status = ? ";
					 PreparedStatement ps3 = conn.prepareStatement(select1);
					 ps3.setString(1, rst.getString(2));
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
					                "Files Added"+
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

			     }catch(Exception e){
			    	e.printStackTrace();
			    }
			    }
			    String message = "3";
		    	JSONObject jobj = new JSONObject();
		    	try {
					jobj.put("response",message);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	out.println(jobj);
		    	
		 }else if(parts.size() == 7){
			 System.out.println("size is 7");
			 imagePart1 = request.getPart("image1");
			 if (imagePart1 != null) {
			        inputStream1 = imagePart1.getInputStream();    
			    }
			    byte[] bytes1 = IOUtils.toByteArray(inputStream1);  
			    System.out.println("bytearray..."+bytes1);
			    
			    try{
				    FileUtils.writeByteArrayToFile(new File(Path+text1+".png"), bytes1);
				    
				    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				  
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text1+".png");
				    ps.setString(3, theString2);
				    insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				   
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			    
			    imagePart2 = request.getPart("image2");
			    if (imagePart2 != null) {
			        inputStream2 = imagePart2.getInputStream();    
			    }
			    byte[] bytes2 = IOUtils.toByteArray(inputStream2);  
			    System.out.println("bytearray..."+bytes2);
			    
			    try{
				    FileUtils.writeByteArrayToFile(new File(Path+text2+".png"), bytes2);
				   
				    				    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				  
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text2+".png");
				    ps.setString(3, theString2);
				    insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				    
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			    
			    imagePart3 = request.getPart("image3");
			    if (imagePart3 != null) {
			        inputStream3 = imagePart3.getInputStream();    
			    }
			    byte[] bytes3 = IOUtils.toByteArray(inputStream3);  
			    System.out.println("bytearray..."+bytes3);
			    
			    try{
				    FileUtils.writeByteArrayToFile(new File(Path+text3+".png"), bytes3);
				   
				    				    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				   
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text3+".png");
				    ps.setString(3, theString2);
				    insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				   
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			    
			    imagePart4 = request.getPart("image4");
			    if (imagePart4 != null) {
			        inputStream4 = imagePart4.getInputStream();    
			    }
			    byte[] bytes4 = IOUtils.toByteArray(inputStream4);  
			    System.out.println("bytearray..."+bytes4);
			    
			    try{
				    FileUtils.writeByteArrayToFile(new File(Path+text4+".png"), bytes4);
				   
				    				    String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				   
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text4+".png");
				    ps.setString(3, theString2);
				    insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			    
			    imagePart5 = request.getPart("image5");
			    if (imagePart5 != null) {
			        inputStream5 = imagePart5.getInputStream();    
			    }
			    byte[] bytes5 = IOUtils.toByteArray(inputStream5);  
			    System.out.println("bytearray..."+bytes5);
			    
			    try{
				    FileUtils.writeByteArrayToFile(new File(Path+text5+".png"), bytes5);
				    
				     String insert = "insert into filesharing (username,path,timestamp) values (?,?,?)";
				    PreparedStatement ps = conn.prepareStatement(insert);
				    
				    ps.setString(1, theString1);
				    ps.setString(2, Path+text5+".png");
				    ps.setString(3, theString2);
				    insertrow = ps.executeUpdate();
				    System.out.println("row inserted..."+insertrow);
				   
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			    if(insertrow>0) {
			    	try {
			    	PreparedStatement np = conn.prepareStatement("select * from deviceinformation where username=?");
					np.setString(1, theString1);
					ResultSet rst = np.executeQuery();
					while(rst.next()) {
					 String select1 = "select * from receiverdeviceinfo where deviceid = ? and status = ? ";
					 PreparedStatement ps3 = conn.prepareStatement(select1);
					 ps3.setString(1, rst.getString(2));
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
					                "Files Added"+
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

			    	
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
			    }
			    
			    String message = "3";
		    	JSONObject jobj = new JSONObject();
		    	try {
					jobj.put("response",message);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	out.println(jobj);
		 }
		 
	 }
	if (conn != null) {
	      
	      try {
	          conn.close();
	      } catch (SQLException ex) {
	          ex.printStackTrace();
	      }
	  }
}

}