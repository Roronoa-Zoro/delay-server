package com.illegalaccess.delay.toolkit.annotation;

import java.lang.annotation.*;

@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface API {

    /**
     * 接口名称
     * @return
     */
    String name() default "";

    /**
     * 接口描述
     * @return
     */
    String desc() default "";

    /**
     * 是否log记录返回值
     * @return
     */
    boolean logResp() default true;

    /**
     * 是否从网关进行调用
     * @return
     */
    boolean fromGw() default false;
}
