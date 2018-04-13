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

@WebServlet(urlPatterns = { "/emailaddress" })
public class ForgotPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public ForgotPassword() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init(ServletConfig config) {
       
    }
   

    Connection conn=null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out=response.getWriter();
		
		
		String details = request.getParameter("emailaddress");
		String EmailAddress=null;
		
		
		
		System.out.println("request parameter..."+details);
		
		try{
			final JSONObject obj = new JSONObject(details);
			  
			EmailAddress 	=	obj.getString("EmailAddress");
			  
			   
			    
			  }catch(JSONException e){
				System.out.println(e);
			 }
		
		
		//String emailaddress=request.getParameter("emailaddress");

		
		String from = "teamaaro.tech@gmail.com";
		String host = "smtp.gmail.com";
		
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
			
			String emailquery="select EmailAddress,Password,UserName from login where EmailAddress=?";
			
			PreparedStatement ps1=conn.prepareStatement(emailquery);
			
			ps1.setString(1, EmailAddress);
			ResultSet rs1=ps1.executeQuery();
			if(rs1.next()){
				String to=rs1.getString(1);
				String Password=rs1.getString(2);
				String UserName=rs1.getString(3);
				Properties properties = new Properties();
				properties.put("mail.smtp.auth", "true");
				properties.put("mail.smtp.starttls.enable", "true");
				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port", "587");
				Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("teamaaro.tech@gmail.com","abcd1234@#");
					}
				}
						);
				
				try {  
					 MimeMessage message = new MimeMessage(session);  
					 message.setFrom(new InternetAddress(from));  
					 message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
					 message.setSubject("Your Aaro Account password");  
					
					 String msg="<body style='background-color:'#B1998B'>"
								 + "<div class=wrap>"+
					               "<div class=logo>"+
				                    "<a href=#>"+
				               "<img src= width = 100%>"+"</a>"
				               +"</div>"
				               
				          +"</div>"
				          +"</div>"+
						 			"<table background=   width=100%>"+"</tr>"+"<tr>"+"<td align= left valign = middle   font-family:Arial, Helvetica, sans-serif;>"
								    +"<br><br>"+"<font size=4 color=#C70039>"+"<b>"+"Dear "+UserName+","+"<br><br>"
									+ "<div >"+"<font size=4 color=#000000>" +"<i>"+ "We have received a request for this email address to be registered to Aaro.In order to add you to our registered member database, we need you to confirm your request."+"<br><br>"+
									 "Please find the below verification number to confirm your registration."	+"<br><br>"	
									
									+"<font color=red>"+"<mark>"+"Your Aaro Account password is: "+"<h1>"+"<mark>"+Password+"</h1>"+"</font>"+"<br><br>"+
									
									"Also, please find below your login details for your records. We request you to keep these safe and confidential."+"<br><br>"
									
										+"<font color=#ff6600>"+"Your Login Id is: "+EmailAddress+"</font>" +"<br><br>"+
									
									"This is a computer-generated email and reply to this is not monitored."+"<br><br>"+
									"Best Wishes,"+"<br>"+
									"Aaro Team."+"<br><br>"+
									
									"Please note: If you have not attempted to get a password of Aaro Account, please ignore this email."
									+"</i>"+"</b>"+"</td>"+"</tr>"+"<tr bgcolor= white>"
									+"<td bgcolor='#564319' width=100%>"+"<tr>"+"<td>"+"<i>"+"<b>"+"<font size=4 color=#C70039>"+"Copy @ Right Reserved."+"<br>"+"please visit: www.aaro.com"+"</font>"+
									"</b>"+"</i>"+"</h2>"+"</td>"+"</tr>"+"</table>"+"</body>"
									;
					 	
					 message.setContent(msg,"text/html");
							 
			
					 //3rd step)send message  
					 Transport.send(message);  
					 
					 String success="3";
						JSONObject jsonobj = new JSONObject();
						jsonobj.put("response", success);
						jsonobj.put("message", " Password has been send to registered EmailAddress...");

						out.println(jsonobj);
					 
					  
					 } catch (MessagingException e) {  
						 e.printStackTrace();
					    throw new RuntimeException(e);  
					 } catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
		 		
		 		}else{
		 			String message="0";
					JSONObject jsonobj = new JSONObject();
					jsonobj.put("response", message);
					jsonobj.put("message", " Email Id Not Registered With Us.Please Register");

					out.println(jsonobj);
		 			
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


