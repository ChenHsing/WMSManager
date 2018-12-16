package com;

import static sql.sqlconnect.GetGoodNum;
import static sql.sqlconnect.SQLconnect;
import static sql.sqlconnect.purchaseGoods;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import clazz.GoodsNum;

/**
 * Servlet implementation class GetNum
 */
@WebServlet("/api/purchaseform")
public class GetNum extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ArrayList<GoodsNum> ans = new ArrayList<GoodsNum>();
	private int status = 0;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetNum() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try{
	           SQLconnect();
	           ans= GetGoodNum();
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	        
	    //转化为Json并上传
	    response.setContentType("text/html;charset=gb2312");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(toString1());
		
	}
	
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String json = readJSONString(request);
		System.out.println("你post的东西:"+json);
		JSONObject jsonObject = new JSONObject(json);
		String tempnum = jsonObject.getString("con");
		
		String []numStr = tempnum.split(",");
		SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");//设置日期格式
		String time = df.format(new Date());
		int [] num = new int[numStr.length];
		for(int i=0;i<numStr.length;i++) {
			num[i] = Integer.valueOf(numStr[i]);
		}
		try {
			status = purchaseGoods(num,time);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String out = toString2();
		System.out.println(out);
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
	
	//get返回的东西
	private String toString1() {
		
		String answer ="";
		for(int i=0;i<ans.size();i++) {
			answer+=ans.get(i).num;
			if(i<ans.size()-1) {
				answer+=",";
			}
		}
		//return answer;
		
		JSONObject test=new JSONObject();
     
        test.put("con",answer);
		return test.toString();
		
	}
	
	//post返回的东西
	@SuppressWarnings("finally")
	public String toString2()
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
