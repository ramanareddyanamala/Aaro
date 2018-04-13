package com.vedas.aaro;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

@WebServlet(urlPatterns = { "/fetching" })
@MultipartConfig(maxFileSize = 2024288000)
public class FetchingFiles extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public final static int SOCKET_PORT = 13262;  // you may change this
	  
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost:3306/aaro";

	   static final String USER = "root";
	   static final String PASS = "vedas";
	
	public void init(ServletConfig config) {
}
       
   
    public FetchingFiles() {
        super();
        // TODO Auto-generated constructor stub
    }
    Connection conn=null;
    /*
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("Content-Type", "video/mp4/MKV"); 
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
try {
PreparedStatement ps = conn.prepareStatement(" select path from filesharing where fileid = ? ");
String s =request.getQueryString();
System.out.println("query string value.."+s);
ps.setString(1, s);
ResultSet rs1=ps.executeQuery();
byte[] imgLen = null;
String p="";
if(rs1.next()){
	p = rs1.getString(1);
	InputStream is = new FileInputStream(new File(p));
  imgLen = IOUtils.toByteArray(is);
System.out.println(imgLen.length);
} 
PreparedStatement ps1 = conn.prepareStatement(" select * from filesharing where fileid = ? ");
String s1 =request.getQueryString();
System.out.println("query string value.."+s1);
ps1.setString(1, s1);
rs1 = ps1.executeQuery();
if(rs1.next()){
	String url = rs1.getString(4);
	System.out.println("path from db...."+rs1.getString(3));
int len = imgLen.length;
byte [] rb = new byte[len];
File f = new File(rs1.getString(3));
System.out.println("file..."+f);
//InputStream readImg = rs1.getBinaryStream(1);
@SuppressWarnings("resource")
InputStream readImg = new FileInputStream(f);
int index=readImg.read(rb, 0, len); 
System.out.println("index"+index);
response.setHeader("Content-Length", Long.toString(index)); 
response.setHeader("Content-Disposition", "inline; filename=\"" + f.getName() + "\""); 
//response.setContentType("text/html");
//String contentType = getServletContext().getMimeType(f.getName()); 
//System.out.println("content type..."+contentType);
//response.setBufferSize(index); 
//response.setHeader("Content-Type", ""); 

//response.reset();

response.getOutputStream().write(rb,0,len);
response.getOutputStream().flush(); 
//PrintWriter out = response.getWriter();

}else {
	System.out.println(" selected fileid not available in DB");
}
} catch (Exception e){
e.printStackTrace();
}
}
*/
    protected void doGet(HttpServletRequest request,
    		   HttpServletResponse response) throws ServletException, IOException {
    	try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		try {
		PreparedStatement ps1 = conn.prepareStatement(" select * from filesharing where fileid = ? ");
		
		String s1 =request.getQueryString();
		
		System.out.println("query string value.."+s1);
		String extension = s1.substring(s1.lastIndexOf("."));
		System.out.println(extension);
		ps1.setString(1, s1);
		ResultSet rs1 = ps1.executeQuery();
		if(rs1.next()){
			//String url = rs1.getString(4);
			System.out.println("path from db...."+rs1.getString(3));

    		  File f = new File(rs1.getString(3));
    		  
    		  //ServletContext ct = getServletContext();
    		  //InputStream input = ct.getResourceAsStream(fileName);
    		  String ext1 = FilenameUtils.getExtension(rs1.getString(3));
    		  System.out.println("file extension.."+ext1);
    		  if(extension.equals("."+ext1)) {
    			 
    		  InputStream input = new FileInputStream(f);
    		  /*
    		  if(ext1.equalsIgnoreCase("mp4")|| (ext1.equalsIgnoreCase("mkv") || (ext1.equalsIgnoreCase("MOV")))){
    		   response.setContentType("video/mp4"); //Use this for VLC player
    		  }*/
    		  if(ext1.equalsIgnoreCase("mp4")) {
    			  response.setContentType("video/mp4");
    		  }else if(ext1.equalsIgnoreCase("mkv")) {
    			  response.setContentType("video/quicktime");
    		  }else if(ext1.equalsIgnoreCase("MOV")) {
    			  response.setContentType("video/quicktime");
    		  }
    		  
    		  //response.setContentType("video/mp4");

    		  response.setHeader("Content-Disposition", "inline; filename=\""
    		    + f + "\"");
    		  OutputStream output = response.getOutputStream();

    		  byte[] buffer = new byte[512000];
    		  int read = 0;
    		  while ((read = input.read(buffer)) != -1) {
    		   output.write(buffer, 0, read);
    		  }
    		  input.close();
    		  output.flush();
    		  output.close();

    		 }else {
    			 System.out.println("no such file in database");
    		 }
		}else {
			System.out.println("no file with this id");
		}
    
	}catch(Exception e) {
		e.printStackTrace();
	}
 }
}
