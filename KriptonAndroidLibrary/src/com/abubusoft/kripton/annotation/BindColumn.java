package com.abubusoft.kripton.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.abubusoft.kripton.binder.database.ColumnType;

/**
 * 
 * This annotation maps a POJO field to db column
 * 
 * @author bulldog
 * @author xcesco
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindColumn {
	
	public String name() default "";
	
	public ColumnType value() default ColumnType.STANDARD;
	
	public boolean nullable() default false;
}