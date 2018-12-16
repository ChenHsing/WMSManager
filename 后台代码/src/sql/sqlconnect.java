package sql;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import clazz.GoodsNum;

public class sqlconnect {
     //���ݿ������Ϣ
	private final static String driver = "com.mysql.jdbc.Driver";
    private final static String URL="jdbc:mysql://localhost:3306/wms?useSSL=false&characterEncoding=UTF-8";
    private final static String NAME = "root";
    private final static String PASSWORD = "";

    static Connection conn = null;
    static PreparedStatement ps = null;
    static ResultSet resultSet = null;
    static ResultSet resultSet2 = null;
    static Statement statement = null;
    //���ݿ����Ӻ���
    public static void SQLconnect() {
        try {
            Class.forName(driver).newInstance(); //����������stringΪ��������
            conn = DriverManager.getConnection(URL, NAME, PASSWORD);
            System.out.println("���ӳɹ�");
        }catch(Exception e) {
        	System.out.println("����ʧ��");
            e.printStackTrace();
        }
    }
    

    //���ݿ��ѯ
    //�ú���Ϊ��¼��ѯ 0Ϊ�û������������ 1Ϊȫ����ȷ 
    public static int SearchUser(String username,String password)throws SQLException{
        //���û��������пո�ȥ��
        //username = username.replaceAll(" ","");
        System.out.println(username);
        System.out.println(password);

        ps = conn.prepareStatement("SELECT * FROM users where u_name = ?");
        ps.setString(1,username);
        //haveResultSet = ps.execute();
        resultSet = ps.executeQuery();
        //����и��û�
        if(resultSet.next()){
            resultSet = ps.executeQuery();
            while(resultSet.next()){
                if(resultSet.getString("password").equals(password)){
                    return 1;//�û�������ȫ��
                }else{
                	System.out.println("�������");
                    return 0;//�������
                }
            }
        }else{
         	System.out.println("�û�������");
            return 0;//�û�������
        }
     	System.out.println("����");

        return 0;
    }
    
    //ȷ�϶��������⺯��
    public static int DealOrder(int [] id,String time) throws Exception{
    	for(int i =0;i<id.length;i++) {
    		//�Ѵ�����deal��Ϊ1
    		ps = conn.prepareStatement("update order_table set deal = 1 where oid = ?");
    		ps.setInt(1, id[i]);
    		ps.executeUpdate();
    		ps = conn.prepareStatement("select * from order_table where oid = ?");
    		ps.setInt(1, id[i]);
    		resultSet = ps.executeQuery();
    		
    		
    		while(resultSet.next()) {
    			
    			
    			String content = resultSet.getString("content");
    			String [] temp = content.split(",");
    			for(int j = 0;j<temp.length;j++) {
    				String [] temp2 = temp[j].split("\\+");
    				String gname = temp2[0];
    				String price = temp2[1];
    				String number = temp2[2];
    				
    				DecimalFormat df = new DecimalFormat("######0.00");   
    				double total_price = Integer.valueOf(number)*Double.valueOf(price);
    				String total = df.format(total_price);
    				double last = Double.valueOf(total);
    				//��д�����
        			ps = conn.prepareStatement("insert into sales_records(g_name,out_price,out_amount,"
        					+ "out_time,total_price) values (?,?,?,?,?)");
        			ps.setString(1, gname);
        			ps.setString(2, price);
        			ps.setString(3, number);
        			ps.setString(4, time);
        			ps.setDouble(5, last);
        			ps.executeUpdate();
        			
    				
    				
    				//���Ŀ���
    				ps = conn.prepareStatement("update goods set g_amount = g_amount+? where g_name =?");
        			ps.setInt(1, Integer.valueOf(number));
        			ps.setString(2, gname);
        			ps.executeUpdate();	
    			}
    			
    			
    		}
    		
    	}
		return 1;
    	
    }
    
    //��⺯��
    public static int purchaseGoods(int[] num,String time)throws Exception {
    	for(int i=0;i<num.length;i++) {
    		//��Ϊ0ʱ��д�������
    		if(num[i]!=0) {
    			ps = conn.prepareStatement("select * from goods where gid =?");
    			ps.setInt(1, i+1);
    			resultSet = ps.executeQuery();
    			while(resultSet.next()) {
    				//��������
    				ps = conn.prepareStatement("insert into purchase_records(gid,g_name,in_price,in_amount,"
    						+ "in_time,total_price) values (?,?,?,?,?,?)");
        			ps.setString(1, resultSet.getString("gid"));
        			ps.setString(2, resultSet.getString("g_name"));
        			ps.setDouble(3, resultSet.getDouble("in_price"));
        			ps.setInt(4, num[i]);
        			ps.setString(5,time);
        			ps.setDouble(6, num[i]*resultSet.getDouble("in_price"));
        			ps.executeUpdate();    			
        			
        			//���Ŀ���
        			ps = conn.prepareStatement("update goods set g_amount = g_amount+? where gid =?");
        			ps.setInt(1, num[i]);
        			ps.setInt(2, i+1);
        			ps.executeUpdate();		
    			}
    		}
    	}
    	
    	return 1;
    }
    
   
    //�õ�δ�����˵���Ʒ����Ŀ
    public static ArrayList<GoodsNum> GetGoodNum() throws Exception{
    	ArrayList<GoodsNum> ans = new ArrayList<GoodsNum>();
    	ps = conn.prepareStatement("select * from goods");
    	resultSet = ps.executeQuery();
    	
    	while(resultSet.next()) {
    		String gname = resultSet.getString("g_name");
    		int price = resultSet.getInt("in_price");
    		int cageNum = resultSet.getInt("g_amount");
    		int num = 0;
    		ps = conn.prepareStatement("select * from order_table where deal = 0");
    		resultSet2 = ps.executeQuery();
    		
    		String content ="";
    		int wantNum = 0;
    		while(resultSet2.next()) {		
    			content = resultSet2.getString("content");
    		//	System.out.println(content);
    			ArrayList<String> name = new ArrayList<String>();
        		ArrayList<Integer> want_num = new ArrayList<Integer>();
        		String []temp = content.split(",");
        		for(int i =0;i<temp.length;i++) {
        			//System.out.println(temp[i]);
        			String[] tempspilt = temp[i].split("\\+");
        			name.add(tempspilt[0]);
        			want_num.add(Integer.valueOf(tempspilt[2]));
        	    }
        		for(int i =0;i<name.size();i++) {
        			if(name.get(i).equals(gname)) {
        				wantNum +=want_num.get(i);
        			}
        		}
    		}
    		
    		if(cageNum < wantNum) {
    			num = wantNum - cageNum; 
    		}else {
    			num = 0;
    		}
    		
    		GoodsNum goodnum = new GoodsNum(gname,num,price);
    		ans.add(goodnum);
    	}
    	
    	return ans;
    }
    
    
    public static ResultSet getOutCage(String name,String start, String end) throws Exception{
    	if(name.equals("")) {
    		ps =conn.prepareStatement("select * from sales_records where out_time>= ? and out_time <= ?");
    		ps.setString(1, start);
    		ps.setString(2, end);
    		resultSet = ps.executeQuery();
    	}else {
    		ps =conn.prepareStatement("select * from sales_records where g_name = ? "
    				+ " and out_time>= ? and out_time <= ?");
    		ps.setString(1, name);
    		ps.setString(2, start);
    		ps.setString(3, end);
    		resultSet = ps.executeQuery();
    	}
    	return resultSet;
    }
    
    public static ResultSet getInCage(String name,String start,String end) throws Exception{
    	if(name.equals("")) {
    		ps =conn.prepareStatement("select * from purchase_records where in_time>= ? and in_time <= ?");
    		ps.setString(1, start);
    		ps.setString(2, end);
    		resultSet = ps.executeQuery();
    	}else {
    		ps =conn.prepareStatement("select * from purchase_records where g_name = ? "
    				+ " and in_time>= ? and in_time <= ?");
    		ps.setString(1, name);
    		ps.setString(2, start);
    		ps.setString(3, end);
    		resultSet = ps.executeQuery();
    	}
    	return resultSet;
    }
    
    
    
    public static ResultSet getOrder(String start, String end) throws Exception{
    	ps = conn.prepareStatement("select * from order_table where o_time>= ? and o_time<=?" );
    	ps.setString(1, start);
    	ps.setString(2, end);
    	resultSet = ps.executeQuery();
    	
    	return resultSet;
    }
    
    public static ResultSet GetCustomer() throws Exception{
    	ps = conn.prepareStatement("select * from order_table");
    	resultSet = ps.executeQuery();
    	
    	return resultSet;
    }
    
    public static ResultSet GetOutName() throws Exception{
    	ps = conn.prepareStatement("select * from sales_records");
    	resultSet = ps.executeQuery();
		return resultSet;
    }
    
    public static ResultSet GetInName() throws Exception{
    	ps = conn.prepareStatement("select * from purchase_records");
    	resultSet = ps.executeQuery();
		return resultSet;
    	
    }
    
    //�õ�δ�����˵�
    public static ResultSet GetDealOrders() throws Exception{
    	ps = conn.prepareStatement("select * from order_table where deal = 0");
    	resultSet = ps.executeQuery();
    	
    	return resultSet;
    }
    
    //�¼ܺ���
    public static int DropGoods(String goods,String reason) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");//�������ڸ�ʽ
	    String time = df.format(new Date());
	    int gid = 0;
    	ps = conn.prepareStatement("select gid from goods where g_name=?");
    	ps.setString(1, goods);
    	resultSet = ps.executeQuery();
    	while(resultSet.next()) {
    		gid = resultSet.getInt("gid");
    	}
    	
    	//���뵽�¼ܱ�
    	ps =conn.prepareStatement("insert into dropgoods(gid,g_name,d_time,reason) values(?,?,?,?) ");
    	ps.setInt(1, gid);
    	ps.setString(2, goods);
    	ps.setString(3, time);
    	ps.setString(4, reason);
    	ps.executeUpdate();
    	
    	//����������Ϊ0
    	ps = conn.prepareStatement("update goods set g_amount = 0 where g_name = ?");
    	ps.setString(1, goods);
    	if(ps.executeUpdate() == 1)
    		return 1;
    	else
    		return 0;
    	
    	
    }
    
    //���Ժ���
    public static ResultSet test(String name,int id)throws Exception {
    	ps = conn.prepareStatement("insert into heros(name,id) values(?,?)");
    	ps.setString(1, name);
    	ps.setInt(2,id);
    	ps.executeUpdate();
    	
    	ps = conn.prepareStatement("select * from heros where id = ?");
    	ps.setInt(1, id);
    	resultSet = ps.executeQuery();
    	
    	return resultSet;
    }
    
  //���Ժ���
    public static ResultSet test2()throws Exception {
    	ps = conn.prepareStatement("select * from heros");
    	resultSet = ps.executeQuery();
    	
    	return resultSet;
    }
    

    
    //�ú���Ϊע�ắ�� 0Ϊʧ�� �û����Ѵ��� 1Ϊ�ɹ�
    public static int RegisterUser(String username,String password)throws SQLException{
        username = username.replaceAll(" ","");
      //  boolean haveResultSet = false;
        System.out.println(username);
        System.out.println(password);
        ps = conn.prepareStatement("SELECT * FROM users where u_name = ?");
        ps.setString(1,username);
        //haveResultSet = ps.execute();
        resultSet = ps.executeQuery();

        //System.out.println(haveResultSet);
        if(resultSet.next())
            return 0;
        else {
            ps = conn.prepareStatement("insert into users(u_name,password) values (?,?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return 1;
        }
    }
 
    
    //�޸����뺯�� 1Ϊ�ɹ�
    public static int ChangePwd(String username,String old_pwd,String new_pwd) throws Exception{
    	ps = conn.prepareStatement("select password from users where u_name = ?");
    	ps.setString(1, username);
    	resultSet = ps.executeQuery();
    	while(resultSet.next()) {
    		if(!resultSet.getString("password").equals(old_pwd)) {
    			System.out.println("���������");
    			return 0;
    		}else {
    			ps = conn.prepareStatement("update users set password = ? where u_name = ?");
    			ps.setString(1, new_pwd);
    			ps.setString(2, username);
    			ps.executeUpdate();
    			return 1;
    		}
    	}
    	return 0;
    }
    
    
    /*�������
     * 
     * */
    
    public static ResultSet OutStorage(String[] id) throws SQLException{
    	String sql = "select * from order_table where oid = ";
    	for(int i =0 ;i <id.length;i++) {
    		if(i==0) {
    			sql = sql + id[i];
    		}
    		else {
    			sql = sql+ " or oid ="+id[i];
    		}
    	}
    	System.out.println("sql "+sql);
    	
    	ps = conn.prepareStatement(sql);
    	
    	resultSet = ps.executeQuery();
    	
    	return resultSet;
    }
    
    
    /*������
     * resultset�Ƕ�����Ĳ�ѯ
     * resultset2�ǲ������in_price
     * resultset��󷵻�ֵ��purchase_record��
     * */
    public static ResultSet InStorage() throws SQLException {
    	ps = conn.prepareStatement("select * from order_table where deal = 0");

    	resultSet = ps.executeQuery();
    	
    	while(resultSet.next()) {
    		ps = conn.prepareStatement("select * from goods where gid =?");
    		//System.out.println(resultSet.getInt("gid"));
        	ps.setInt(1,resultSet.getInt("gid"));
        	resultSet2 = ps.executeQuery();
        	
        	double in_price =0;
        	int left_count = 0;//ʣ�����
    		String gid = resultSet.getString("gid");
    		String gname = resultSet.getString("g_name");
    		while(resultSet2.next()) {
    			in_price = resultSet2.getDouble("in_price");
    			left_count = resultSet2.getInt("g_amount");
    		}
    		
    		
    		
    		int in_amount = resultSet.getInt("g_amount");
    		SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");//�������ڸ�ʽ
    	    String time = df.format(new Date());
    	    
    	    if(in_amount > left_count) {
    	    	//������Ҫ����������ʣ�������
    	    	//��������¼
    	    	ps = conn.prepareStatement("insert into purchase_records(gid,g_name,in_price,in_amount,in_time,total_price)"
        	    		+ "values(?,?,?,?,?,?)");
        	    ps.setString(1, gid);
        	    ps.setString(2, gname);
        	    ps.setDouble(3,in_price);
        	    ps.setInt(4, in_amount);
        	    ps.setString(5, time);
        	    ps.setDouble(6, in_price*in_amount);
        	    ps.executeUpdate();
        	    
        	    //���ӿ��
        	    ps = conn.prepareStatement("update goods set in_amount = in_amount + ? where gid = ?");
        	    ps.setInt(1, in_amount);
        	    ps.setString(2, gid);
        	    ps.executeUpdate();
        	    
    	    }else {
    	    	//����ֿ�ʣ�������������Ҫ��⡣
    	    }
    	    
    	}
    	ps = conn.prepareStatement("select * from purchase_records");
	    resultSet = ps.executeQuery();
	  
		return resultSet;
    }
    
    //�ύ������1Ϊ�ɹ���0Ϊʧ��(�ύ�Ķ����еĲ���)
    public static int SendOrder(String customer_name, String phone,String[] goods,String []count,
    		String address,String time,String[] price) throws Exception {
    	//���+�۸�+����
    	String content = "";
    	for(int i = 0;i <goods.length;i++) {
    		content += goods[i]+"+"+ price[i]+"+"+count[i];
    		if(i<goods.length-1) {
    			content+=",";
    		}
    	}
    	
    	ps = conn.prepareStatement("insert into order_table(content,c_name,phone_number,address,o_time,deal)"
    			+ " values(?,?,?,?,?,?)");
		ps.setString(1, content);
		ps.setString(2, customer_name);
		ps.setString(3, phone);
		ps.setString(4, address);
		ps.setString(5, time);
		ps.setInt(6, 0);
	
		ps.executeUpdate();
    	
    	int enough = 1;
    	for(int i =0;i<goods.length;i++) {
        	enough = isEnough(goods[i],Integer.valueOf(count[i]));
        	if(enough == 0) {
        		return 0;
        	}
    	}
    	return 1;
    }
    
    //���С��Ҫ�󣬷���0�����򷵻�1
    public static int isEnough(String name,int num) {
    	try {
			ps = conn.prepareStatement("select * from goods where g_name = ?");
			ps.setString(1, name);
			resultSet2 = ps.executeQuery();
			while(resultSet2.next()) {
				if(resultSet2.getInt("g_amount")<num) {
					return 0;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return 1;
    }
    
    //��ѯ�����Ϣ
    public static ResultSet getGoods(String g_name,String brand,int lowcount,int highcount) throws Exception{
    	if(g_name.equals("")&&brand.equals("")) {
    		ps = conn.prepareStatement("select * from goods where g_amount>= ? and g_amount <= ?");
    		ps.setInt(1, lowcount);
    		ps.setInt(2,highcount);
        	resultSet = ps.executeQuery();
    	}else if(!g_name.equals("")) {
    		if(!brand.equals("")) {
    			ps = conn.prepareStatement("select * from goods where g_name = ? and  g_amount>= ? "
    					+ "and g_amount <= ? and brand = ?");
    			ps.setString(1, g_name);
        		ps.setInt(2, lowcount);
        		ps.setInt(3,highcount);
        		ps.setString(4, brand);
            	resultSet = ps.executeQuery();
    		}else {
    			ps = conn.prepareStatement("select * from goods where g_name = ? and  g_amount>= ? "
    					+ "and g_amount <= ?");
    			ps.setString(1, g_name);
        		ps.setInt(2, lowcount);
        		ps.setInt(3,highcount);
            	resultSet = ps.executeQuery();
    		}
    	}else if(g_name.equals("")) {
    		if(!brand.equals("")) {
    			ps = conn.prepareStatement("select * from goods where g_amount>= ? "
    					+ "and g_amount <= ? and brand = ?");
        		ps.setInt(1, lowcount);
        		ps.setInt(2,highcount);
        		ps.setString(3, brand);
            	resultSet = ps.executeQuery();
    		}
    	}

    
    	return resultSet;
    }
    
    
    //��ȡ��ǰ����
    public static String getDate(){
        //��ȡ��ǰ������
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);

        int month = now.get(Calendar.MONTH ) + 1;
        //System.out.println(month);
        int day = now.get(Calendar.DATE);

        int hour = now.get(Calendar.HOUR_OF_DAY);

        int minute = now.get(Calendar.MINUTE);

        int second = now.get(Calendar.SECOND);
        String month1 = "";
        String day1 = "";
        String hour1 = "";
        String minute1 = "";
        String second1 = "";
        if(month < 10){
            month1 += "0";
            month1 +=  Integer.toString(month);
        }else{
            month1 = Integer.toString(month);
        }
        if(day < 10){
            day1 += "0";
            day1 += Integer.toString(day);
        }else{
            day1 =Integer.toString(day);
        }
        if(hour < 10){
            hour1 += "0";
            hour1 += Integer.toString(hour);
        }else{
            hour1 =Integer.toString(hour);
        }
        if(minute < 10){
            minute1 += "0";
            minute1 += Integer.toString(minute);
        }else{
            minute1 =Integer.toString(minute);
        }
        if(second < 10){
            second1 += "0";
            second1 += Integer.toString(second);
        }else{
            second1 =Integer.toString(second);
        }


        String date = Integer.toString(year) + "-" + month1 + "-" + day1 + " " + hour1 + ":" + minute1 + ":" + second1;
        //System.out.println(date);
        //System.out.println(month + " " +month1);
        return date;
    }

}
