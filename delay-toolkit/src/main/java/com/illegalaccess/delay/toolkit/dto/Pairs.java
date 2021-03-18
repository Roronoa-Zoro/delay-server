package com.illegalaccess.delay.toolkit.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Jimmy Li
 * @date 2021-03-04 10:28
 */
public class Pairs implements Serializable {

    private static final long serialVersionUID = -1L;

    private String key;
    private String value;

    public static Pairs of(String key, String value) {
        return new Pairs(key, value);
    }

    public Pairs() {
    }

    public Pairs(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pairs)) {
            return false;
        }
        Pairs pairs = (Pairs) o;
        return key.equals(pairs.key) &&
                value.equals(pairs.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "Pairs{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
