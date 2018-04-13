package com.vedas.aaro;

import java.io.File;
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

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(urlPatterns = { "/deletefiles" })

	public class DeleteFiles extends HttpServlet {
		private static final long serialVersionUID = 1L;
	       
	   public void init(ServletConfig config) {
		   
	   }
	    public DeleteFiles() {
	        super();
	        // TODO Auto-generated constructor stub
	    }
	    Connection conn=null;
	   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			String details = request.getParameter("delete");
			
			String fileid = null;
			String username = null;
			//String deviceid = null;
			String serverkey = "AIzaSyBOEYBodORKsGnyFUVDTw1wJH-qk_iApnk";
			
			System.out.println("request parameter..."+details);
			try{
				final JSONObject obj = new JSONObject(details);
			    		fileid = obj.getString("fileid");
			    		username = obj.getString("username");  		    		     
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
			int rowdelete=0;
			String [] s1 = fileid.split(",");
			for(int i=0;i<s1.length;i++) {
				
			int id = Integer.parseInt(s1[i]);
			
				 try {
					 
					String delete = "select * from filesharing where fileid = ?";
					PreparedStatement ps = conn.prepareStatement(delete);
					ps.setInt(1, id);
					ResultSet rs = ps.executeQuery();
					if(rs.next()) {
								int id1 = rs.getInt(1);
								System.out.println("id value..."+id1);
								String path = rs.getString(3);
								System.out.println("fileid path..."+path);
								File f = new File(path);
								
								if(f.delete()) {
									System.out.println("file deleted succesfully from path folder");
								}else {
									System.out.println("file not deleted succesfully from path folder");
								}
								PreparedStatement ps1=conn.prepareStatement("delete from filesharing where fileid=?");
								ps1.setInt(1, id);
								
								 rowdelete = ps1.executeUpdate();
								 
					}

				 } catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			JSONArray ja1 = new JSONArray();
			if(rowdelete>0) {
				 

					try {
						String select = "select * from filesharing where username = ?";
						PreparedStatement ps = conn.prepareStatement(select);
						ps.setString(1, username);
						ResultSet rs = ps.executeQuery();
						
						
						while(rs.next())
						{
							int id =rs.getInt(1);
							String path1 = rs.getString(3);
							System.out.println("user id's"+id);
							System.out.println("user path's.."+path1);
							
							String extension = path1.substring(path1.lastIndexOf("."));
							System.out.println(extension);
							File f = new File(path1);
							System.out.println("path"+f);
							String n = f.getName();
							System.out.println("your filename..."+n);
							
							JSONObject jb2 = new JSONObject();
							if(extension.equals(".png")) {
							String p1= "image";
							String image = "http://117.247.13.135:8096/static/"+n;
							System.out.println("user url array..."+image);
							jb2.put("URL", image);
							jb2.put("FileType", p1);
							jb2.put("id", id);
							ja1.put(jb2);	
							}else {
								String p2 = "video";
								String image = "http://117.247.13.135:8096/static/"+n;
								String fileNameWithOutExt = FilenameUtils.removeExtension(n);
								System.out.println("file with out extension.."+fileNameWithOutExt);
								System.out.println("user url array..."+image);
								jb2.put("URL", image);
								jb2.put("FileType", p2);
								jb2.put("id", id);
								jb2.put("thumbnail", "http://117.247.13.135:8096/static/"+fileNameWithOutExt+".png");
								ja1.put(jb2);
							}
						}
						
						
					}catch(Exception e) {
						e.printStackTrace();
					}
			try {
				PreparedStatement np = conn.prepareStatement("select * from deviceinformation where username=?");
				np.setString(1, username);
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
				                "Files Deleted"+
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
			
					}catch(Exception e) {
						e.printStackTrace();
					}
					
				    String msg = "3";
				    JSONObject jsonobj = new JSONObject();
				   	try {
						jsonobj.put("response", msg);
						jsonobj.put("RemainingFiles", ja1);
						jsonobj.put("message"," File Deleted Successfully!!!");
					   
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				   	
	        	 out.println(jsonobj);
			
		    }else {
			       String e= "0" ;
			       JSONObject jsonobj = new JSONObject();
			       try {
					jsonobj.put("response", e);
					jsonobj.put("message"," File Not Deleted Successfully!!!");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			       out.println(jsonobj);
		        }
			
		}

	}
