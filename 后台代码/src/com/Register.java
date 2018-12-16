package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import static sql.sqlconnect.SQLconnect;
import static sql.sqlconnect.RegisterUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class Register
 */

@WebServlet("/api/register")
public class Register extends HttpServlet {
	public int status = 0;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 String json = readJSONString(request);
	     System.out.println("get"+json);
		
		
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
        System.out.println(name);
        System.out.println(pwd);
     

        try {
            SQLconnect();
            status = RegisterUser(name,pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String out = toString1();
        System.out.println("注册功能返回"+out);

        //转化为Json并上传
        response.setContentType("text/html;charset=gb2312");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(out);
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
