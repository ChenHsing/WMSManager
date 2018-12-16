package com;

import java.io.BufferedReader;
import static sql.sqlconnect.OutStorage;
import java.io.IOException;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class OutStorage
 */
@WebServlet("/OutStorage")
public class OutStorage extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static ResultSet  resultSet= null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OutStorage() {
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
		//doGet(request, response);
		
		 String json = readJSONString(request);
		 System.out.println(json);
		 
		 JSONObject jsonObject = new JSONObject(json);
		 String order = jsonObject.getString("order");
		 System.out.println("order"+order);
		 
		 String[] id = order.split(",");
		
		 try {
			 resultSet = OutStorage(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		//转化为Json并上传
		 
		 
		String out = toString1();
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
		JSONArray array=new JSONArray();
        try {
            int i = 0;
            while(resultSet.next()){
                JSONObject test=new JSONObject();
                String customer_name = resultSet.getString("c_name");
				String phone = resultSet.getString("phone_number");
				String goods = resultSet.getString("g_name");
				int count = resultSet.getInt("g_amount");
				double selling_price = resultSet.getDouble("out_price");
				String address = resultSet.getString("address");
				String time = resultSet.getString("o_time");
				int order_id = resultSet.getInt("oid");
				
                test.put("customer_name",customer_name);
                test.put("phone",phone);
                test.put("goods",goods);
                test.put("count",count);
                test.put("selling_price",selling_price);
                test.put("address",address);
                test.put("time",time);
                test.put("order_id",order_id);
                //System.out.println(test.toString());
                array.put(test);
                i++;
            }
            //System.out.println(array.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally
        {
            if(array.toString().equals("[]")){
                return null;
            }

            else{
                //System.out.println(array.toString());
                System.out.println(array.toString().length());
                return array.toString();
            }
        }
    }

}
