package banksocket;

import Class.ObjectClass;
import Class.ben;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
class Socket4 implements Runnable
{
    Functions obj;
    String k;
    Socket s;
    ReentrantLock l;
    Socket4(Socket socket,ReentrantLock l) {
	this.l=l;
	this.s=socket;
	new Thread(this);
    }
    public void run()
    {
	 obj=new Functions();
	try {
	    DataOutputStream out=new DataOutputStream(s.getOutputStream());
	    DataInputStream in=new DataInputStream(s.getInputStream());
	    String user=in.readUTF();
	    String pass=in.readUTF();
	    String name=in.readUTF();
	    long cust=in.readLong();
	    System.out.println(user+" "+pass+" "+name+" "+cust);
	    l.lock();
	    k=obj.createLogin(user,pass,name,cust);
	    out.writeUTF(k);
	    in.close();
	    out.close();
	    s.close();
	} catch (IOException ex) {
	    Logger.getLogger(Socket4.class.getName()).log(Level.SEVERE, null, ex);
	} 
	finally{
	    l.unlock();
	}
            
    }
}
class Socket3 implements Runnable
{
    Functions obj;
    String k;
    Socket s;
    ReentrantLock l;
    Socket3(Socket socket,ReentrantLock l) {
	this.l=l;
	this.s=socket;
	new Thread(this);
    }
    public void run()
    {
	obj =  new Functions();
	try {
	    PrintWriter out=new PrintWriter(s.getOutputStream(),true);
	    BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
	    String cust=in.readLine();
	    String pin=in.readLine();
	    l.lock();
	    k=obj.Verfiy(cust,pin);
	    out.println(k);
	} catch (IOException ex) {
	    Logger.getLogger(Socket3.class.getName()).log(Level.SEVERE, null, ex);
	}
	finally{
	    l.unlock();
	}
            
    }
}
class Socket1 implements Runnable
{
    Functions obj;
    long cust;
    Socket s;
    ReentrantLock l;
    Socket1(Socket socket,ReentrantLock l) {
	this.l=l;
	this.s=socket;
	new Thread(this);
    }
    public void run()
    {
        try {
	    obj =  new Functions();
            PrintWriter out=new PrintWriter(s.getOutputStream(),true);
            BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            String user=in.readLine();
            String pass=in.readLine();
	    l.lock();
            cust=obj.login(user, pass);
            out.println(cust);
	    s.close();
	    }
	catch (IOException | SQLException e) 
	{
            e.printStackTrace();
        }
	finally{
	    l.unlock();
	}
    }
}
    class Socket2  implements Runnable{
	Functions obj;
	Socket socket;
	ReentrantLock l;
	public Socket2(Socket socket,ReentrantLock l) {
	    this.socket=socket;
	    this.l=l;
	    new Thread(this);
	}
	public void run() {
	    obj =  new Functions();
	    long cust = 0;
	    ObjectClass k = null;
	    try {
		ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
		k=(ObjectClass)in.readObject();
		in.close();
		out.close();
		socket.close();
		l.lock();
		obj.openacc(k.type,k.name1,k.name2,k.name3,k.mbno,k.phno,k.email,k.add,k.dob,k.uid,k.pan,k.gen,k.cust);
		
	    } catch (IOException | ClassNotFoundException ex) {
		Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    finally{
		l.unlock();
	    }
	    
	    
	}
    }
    class Accounts implements Runnable
    {
	Socket s;
	Functions obj;
	ReentrantLock l;
	public Accounts(Socket s,ReentrantLock l)
	{
	    this.l=l;
	    this.s=s;
	    new Thread(this);
	}
	public void run()
	{
	    obj=new Functions();
	    try {
		ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream in=new ObjectInputStream(s.getInputStream());
		long cust=in.readLong();
		l.lock();
		ArrayList<Long> a=obj.Accounts(cust);
		out.writeObject(a);
		in.close();
		out.close();
		s.close();
	    } catch (IOException ex) {
		Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    finally{
		l.unlock();
	    }
	    }
	}
	class Saving implements Runnable
	{
	    Socket s;
	    ReentrantLock l;
	    public Saving(Socket s,ReentrantLock l){
		this.s=s;
		this.l=l;
		new Thread(this);
	    }
	    public void run()
	    {
		 Functions obj=new Functions();
		try {
		    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
		    ObjectInputStream in=new ObjectInputStream(s.getInputStream());
		    ArrayList<Long> a=(ArrayList<Long>)in.readObject();
		    l.lock();
		    long account=obj.getSavingAccount(a);
		    float amount=obj.getamount();
		    out.flush();
		    out.writeLong(account);
		    out.flush();
		    out.writeFloat(amount);
		    out.flush();
		    in.close();
		    out.close();
		    s.close();
		} catch (IOException | ClassNotFoundException ex) {
		    Logger.getLogger(Saving.class.getName()).log(Level.SEVERE, null, ex);
		} 
		finally{
		    l.unlock();
		}
		
	    }
	}
    
class Loan_details implements Runnable
{
    Socket s;
    ReentrantLock l;
	    public Loan_details(Socket s,ReentrantLock l){
		this.l=l;
		this.s=s;
		new Thread(this);
	    }
	    public void run()
	    {
		 Functions obj=new Functions();
		try {
		    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
		    ObjectInputStream in=new ObjectInputStream(s.getInputStream());
		    ArrayList<Long> a=(ArrayList<Long>)in.readObject();
		    l.lock();
		    long account=obj.getLoanAccount(a);
		    float amount=obj.getamount();
		    out.flush();
		    out.writeLong(account);
		    out.flush();
		    out.writeFloat(amount);
		    out.flush();
		    in.close();
		    out.close();
		    s.close();
		} catch (IOException | ClassNotFoundException ex) {
		    Logger.getLogger(Saving.class.getName()).log(Level.SEVERE, null, ex);
		} 
		finally{
		    l.unlock();
		}
		
	    }
}
class FD implements Runnable
{
    Socket s;
    ReentrantLock l;
	    public FD(Socket s,ReentrantLock l){
		this.s=s;
		this.l=l;
		new Thread(this);
	    }
	    public void run()
	    {
		 Functions obj=new Functions();
		try {
		    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
		    ObjectInputStream in=new ObjectInputStream(s.getInputStream());
		    ArrayList<Long> a=(ArrayList<Long>)in.readObject();
		    l.lock();
		    long account=obj.getFDAccount(a);
		    float amount=obj.getamount();
		    long no_of_days=obj.getDays();
		    out.flush();
		    out.writeLong(account);
		    out.flush();
		    out.writeFloat(amount);
		    out.flush();
		    out.writeLong(no_of_days);
		    out.flush();
		    in.close();
		    out.close();
		    s.close();
		} catch (IOException | ClassNotFoundException ex) {
		    Logger.getLogger(FD.class.getName()).log(Level.SEVERE, null, ex);
		} 
		finally{
		    l.unlock();
		}
		
	    }
}
class AddBen implements Runnable
{
    Socket s;
    ReentrantLock l;
    public AddBen(Socket s,ReentrantLock l)
    {
	this.l=l;
	this.s=s;
	new Thread(this);
    }
    @Override
    public void run()
    {
	Functions obj=new Functions();
	try {
	  ObjectOutputStream  out = new ObjectOutputStream(s.getOutputStream());
	  ObjectInputStream in=new ObjectInputStream(s.getInputStream());
	  ben b=(ben)in.readObject();
	  l.lock();
	  String k=obj.addben(b.cust,b.account,b.name,b.IFSC,b.email,b.mobile,Integer.parseInt(b.pin));
	  out.flush();
	  out.writeUTF(k);
	  out.flush();
	} catch (IOException ex) {
	    Logger.getLogger(AddBen.class.getName()).log(Level.SEVERE, null, ex);
	} catch (ClassNotFoundException ex) {
	    Logger.getLogger(AddBen.class.getName()).log(Level.SEVERE, null, ex);
	} 
	finally{
	    l.unlock();
	}
    }
}
class showben implements Runnable
{
    Socket s;
    ReentrantLock l;
    public showben(Socket s,ReentrantLock l)
    {
	this.l=l;
	this.s=s;
	new Thread(this);
    }
    public void run()
    {
	try {
	    Functions obj=new Functions();
	    ObjectOutputStream  out = new ObjectOutputStream(s.getOutputStream());
	    ObjectInputStream in = new ObjectInputStream(s.getInputStream());
	    long cust=in.readLong();
	    l.lock();
	    ArrayList<ben> a=obj.getben(cust);
	    out.flush();
	    out.writeObject(a);
	    out.flush();
	    in.close();
	    out.close();
	    s.close();
	} catch (IOException ex) {
	    Logger.getLogger(showben.class.getName()).log(Level.SEVERE, null, ex);
	}
	finally{
	    l.unlock();
	}
    }
}
class Deleteben implements Runnable
{
    Socket s;
    ReentrantLock l;
    public Deleteben(Socket s,ReentrantLock l)
    {
	this.l=l;
	this.s=s;
	new Thread(this);
    }
    public void run()
    {
	try {
	    Functions obj=new Functions();
	    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
	    ObjectInputStream in = new ObjectInputStream(s.getInputStream());
	    String mobile=in.readUTF();
	    l.lock();
	    String k=obj.deleteben(mobile);
	    
	    out.flush();
	    out.writeUTF(k);
	    out.flush();
	    in.close();
	    out.close();
	    s.close();
	} catch (IOException ex) {
	    Logger.getLogger(Deleteben.class.getName()).log(Level.SEVERE, null, ex);
	} finally{
	    l.unlock();
	}
    }
}
class NEFT implements Runnable
{
    Socket s;
    ReentrantLock l;
    public NEFT(Socket s,ReentrantLock l)
    {
	this.l=l;
	this.s=s;
	new Thread(this);
    }
    public void run()
    {
	Functions obj=new Functions();
	try
	{
	ObjectOutputStream out=new ObjectOutputStream(s.getOutputStream());
	ObjectInputStream in=new ObjectInputStream(s.getInputStream());
	long cust=in.readLong();
	long From_account=in.readLong();
	long to_account=in.readLong();
	float amount=in.readFloat();
	int pin=in.readInt();
	l.lock();
	String k=obj.NEFT(cust, From_account, to_account, amount, pin);
	System.out.println(k);
	out.flush();
	out.writeUTF(k);
	out.flush();
	in.close();
	out.close();
	s.close();
	}catch(IOException e)
	{
	    System.err.println(e);
	}
	finally{
	    l.unlock();
	}
    }
}
public class JavaApplication1 {
    public static void main(String[] args) 
    {
	while(true)
	{
	try {
	    Selector selector=Selector.open();
	    ServerSocketChannel server;
	    int[] ports={99,255,575,1212,4242,4444,4545,5555,8585,3636,6363,7878};
	    for(int port:ports)
	    {
		try{
		server=ServerSocketChannel.open();
		server.configureBlocking(false);
		server.socket().bind(new InetSocketAddress(port));
		server.register(selector,SelectionKey.OP_ACCEPT);
		}
		catch(BindException e){}
	    }
	    while(selector.isOpen())
	    {
		selector.select();
		Set readKeys=selector.selectedKeys();
		Iterator<SelectionKey> iterator=readKeys.iterator();
		ReentrantLock l=new ReentrantLock();
		ExecutorService e=Executors.newFixedThreadPool(readKeys.size());
		while(iterator.hasNext())
		{
		    SelectionKey key=iterator.next();
		    if(key.isAcceptable())
		    {
			try{
			SocketChannel client=((ServerSocketChannel)key.channel()).accept();
			Socket socket=client.socket();
			System.out.println("Connection Accepted");
			switch(socket.getLocalPort())
			{
			    case 575:
			    {
				e.execute(new NEFT(socket,l));
				break;
			    }
			    case 99:
			    {
				e.execute(new Socket1(socket,l));
				break;
			    }
			    case 255:
			    {
				e.execute(new Socket2(socket,l));
				break;
			    }
			    case 1212:
			    {
				e.execute(new Loan_details(socket,l));
				break;
			    }
			    case 3636:
			    {
				e.execute(new Accounts(socket,l));
				break;
			    }
			    case 4242:
			    {
				e.execute(new Socket3(socket,l));
				break;
			    }
			    case 4444:
			    {
				e.execute(new Deleteben(socket,l));
				break;
			    }
			    case 4545:
			    {
				e.execute(new FD(socket,l));
				break;
			    }
			    case 5555:
			    {
				e.execute(new showben(socket,l));
				break;
			    }
			    case 6363:
			    {
				e.execute(new AddBen(socket,l));
				break;
			    }
			    case 7878:
			    {
				
				e.execute(new Saving(socket,l));
				break;
			    }
			    case 8585:
			    {
				e.execute(new Socket4(socket,l));
				break;
			    }
			}
			}
			catch(NullPointerException n){}
		    }
		    
		}
		e.shutdown();
	    }
	     
	}
	catch (IOException ex) {
	    Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    }
}