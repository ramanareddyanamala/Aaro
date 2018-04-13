package com.vedas.aaro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

@WebServlet ( urlPatterns = {"/ChangePassword"} )
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;    
    public ChangePassword() {
        super();        
    }
    Connection conn		=	null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out		=	response.getWriter();		
		String details = request.getParameter("changepassworddetails");
		String UserName=null;
		String Password=null;
		String NewPassword=null;		
		System.out.println("request parameter..."+details);
		try{
			final JSONObject obj = new JSONObject(details);			  
		    	UserName 	=	obj.getString("UserName");
			    Password 	=	obj.getString("Password");
			    NewPassword =   obj.getString("NewPassword");			    
			  }catch(JSONException e){
				System.out.println(e);
		       }		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
				e.printStackTrace();
		}		
		try {
			conn	=	DriverManager.getConnection("jdbc:mysql://localhost:3306/aaro", "root", "vedas");
		} catch (SQLException e) {		
			e.printStackTrace();
		}		
		if(EmailValidate.checkUser(UserName, Password)){
		 	try{			
				String emailquery		=	"select username,password from login where username=?";
				PreparedStatement ps	=	conn.prepareStatement(emailquery);
				ps.setString(1,UserName);
				ResultSet rs			=	ps.executeQuery();
				if(rs.next()){
					String updatequery		=	"update login set password=? where username=?";
					PreparedStatement ps1	=	conn.prepareStatement(updatequery);
					ps1.setString(1, NewPassword);
					ps1.setString(2, UserName);
					int updaterow	=	ps1.executeUpdate();
					if(updaterow>0){
						String message		=	"3";
						JSONObject jsonobj 	=	 new JSONObject();
						jsonobj.put("response", message);
						jsonobj.put("message", "Password Changed Successfully ");
						out.println(jsonobj);	
					}
					else{
						String message		=	"0";
						JSONObject jsonobj	=	new JSONObject();
						jsonobj.put("response", message);
						jsonobj.put("message", "Operation Failed");
						out.println(jsonobj);
					}
				}		
				  
			}catch (SQLException  e) {
			
			e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{				
				String message		= 	"0";
				JSONObject jsonobj	=	new JSONObject();
				try {
					jsonobj.put("response", message);
					jsonobj.put("message", "Operation Failed");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out.println(jsonobj);
			}
	}
}
