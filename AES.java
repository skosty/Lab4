import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class AES {

  public static void main(String[] args) {
    
    
    boolean encrypt = true;
    
    if(args[0].equalsIgnoreCase("d")){
      
      encrypt = false;
    }
    
    String keyval = null;
    
    try{
    Scanner scanner = new Scanner(new FileInputStream(args[1]));
    keyval = scanner.nextLine();
    scanner.close();
    } catch(IOException ioe){
      System.out.println("Error reading key file " + args[1]);
    }
    
    // Create and Expand Key 
    Key k = new Key(AESHelper.assembleKeyArray(keyval));
    k.expandKey();
    
    
    // Encryption Loop 
    String stateval = null;
    try{
      
      BufferedReader br = new BufferedReader(new FileReader(args[2]));
      String ext = (encrypt) ? ".enc" : ".dec";
      File outfile = new File(args[2] + ext);
//      System.out.println(outfile.getAbsolutePath());
      // Delete file contents
      FileOutputStream fos = new FileOutputStream(outfile, false);
      fos.close();
      BufferedWriter bw = new BufferedWriter(new FileWriter(outfile, true));
//      PrintWriter pw = new PrintWriter(new FileOutputStream(new File(args[1] + ".enc"), true));
      while((stateval = br.readLine()) != null){
//        System.out.println(stateval.length());
        if(stateval.isEmpty()) continue;
        if(!stateval.matches("[0-9a-fA-F]+")) continue;
        if(stateval.length() != 32) continue;
        State s = new State(AESHelper.assembleStateArray(stateval));
        int[][] output = null;
        if(encrypt)
        output = AESHelper.encrypt(s, k);
        else
        output = AESHelper.decrypt(s, k);
        
        
        // Save output to file 
        for(int col = 0; col < output.length; col++){
          for(int row = 0; row < output[col].length; row++){
           
//            pw.print(Integer.toHexString(output[col][row]));
           String outval = Integer.toHexString(output[col][row]);
           if(outval.length() == 1) outval = "0" + outval;
            bw.write(outval);
//            System.out.println(outval);
            bw.flush();
             
          }
          
        }
        
        bw.write("\n");
          bw.flush();
        
        
      } // end while 
      
      br.close();
      bw.close();
    }catch(IOException ioe){
      System.out.println("Error reading input file " + args[1]);
    } // end catch 
    
    
    
  } // endMain 
  
  
     public static void printColumnMatrix(int[][] m){
        for(int i = 0; i < m[0].length; i++){
      for(int j = 0; j < m.length; j++){
        System.out.print(Integer.toHexString(m[j][i]) + "\t");
      }
      System.out.println("");
    }
    } // end printColumnMatrix
  
} // end class 
