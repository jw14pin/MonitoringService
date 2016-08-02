package com.filter;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.memory.KeyDB;

/*
 * md5sum for now, but there are better encryption tools out there
 */
public class Authentication implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String username = "admin";
		String password = "123456";
		String path = ((HttpServletRequest) request).getRequestURI();
		
		if(path.startsWith("/MonitoringService/rest/application.wadl")) {
			chain.doFilter(request, response);
		} else {
		
			//requires a key
			if(request.getParameter("username") != null && request.getParameter("username").equals(username) && request.getParameter("password") != null && request.getParameter("password").equals(password)) {
				//needs a key
				MessageDigest md = null;
				try {
					md = MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
		        md.update((request.getParameter("password")+new Date().toString()).getBytes());
		 
		        byte byteData[] = md.digest();
		 
		        //convert the byte to hex format method 1
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < byteData.length; i++) {
		        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		        }
		        
		        KeyDB.addKey(sb.toString());
		        
		        response.getWriter().append(sb.toString()).flush();
		        
		        return;
			} else if(!(request.getParameter("key") != null && KeyDB.containsKey(request.getParameter("key")))) {
				request.setAttribute("errormessage", "InvalidKey");
				request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
				return;
			}
			
			chain.doFilter(request, response);
		}
	
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
}
