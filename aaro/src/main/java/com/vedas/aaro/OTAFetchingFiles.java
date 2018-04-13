package com.vedas.aaro;

import java.io.File;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(urlPatterns = { "/otafetch1" })

	public class OTAFetchingFiles extends HttpServlet {
		private static final long serialVersionUID = 1L;
	       
	   public void init(ServletConfig config) {
		   
	   }
	    public OTAFetchingFiles() {
	        super();
	        // TODO Auto-generated constructor stub
	    }
	    Connection conn=null;
	   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			String details = request.getParameter("otadetails");
			String version=null;
			//String id = null;
			System.out.println("request parameter..."+details);
			try{
				final JSONObject obj = new JSONObject(details);
			    		  version 	=	obj.getString("version");
		    		     
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
					String query="select * from appota where id = ?";
		    	    PreparedStatement ps=conn.prepareStatement(query);
		    	    
					ps.setString(1, "1");
					ResultSet rs = ps.executeQuery();
					
					
					if(rs.next())
					{
						String version1 =rs.getString(3);
						int fileid = rs.getInt(1);
						System.out.println("user id's"+version1);
						String path = rs.getString(2);
						File f = new File(path);
						System.out.println("path"+f);
						String n = f.getName();
						if(version1.equals(version)) {
							String message1="0";
							JSONObject jsonobj2 = new JSONObject();
							jsonobj2.put("response", message1);
							jsonobj2.put("message", "Same Version");
							out.println(jsonobj2);
							
						}else {
							JSONObject jb1 = new JSONObject();
							String msg = "3";
							String image = "http://117.247.13.135:8096/static/"+n;
							System.out.println("user url array..."+image);
							jb1.put("URL", image);
							jb1.put("id", fileid);
							jb1.put("version", version1);
							jb1.put("response", msg);
							out.println(jb1);
						}
						
					}
					
				 } catch ( SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
