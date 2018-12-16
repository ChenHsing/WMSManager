package com;

import java.io.BufferedReader;
import static sql.sqlconnect.InStorage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class Instorage
 */
@WebServlet("/Instorage")
public class Instorage extends HttpServlet {
	private static final long serialVersionUID = 1L;
    ResultSet resultSet = null;


    /**
     * @see HttpServlet#HttpServlet()
     */
    public Instorage() {
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
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
		 String judge = jsonObject.getString("judge");
		 if(judge.equals("0") ) {
			 try {
				resultSet = InStorage();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 
		try {
			String out=toString1();
			System.out.println(out);
		    response.setContentType("text/html;charset=gb2312");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write(out);
		} catch (Exception e) {
			System.out.println("´íÎó");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	     
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
                String goods = resultSet.getString("g_name");
				int count = resultSet.getInt("in_amount");
				double purchase_price = resultSet.getDouble("in_price");
				String time = resultSet.getString("in_time");
				
                test.put("goods",goods);
                test.put("count",count);
                test.put("purchase_price",purchase_price);
                test.put("time",time);
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
