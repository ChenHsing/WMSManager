package com;

import static sql.sqlconnect.GetInName;
import static sql.sqlconnect.SQLconnect;
import static sql.sqlconnect.getInCage;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class SelectIn
 */
@WebServlet("/api/inname")
public class SelectIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static ResultSet resultSet = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectIn() {
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
	           resultSet= GetInName();
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
		String gname = jsonObject.getString("gname");
		String start = jsonObject.getString("sdate");
		String [] temp = start.split("T");
		start = temp[0];
		String end = jsonObject.getString("edate");
		temp = end.split("T");
		end = temp[0];
		try {
			resultSet = getInCage(gname,start,end);
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

	private String toString1()   {
	    
	    ArrayList<String> goods = new ArrayList<String>();
		String goodsStr ="";
		try {
			while(resultSet.next()) {
				goods.add(resultSet.getString("g_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> goodsSet = new HashSet<String>(goods);
		List<String> afterHashSetList = new ArrayList<String>(goodsSet);
		for(int i = 0;i<afterHashSetList.size();i++) {
			goodsStr += afterHashSetList.get(i);
			if(i<afterHashSetList.size()-1) {
				goodsStr+=",";
			}
		}
		
		
		JSONObject test=new JSONObject();
        test.put("gname", goodsStr);
		return test.toString();
	}
	
	@SuppressWarnings("finally")
	private String toString2() {
		JSONArray array=new JSONArray();
        try {
            while(resultSet.next()){
                JSONObject test=new JSONObject();
               
                
                test.put("g_name",resultSet.getString("g_name"));
                test.put("price",resultSet.getDouble("in_price"));
                test.put("amount",resultSet.getInt("in_amount"));
                test.put("time", resultSet.getString("in_time"));
                test.put("total_price", resultSet.getDouble("total_price"));
                
                array.put(test);
                
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
