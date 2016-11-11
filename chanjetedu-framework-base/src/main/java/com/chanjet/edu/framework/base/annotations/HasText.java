package com.chanjet.edu.framework.base.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 判断某个参数是否包含文本
 * Created by shuai.w on 16-9-23.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface HasText {

    String value() default "";

    String alisa();
}
