package org.ame.jsforge.internal;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Amelorate
 * Indicates this field should not be used directly in a mod.
 */
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Internal {
}
