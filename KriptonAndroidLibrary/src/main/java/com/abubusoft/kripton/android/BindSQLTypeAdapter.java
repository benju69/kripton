/**
 * 
 */
package com.abubusoft.kripton.android;

import com.abubusoft.kripton.BindTypeAdapter;

/**
 * Allows to manage a field of type J as a field of type D. It's usefully for unsupported type.   
 * 
 * @author Francesco Benincasa (info@abubusoft.com)
 *
 * @param <J>
 * 		field type
 * @param <D>
 * 		data format
 */
public interface BindSQLTypeAdapter<J, D> extends BindTypeAdapter<J, D> {

	/**
	 * Convert a field value to its string representation. Usually it's done when you use bean property in where conditions.
	 * 
	 * @param javaValue
	 * @return
	 */
	String toString(J javaValue);
	
}
