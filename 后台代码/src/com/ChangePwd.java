package com;

import static sql.sqlconnect.RegisterUser;
import static sql.sqlconnect.ChangePwd;
import static sql.sqlconnect.SQLconnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class ChangePwd
 */
@WebServlet("/ChangePwd")
public class ChangePwd extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private int status=0;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePwd() {
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
	    String name = jsonObject.getString("username");
	    String old_pwd = jsonObject.getString("old_password");
	    String new_pwd = jsonObject.getString("new_password");
	    
	    System.out.println(name);
	    System.out.println(old_pwd);
	    System.out.println(new_pwd);
	    

        try {
            SQLconnect();
            status = ChangePwd(name,old_pwd,new_pwd);
        } catch (Exception e) {
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
            test.put("status",status);         //   状态
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
