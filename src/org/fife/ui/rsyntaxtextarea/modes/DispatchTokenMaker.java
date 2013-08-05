package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMakerBase;

public class DispatchTokenMaker extends TokenMakerBase {

  public DispatchTokenMaker(){
    this(null);
  }
  
  public DispatchTokenMaker(Tokenizer tokenizer_){
    setLanguageIndex(0);
    tokenizer = tokenizer_; 
  }
  
  public static interface Tokenizer {
    void tokenize(DispatchTokenMaker blockTokenMaker, Segment text,
        int initialTokenType, int startOffset);
  }
  
  protected Tokenizer tokenizer = null;
  
  @Override
  public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
    if( tokenizer == null ){
      addToken(text,startOffset,startOffset+text.count-1,startOffset,initialTokenType);
    }else{
      tokenizer.tokenize(this,text,initialTokenType,startOffset);
    }
    return firstToken;
  }

}
