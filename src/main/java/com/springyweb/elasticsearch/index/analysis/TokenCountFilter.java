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
 * Prepends the count of the token in the stream to the term stored in the index 
 * 
 */
public class TokenCountFilter extends TokenFilter {

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private int count = 0;
	
	public TokenCountFilter(final TokenStream input) {
		super(input);
	}
	
	/**
	 * Prepend the value of ${count} to each token and increment ${count}
	 */
	@Override
	public final boolean incrementToken() throws IOException {
		final boolean increment = input.incrementToken();
		if (increment) {
			final char[] count = String.valueOf(this.count++).toCharArray();
			final int newLength = termAtt.length() + count.length;
			final char[] resizedBuffer = termAtt.resizeBuffer(newLength);
			termAtt.setLength(newLength);
			System.arraycopy(resizedBuffer, 0, resizedBuffer, count.length, termAtt.length());
			System.arraycopy(count, 0, resizedBuffer, 0, count.length);
		}
		return increment;
	}
	
	@Override 
	public void end() throws IOException {
		super.end();
		count = 0;
	}
	
	@Override
	public void reset() throws IOException {
		super.reset();
		count = 0;
	}
}