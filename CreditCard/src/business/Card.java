/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.NumberFormatException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
/**
 *
 * @author Admin
 */
public class Card {
    
    private int acctno;
    private double climit, baldue;
    private String errmsg, actmsg;
    
    public Card(){
        this.acctno = 0;
        this.climit = 0;
        this.baldue = 0;
        this.errmsg = "";
        this.actmsg ="";
        
        while (this.acctno == 0) {
            try {
                this.acctno = (int)(Math.random()* 1000000);
                BufferedReader in = new BufferedReader(
                    new FileReader("cc" + this.acctno + ".txt"));
                
                //account already exists
                in.close();
                this.acctno = 0;
                
            }catch(IOException e){
            //cc + ACCT  odes not yet exist
                this.climit = 1000;
                
                if (writeStatus()== true){
                    this.actmsg = " Account " + this.acctno + " Opened.";
                        if (writeLog(this.actmsg)== false){
                           this.climit= 0;
                           this.acctno = 0;
                           
                        }else {
                            this.climit= 0;
                            this.acctno =0;
                        }
                }
            }
        }//end of while
        
    }//end of the empty constructor
    
 public Card (int acno){

this.acctno = acno;
this.climit = 0;
this.baldue = 0;
this.errmsg = "";
this.actmsg = "";

try {
BufferedReader in = new BufferedReader ( 
    new FileReader ("CC" + this.acctno + ".txt"));
    this.climit =Double.parseDouble(in.readLine());
    this.baldue = Double.parseDouble (in.readLine());
    in.close();
} catch(NumberFormatException e){
    this.errmsg = "Error operning account" + this.acctno + " " + e.getMessage();
}/**
    this.acctno = 0;
    this.climit = 0;
    this.baldue = 0;
}
}   
    
private boolean writeStatus() {
//update cc file with current climate and baldue

boolean result = true;
try {
    PrintWriter out = new PrintWriter (
        new FileWriter("CC" + this.acctno + ".txt"));
    out.println(this.climit);
    out.println(this.baldue);
    out.close();

}catch (IOException e){
    result = false;
    this.errmsg = "Write status error: " + e.getMessage();

}
return result;
}//end of writeStatus

private boolean writeLog (String msg){

boolean result = true;
try {
    PrintWriter out = new PrintWriter (
      new FileWriter("CCL" + this.acctno + ".txt", true));
        Calendar cal = Calendar.getInstance();
        DateFormat df = DateFormat.getDateTimeInstance();
        String ts = df.format(cal.getTime());
        out.println(ts + ":\t" + msg);
        out.close();
}catch (IOException e) {
    result = false;
    this.errmsg = "Write log error: " + e.getMessage();
    }
return result;
}//end of writLog

public int getAcctNo() {

return this.acctno;
}

public double getCLimit(){

return this.climit;

}

public double getBDue(){

return this.baldue;

}


public double getAvail(){
    
  return (this.climit - this.baldue);  
}


public String getErrorMsg(){

    return this.errmsg;
}


public String getActMsg(){
    
    return this.actmsg;
}

public void setPayment(double pmt){
    this.errmsg = "";
    this.actmsg = "";

    if (this.acctno <=0){
        this.errmsg = "Payment attempt against invalid account.";
        return;
    }
    if (pmt <= 0){
        this.actmsg = "Payment of " + pmt +
                " rejected: amoutn must be positive.";
        if (!writeLog(this.actmsg));{
        this.actmsg = "";
        }
    } else {
        this.baldue -= pmt;
        if (writeStatus()){
            this.actmsg = " Payment of " + pmt + " posted.";
            if (!writeLog (this.actmsg)){
                this.actmsg = "";
            }
        }else {
            this.actmsg = "";
            this.baldue += pmt;
        }
    }

}// end of setPayment method
    
    public ArrayList<String>getLog() {
        
        this.errmsg = "";
        this.actmsg = "";
        
        ArrayList<String> log = new ArrayList<>();
        
        if (this.acctno <=0){
            this.actmsg = " Log request on non-active account.";
            return log;
        } 
        
        try {
            BufferedReader in = new BufferedReader ( new FileReader ("CCL" +
                    this.acctno + ".txt"));
            String s= in.readLine();
            while (s !=null){
                log.add(s);
                s= in.readLine();
                
            }
            in.close();
            this.actmsg = " Log returned for account " +
                    this.acctno;
            
        }catch (FileNotFoundException e){
            this.errmsg = " Log file missing for account: " +
                    this.acctno;
        } catch (IOException e){
            this.errmsg = "Error reading log file for account" +
                    this.acctno;
        }
       return log;
    }//end of getLong method
    
    
    
    
    
    
}//end of class
