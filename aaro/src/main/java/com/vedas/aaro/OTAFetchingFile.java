package com.vedas.aaro;

import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

@WebServlet(urlPatterns = { "/otafetch" })
public class OTAFetchingFile extends HttpServlet{
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost:3306/aaro";

	   static final String USER = "root";
	   static final String PASS = "vedas";
	private static final long serialVersionUID = 1L;
    public OTAFetchingFile() {
        super();
        // TODO Auto-generated constructor stub
    }
//public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
//
//Connection con=null;
//try {
//			Class.forName("com.mysql.jdbc.Driver");
//		} catch (ClassNotFoundException e) {
//			
//			e.printStackTrace();
//		}
//		
//		try {
//			con = DriverManager.getConnection(DB_URL, USER, PASS);
//		} catch (SQLException e) {
//			
//			e.printStackTrace();
//		}

//PreparedStatement ps = con.prepareStatement(" select path from appota where id = ? ");
//String s =request.getQueryString();
//System.out.println("query string value.."+s);
//ps.setString(1, s);
//ResultSet rs1=ps.executeQuery();
//byte[] imgLen = null;
//String p="";
//if(rs1.next()){
//	p = rs1.getString(1);
//	InputStream is = new FileInputStream(new File(p));
//  imgLen = IOUtils.toByteArray(is);
//System.out.println(imgLen.length);
//} 
//PreparedStatement ps1 = con.prepareStatement(" select * from appota where id = ? ");
//String s1 =request.getQueryString();
//System.out.println("query string value.."+s1);
//ps1.setString(1, s1);
//rs1 = ps1.executeQuery();
//if(rs1.next()){
//	System.out.println("path from db...."+rs1.getString(2));
//int len = imgLen.length;
//
//byte [] rb = new byte[len];
//File f = new File(rs1.getString(2));
//System.out.println("file..."+f);
////InputStream readImg = rs1.getBinaryStream(1);
//InputStream readImg = new FileInputStream(f);
//int index=readImg.read(rb, 0, len); 
//System.out.println("index"+index);
////response.reset();
//response.setContentType("app-debug.apk");
//response.sendRedirect(request.getContextPath()+"/report.jsp");
//
//response.getOutputStream().write(rb,0,len);
//response.getOutputStream().flush(); 
//response.getOutputStream().close();
//}else {
//	System.out.println(" selected fileid not available in DB");
//}
	protected void doGet(HttpServletRequest request,
 		   HttpServletResponse response) throws ServletException, IOException {
		Connection conn=null;
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
		PreparedStatement ps1 = conn.prepareStatement(" select * from appota where id = ? ");
		String s1 =request.getQueryString();
		System.out.println("query string value.."+s1);
		ps1.setString(1, s1);
		ResultSet rs1 = ps1.executeQuery();
		if(rs1.next()){
			//String url = rs1.getString(4);
			System.out.println("path from db...."+rs1.getString(2));

 		  File f = new File(rs1.getString(2));
 		  //ServletContext ct = getServletContext();
 		  //InputStream input = ct.getResourceAsStream(fileName);
 		  String ext1 = FilenameUtils.getExtension(rs1.getString(2));
 		 String ext2 = FilenameUtils.getName(rs1.getString(2));
 		  String x = FilenameUtils.getName(ext2);
 		 String fileNameWithOutExt = FilenameUtils.removeExtension(x);
			System.out.println("file with out extension.."+fileNameWithOutExt);
			
 		  System.out.println("file extension.."+ext1);
 		 System.out.println("file name.."+ext2);
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
 		 + fileNameWithOutExt + "\".apk");
 		  OutputStream output = response.getOutputStream();

 		  byte[] buffer = new byte[512];
 		  int read = 0;
 		  while ((read = input.read(buffer)) != -1) {
 		   output.write(buffer, 0, read);
 		  }
 		  input.close();
 		  output.flush();
 		  output.close();

 		 }
 
	}catch(Exception e) {
		e.printStackTrace();
	}


}
}