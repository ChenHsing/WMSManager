package com;

import static sql.sqlconnect.GetCustomer;
import static sql.sqlconnect.getOrder;
import static sql.sqlconnect.SQLconnect;

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
import org.json.JSONObject;

/**
 * Servlet implementation class SelectOrder
 */
@WebServlet("/api/getorder")
public class SelectOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    private static ResultSet resultSet = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectOrder() {
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
	           
	           resultSet= GetCustomer();
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
		String cname = jsonObject.getString("cname");
		String start = jsonObject.getString("sdate");
		String[] temp = start.split("T");
		start = temp[0];
		String end = jsonObject.getString("edate");
		temp = end.split("T");
		end = temp[0];
		try {
			resultSet = getOrder(start,end);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<Integer> oid = new ArrayList<Integer>();
		ArrayList<String> content = new ArrayList<String>();
		ArrayList<String> c_name = new ArrayList<String>();
		ArrayList<String> phone_number = new ArrayList<String>();
		ArrayList<String> address = new ArrayList<String>();
		ArrayList<String> o_time = new ArrayList<String>();
		ArrayList<Integer> deal = new ArrayList<Integer>();
		
		
		ArrayList<Integer> Resoid = new ArrayList<Integer>();
		ArrayList<String> Rescontent = new ArrayList<String>();
		ArrayList<String> Resc_name = new ArrayList<String>();
		ArrayList<String> Resphone_number = new ArrayList<String>();
		ArrayList<String> Resaddress = new ArrayList<String>();
		ArrayList<String> Reso_time = new ArrayList<String>();
		ArrayList<Integer> Resdeal = new ArrayList<Integer>();
		
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		try {
			while(resultSet.next()) {
				oid.add(resultSet.getInt("oid"));
				content.add(resultSet.getString("content"));
				c_name.add(resultSet.getString("c_name"));
				phone_number.add(resultSet.getString("phone_number"));
				address.add(resultSet.getString("address"));
				o_time.add(resultSet.getString("o_time"));
				deal.add(resultSet.getInt("deal"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//获得index的值
		for(int i = 0 ;i<oid.size();i++) {
			if(!cname.equals("")) {
				if(!gname.equals("")) {
					//System.out.println(content.get(i));
					if(getcontain(content.get(i),gname)&& cname.equals(c_name.get(i))) {
						index.add(i);
					}
				}else if(cname.equals(c_name.get(i))) {
					index.add(i);
				}
			}else {
				if(!gname.equals("")) {
					if(getcontain(content.get(i),gname)) {
						index.add(i);
					}
				}else {
					index.add(i);
				}
			}
		}
		//把index的对应的位置添加的查询的里面
		for(int j=0;j<index.size();j++) {
			int i = index.get(j);
			Resoid.add(oid.get(i));
			Rescontent.add(content.get(i));
			Resc_name.add(c_name.get(i));
			Resphone_number.add(phone_number.get(i));
			Resaddress.add(address.get(i));
			Reso_time.add(o_time.get(i));
			Resdeal.add(deal.get(i));
		}
		
		JSONArray array=new JSONArray();
		for(int i = 0;i<Resoid.size();i++) {
			 JSONObject test=new JSONObject();
	         int oid1 = Resoid.get(i);
			 String content1 = Rescontent.get(i);
			 String c_name1 = Resc_name.get(i);
			 String phone_number1 = Resphone_number.get(i);
			 String address1 = Resaddress.get(i);
			 String o_time1 = Reso_time.get(i);
			 int deal1 = Resdeal.get(i);
			
				
	         test.put("id",oid1);
	         test.put("content",content1);
	         test.put("name",c_name1);
	         test.put("phone",phone_number1);
	         test.put("address",address1);
	         test.put("time",o_time1);
	         test.put("deal",deal1);
	         //System.out.println(test.toString());
	         array.put(test);
		}
		
		//转化为Json并上传
		System.out.println(array.toString());
	    response.setContentType("text/html;charset=gb2312");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(array.toString());
	}
	
	
	private boolean getcontain(String content,String gname) {
		String [] temp = content.split(",");
		for(int i =0;i<temp.length;i++) {
			String[]temp2 = temp[i].split("\\+");
			if(temp2[0].equals(gname)) {
				return true;
			}
		}
		return false;
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
	    ArrayList<String> customer = new ArrayList<String>();
	    ArrayList<String> tempgoods = new ArrayList<String>();
	    ArrayList<String> goods = new ArrayList<String>();
		String cNameStr ="";
		String goodsStr ="";
		try {
			while(resultSet.next()) {
				customer.add(resultSet.getString("c_name"));
				tempgoods.add(resultSet.getString("content"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> customerSet = new HashSet<String>(customer);
		List<String> afterHashSetList = new ArrayList<String>(customerSet);
		for(int i = 0;i<afterHashSetList.size();i++) {
			cNameStr += afterHashSetList.get(i);
			if(i<afterHashSetList.size()-1) {
				cNameStr+=",";
			}
		}
		//分割content得到商品名
		for(int i =0;i<tempgoods.size();i++) {
			String [] temp = tempgoods.get(i).split(",");
			for(int j = 0; j<temp.length;j++) {
				String [] temp2 = temp[j].split("\\+");
				goods.add(temp2[0]);
			}
		}
		
		Set<String> goodsSet = new HashSet<String>(goods);
		List<String> afterHashSetList2 = new ArrayList<String>(goodsSet);
		for(int i = 0;i<afterHashSetList2.size();i++) {
			goodsStr += afterHashSetList2.get(i);
			if(i<afterHashSetList2.size()-1) {
				goodsStr+=",";
			}
		}
		
		
		JSONObject test=new JSONObject();
        test.put("cname",cNameStr);
        test.put("gname", goodsStr);
		return test.toString();
	}

}
