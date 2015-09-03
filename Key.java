import java.util.Arrays;

class Key {
  
  int[][] enckey;
  int[][] exkey;
  int iword = 0;
  int keysize = KeySize.SMALL.value;
  int ircon = 1;
  boolean expanded = false;
  public enum KeySize {
    SMALL(128), MEDIUM(192), LARGE(256);
    public int value;
    
    private KeySize(int value){
      this.value = value;
    }
    
  }; // end KeySize enum
  
  
  
  public Key(int[][] initialKey){
    
    this.enckey = initialKey;
    
  }
 
  public boolean isExpanded(){
    return expanded;
  }
  
  public void expandKey(){
    if(this.isExpanded()) return;
    if(this.keysize == Key.KeySize.SMALL.value){
      // Instantiate ekey
      exkey = new int[44][4];
      for(int i = 0; i < exkey.length; i++){
        // Set first 4 words
        if(i < 4){
          exkey[i] = enckey[i];
          continue;
        } // end if i < 4
      
        exkey[i] = Arrays.copyOf(exkey[i-1], 4); // Set array to previous word
        
        // First word of round key
        if( i % 4 == 0){
         
          schedule_core(exkey[i], ircon);
          ircon++;
        }// end first word 
        
        // XOR with 4th previous word
        exkey[i] = AESHelper.arrayXOR(exkey[i], exkey[i-4]);
        
      } // end for 
    } // end small keysize (128)
    
//    System.out.println(Arrays.toString(exkey[exkey.length-1]));
    
    this.expanded = true;
    
  } // end expandKey
  
  
  public int[][] getRoundKey(int round){
    if(!this.isExpanded()) this.expandKey();
    int i = round*4;
    int[][] rkey = new int[][] {exkey[i],exkey[i+1],exkey[i+2],exkey[i+3]};
    
    return rkey;
  } // end getRoundKey
  
   public void printExKey(){
     
     for(int i = 0; i < exkey.length; i++){
      for(int j = 0; j < exkey[i].length; j++){
          System.out.print(Integer.toHexString(exkey[i][j]) + "\t");
        }
        
        if( i % 4 == 3 ) System.out.println("");
     }
    } // end printExKey
    
  
  private static int[] circshift(int[] word){
    int firstint = word[0];
      for(int j = 0; j < word.length - 1; j++){
        word[j] = word[j+1];
      }
      word[word.length - 1] = firstint; 
      
      return word;
  } // end circshift 
  
  private static int[] circshift(int[] word, int n){
    
    for(int i = 0; i < n; i++ ){
      circshift(word);
    }
    
    return word;
    
  } // end circshift 
  
  private static int[] swapSBox(int[] word){
    for(int i = 0; i < word.length; i++){
      word[i] = AESHelper.subTableQuery(word[i]);
    }
    return word;
  }
   
  private static int rcon(int i){
    if(i == 0) return 0;
    int c = 1;
    while( i != 1){
      c = AESHelper.gmul(c, 2);
      i--;
    }
    return c; 
    
  } // end rcon(i)
  
  private static int[] schedule_core(int[] word, int ircon){
    // Remember to increment the ircon after calling!!!!!
    circshift(word);
    swapSBox(word);
    word[0] ^= rcon(ircon);
    return word;
  }
  
  public int[] getNextWord(){
    iword++;
    return exkey[iword - 1];
  } // getNextWord
  
  public void printKey(){
    for(int i = 0; i < exkey.length/4; i++){
      printRoundKey(i);
      System.out.println("");
    }
  }
  
  public int[] getPreviousWord(){
    iword--;
    return exkey[iword +1];
  }
  
  
  public void printRoundKey(int j){
    int i = j*4;
    int[][] rkey = new int[][] {exkey[i],exkey[i+1],exkey[i+2],exkey[i+3]};
    AES.printColumnMatrix(rkey);
//    for(int i = ircon; i < 4; i++){
//      for(int j = 0; j < exkey[i].length; j++){
//      System.out.print(exkey[i][j] + " ");
//      }
//    }
//    System.out.println("");
  }
  
}
