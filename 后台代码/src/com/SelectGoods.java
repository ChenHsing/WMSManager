package com;

import java.io.BufferedReader;
import static sql.sqlconnect.getGoods;
import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class SelectGoods
 */
@WebServlet("/api/goods")
public class SelectGoods extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static ResultSet resultSet = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectGoods() {
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
		 System.out.println(json);
		 JSONObject jsonObject = new JSONObject(json);

		 String g_name = jsonObject.getString("name");
		 String brand = jsonObject.getString("brand");
		 int lowcount = jsonObject.getInt("minNumber");
		 int highcount = jsonObject.getInt("maxNumber");
		 
		 
		 try {
			resultSet = getGoods(g_name,brand,lowcount,highcount);
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
	public String toString1() {
		JSONArray array=new JSONArray();
        try {
            int i = 0;
            while(resultSet.next()){
            	
                JSONObject test=new JSONObject();
                
                test.put("gid",resultSet.getString("gid"));
                test.put("g_name",resultSet.getString("g_name"));
                test.put("in_price",resultSet.getString("in_price"));
                test.put("out_price",resultSet.getInt("out_price"));
                test.put("g_amount",resultSet.getDouble("g_amount"));
                test.put("category",resultSet.getString("category"));
                test.put("brand",resultSet.getString("brand"));
               
                
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

}
