/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

/**
 *
 * @author aditya
 */
import java.io.Serializable;
import java.util.Date;


public class ObjectClass implements Serializable
{
     public String type,name1,name2,name3,mbno,phno,email,add,uid,pan,gen;
     public Date dob;
     public long cust;
    ObjectClass()
    {
       
    }
   
   public  ObjectClass(String type,String name1,String name2,String name3,String mbno,String phno,String email,String add,Date DOB,String uid,String pan,String gen,long cust)
    {
        this.type=type;
        this.name1=name1;
        this.name2=name2;
        this.name3=name3;
        this.mbno=mbno;
        this.phno=phno;
        this.email=email;
        this.add=add;
        this.uid=uid;
        this.pan=pan;
        this.gen=gen;
        this.dob=DOB;
	this.cust=cust;
    }
}