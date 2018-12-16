package com;

import static sql.sqlconnect.SQLconnect;
import static sql.sqlconnect.DealOrder;
import static sql.sqlconnect.GetDealOrders;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class GetOrder
 */
@WebServlet("/api/untreatedform")
public class GetOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static ResultSet resultSet=null;
    private static int status = 0;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetOrder() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 try {
	            SQLconnect();
	            resultSet= GetDealOrders();
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
		System.out.println(json);
		
		JSONObject jsonObject = new JSONObject(json);
		String deal_id = jsonObject.getString("con");
		String [] dealStr = deal_id.split(",");
		SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");//设置日期格式
		String time = df.format(new Date());
		int [] id = new int[dealStr.length];
		
		for(int i=0;i<dealStr.length;i++) {
			id[i] = Integer.valueOf(dealStr[i]);
			
		}
		
		try {
			status = DealOrder(id,time);
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

	
	@SuppressWarnings("finally")
	public String toString1() {
		JSONArray array=new JSONArray();
        try {
            int i = 0;
            while(resultSet.next()){
                JSONObject test=new JSONObject();
                test.put("content",resultSet.getString("content"));
                test.put("phone",resultSet.getString("phone_number"));
                test.put("address",resultSet.getString("address"));
                test.put("time",resultSet.getString("o_time"));
                test.put("order_id",resultSet.getInt("oid"));
                test.put("customer", resultSet.getString("c_name"));
                
                array.put(test);
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally
        {
            //System.out.println(array.toString().length());
            return array.toString();
        }
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
