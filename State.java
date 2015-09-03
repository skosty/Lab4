import java.util.Arrays;

public class State {
  
  int[][] state = new int[4][4];
  public static int[][] mixColMat = {
    {2,1,1,3},
    {3,2,1,1},
    {1,3,2,1},
    {1,1,3,2},
  };
  
  public static int[][] invmixColMat = {
    {0x0e,0x09,0x0d,0x0b},
    {0x0b,0x0e,0x09,0x0d},
    {0x0d,0x0b,0x0e,0x09},
    {0x09,0x0d,0x0b,0x0e},
  };
  
  
  public State(int[][] _state){
    this.state = _state;
  }
  
  public int[][] getState(){
    return this.state;
  }
  
  public void subBytes(){
    for(int i = 0; i < 4; i++){
      for(int j = 0; j < 4; j++){
        this.state[i][j] = AESHelper.subTableQuery(this.state[i][j]);
      }
    }
//    System.out.println("After subBytes: ");
//    aesencryption.printColumnMatrix(state);
  } // end subBytes
  
  public void invsubBytes(){
    for(int i = 0; i < 4; i++){
      for(int j = 0; j < 4; j++){
        this.state[i][j] = AESHelper.invsubTableQuery(this.state[i][j]);
      }
    }
  }
  
  
  public void shiftRows(){
    int shifter = 1; // start at row 1, shifting 1 time
    for(int row = 1; row < 4; row++ ){
      for(int ishift = 0; ishift < shifter; ishift++){
        
        int firstint = this.state[0][row];
        for(int icol = 0; icol < 3; icol++){
          this.state[icol][row] = this.state[icol+1][row];
        }
        this.state[3][row] = firstint; 
        
      } // shift the row shifter times by one each time 
      shifter++;
    }
//    System.out.println("After shiftRows: ");
//    aesencryption.printColumnMatrix(state);
    
  } // end shiftRows
  public void invShiftRows(){
    int shifter = 1; // start at row 1, shifting 1 time
    for(int row = 1; row < 4; row++ ){
      for(int ishift = 0; ishift < shifter; ishift++){
        
        int lastint = this.state[3][row];
        for(int icol = 3; icol >0; icol--){
          this.state[icol][row] = this.state[icol-1][row];
        }
        this.state[0][row] = lastint; 
        
      } // shift the row shifter times by one each time 
      shifter++;
    }
    
  } // end invShiftRows() 
  
  public void mixColumns(){
    for(int iword = 0; iword < 4; iword++){ // for each wor (column) in state 
    
      state[iword] = gMatMult(state[iword], mixColMat);
    
    }
//    System.out.println("After mixColumns: ");
//    aesencryption.printColumnMatrix(state);
  }
  public void invmixColumns(){
    for(int iword = 0; iword < 4; iword++){ // for each wor (column) in state 
    
      state[iword] = gMatMult(state[iword], invmixColMat);
    
    }
  }
  
  private int[] gMatMult(int[] word, int[][] mat){
    
    int[] product = new int[word.length]; 
    for(int row = 0; row < 4; row++){
      for(int col = 0; col < 4; col++){
          product[row] ^= AESHelper.gmul(word[col], mat[col][row]);
      }
    } 
    
  return product;
    
  } // gMatMult
  
  
//  public void mixColumns(){
//    for(int col = 0; col < 4; col++){ // for each column col in state 
//      int[] newcol = new int[4];
//      for(int row = 0; row < 4; row++){ // for each row in mixColMat
//        for(int i = 0; i < 4; i++){
//          newcol[row] ^= AESHelper.gmul(this.state[col][i], mixColMat[row][i]);
//        }
//        this.state[col] = Arrays.copyOf(newcol, 4);
//      }
//    }
//  } // end mixColumns
  
  public void addRoundKey(Key k, int round){
    for(int col = 0; col < 4; col++){
      this.state[col] = AESHelper.arrayXOR(this.state[col], k.getRoundKey(round)[col]);
    }
  } // end addRoundKey
  
  
  
  public void printState(String s){
    System.out.println(s);
    AES.printColumnMatrix(state);
  }
  
}
