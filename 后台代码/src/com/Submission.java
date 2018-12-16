package com;

import java.io.BufferedReader;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;

import static sql.sqlconnect.SQLconnect;
import static sql.sqlconnect.SendOrder;

/**
 * Servlet implementation class Submission
 */
@WebServlet("/api/order")
public class Submission extends HttpServlet {
	private int status = 0;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Submission() {
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
		request.setCharacterEncoding("UTF-8");
		String json = readJSONString(request);
		System.out.println("json"+json);
	    JSONObject jsonObject = new JSONObject(json);
	    
	    String tempgoods = jsonObject.getString("goods");
	    String tempcount = jsonObject.getString("number");
	    String tempprice = jsonObject.getString("prices");
	    String customer_name = jsonObject.getString("name");
	    String phone = jsonObject.getString("phone");
	    
	    String address = jsonObject.getString("address");
	    
	    String[] goods = tempgoods.split(",");
	    
	    String[] count = tempcount.split(",");
	    String[] price = tempprice.split(",");
	    
	    
	    SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");//设置日期格式
	    String time = df.format(new Date());
	    try {
	    	SQLconnect();
			status = SendOrder(customer_name,phone,goods,count,address,time,price);
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
