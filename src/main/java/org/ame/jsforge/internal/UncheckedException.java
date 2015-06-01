package org.ame.jsforge.internal;

/**
 * @author Amelorate
 * Wraps an exception to be unchecked.
 */
public class UncheckedException extends RuntimeException {
	public UncheckedException(Throwable cause) {
		super(cause);
	}
}
