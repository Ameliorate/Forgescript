package org.ame.jsforge.internal;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Amelorate
 * Indicates that a method or class isn't implemented yet and shouldn't be used yet.
 */
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface NotImplemented {
}
