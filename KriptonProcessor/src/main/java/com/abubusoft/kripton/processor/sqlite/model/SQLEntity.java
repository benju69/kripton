package com.abubusoft.kripton.processor.sqlite.model;

import java.lang.annotation.Annotation;

import javax.lang.model.element.TypeElement;

import com.abubusoft.kripton.annotation.BindColumn;
import com.abubusoft.kripton.binder.database.ColumnType;
import com.abubusoft.kripton.processor.core.ModelAnnotation;
import com.abubusoft.kripton.processor.core.ModelClass;
import com.abubusoft.kripton.processor.core.ModelProperty;

public class SQLEntity extends ModelClass {

	public SQLEntity(TypeElement element) {
		super(element);
	}

	/**
	 * Check how many PK are defined in entity. Only one field can be PK.
	 * 
	 * @return number of PK
	 */
	public int countPrimaryKeys() {
		int countAnnotation = 0;
		ModelAnnotation annotation;
		String value;

		for (ModelProperty item : collection) {
			annotation = item.getAnnotation(BindColumn.class);
			if (annotation != null) {
				value = annotation.getAttribute(AnnotationAttributeType.ATTRIBUTE_VALUE);
				if (value != null && !"id".equals(item.getName()) && value.contains(ColumnType.PRIMARY_KEY.toString())) {
					countAnnotation++;
				}

			}
		}

		// try to get id
		ModelProperty id = findByName("id");
		if (id != null) {
			countAnnotation++;
		}

		return countAnnotation;
	}

	/**
	 * True if there is a primary key
	 * 
	 * @return true if there is a primary key
	 */
	public ModelProperty getPrimaryKey() {
		ModelAnnotation annotation;
		String value;

		for (ModelProperty item : collection) {
			annotation = item.getAnnotation(BindColumn.class);
			if (annotation != null) {
				value = annotation.getAttribute(AnnotationAttributeType.ATTRIBUTE_VALUE);
				if (value != null && value.contains(ColumnType.PRIMARY_KEY.toString())) {
					return item;
				}

			}
		}

		// try to get id
		ModelProperty id = findByName("id");

		return id;
	}

	public boolean containsAnnotation(Class<? extends Annotation> annotation) {
		return getAnnotation(annotation) != null;
	}

}
