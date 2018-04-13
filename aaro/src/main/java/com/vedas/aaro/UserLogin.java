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

@WebServlet(urlPatterns = { "/UserLogin" })

	public class UserLogin extends HttpServlet {
		private static final long serialVersionUID = 1L;
	       
	   public void init(ServletConfig config) {
		   
	   }
	    public UserLogin() {
	        super();
	        // TODO Auto-generated constructor stub
	    }
	    Connection conn=null;
	   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			String details = request.getParameter("logindetails");
			String UserName=null;
			String Password=null;
			String EmailAddress = null;
			String deviceid = null;
			String devicetoken = null;
			System.out.println("request parameter..."+details);
			try{
				final JSONObject obj = new JSONObject(details);
			    		  UserName 	=	obj.getString("UserName");
		    		      Password 	=	obj.getString("Password");
					   	  if(UserName==null) {
						  UserName = obj.getString("EmailAddress");
						  deviceid = obj.getString("DeviceId");
						  devicetoken = obj.getString("DeviceToken");
				          }
				    
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
			System.out.println("Password..."+Password);
			if(EmailValidate.checkUser(UserName, Password)){
				 try {
					String query="select UserName,Password,EmailAddress,FirstName,LastName,Country from login where (UserName=? or EmailAddress=?) and Password=? ";
		    	    PreparedStatement ps=conn.prepareStatement(query);
					ps.setString(1, UserName);
					ps.setString(2, UserName);
					ps.setString(3, Password);
					ResultSet rs = ps.executeQuery();
					while(rs.next())
					{
						String username1 =rs.getString("UserName");
						String password1 = rs.getString("Password");
						String EmailAddress1 =rs.getString("EmailAddress");
						String FirstName = rs.getString("FirstName");
						String LastName = rs.getString("LastName");
						String Country = rs.getString("Country");
						
						String push = "select * from pushnotification where username=? and deviceid=?";
						PreparedStatement ps19=conn.prepareStatement(push);
						ps19.setString(1, EmailAddress);
						ps19.setString(2, deviceid);
						ResultSet rs1=ps19.executeQuery();
						//System.out.println(rs);
						if(rs1.next()){
							
								PreparedStatement ps20=conn.prepareStatement("update pushnotification set username=?, devicetoken = ?,status=? where deviceid = ? and username=?");
								ps20.setString(1, EmailAddress);
								ps20.setString(2, devicetoken);
								ps20.setString(3, "1");
								ps20.setString(4, deviceid);
								ps20.setString(5, EmailAddress);
								int r = ps20.executeUpdate();
								System.out.println("row update..."+r);
							
								
						}else{
							String push1 = "insert into pushnotification(username,deviceid,devicetoken,status)values (?,?,?,?)";
							PreparedStatement ps21=conn.prepareStatement(push1);
							ps21.setString(1, EmailAddress);
							ps21.setString(2, deviceid);
							ps21.setString(3, devicetoken);
							ps21.setString(4, "1");
							
							int row1 =ps21.executeUpdate();
							System.out.println("row inserted..."+row1);
						}
						
						
						String message1="3";
						JSONObject jsonobj2 = new JSONObject();
						jsonobj2.put("response", message1);
						jsonobj2.put("message", "User login Successfully...");
						jsonobj2.put("UserName", username1);
						jsonobj2.put("Password", password1);
						jsonobj2.put("EmailAddress", EmailAddress1);
						jsonobj2.put("FirstName", FirstName);
						jsonobj2.put("LastName", LastName);
						jsonobj2.put("Country", Country);
						out.println(jsonobj2);
       				}					
					
				 } catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }
					
			 else{
				 try {
					 String message="0";
					 JSONObject jsonobj = new JSONObject();
					
					jsonobj.put("response", message);
					jsonobj.put("message", "Not a valid user");

					out.println(jsonobj);
								
				 } catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 
			}
		}

	}
