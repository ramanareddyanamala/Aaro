package com.vedas.aaro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;


@WebServlet ( urlPatterns = {"/devicedelete"} )
public class AaroDeviceInformationDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;    
    public AaroDeviceInformationDelete() {
        super();        
    }
    Connection conn		=	null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out		=	response.getWriter();		
		String details = request.getParameter("deletedevice");
		String  deviceid = null;
				
		System.out.println("request parameter..."+details);
		try{
			final JSONObject obj = new JSONObject(details);			  
			deviceid 	=	obj.getString("deviceid");
			   	    
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
		
		
		try {
		String check = "select * from deviceinformation where deviceid = ? ";
		PreparedStatement pstmt = conn.prepareStatement(check);
		pstmt.setString(1, deviceid);
		
		java.sql.ResultSet verify = pstmt.executeQuery();
		if(verify.next()) {
			
	
		
		 	try{			
				String query		=	" delete  from deviceinformation where deviceid=? ";
				PreparedStatement ps	=	conn.prepareStatement(query);
				ps.setString(1,deviceid);
					int updaterow	=	ps.executeUpdate();
					if(updaterow>0){
						String message		=	"3";
						JSONObject jsonobj 	=	 new JSONObject();
						jsonobj.put("response", message);
						jsonobj.put("message", "Device Data was Deleted successfully...");
						out.println(jsonobj);	
					}
					else{
						String message		=	"0";
						JSONObject jsonobj	=	new JSONObject();
						jsonobj.put("response", message);
						jsonobj.put("message", "Device Data Deletion is Failure...");
						out.println(jsonobj);
					}
						
				  
			}catch (SQLException e) {
			
			e.printStackTrace();
			}
		 	
		}
		else {
			
			String message		=	"2";
			JSONObject jsonobj 	=	 new JSONObject();
			jsonobj.put("response", message);
			jsonobj.put("message", "Ur Selected Device Data Not available/founded at an our Database");
			out.println(jsonobj);	
		}
		 	
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
		
	}
}
