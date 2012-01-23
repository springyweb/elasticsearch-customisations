/**
 * 
 */
package com.springyweb.elasticsearch.index.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * @author si<si@springyweb.com>
 * 
 */
public class TokenCountFilter extends TokenFilter {

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private int depth = 0;
	
	public TokenCountFilter(final TokenStream input) {
		super(input);
	}
	
	/**
	 * Prepend the value of ${depth} to each token and increment $depth
	 */
	@Override
	public final boolean incrementToken() throws IOException {
		boolean increment = false;
		if (input.incrementToken()) {
			final char[] depth = String.valueOf(this.depth++).toCharArray();
			termAtt.resizeBuffer(termAtt.length() + depth.length);
			termAtt.setLength(termAtt.length() + depth.length);
			System.arraycopy(termAtt.buffer(), 0, termAtt.buffer(), depth.length, termAtt.length());
			System.arraycopy(depth, 0, termAtt.buffer(), 0, depth.length);
			increment = true;
		}
		return increment;
	}
	
	@Override 
	public void end() {
		depth = 0;
	}
}	