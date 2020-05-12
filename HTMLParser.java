
package Parser;

import downloader.SeleniumDownloader ;
import Puzzle.Cell;
//import sun.misc.JavaNetAccess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.net.*;
import java.lang.*;

public class HTMLParser{

    public ArrayList<String>[] hints; //clues stored here
    public Cell[][] puzzle;  //puzzle ans stored here
    public int[] hint_length = new int[10];;
    public ArrayList<String> answers ;
    public ArrayList<String> ints ;
    
    
    ArrayList<String> across = new ArrayList<String>() ;
	ArrayList<String> down = new ArrayList<String>();
    
    
    public HTMLParser() {
      /*  hints = new ArrayList[2];
        hint_length = new int[10];
        hints[0] = new ArrayList<String>();
        hints[1] = new ArrayList<String>();
        puzzle = new Cell[5][5];
        for(int i = 0;i < 5;i++)
            for(int j = 0; j<5;j++)
                puzzle[i][j] = new Cell();*/
    }

    public boolean isBlank(int x,int y)
    {
        return ("" + puzzle[x][y].getLetterOnCell()).equals("-1");
    }

    public  void readPuzzle( String puzzleName){
        String content;
        StringBuilder contentBuilder = new StringBuilder();
        // /Users/erimerdal/Desktop/AIPuzzleSolver/src/ instead of dot

        String revealPuzzlePath = "C:/Users/Hatim/Desktop/AI 2019/AI final/src/" + puzzleName + ".txt";
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
        parsePuzzle( content);
    }

    public void setHintLengths(){
        // This one is used for finding the lengths of across hints.
        for(int i = 0; i < 5; i++){
            int hintLength = 0;
            for(int j = 0; j < 5; j++){
                if(isBlank(i,j)){

                }
                else{
                    hintLength = hintLength + 1;
                }
            }
            hint_length[i] = hintLength;
        }
        // This one is used for finding lengths of down hints.
        for(int a = 0; a < 5; a++){
            int hintLength = 0;
            for(int b = 0; b < 5; b++){
                if(isBlank(b,a)){

                }
                else{
                    hintLength = hintLength + 1;
                }
            }
            hint_length[a + 5] = hintLength;
        }

        /**for(int test = 0; test < 10; test++){
            System.out.println("Length of hint" + test + " is = " + hint_length[test]);
        }*/
    }

//Searching for hints and clues using certain patterns
    private  void parsePuzzle(String content){
        
    	
    	answers = new ArrayList<String>();
    	boolean continu = true ;
    	hints = new ArrayList[2];
        hints[0] = new ArrayList<String>();
        hints[1] = new ArrayList<String>();
        puzzle = new Cell[5][5];
       
        
        
        for(int i = 0;i < 5;i++)
            for(int j = 0; j<5;j++)
                puzzle[i][j] = new Cell();
       
        String hintNoPattern = "\"Clue-label--";
        String hintPattern = "\"Clue-text--";
        int acrossDown = 0; // 0 for across, 1 for down
        String tempContent = content;
        tempContent = tempContent.substring(tempContent.indexOf(">Across<")+8);
        while(true){
            String hintString;
            int downIndex = 99999999;
            if(tempContent.contains(">Down<")){
                downIndex = tempContent.indexOf(">Down<");
            }
            int hintNoIndex = tempContent.indexOf(hintNoPattern); //finding index
            if(downIndex < hintNoIndex){
                acrossDown = 1;
                tempContent = tempContent.substring(tempContent.indexOf(">Down<")+6);
                hintNoIndex = tempContent.indexOf(hintNoPattern); //finding index after update
                //System.out.println("Down");
            }
            if(hintNoIndex == -1)
                break;
            hintNoIndex = hintNoIndex+20;
            String hintNo = ""+ tempContent.charAt(hintNoIndex);
            if(tempContent.charAt(hintNoIndex+1) != '<'){
                hintNo = hintNo +tempContent.charAt(hintNoIndex+1);
            }
            hintString = hintNo + "-";
            tempContent = tempContent.substring(hintNoIndex);

            int hintStart = tempContent.indexOf(hintPattern); //finding hint
            if(hintStart == -1)
                break;
            hintStart = hintStart + 19;
            String hint = "";
            while(tempContent.charAt(hintStart) != '<'){
                hint = hint + tempContent.charAt(hintStart);
                hintStart++;
            }
            hintString = hintString + hint;
            if(acrossDown == 0)
                hints[0].add(hintString);
            if(acrossDown == 1)
                hints[1].add(hintString);
            tempContent = tempContent.substring(hintStart);
        }
       
        //To get downa nd across integers list
        System.out.println( hints[0].toString()) ;
        System.out.println(hints[1].toString()) ;
      
        for(int i = 0 ; i < hints[0].size() ; i++)
        {
        	
        	across.add(hints[0].get(i).charAt(0)+"") ;
        }
        System.out.println(across.toString());
        for(int i = 0 ; i < hints[1].size() ; i++)
        {
        	
        	down.add(hints[1].get(i).charAt(0)+"") ;
        }
        System.out.println(down.toString());
        ///PARSING HINTS END
        //PARSING PUZZLE
        tempContent = content;
      
        String cellEmptyPattern = "Cell-block";
        String cellLetterPattern = "text-anchor=\"middle\" font-size=\"66.67\">"; // 39
        String cellLetterNo = "text-anchor=\"start\" font-size=\"33.33\">";
        
        
        tempContent = tempContent.substring(content.indexOf(cellEmptyPattern) + 10);
        
        for(int j = 0 ; j < 5 ; j ++){
        	for(int i = 0 ; i < 5 ; i ++){
            //
            int cellEmptyIndex = tempContent.indexOf(cellEmptyPattern);
            //
            int cellLetterNoIndex = tempContent.indexOf(cellLetterNo);
            //
            int cellLetterIndex = tempContent.indexOf(cellLetterPattern);
            if(cellEmptyIndex == -1){ //cell is not empty 

            
            
            
            }
            else if((cellLetterIndex == -1 && cellLetterNoIndex == -1 && cellEmptyIndex != -1) || (cellEmptyIndex < cellLetterIndex && cellEmptyIndex < cellLetterNoIndex && cellEmptyIndex != -1)){
                puzzle[i][j].setQuestionNumber("");
                puzzle[i][j].setLetterOnCell("-1"); //-1 for blank
                System.out.println("Letter: -1");
                tempContent = tempContent.substring(cellEmptyIndex + 10);
               

                continue;
            }
            else{}
            if(cellLetterNoIndex == -1) //no cell letter
                cellLetterNoIndex = 9999999;
            if((cellLetterNoIndex < cellLetterIndex)){
                String letterNo = "";
                cellLetterNoIndex = cellLetterNoIndex + 38 +59; // 38
                while(tempContent.charAt(cellLetterNoIndex) != '<'){
                    letterNo = letterNo + tempContent.charAt(cellLetterNoIndex);
                    cellLetterNoIndex++;
                    System.out.println("Letter: right");
                }
                tempContent = tempContent.substring(cellLetterNoIndex);
                puzzle[i][j].setQuestionNumber(letterNo);
                System.out.println(letterNo);
            }

            cellLetterIndex = tempContent.indexOf(cellLetterPattern);
            if(cellLetterIndex == -1)
                break;
            cellLetterIndex = cellLetterIndex + 39 + 52;
            String letter = "";
            //System.out.println("Hülo: " + tempContent);
            while(tempContent.charAt(cellLetterIndex) != '<'){
                //System.out.println("Hülo: " + tempContent.charAt(cellLetterIndex));
                letter = letter + tempContent.charAt(cellLetterIndex);
                cellLetterIndex++;
            }
            tempContent = tempContent.substring(cellLetterIndex);
            puzzle[i][j].setLetterOnCell(letter);
            //System.out.println("Letter: " + letter);
            System.out.println(letter);
          
        	}
        	
        	
        }
        setHintLengths();
        answerlist() ;
    }
    
    public Cell[][] getAnswers()
    {
    	
    	return puzzle ;
    }
   
    public ArrayList<String> answerlist()
    {
    	
        answers = new ArrayList<>() ;
    	String answer = "" ;
    	ints = new ArrayList<>() ;
    	
    	
    	    for(int j = 0 ; j < 5 ; j ++){
        	for(int i = 0 ; i < 5 ; i ++){
    	
        		for(int n = 0 ; n < down.size() ; n ++)   
        		{
        			if(puzzle[i][j].getQuestionNumber().equals(down.get(n)) )
        		{
        			for(int k = j ; k < 5 ; k++)
        			{
        				if( puzzle[i][k].getLetterOnCell().equals("-1")){
        					
        					continue ;
        				}
        			answer = answer + puzzle[i][k].getLetterOnCell() ;
        			}
        			answers.add(answer) ;	
        	        ints.add(down.get(n)+ "" + "Down") ;
        		}	
        		}
        		
        		answer = "" ;
        		for(int n = 0 ; n < across.size() ; n ++)   
        		{
        		
        		if(puzzle[i][j].getQuestionNumber().equals(across.get(n)))
        		{
        			
        			for(int k = i ; k < 5 ; k++)
        			{
                       if( puzzle[k][j].getLetterOnCell().equals("-1")){
        					
        					continue ;
        				}
        			answer = answer + puzzle[k][j].getLetterOnCell() ;
        			}
    			   answers.add(answer);	
    			   ints.add(across.get(n)+ "" + "Across") ;        		}	
        		
        		
        		}
        		answer = "" ;
       }
        	}
        	
    	   for(int i = 0 ; i < answers.size(); i++)
    	   {
    		  answers.set(i, answers.get(i).replaceAll("[0-9]","")) ;
    		   
    	   }
            System.out.println(ints.toString());
    	    return answers ;
    	
    }
    

}