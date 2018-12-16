package com;



import static sql.sqlconnect.SQLconnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static sql.sqlconnect.test;
import static sql.sqlconnect.test2;
/**
 * Servlet implementation class Test
 */
@WebServlet("/api/Heroes")
public class Heroes extends HttpServlet {
	ResultSet resultSet = null;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Heroes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
//	
//		String id = 11
//		String name = request.getParameter("name");
//		System.out.println(name);
        try {
            SQLconnect();
            resultSet= test2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String heroes=toString2();
        response.getWriter().write(heroes);
        System.out.println(heroes);
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		//String name =request.getParameter("name");
		String json = readJSONString(request);
		System.out.println(json);
		JSONObject jsonObject = new JSONObject(json);
		    
		String name = jsonObject.getString("name");
		int id =9;
		
        try {
        	SQLconnect();
			resultSet= test(name,id);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String out = toString1();
        System.out.println(out);
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
		JSONObject test=new JSONObject();
        try {
      
            while(resultSet.next()){
            	 Map<String,Object> map=new HashMap<String,Object>();
                 map.put("name", resultSet.getString("name"));
                 map.put("id", resultSet.getInt("id"));
                 test=new JSONObject(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally
        {
            //System.out.println(array.toString().length());
            return test.toString();
        }
	}
	
	@SuppressWarnings("finally")
	public String toString2() {
		JSONArray array=new JSONArray();
        try {
      
            while(resultSet.next()){
                JSONObject test=new JSONObject();
                String name = resultSet.getString("name");
				int id = resultSet.getInt("id");
				
				
                test.put("name",name);
                test.put("id",id);
          
                //System.out.println(test.toString());
                array.put(test);
                
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
