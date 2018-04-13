package com.vedas.aaro;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
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

@WebServlet(urlPatterns = { "/fetch" })

	public class FetchingUserFiles extends HttpServlet {
		private static final long serialVersionUID = 1L;
	       
	   public void init(ServletConfig config) {
		   
	   }
	    public FetchingUserFiles() {
	        super();
	        // TODO Auto-generated constructor stub
	    }
	    Connection conn=null;
	   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			String details = request.getParameter("fetchdetails");
			String UserName=null;
			System.out.println("request parameter..."+details);
			try{
				final JSONObject obj = new JSONObject(details);
			    		  UserName 	=	obj.getString("UserName");
		    		     
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
					String query="select fileid,path from filesharing where username = ?";
		    	    PreparedStatement ps=conn.prepareStatement(query);
		    	    
					ps.setString(1, UserName);
					ResultSet rs = ps.executeQuery();
					JSONArray ja = new JSONArray();
					
					while(rs.next())
					{
						int fileid =rs.getInt(1);
						String path = rs.getString(2);
						System.out.println("user id's"+fileid);
						System.out.println("user path's.."+path);
						JSONObject jb1 = new JSONObject();
						String extension = path.substring(path.lastIndexOf("."));
						System.out.println(extension);
						File f = new File(path);
						System.out.println("path"+f);
						 BufferedImage image2 = ImageIO.read(f);
						   int height = image2.getHeight();
						   int width = image2.getWidth();
						   System.out.println("Height : " + height);
						   System.out.println("Width : " + width);
						   
						String n = f.getName();
						System.out.println("your filename..."+n);
						
						if(extension.equals(".png")) {
						String p1= "image";
						String image = "http://117.247.13.135:8096/static/"+n;
						System.out.println("user url array..."+image);
						jb1.put("URL", image);
						jb1.put("FileType", p1);
						jb1.put("id", fileid);
						jb1.put("height", height);
						jb1.put("width", width);
						ja.put(jb1);	
						}
					}	
					String query1="select fileid,path from filesharing where username = ?";
		    	    PreparedStatement ps1=conn.prepareStatement(query1);
		    	    
					ps1.setString(1, UserName);
					ResultSet rs1 = ps1.executeQuery();
					while(rs1.next()) {
						int fileid1 =rs1.getInt(1);
						String path1 = rs1.getString(2);
						System.out.println("user id's"+fileid1);
						System.out.println("user path's.."+path1);
						JSONObject jb1 = new JSONObject();
						String extension1 = path1.substring(path1.lastIndexOf("."));
						System.out.println(extension1);
						File f1 = new File(path1);
						System.out.println("path"+f1);
						String n1 = f1.getName();
						System.out.println("your filename..."+n1);
						
						if(extension1.equals(".mp4")) {
						
							String p2 = "video";
							String image = "http://117.247.13.135:8096/static/"+n1;
							String fileNameWithOutExt1 = FilenameUtils.removeExtension(n1);
							System.out.println("file with out extension.."+fileNameWithOutExt1);
							System.out.println("user url array..."+image);
							jb1.put("URL", image);
							jb1.put("FileType", p2);
							jb1.put("id", fileid1);
							jb1.put("thumbnail", "http://117.247.13.135:8096/static/"+fileNameWithOutExt1+".png");
							ja.put(jb1);
						}
					}
					
					
					System.out.println("url's object..."+ja);
						
						if(rs.first() && rs1.first()) {
							String message1="3";
							JSONObject jsonobj2 = new JSONObject();
							jsonobj2.put("response", message1);
							jsonobj2.put("Files", ja);
							out.println(jsonobj2);
						}else {
							String message1="0";
							JSONObject jsonobj2 = new JSONObject();
							jsonobj2.put("response", message1);
							jsonobj2.put("message", "Files is not available with this username");
							out.println(jsonobj2);
						}
						
       								
					
				 }catch (SQLException  e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						String message="0";
						JSONObject jobj = new JSONObject();
						try {
							jobj.put("response", message);
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						out.println(jobj);	
					} catch (JSONException e) {
					// TODO Auto-generated catch block
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
