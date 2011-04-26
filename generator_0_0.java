/*     
 *    
 *  Author     : ACIMS(Arizona Centre for Integrative Modeling & Simulation)
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */
package DWLProject;



//import java.lang.*;

import GenCol.*;


import model.modeling.*;
//import model.simulation.*;

import view.modeling.ViewableAtomic;
//import view.simView.*;


public class generator_0_0 extends ViewableAtomic{


  protected double int_arr_time;
  protected int count;
  protected int records = 1000;
  protected int errors = 0;
  protected int categories = 5;

  public generator_0_0() {this("generator", 10);}

public generator_0_0(String name,double Int_arr_time){
   super(name);
  // addInport("in");
   addOutport("start");
   addOutport("FFout");
   //addInport("stop");
  // addInport("start");
   int_arr_time = Int_arr_time ;
   initialize();
    //addTestInput("start",new entity(""));
    //addTestInput("stop",new entity(""));
}

public void initialize(){
   holdIn("passive", 30);

   //   phase = "passive";
   //  sigma = INFINITY;
     count = 1000;
     super.initialize();
 }

public void  deltext(double e,message x)
{
Continue(e);
/*if(phaseIs("passive")){
   for (int i=0; i< x.getLength();i++)
      if (messageOnPort(x,"start",i))
         holdIn("active",int_arr_time);
}
 if(phaseIs("active"))
   for (int i=0; i< x.getLength();i++)
      if (messageOnPort(x,"stop",i))
         phase = "finishing";  */
}

public void  deltint( )
{
/*
System.out.println(name+" deltint count "+count);
System.out.println(name+" deltint int_arr_time "+int_arr_time);
System.out.println(name+" deltint tL "+tL);
System.out.println(name+" deltint tN "+tN);
*/



	if(phaseIs("passive")){
   //count = count +1;
   holdIn("createFF",int_arr_time);
}
else 
	if (phaseIs("createFF")){
		
		passivate();
	};
}

public message  out( )
{

//System.out.println(name+" out count "+count);

   message  m = new message();
   if (phaseIs("passive")){
	   content con = makeContent("start",new entity("start"));
	   m.add(con);
   } else
	   if (phaseIs("createFF")){
		   FlatFile ff = new FlatFile();
		   ff.setNumberOfRecords(records);
		   ff.setNumberOfErrors(errors);
		   ff.setNumberOfCategories(categories);
		   m.add(makeContent("FFout", ff));          
	   };
  return m;
}

public void showState(){
    super.showState();
    System.out.println("int_arr_t: " + int_arr_time);
}

public String getTooltipText(){
   return
   super.getTooltipText()
    +"\n"+" #Records: " + records
    +"\n"+" #Categories: " + categories
     +"\n"+" #Errors: " + errors;
  }

}

