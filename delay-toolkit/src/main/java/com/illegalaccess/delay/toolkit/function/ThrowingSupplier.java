package com.illegalaccess.delay.toolkit.function;

/**
 *
 * @author Jimmy Li
 * @date 2021-03-04 10:29
 */
@FunctionalInterface
public interface ThrowingSupplier<T> {
    T get() throws Exception;
}
