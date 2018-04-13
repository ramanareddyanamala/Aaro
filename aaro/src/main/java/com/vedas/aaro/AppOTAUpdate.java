package com.vedas.aaro;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(urlPatterns = { "/OTA" })
@MultipartConfig(maxFileSize = 20971520)

public class AppOTAUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   public void init(ServletConfig config) {
	   
   }
    public AppOTAUpdate() {
        super();
        // TODO Auto-generated constructor stub
    }
    Connection conn=null;
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		 InputStream inputStream = null,series,tstamp = null;
		    Part version = request.getPart("version");
		    Part timestamp= request.getPart("timestamp");
		    
		    series = version.getInputStream();
		    String theString1 = IOUtils.toString(series, StandardCharsets.UTF_8.name()); 
		 
		    
		    tstamp = timestamp.getInputStream();
		    String theString2 = IOUtils.toString(tstamp, StandardCharsets.UTF_8.name()); 
		    
		     
		    
		    Part filePart = request.getPart("Apk");
		    System.out.println("Apk data..."+filePart.getSubmittedFileName());
		    if (filePart != null) {
		        // prints out some information for debugging
		       
		         
		        // obtains input stream of the upload file
		        inputStream = filePart.getInputStream();
		        
		    }
		    String Path ="C:\\images\\";
		    String serverkey = "AIzaSyBOEYBodORKsGnyFUVDTw1wJH-qk_iApnk";
			
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
		 byte[] bytes1 = IOUtils.toByteArray(inputStream);
		try {
			FileUtils.writeByteArrayToFile(new File(Path+filePart.getSubmittedFileName()), bytes1);
			String ota = "update appota set path = ?, version = ?, timestamp = ? where id = ? ";
			PreparedStatement ps  = conn.prepareStatement(ota);
			ps.setString(1, Path+filePart.getSubmittedFileName());
			ps.setString(2, theString1);
			ps.setString(3, theString2);
			ps.setString(4, "1");
			
			int rowupdate = ps.executeUpdate();
			if(rowupdate>0) {
					
				    PreparedStatement ps1 = conn.prepareStatement("select * from receiverdeviceinfo where status = ?");
				    ps1.setString(1, "1");
				    ResultSet rs = ps1.executeQuery();
				    while(rs.next()) {
				    	String device_token = rs.getString(3);
						 
						 System.out.println("Device Token..."+device_token);
						 
						 String title = "Aaro Notification";
							
					        String pushMessage = "{\"notification\":{\"title\":\"" +
					                title +
					                "\",\"body\":\"" +
					                "Upadate Available"+
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
				    String msg = "3";
					JSONObject jb = new JSONObject();
					jb.put("response", msg);
					out.println(jb);
				    
				
				
			}else {
				String msg = "0";
				JSONObject jb = new JSONObject();
				jb.put("response", msg);
				out.println(jb);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
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