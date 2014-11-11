package com.xb.wap;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IpadAction extends HttpServlet {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
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
		request.getRequestDispatcher("ipad.jsp").forward(request, response);
	}
}
