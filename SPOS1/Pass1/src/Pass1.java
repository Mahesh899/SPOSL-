// for error handling for mneumonic defined as labe;l check in the mneumonic table and  check if the label is present in the mneuminic table if present then error

import java.io.*;
import java.util.*;

class Mnemonic
{	
	String mname;
	String stmnt;
	String opcode;
	String length;	
}
class t_data   //to pass the complete data via object to the file
{
	int sno;
	String value;
	int addr;
	t_data()
	{
		addr = 0;
	}
}
public class Pass1
{	
	public static void main(String[] args)throws Exception
	{
		
		ArrayList tokens=new ArrayList();   //arraylist to store the tokens 
		
		File file = new File("SPOS/source_code.asm");
		
		StringBuffer stringBuffer = new StringBuffer();   //required for StringTokenizer

		BufferedReader br = new BufferedReader(new FileReader(file));
 
		String st;
		
		while ((st = br.readLine()) != null)
		{
			StringTokenizer str=new StringTokenizer(st," ,");  //Delimiters as space and ,
			while (str.hasMoreTokens())  
			{
				tokens.add(str.nextToken());  //adding tokens to our arraylist (tokens)
				//System.out.println(str.nextToken());		
			}
			stringBuffer.append(st);
			stringBuffer.append("\n");
		}
		
		System.out.println("Contents of file:");
		System.out.println(stringBuffer.toString());
		
		Iterator itr = tokens.iterator();
		
		System.out.println("Tokens:");
		while(itr.hasNext()) 
		{	
	         System.out.print(itr.next()+ "\n");
	    }
  	 
		br.close();
		
		File file1 = new File("SPOS/Mneumonics_table.txt");
	
		BufferedReader br1 = new BufferedReader(new FileReader(file1));
   
		ArrayList<Mnemonic> m=new ArrayList<Mnemonic>();
   
		String st1;
		
		while ((st = br1.readLine()) != null)
		{
			StringTokenizer str=new StringTokenizer(st," ");
			while (str.hasMoreTokens())
			{
				Mnemonic obj=new Mnemonic();
				obj.mname=str.nextToken();
				obj.stmnt=str.nextToken();
				obj.opcode=str.nextToken();
				obj.length=str.nextToken();
				
				m.add(obj);  //adding mneumonics to our arraylist (m)
				
				//System.out.println(str.nextToken());
			}
			
		}
		br1.close();
		
		
		/*  Displaying tokenized mnemonics
		  
		      for(int i=0;i<m.size();i++)
		      { 
		         System.out.println(m.get(i).mname);
            	 System.out.println(m.get(i).stmnt);
		         System.out.println(m.get(i).opcode);
		         System.out.println(m.get(i).length);
		      }           */         
		                              
		
		
		File lit=new File("SPOS/literal_table.txt");
		FileWriter fr=new FileWriter(lit);
		
		File ic=new File("SPOS/intermediate_code.txt");
		FileWriter fr1=new FileWriter(ic);
		
		File sym=new File("SPOS/symbol_table.txt");
		FileWriter fr2=new FileWriter(sym);
		
		File pool=new File("SPOS/pool_table.txt");
		FileWriter fr3=new FileWriter(pool);
		
	
		int cntlit=0; //for the srno of literal table
		int cntsym=0;  //for the srno of symbol table
		int lc=0;   //location counter

		int check=0;
		
		ArrayList<t_data> array_lit = new ArrayList<t_data>();  
		ArrayList<t_data> array_sym = new ArrayList<t_data>();
		
		int flag = 0;
		int f = 0;
		int check_lit_after_ltorg = 0;
		int cnt_pool = 0;
		
	//--------------------------------------------------------------------------------------------------
		
		for(int i=0;i<tokens.size();i++)  //loop to check and compare the read input and store it in corresponding files
		{
			String str=(String)tokens.get(i);
			
			if(str.equals("START"))
			{
				//System.out.println("Value of i in START"+i);
				flag = 1;
				lc=Integer.parseInt(tokens.get(i+1).toString()); //storing the location counter value in the instruction start 
				i++;  //to skip the address
				//System.out.println("Value of i in after START"+i);
		    }
					
			else if(str.contains("="))
			{			 
				if(cnt_pool == 0)  //first literal and hence first entry in pool table
				{
					//System.out.println("RED");
					fr3.write("# 1");
					cnt_pool++;
				}
				String temp = str.substring(2,3);
					 int tem = 0;
					 for(int y = 0;y < array_lit.size();y++)
					 {
						 if(array_lit.get(y).value.equals(temp))
						 {
							 tem = 1;
							 break;
						 }
					 }
					 if(check_lit_after_ltorg > 0)    //to check if the LTORG instruction is executed
					 {
						 t_data a=new t_data();
					     cntlit++;
						 a.sno=cntlit;
						 a.value=new String(temp);
						 array_lit.add(a);
						 fr1.write(" ,"+"("+"L,"+cntlit+")");  //to make entry in intermediate code
						 
						 fr3.write("\n# "+cntlit);  //write to pool table
					 }
					 if(tem == 0)
					 {
						 t_data a=new t_data();
					     cntlit++;
						 a.sno=cntlit;
						 a.value=new String(temp);
						 array_lit.add(a);
						 fr1.write(" ,"+"("+"L,"+cntlit+")");  //to make entry in intermediate code
					 }
					// System.out.println("Value of i in ="+i);
			 }
				
			else if((str.equals("AREG"))||(str.equals("BREG"))||(str.equals("CREG"))||(str.equals("DREG")))
			{
				fr1.write(" "+str);  //write to intermediate code file
				//System.out.println("Value of i in reg"+i);
			}
			 
			else if((str.equals("MOVER"))||(str.equals("MOVEM"))||(str.equals("ADD"))||(str.equals("SUB"))||(str.equals("MULT"))||(str.equals("STOP"))||(str.equals("BC")))
			{
				
				int g=0;  //for the length of instruction
				
				for(int j=0;j<m.size();j++)  //to check in mneumonic table
				{
					if(m.get(j).mname.equals(str))
					{
						fr1.write("\n"+lc+" "+"("+m.get(j).stmnt+","+m.get(j).opcode+")"+",");  //write in intermediate code file
						g=Integer.parseInt(m.get(j).length);  //length of instruction
						break;
					}
				}
				//System.out.println("Value of i in intruc"+i);
				lc=lc+g;  //adding length of instruction in location counter
			}
			 
			else if( (str.equals("END")) ||(str.equals("ORIGIN"))||(str.equals("EQU")))  //start is already computed
			{
				//System.out.println("hello");
				if(str.equals("END"))
				{
					fr1.write("\n"+lc+" (AD,02)");  //write to intermediate code file
					
					for(int y=0;y<array_lit.size();y++)  //to allocate memory location to the literals after END is encountered
					{
						if( ( array_lit.get(y).addr == 0))
						{
							array_lit.get(y).addr=lc;
							lc++;
							i++;  //i is to get the next token from the arraylist token
							String lit_value = tokens.get(i).toString().substring(2,3);
							fr1.write("\n"+"       "+lit_value);
						}
					}
					break;
				}
				if(str.equals("ORIGIN"))
				{
					fr1.write("\n"+lc+" (AD,03) "+tokens.get(i+1).toString());  //write to intermediate code file
					
					String nxtstr = tokens.get(i+1).toString();  //extract the next token from the arraylist eg LOOP+2
					StringTokenizer st3=new StringTokenizer(nxtstr,"+");  //seperate the token eg LOOP+2 then seperate LOOP and 2 with delimiter as +
			
					String var=st3.nextToken();  //stores LOOP
					int a=Integer.parseInt(st3.nextToken().toString());  //stores 2
				
					int p=0; //to store the address of LOOP from symbol table
					
					for(int y=0;y<array_sym.size();y++)
					{
						if(array_sym.get(y).value.equals(var))   //to retrieve the address of LOOP from the symbol table
						{
							p=array_sym.get(y).addr;
							break;
						}
					}
					lc=p+a;
					i++;  //incrementing the token arraylist index 				 
				}
				if(str.equals("EQU"))
				{
					fr1.write("\n"+lc+" (AD,04) "+tokens.get(i+1).toString());  //write to intermediate code file
					
					String prev_token = tokens.get(i-1).toString();  //retrieve BACK
					String nxt_token = tokens.get(i+1).toString();   //retrieve LOOP
					
					int address=0;
					
					for(int u=0;u<array_sym.size();u++) //to retrieve the address of LOOP
					{
						if(array_sym.get(u).value.equals(nxt_token))
						{
							address = array_sym.get(u).addr;
							break;
						}
					}
					for(int u=0;u<array_sym.size();u++)  //to assign the address to BACK
					{
						if(array_sym.get(u).value.equals(prev_token))
						{
							array_sym.get(u).addr = address;
							break;
						}
					}
   					i++; 
				}
			}
			else if(str.equals("LTORG"))  
			{	
				check_lit_after_ltorg++;
				
				//System.out.println("Value of i in ltorg"+i);
				//fr1.write("\n"+"    "+"(AD,05) "+tokens.get(i+1).toString());  //write to intermediate code file
				i++;
				
				for(int y=0;y<array_lit.size();y++)   //to check if the literal specified after LTORG is assigned a memory location in the literal table if not then assign
				{
					if( ( array_lit.get(y).value.equals(tokens.get(i).toString().substring(2,3)) ) && (array_lit.get(y).addr==0) )
					{
						array_lit.get(y).addr = lc;
						String lit_value = tokens.get(i).toString().substring(2,3);
						fr1.write("\n"+lc+" (AD,05) "+lit_value);
						lc++;
						i++;
					}
				}
				i--;
				//System.out.println("Value of i in ="+i);
		    }
			else if(str.equals("DS"))
			{	
				fr1.write("\n"+lc+" (DL,02) "+tokens.get(i+1).toString());
				int p=Integer.parseInt(tokens.get(i+1).toString());
				lc=lc+p;
				i++;  //to skip the next token
				//System.out.println(i);
			}
			 else   //in case of variables
			 {
				 if(check==0)  
				 {
					 t_data b = new t_data();
					 cntsym ++;
					 b.sno = cntsym;
					 b.value = new String(str);
					 
					 if(tokens.get(i+1).equals(":"))   //for the address of label field 
					 {
						 for(int k=0;k<m.size();k++)
						 {
							// if(m.get(k).mname.equals(str))
							 //{
							//	 System.out.println("ERROR!Mneumonics cannot be used as a label");
							 //}
							 //else
							 //{
								 b.addr=lc;
								 i++;	
								 array_sym.add(b);
							 //}
						 }
					 }
					 else
					 {
						 array_sym.add(b);
						 fr1.write(" , "+str); 
					 }
					 check=1;
					// System.out.println("Value of i in after first A"+i);
				 }
				 else
				 {
					 f=0;
					
				 for(int k=0;k<array_sym.size();k++)
				 {
					 if(array_sym.get(k).value.equals(str))
					 {
						 //System.out.println(tokens.get(i));
						 //System.out.println("yellow");
						 //System.out.println("Value of i in ="+i);
						 if(tokens.get(i+1).equals(":"))
						 {
							 array_sym.get(k).addr=lc;
							 i++;  //to skip :
							// System.out.println("Value of i in ="+i);
						 }						 
						 else if((tokens.get(i+1).equals("DS")) || (tokens.get(i+1).equals("EQU")) )
						 {
							// System.out.println(i);
							 if(tokens.get(i+1).equals("DS"))
							 {
								 for(int y=0;y<array_sym.size();y++)
								 {
									 if(array_sym.get(y).value.equals(str))
									 {
										 array_sym.get(y).addr = lc;
									 }
								 }
								// System.out.println(i);
							 }
							 if(tokens.get(i+1).equals("EQU"))
							 {
								 f = 1;  //inorder to skip the if(f==0) loop
								 break;
							 }
						 }
						 else
						 {
							 fr1.write(" , "+str);
							 //System.out.println("Value of i in after second a"+i);
						 }
   						 f=1;
						 break;
					 }
				 }
				 if(f==0)
				 {
				 t_data b=new t_data();
				 cntsym++;
				 b.sno=cntsym;
				 b.value=new String(str);
				 
				 if(tokens.get(i+1).equals(":"))  //if the variable is a label
				 {
					 b.addr=lc;
					 //System.out.println("Value of i in before LOOP"+i);
					 i++;   //to skip ':'
					// System.out.println("Value of i in after skiping : "+i);
					 array_sym.add(b);
				 }
				 else
				 {
					 fr1.write(" , "+str);
					 array_sym.add(b);
					 //System.out.println("Value of i in after B "+i);
					 //System.out.println("LOC"+lc);
				 }
				 }
				 }
				 }
			
			}
		
		//-----------------------------------------------------------------------------------------------------------
		fr3.close();
		FileWriter fw= new FileWriter(lit);
		
		for(int y=0;y<array_lit.size();y++)
		{
			fw.write(array_lit.get(y).sno+" "+array_lit.get(y).value+" "+array_lit.get(y).addr+"\n");
		}
		
		fw.close();
		
		FileWriter fw1= new FileWriter(sym);
		for(int y=0;y<array_sym.size();y++)
		{
			fw1.write(array_sym.get(y).sno+" "+array_sym.get(y).value+" "+array_sym.get(y).addr+"\n");
		}
		fw1.close();
		fr1.close();
		}
	}
