package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMakerBase;

public class DispatchTokenMaker extends TokenMakerBase {

  public DispatchTokenMaker(){
    this(null);
  }
  
  public DispatchTokenMaker(Tokenizer tokenizer_){
    this(0,null,tokenizer_);
  }
  
  public DispatchTokenMaker(Integer index_, Object delegate_, Tokenizer tokenizer_){
    setLanguageIndex(index_);
    tokenizer = tokenizer_;
    setDelegate(delegate_);
  }

  public static interface Tokenizer {
    void setDelegate( Object obj_ );
    Object getDelegate();
    void tokenize(DispatchTokenMaker blockTokenMaker, Segment text,
        int initialTokenType, int startOffset);
  }
  
  protected Tokenizer tokenizer = null;
  protected Object _delegate = null;
  
  public void setDelegate( Object obj_ ){
    _delegate = obj_;
    if( tokenizer != null ){
      tokenizer.setDelegate(_delegate);
    }
  }
  
  public Object getDelegate(){
    return _delegate;
  }
  
  @Override
  public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
    resetTokenList();
    if( tokenizer == null ){
      addToken(text,startOffset,startOffset+text.count-1,startOffset,initialTokenType);
    }else{
      tokenizer.tokenize(this,text,initialTokenType,startOffset);
    }
    return firstToken;
  }

  public Token getFirstToken() {
    return firstToken;
  }

}
