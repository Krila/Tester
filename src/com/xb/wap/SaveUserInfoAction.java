package com.xb.wap;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xb.dao.JdbcUtils;

/***
 * �����û���Ϣ
 * 
 * @author Ѷ������
 *
 */
public class SaveUserInfoAction extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//�Ƿ�֧��wap����
		String accepttype = request.getHeader("accept");
		String customType = null;
		if(accepttype.indexOf("application/xhtml+xml")>-1
				||accepttype.indexOf("application/xml")>-1){
			//֧��wap��ʽ�ķ���
			customType = "application/xhtml+xml;charset=utf-8";
		}else{
			//��֧��wap��ʽ�ķ���
			customType = "text/html;charset=utf-8";
		}
		request.setAttribute("customType", customType);
		
		String username = request.getParameter("username");
		String age = request.getParameter("age");
		String sex = request.getParameter("sex");
		String phone = request.getParameter("phone");
		
		StringBuffer buffer = new StringBuffer();
		boolean flag = true;
		if(null==username||username.length()<1){
			buffer.append("����д����������<br/>");
			flag = false;
		}
		if(flag && phone.matches("^\\d{11}$")){
			JdbcUtils jdbcutils = JdbcUtils.instance();
			Connection conn =  jdbcutils.loadConn();
			try {
				PreparedStatement ptst = conn.prepareStatement("insert into beauty_adver(username,age,sex,phone,create_time) values(?,?,?,?,?)");
				ptst.setString(1,username);
				ptst.setString(2,age);
				ptst.setString(3,sex);
				ptst.setString(4,phone);
				ptst.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
				ptst.executeUpdate();
				request.getRequestDispatcher("result-success.jsp").forward(request, response);
				
				saveVisiteLog(request);
				return;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			buffer.append("�ֻ������ʽ����<br/>");
		}
		request.setAttribute("errormsg", buffer);
		request.getRequestDispatcher("result.jsp").forward(request, response);
	}
	
	public void saveVisiteLog(HttpServletRequest request){
		//�жϿͻ��˷��ʹ���
		String userAgent = request.getHeader("user-agent");
		String browserType = "iPad";
		if(userAgent.indexOf("iPad")>-1){//ipad = ƽ��Ĵ���
			browserType = "iPad";
		}else if(userAgent.indexOf("iPhone")>-1){//iphone = �ֻ��Ĵ���
			browserType = "iPhone";
		}else if(userAgent.indexOf("Android")>-1){//android�ֻ�
			browserType = "Android";
		}else{
			browserType = "�����ܻ�";
		}
		String pageType=request.getParameter("pageType");//iphneҳipadҳ
		
		JdbcUtils jdbcUtils = JdbcUtils.instance();
		Connection conn = jdbcUtils.loadConn();
		
		String sql = "insert into beauty_visite(visiteType,visitTime,phoneType,ip,useragent) values(?,?,?,?,?)";
		try {
			PreparedStatement ptst = conn.prepareStatement(sql);
			ptst.setObject(1, pageType);
			ptst.setObject(2, new Date());
			ptst.setObject(3, browserType);
			ptst.setObject(4, this.getIpAddr(request));
			ptst.setObject(5, this.requestHeader(request));
			ptst.executeUpdate();
			JdbcUtils.closeResource(conn, ptst);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getIpAddr(HttpServletRequest request) {
	       String ip = request.getHeader("x-forwarded-for");
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	          ip = request.getHeader("Proxy-Client-IP");
	       }
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	          ip = request.getHeader("WL-Proxy-Client-IP");
	       }
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	          ip = request.getRemoteAddr();
	       }
	       return ip;
	}
	
	public String requestHeader(HttpServletRequest request){
		Enumeration<String> headers =  request.getHeaderNames();
		String name = null;
		StringBuffer buffer = new StringBuffer();
//		while(headers.hasMoreElements()){
//		name = headers.nextElement();
//		buffer.append(name+"="+request.getHeader(name)+",");
//	}
	buffer.append(request.getHeader("user-agent"));
		return buffer.toString();
	}
}
