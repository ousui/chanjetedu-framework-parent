package com.chanjet.edu.framework.mybatis.interceptor.annotations;

import java.lang.annotation.*;

/**
 * Created by shuai.w on 2016/5/26.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SqlTemplate {

	/**
	 * load the path of velocity template
	 * default is the class package path "{p/a/c/k/a/g/e}/{class}.{method}.vm"
	 *
	 * @return
	 */
	String path() default "";

}
