/*
 * 12/14/08
 *
 * AbstractTokenMakerFactory.java - Base class for TokenMaker implementations.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.fife.ui.rsyntaxtextarea.modes.DispatchTokenMaker;


/**
 * Base class for {@link TokenMakerFactory} implementations.  A mapping from
 * language keys to the names of {@link TokenMaker} classes is stored.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class AbstractTokenMakerFactory extends TokenMakerFactory {

	/**
	 * A mapping from keys to the names of {@link TokenMaker} implementation
	 * class names.  When {@link #getTokenMaker(String)} is called with a key
	 * defined in this map, a <code>TokenMaker</code> of the corresponding type
	 * is returned.
	 */
	private Map tokenMakerMap;


	/**
	 * Constructor.
	 */
	protected AbstractTokenMakerFactory() {
		tokenMakerMap = new HashMap();
		initTokenMakerMap();
	}


	/**
	 * Returns a {@link TokenMaker} for the specified key.
	 *
	 * @param key The key.
	 * @return The corresponding <code>TokenMaker</code>, or <code>null</code>
	 *         if none matches the specified key.
	 */
	protected TokenMaker getTokenMakerImpl(String key) {
		TokenMakerCreatorIF tmc = (TokenMakerCreatorIF)tokenMakerMap.get(key);
		if (tmc!=null) {
			try {
				return tmc.create();
			} catch (RuntimeException re) { // FindBugs
				throw re;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	/**
	 * Populates the mapping from keys to instances of
	 * <code>TokenMakerCreator</code>s.  Subclasses should override this method
	 * and call one of the <code>putMapping</code> overloads to register
	 * {@link TokenMaker}s for syntax constants.
	 *
	 * @see #putMapping(String, String)
	 * @see #putMapping(String, String, ClassLoader)
	 */
	protected abstract void initTokenMakerMap();


	/**
	 * {@inheritDoc}
	 */
	public Set keySet() {
		return tokenMakerMap.keySet();
	}


	/**
	 * Adds a mapping from a key to a <code>TokenMaker</code> implementation
	 * class name.
	 *
	 * @param key The key.
	 * @param className The <code>TokenMaker</code> class name.
	 * @see #putMapping(String, String, ClassLoader)
	 */
	public void putMapping(String key, String className) {
		putMapping(key, className, null);
	}


  /**
   * Adds a mapping from a key to a <code>TokenMakerCreatorIF</code>
   *
   * @param key The key.
   * @param creator <code>TokenMakerCreatorIF</code> factory.
   */
	public void putMapping( String key, TokenMakerCreatorIF creator){
	  tokenMakerMap.put(key, creator);
	}
	
  /**
   * Adds a mapping from a key to a <code>DispatchTokenMaker</code>
   * using <code>DispatchTokenMaker.Tokenizer</code>
   *
   * @param key The key.
   * @param tokenizer <code>DispatchTokenMaker.Tokenizer</code> dispatch.
   */
  public void putMapping( String key, DispatchTokenMaker.Tokenizer tokenizer){
    tokenMakerMap.put(key, new DispatchTokenMakerCreator(tokenizer));
  }

	/**
	 * Adds a mapping from a key to a <code>TokenMaker</code> implementation
	 * class name.
	 *
	 * @param key The key.
	 * @param className The <code>TokenMaker</code> class name.
	 * @param cl The class loader to use when loading the class.
	 * @see #putMapping(String, String)
	 */
	public void putMapping(String key, String className, ClassLoader cl) {
		putMapping(key, new TokenMakerCreator(className, cl));
	}


	public static interface TokenMakerCreatorIF {
	  TokenMaker create() throws Exception;
	}
	
	/**
	 * Wrapper that handles the creation of TokenMaker instances.
	 */
	public static class TokenMakerCreator implements TokenMakerCreatorIF {

		private String className;
		private ClassLoader cl;

		public TokenMakerCreator(String className, ClassLoader cl) {
			this.className = className;
			this.cl = cl!=null ? cl : getClass().getClassLoader();
		}

		public TokenMaker create() throws Exception {
			return (TokenMaker)Class.forName(className, true, cl).newInstance();
		}

	}

	 /**
   * Wrapper that handles the creation of DispatchTokenMaker instances.
   */
  public static class DispatchTokenMakerCreator implements TokenMakerCreatorIF {

    protected DispatchTokenMaker.Tokenizer tokenizer;

    public DispatchTokenMakerCreator(DispatchTokenMaker.Tokenizer tokenizer_) {
      tokenizer = tokenizer_;
    }

    public TokenMaker create() throws Exception {
      DispatchTokenMaker tokenMaker = new DispatchTokenMaker(tokenizer);
      return tokenMaker;
    }

  }


}