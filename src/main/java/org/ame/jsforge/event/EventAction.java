package org.ame.jsforge.event;

/**
 * @author Amelorate
 * Allows subscribing to a event using lambdas.
 */
@FunctionalInterface
public interface EventAction {
	Object call(Object... args);
}
