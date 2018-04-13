package com.vedas.aaro;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
@WebServlet(urlPatterns = { "/regdetails" })

public class UserRegistration extends HttpServlet   {
	private static final long serialVersionUID = 1L;
   
    public UserRegistration() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init(ServletConfig config) {
       
    }
   

    Connection conn=null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out=response.getWriter();
		request.setCharacterEncoding("UTF-8");
		
		
		String detailes=request.getParameter("regdetails");
		
		String FirstName=null;
		String LastName=null;
		String EmailAddress=null;
		String Password=null;
		String Country=null;
		String UserName=null;
		System.out.println("request parameter..."+detailes);
		String pin = String.valueOf((int)(((Math.random())*1000)+1990));
		try{
			final JSONObject obj = new JSONObject(detailes);
			    FirstName	=	obj.getString("FirstName");
			    LastName 	=	obj.getString("LastName");
			    EmailAddress =	obj.getString("EmailAddress");
			    Password 	=	obj.getString("Password");
			    Country 	=	obj.getString("Country");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		try {
			String query2="select * from login where UserName=? ";
			PreparedStatement ps1=conn.prepareStatement(query2);
		 	ps1.setString(1,UserName);
		 	
		 	ResultSet rs1=ps1.executeQuery();
		 	
		 	if(rs1.next()){
		 		
		 		String message="2";
	 			JSONObject jsonobj = new JSONObject();
	 			jsonobj.put("response", message);
	 			jsonobj.put("message", "Username already exist");
	 			out.println(jsonobj);
		 	}else {
		 		String query="select * from login where EmailAddress=? ";
				PreparedStatement ps=conn.prepareStatement(query);
			 	ps.setString(1,EmailAddress);
			 	ResultSet rs=ps.executeQuery();
			 	if(rs.next()) {
			 		String message="1";
 					JSONObject jsonobj = new JSONObject();
 					jsonobj.put("response", message);
 					jsonobj.put("message", "Email ID already exist");
 					out.println(jsonobj);
		 	
			       }else{
		
			
				final String username1 = "teamaaro.tech@gmail.com";
				final String password1 = "abcd1234@#";

				Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", "smtp.gmail.com");
				props.put("mail.smtp.port", "587");

				Session session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username1, password1);
					}
				  });

				try {

					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress("teamaaro.tech@gmail.com"));
					message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(EmailAddress));
					 
					 String msg="<body style='background-color:'#B1998B'>"
								 + "<div class=wrap>"+
					               "<div class=logo>"+
				                    "<a href=#>"+
				               "<img src=http://52.90.98.131/Image/image?id=21 width = 100%>"+"</a>"
				               +"</div>"
				               
				          +"</div>"
				          +"</div>"+
						 			"<table background=  http://52.90.98.131/Image/image?id=17  width=100%>"+"</tr>"+"<tr>"+"<td align= left valign = middle   font-family:Arial, Helvetica, sans-serif;>"
								    +"<br><br>"+"<font size=4 color=#C70039>"+"<b>"+"Dear "+UserName+","+"<br><br>"
									+ "<div >"+"<font size=4 color=#000000>" +"<i>"+ "We have received a request for this email address to be registered to Aaro.In order to add you to our registered member database, we need you to confirm your request."+"<br><br>"+
									 "Please find the below verification number to confirm your registration."	+"<br><br>"	
									
									+"<font color=red>"+"<mark>"+"Your Aaro Account verification number is: "+"<h1>"+"<mark>"+pin+"</h1>"+"</font>"+"<br><br>"+
									
									"Also, please find below your login details for your records. We request you to keep these safe and confidential."+"<br><br>"
									
										+"<font color=#ff6600>"+"Your Login Id is: "+EmailAddress+"</font>" +"<br><br>"+
									
									"This is a computer-generated email and reply to this is not monitored."+"<br><br>"+
									"Best Wishes,"+"<br>"+
									"Aaro Team."+"<br><br>"+
									
									"Please note: If you have not attempted to register with Aaro, please ignore this email."
									+"</i>"+"</b>"+"</td>"+"</tr>"+"<tr bgcolor= white>"
									+"<td bgcolor='#564319' width=100%>"+"<tr>"+"<td>"+"<i>"+"<b>"+"</b>"+"</i>"+"</h2>"+"</td>"+"</tr>"+"</table>"+"</body>"
									;
					message.setSubject("Registration confirmation!!");
					message.setContent(msg,"text/html");

					Transport.send(message);
						
								String success="3";
								JSONObject jsonobj = new JSONObject();
								jsonobj.put("response", success);
								jsonobj.put("message", "Registration successfull");
								out.println(jsonobj);
							
						
							
						    String phonequery="select EmailAddress from pindetailes where EmailAddress=?";
							PreparedStatement ps12=conn.prepareStatement(phonequery);
							ps12.setString(1, EmailAddress);
							ResultSet rs12=ps12.executeQuery();
							if(rs12.next()){
								String update="update pindetailes set pinnumber=? where EmailAddress=?";
			            		PreparedStatement ps2=conn.prepareStatement(update);
			    				ps2.setString(1, pin);
			    				ps2.setString(2, EmailAddress);
			    				int updatecount=ps2.executeUpdate();
			    				System.out.println(updatecount);
			    				}else{
								String insertpin="INSERT INTO pindetailes (EmailAddress  , pinnumber ) values (?, ? )";
				            	PreparedStatement ps3=conn.prepareStatement(insertpin);
				            	ps3.setString(1, EmailAddress);
				            	ps3.setString(2, pin);
								int insert=ps3.executeUpdate();
								System.out.println(insert);
			    				}
							
						 } catch (MessagingException e) {  
							 e.printStackTrace();
						    throw new RuntimeException(e);  
						 } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		 	}
		 	}
	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{

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
