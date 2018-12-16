package com;
import java.io.BufferedReader;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
//import sun.nio.ch.IOUtil;
//import sun.nio.ch.IOUtil;

import java.sql.*;
import static sql.sqlconnect.SQLconnect;
import static sql.sqlconnect.SearchUser;


/**
 * Servlet implementation class Login
 */
@WebServlet("/api/users")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public ResultSet resultset = null;
	private int status = 0;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

        //解析Json
        String json = readJSONString(request);
        System.out.println(json);
        JSONObject jsonObject = new JSONObject(json);
        String name = jsonObject.getString("name");
        String pwd = jsonObject.getString("password");
      
        try {
            SQLconnect();
            status = SearchUser(name,pwd);
            //resultset = userSetting(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //转化为Json并上传
        response.setContentType("text/html;charset=gb2312");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(toString1());
	}
	
	  private String readJSONString(HttpServletRequest request){
	        StringBuffer json = new StringBuffer();
	        String line = null;
	        try {
	            BufferedReader reader = request.getReader();
	            while((line = reader.readLine()) != null) {
	                json.append(line);
	            }
	        }
	        catch(Exception e) {
	            System.out.println(e.toString());
	        }
	        return json.toString();
	    }



	    @SuppressWarnings("finally")
		public String toString1()
	    {
	        JSONObject test=new JSONObject();
	        try {
	            test.put("state",status);         //   状态
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        finally
	        {
	            System.out.println(test.toString());
	            return test.toString();
	        }
	    }

}
