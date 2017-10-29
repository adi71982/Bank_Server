package banksocket;

import Class.ben;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Functions
{
    ResultSet rs,rd;
    PreparedStatement p1,p2,p3,ps,pd,p;
    Connection con;
    float amount;
    long day;
    Functions()
    {
	try
	{
	    Class.forName("com.mysql.jdbc.Driver");
	    con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","mysql");
	 }
	catch(ClassNotFoundException | SQLException e){
	    System.err.println(e);
	}
    }
    public ArrayList<Long> Accounts(long cust)
    {
	
	ArrayList<Long> a=new ArrayList<>();
	try
	{
	ps = (PreparedStatement)con.prepareStatement("select * from account_details");
	rs = (ResultSet)ps.executeQuery();
	while(rs.next())
	{
	    
	    if(rs.getLong(2)==cust)
	    {
		a.add(rs.getLong(1));
	    }
	}
	}catch(SQLException s)
	{
	    System.err.println(s);
	}
	finally{
	    try {
		rs.close();
		ps.close();
	    } catch (SQLException ex) {
		Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	
	return a;
    }
    public long login(String user,String pass) throws SQLException
    {
	boolean flag=false;
	long cust=0;
	ps = (PreparedStatement) con.prepareStatement("select * from login ");
	rs = (ResultSet)ps.executeQuery();
	while(rs.next())
	{
            if((user.equals(rs.getString(1))) &&(pass.equals(rs.getString(2))))
            {
                flag=true;
                cust=rs.getLong(4);
                break;
            }
	}
	rs.close();
	ps.close();
	if(flag)
	{
            return cust;
	}
	else
	{
            return 0;
	}
    }
    
    
    public void openacc(String type, String name1, String name2, String name3, String mbno, String phno, String email, String add, Date dob, String uid, String pan, String gen,long cust)
    {
	try
	{
	    con.setAutoCommit(false);
	p1 = (PreparedStatement) con.prepareStatement("insert into general values (?,?)");
        p1.setLong(1,cust);
        p1.setString(2,name1+" "+name2+" "+name3);
	p1.executeUpdate();
        
        p2 = (PreparedStatement) con.prepareStatement("insert into contact values (?,?,?,?,?)");
	p2.setLong(1,cust);
        p2.setString(2, mbno);
        p2.setString(3, phno);
        p2.setString(4, email);
        p2.setString(5, add);
        p2.executeUpdate();
        
        p3 = (PreparedStatement) con.prepareStatement("insert into personal values (?,?,?,?,?)");
	p3.setLong(1, cust);
	java.sql.Date d=new java.sql.Date(dob.getTime());
        p3.setDate(5, d);
        p3.setString(2, uid);
        p3.setString(3, pan);
        p3.setString(4, gen);
        p3.executeUpdate();
	con.commit();
	}catch(SQLException ex)
	{
	    System.err.println(ex);
	    if(con!=null)
	    {
		System.err.println("Transaction being rolled back");
		try {
		    con.rollback();
		} catch (SQLException ex1) {
		    System.err.println(ex);
		}
	    }
	}
	finally
	{
	    try {
		p1.close();
		p2.close();
		p3.close();
		con.setAutoCommit(true);
	    } catch (SQLException ex) {
		System.err.println(ex);
	    }
	}
    }    

    String Verfiy(String cust, String pin) 
    {
	String k="";
	try 
	{
	    long cust1=Long.parseLong(cust);
	    int pin1=Integer.parseInt(pin);
	    
	    ps = (PreparedStatement) con.prepareStatement("select * from bank_detail");
	    rs=(ResultSet)ps.executeQuery();
	    while(rs.next())
	    {
		if((cust1==rs.getLong(1))&&(pin1==rs.getLong(2)))
			{
			    k="OK";
			}
	    }
	    rs.close();
	    ps.close();
	    
	} catch (SQLException ex) {
	    System.err.println(ex);
	}
	return k;
	    
    }

    String createLogin(String user, String pass,String name,long cust) 
    {
	String k="";
	try {
	    con.setAutoCommit(false);
	    p1 = (PreparedStatement) con.prepareStatement("insert into login values (?,?,?,?)");
	    p1.setString(1,user);
	    p1.setString(2,pass);
	    p1.setString(3,name);
	    p1.setLong(4,cust);
	    p1.execute();
	    k="OK";   
	} catch (SQLException ex) {
	    System.err.println(ex);
	    if(con!=null)
	    {
		System.err.println("Transaction being rolled back");
		try {
		    con.rollback();
		} catch (SQLException ex1) {
		    System.err.println(ex1);
		}
	    }
	}
	finally
	{
	    try {
		p1.close();
		con.setAutoCommit(true);
	    } catch (SQLException ex) {
		System.err.println(ex);
	    }
	}
	return k;
    }

    long getSavingAccount(ArrayList<Long> a) {
	try
	{
	    ps = (PreparedStatement) con.prepareStatement("select * from depositor");
	    rs=(ResultSet)ps.executeQuery();
	    while(rs.next())
	    {
		if(a.contains(rs.getLong(1)))
		{
		   this.amount=rs.getFloat(2);
		   return rs.getLong(1);
		}
	    }
	}
	catch(SQLException s)
	{
	    System.err.println(s);
	}
	finally{
	    try {
		rs.close();
		ps.close();
	    } catch (SQLException ex) {
		System.err.println(ex);
	    }
	}
	return 0;
    }

    float getamount() {
	return this.amount;
    }

    long getLoanAccount(ArrayList<Long> a) {
	try
	{
	    ps = (PreparedStatement) con.prepareStatement("select * from borrower");
	    rs=(ResultSet)ps.executeQuery();
	    while(rs.next())
	    {
		if(a.contains(rs.getLong(1)))
		{
		   this.amount=rs.getFloat(2);
		   return rs.getLong(1);
		}
	    }
	}
	catch(SQLException s)
	{
	    System.err.println(s);
	}
	finally
	{
	    try {
		rs.close();
		ps.close();
	    } catch (SQLException ex) {
		System.err.println(ex);
	    }
	}
	return 0;
    }

    long getFDAccount(ArrayList<Long> a) {
	try
	{
	    ps = (PreparedStatement) con.prepareStatement("select * from FD");
	    rs=(ResultSet)ps.executeQuery();
	    while(rs.next())
	    {
		if(a.contains(rs.getLong(1)))
		{
		   this.amount=rs.getFloat(2);
		   Date d1=rs.getDate(3);
		   Calendar c=Calendar.getInstance();
		   Date d2=c.getTime();
		   long diff=d1.getTime()-d2.getTime();
		   this.day=TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		   return rs.getLong(1);
		}
	    }
	}
	catch(SQLException s)
	{
	    System.err.println(s);
	}
	finally
	{
	    try {
		rs.close();
		ps.close();
	    } catch (SQLException ex) {
		System.err.println(ex);
	    }
	}
	return 0;
    }

    long getDays() {
	return this.day;
    }

    String addben(long cust, long account, String ben, String IFSC, String email, String mobile, int pin) {
	String k="";
	try{
	    p1=(PreparedStatement)con.prepareStatement("insert into ben values (?,?,?,?,?,?,?)");
	    p1.setLong(1,cust);
	    p1.setString(2,ben);
	    p1.setString(3,IFSC);
	    p1.setString(4,email);
	    p1.setString(5,mobile);
	    p1.setInt(6,pin);
	    p1.setLong(7,account);
	    p1.execute();
	    k="OK";
	    
	} catch (SQLException ex) {
	    System.err.println(ex);
	}
	finally{
	    try {
		p1.close();
	    } catch (SQLException ex) {
		System.err.println(ex);
	    }
	    return k;
	}
	
    }

    ArrayList<ben> getben(long cust) {
	ArrayList<ben> a=new ArrayList<>();
	try {
	    
	   
	    ps = (PreparedStatement) con.prepareStatement("select * from ben");
	    rs = (ResultSet)ps.executeQuery();
	    while(rs.next())
	    {
		if(cust==rs.getLong(1))
		{
		    ben b=new ben(cust,rs.getLong(7),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),String.valueOf(rs.getInt(6))); 
		    a.add(b);
		}
	    }
	    
	} catch (SQLException ex) {
	    Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
	}
	return a;
    }

    String deleteben(String mobile) {
	String k="";
	try{
	con.setAutoCommit(false);
	ps = (PreparedStatement) con.prepareStatement("delete from ben where mobile_no=?");
	ps.setString(1,mobile);
	ps.executeUpdate();
	con.commit();
	k="OK";
	}
	catch(SQLException s)
	{
	    System.err.println(s);
	    try
	    {
		con.rollback();
	    }
	    catch(SQLException s1)
	    {
		System.err.println(s);
	    }
	}
	finally
	{
	    try {
		ps.close();
		con.setAutoCommit(true);
	    } catch (SQLException ex) {
		System.err.println(ex);
	    }
	    
	}
	return k;
    }

    String NEFT(long cust,long from_account, long to_account, float amount, int pin) 
    {
	String k="hello";
	System.out.println(cust+" "+from_account+" "+to_account+" "+amount+" "+pin);
	try
	{
	    int num = 0;
	    con.setAutoCommit(false);
	    String l="select pin from bank_detail where customer_id="+cust+" limit 1;";
	    ps = (PreparedStatement) con.prepareStatement(l);
	    rs = (ResultSet)ps.executeQuery();
	    rs.next();
	    num=rs.getInt(1);
	    if(num==pin)
	    {
		l="select amount from depositor where account_no="+from_account+" limit 1;";
		pd = (PreparedStatement) con.prepareStatement(l);
		rd = (ResultSet)pd.executeQuery();
		if(rd.next())
		{
		    p=(PreparedStatement) con.prepareStatement("update depositor set amount=amount-? where account_no=?");
		    p.setFloat(1,amount);
		    p.setLong(2,from_account);
		    p.executeUpdate();
		    k="OK";
		}
		else
		{
		    k="account must be a savings account";
		    return k;
		}
		l="select amount from depositor where account_no="+to_account+" limit 1;";
		pd = (PreparedStatement) con.prepareStatement(l);
		rd = (ResultSet)pd.executeQuery();
		if(rd.next())
		{
		    p=(PreparedStatement) con.prepareStatement("update depositor set amount=amount+? where account_no=?");
		    p.setFloat(1,amount);
		    p.setLong(2,to_account);
		    p.executeUpdate();
		    k="OK";
		}
		con.commit();
	    }
	    else
	    {
		k="Pin entered is incorrect";
	    }
	}catch(SQLException s)
	{
	    try {
		System.err.println(s+"Transaction is being rolled back");
		con.rollback();
		k="Unable to transfer\n Please try again later";
	    } catch (SQLException ex) {
		Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
		k="Unable to transfer\n Please try again later";
	    }
	}
	try {
	    con.setAutoCommit(true);
	} catch (SQLException ex) {
	    Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
	}
	return k;
    }

    
}