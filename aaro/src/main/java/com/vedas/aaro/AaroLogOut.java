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
import org.json.JSONObject;

@WebServlet(urlPatterns = {"/logout"})
public class AaroLogOut extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public AaroLogOut() {
        super();
        // TODO Auto-generated constructor stub
    }

    Connection conn=null;
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		String logindetailes=request.getParameter("logoutstatus");
		System.out.println("loggedout status..."+logindetailes);
		String username=null;
		String deviceid=null;
		String devicetoken=null;
		System.out.println("device id...."+deviceid);
		System.out.println("device token..."+devicetoken);
		
		try {
			final JSONObject obj = new JSONObject(logindetailes);
			username = obj.getString("username");
			deviceid = obj.getString("deviceid");
			devicetoken = obj.getString("devicetoken");
			
		}catch(Exception e) {
			e.printStackTrace();
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
		
		try{
			String q1 = "update pushnotification set status=? where deviceid=? and username =?";
			PreparedStatement ps=conn.prepareStatement(q1);
			ps.setString(1, "0");
			ps.setString(2,deviceid);
			ps.setString(3, username);
			int r=ps.executeUpdate();
			if(r>0){
     			System.out.println("user successfully loggedout");
				String msg = "3";
				JSONObject jb = new JSONObject();
				jb.put("response", msg);
				jb.put("message", "user successfully loggedout");
				out.print(jb);
			}else{
				String msg = "0";
				JSONObject jb = new JSONObject();
				jb.put("response", msg);
				jb.put("message", "Loggedout Failed");
				out.print(jb);
			}
			
		}catch(Exception e){
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
