/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

import java.io.Serializable;


public class ben implements Serializable
{
    public long cust,account;
    public String name,IFSC,email,mobile,pin;
    public ben(){}
    public ben(long cust,long account,String name,String IFSC,String email,String mobile,String pin)
    {
	this.cust=cust;
	this.account=account;
	this.name=name;
	this.IFSC=IFSC;
	this.email=email;
	this.mobile=mobile;
	this.pin=pin;
    }
}
