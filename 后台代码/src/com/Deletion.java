package com;

import static sql.sqlconnect.SQLconnect;
import static sql.sqlconnect.DropGoods;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class Deletion
 */
@WebServlet("/api/withdrawform")
public class Deletion extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private int status = 0;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Deletion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//解析Json
		
		request.setCharacterEncoding("UTF-8");
        String json = readJSONString(request);
        System.out.println(json);
        
        JSONObject jsonObject = new JSONObject(json);
        String goods = jsonObject.getString("name");
        String reason = jsonObject.getString("reason");
        System.out.println(goods);
        System.out.println(reason);
     

        try {
            SQLconnect();
            status = DropGoods(goods,reason);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //转化为Json并上传
        response.setContentType("text/html;charset=gb2312");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(toString1());
	}

	private String readJSONString(HttpServletRequest request) {
		// TODO Auto-generated method stub
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
