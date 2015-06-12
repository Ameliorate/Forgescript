package org.ame.jsforge.internal.ModLoading;

/**
 * @author Amelorate
 * Indicates a mod is invalad somehow that should be exposed to the user.
 */
public class InvalidModException extends RuntimeException {
	public InvalidModException(String message) {
		super(message);
	}

	public InvalidModException(String message, Throwable cause) {
		super(message, cause);
	}
}
