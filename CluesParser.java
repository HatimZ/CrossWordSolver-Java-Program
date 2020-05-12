
package Parser;

 

//import sun.misc.JavaNetAccess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.net.*;
import java.lang.*;
 

public class CluesParser extends HTMLParser{

    public ArrayList<String>[] hints; //clues stored here
    public int[] hint_length = new int[10];;
    public ArrayList<String> newclues ;
    public ArrayList<String> incomplete ;
    public ArrayList<String> complete ;
    ArrayList<String> across = new ArrayList<String>() ;
	ArrayList<String> down = new ArrayList<String>();
    HTMLParser parse ;

    public CluesParser() {
    
    }

    

    public  void readClues( String ClueName){
        String content;
        StringBuilder contentBuilder = new StringBuilder();
       

        String revealPuzzlePath = "C:/Users/Hatim/Desktop/AI 2019/AI final/src/"+ ClueName +".txt";
        try { 
            BufferedReader fileReader = new BufferedReader(new FileReader(revealPuzzlePath));
            String str;
           
            //html file is read into Stringbuilder to use its methods
            while ((str = fileReader.readLine()) != null) {
                contentBuilder.append(str);
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println(" I HAVE IO EXCEPTION DAMNIT ");
            return;
        }
        content = contentBuilder.toString();
        parseClues( content);
    }

//Searching for hints and clues using certain patterns
    
    
    private  void parseClues(String content){
        
    	//Found words
        newclues = new ArrayList<>() ;
        String CluePattern = "td class=\"def\"";
        String tempcontent = content ;
        while(true){
        int clueNoIndex = tempcontent.indexOf(CluePattern);
        if(clueNoIndex == -1) //defination of clue doesnot exist
        { break;
        } 
        
        String clue = "";
        clueNoIndex = clueNoIndex + 15 ; // 38
        while( tempcontent.charAt(clueNoIndex) != '<'){
            clue = clue + tempcontent.charAt(clueNoIndex);
            clueNoIndex++;
        }
        //Found clues through HTMLScraping
        newclues.add(clue) ;
        System.out.println( "New Clues are " + newclues.toString());
        tempcontent = tempcontent.substring(clueNoIndex);
        }
        
        //Not found words
        complete = new ArrayList<>() ;
        String wordfoundpattern = "td class=\"word\"";
        String tempcontent2 = content ;
        while(true){
        int wordfoundpatternIndex = tempcontent2.indexOf(wordfoundpattern);
        if(wordfoundpatternIndex == -1) //No words found
        { break;
        } 
        
        String wordfound = "";
        wordfoundpatternIndex = wordfoundpatternIndex + 16 ; // 38
        while( tempcontent2.charAt(wordfoundpatternIndex) != '<'){
            wordfound = wordfound + tempcontent2.charAt(wordfoundpatternIndex);
            wordfoundpatternIndex++;
        }
        
        complete.add(wordfound) ;
        
        tempcontent2 = tempcontent2.substring(wordfoundpatternIndex);
        }
        
        incomplete = new ArrayList<>() ;
        boolean completed = false ;
        ArrayList<String> tempp = new ArrayList<>() ;
        readPuzzle("26-12-2019") ;
        tempp = answerlist() ;
        
        System.out.println("Original Answers");
        System.out.println(tempp.toString());
       
      for(int i = 0 ; i < tempp.size(); i++){
        	
        	tempp.set(i ,tempp.get(i).toLowerCase()) ;
        } 

       /* for (int i = 0 ; i < complete.size();i ++)
        {
     	  for(int j = 0 ; j < tempp.size() ; j ++)
     	  {
     		  
     		  if(complete.get(i).equals(tempp.get(j)))
     		  {
     			  
     			  complete.set(i, j + complete.get(i)+"") ;
     		  }
     		  
     	  }
        }
        */
        
        
        
        
      tempp.removeAll(complete);
       incomplete = tempp ;  
       
       System.out.println("Not found Words");
       System.out.println(incomplete.toString());
       
      

       
       
       
    }
        
        
        

}