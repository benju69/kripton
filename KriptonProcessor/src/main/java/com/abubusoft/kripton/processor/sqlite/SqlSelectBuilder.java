/*******************************************************************************
 * Copyright 2015, 2017 Francesco Benincasa (info@abubusoft.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.abubusoft.kripton.processor.sqlite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import com.abubusoft.kripton.android.annotation.BindSqlDynamicWhere;
import com.abubusoft.kripton.android.annotation.BindSqlPageSize;
import com.abubusoft.kripton.android.annotation.BindSqlSelect;
import com.abubusoft.kripton.android.sqlite.OnReadBeanListener;
import com.abubusoft.kripton.android.sqlite.OnReadCursorListener;
import com.abubusoft.kripton.android.sqlite.PaginatedResult;
import com.abubusoft.kripton.android.sqlite.SqlUtils;
import com.abubusoft.kripton.common.One;
import com.abubusoft.kripton.common.Pair;
import com.abubusoft.kripton.common.StringUtils;
import com.abubusoft.kripton.processor.core.AnnotationAttributeType;
import com.abubusoft.kripton.processor.core.AssertKripton;
import com.abubusoft.kripton.processor.core.ModelAnnotation;
import com.abubusoft.kripton.processor.core.ModelMethod;
import com.abubusoft.kripton.processor.core.reflect.AnnotationUtility.MethodFoundListener;
import com.abubusoft.kripton.processor.core.reflect.TypeUtility;
import com.abubusoft.kripton.processor.sqlite.SelectBuilderUtility.SelectType;
import com.abubusoft.kripton.processor.sqlite.SqlModifyBuilder.ModifyType;
import com.abubusoft.kripton.processor.sqlite.grammars.jql.JQLChecker;
import com.abubusoft.kripton.processor.sqlite.grammars.jql.JQLPlaceHolder;
import com.abubusoft.kripton.processor.sqlite.grammars.jql.JQLChecker.JQLParameterName;
import com.abubusoft.kripton.processor.sqlite.grammars.jql.JQLChecker.JQLPlaceHolderReplacerListener;
import com.abubusoft.kripton.processor.sqlite.grammars.jql.JQLChecker.JQLReplacerListener;
import com.abubusoft.kripton.processor.sqlite.grammars.uri.ContentUriPlaceHolder;
import com.abubusoft.kripton.processor.sqlite.model.SQLColumnType;
import com.abubusoft.kripton.processor.sqlite.model.SQLDaoDefinition;
import com.abubusoft.kripton.processor.sqlite.model.SQLEntity;
import com.abubusoft.kripton.processor.sqlite.model.SQLProperty;
import com.abubusoft.kripton.processor.sqlite.model.SQLiteDatabaseSchema;
import com.abubusoft.kripton.processor.sqlite.model.SQLiteModelMethod;
import com.abubusoft.kripton.processor.sqlite.transform.SQLTransformer;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec.Builder;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public abstract class SqlSelectBuilder {

	/**
	 * Iterate over methods.
	 * 
	 * @param elementUtils
	 * @param typeElement
	 * @param listener
	 */
	public static void forEachMethods(Elements elementUtils, TypeElement typeElement, MethodFoundListener listener) {
		List<? extends Element> list = elementUtils.getAllMembers(typeElement);

		for (Element item : list) {
			if (item.getKind() == ElementKind.METHOD) {
				listener.onMethod((ExecutableElement) item);
			}
		}
	}

	public static boolean hasParameterOfType(ModelMethod method, TypeName parameter) {
		return countParameterOfType(method, parameter)>0;
	}

	public static int countParameterOfType(ModelMethod method, TypeName parameter) {
		int counter = 0;
		for (Pair<String, TypeName> item : method.getParameters()) {
			if (item.value1.equals(parameter)) {
				counter++;
			}
		}

		return counter;
	}

	public static String getNameParameterOfType(ModelMethod method, TypeName parameter) {
		for (Pair<String, TypeName> item : method.getParameters()) {
			if (item.value1.equals(parameter)) {
				return item.value0;
			}
		}

		return null;
	}

	/**
	 * 
	 * @param elementUtils
	 * @param builder
	 * @param method
	 */
	public static void generateSelect(Elements elementUtils, Builder builder, SQLiteModelMethod method) {
		SQLDaoDefinition daoDefinition = method.getParent();
		SQLEntity entity = daoDefinition.getEntity();

		SelectBuilderUtility.SelectType selectResultType = null;

		// if true, field must be associate to ben attributes
		TypeName returnTypeName = method.getReturnClass();

		ParameterizedTypeName readBeanListener=ParameterizedTypeName.get(ClassName.get(OnReadBeanListener.class), ClassName.get(entity.getElement()));
		ClassName readCursorListener=ClassName.get(OnReadCursorListener.class);

		ModelAnnotation annotation = method.getAnnotation(BindSqlSelect.class);
		int pageSize = annotation.getAttributeAsInt(AnnotationAttributeType.PAGE_SIZE);
		
		AssertKripton.failWithInvalidMethodSignException(pageSize<0, method, "in @%s(pageSize) must be set with positive number",BindSqlSelect.class.getSimpleName());
		AssertKripton.failWithInvalidMethodSignException(pageSize>0 && method.hasDynamicPageSizeConditions(), method, "can not define @%s(pageSize) and mark a method parameter with @%s ",BindSqlSelect.class.getSimpleName(), BindSqlPageSize.class.getSimpleName());		

		if (TypeUtility.isTypeIncludedIn(returnTypeName, Void.class, Void.TYPE)) {
			// return VOID (in the parameters must be a listener)
			if (hasParameterOfType(method, readCursorListener)) {
				selectResultType = SelectBuilderUtility.SelectType.LISTENER_CURSOR;
			} else if (hasParameterOfType(method, readBeanListener)) {
				selectResultType = SelectBuilderUtility.SelectType.LISTENER_BEAN;
			}
		} else if (TypeUtility.isTypeIncludedIn(returnTypeName, Cursor.class)) {
			// return Cursor (no listener)
			selectResultType = SelectBuilderUtility.SelectType.CURSOR;
		} else if (returnTypeName instanceof ParameterizedTypeName) {
			ParameterizedTypeName returnParameterizedTypeName = (ParameterizedTypeName) returnTypeName;
			ClassName returnParameterizedClassName = returnParameterizedTypeName.rawType;

			// return List (no listener)
			AssertKripton.assertTrueOrInvalidMethodSignException(returnParameterizedTypeName.typeArguments.size() == 1, method, "return type %s is not supported", returnTypeName);
			TypeName elementName = returnParameterizedTypeName.typeArguments.get(0);

			try {
				Class<?> wrapperClazz = Class.forName(returnParameterizedClassName.toString());
				if (PaginatedResult.class.isAssignableFrom(wrapperClazz)) {
					// method must have pageSize, statically or dynamically
					// defined
					AssertKripton.assertTrueOrInvalidMethodSignException(method.hasDynamicPageSizeConditions() || pageSize > 0, method,
							"use of PaginatedResult require 'pageSize' attribute or a @%s annotated parameter", returnTypeName, BindSqlPageSize.class.getSimpleName());

					// paged result
					AssertKripton.assertTrueOrInvalidMethodSignException(TypeUtility.isEquals(elementName, entity.getName().toString()), method, "return type %s is not supported", returnTypeName);
					selectResultType = SelectBuilderUtility.SelectType.PAGED_RESULT;
					// set typeName of paginatedResult
					method.paginatedResultName="paginatedResult";
				} else if (Collection.class.isAssignableFrom(wrapperClazz)) {
					if (TypeUtility.isEquals(elementName, entity.getName().toString())) {
						// entity list
						selectResultType = SelectBuilderUtility.SelectType.LIST_BEAN;
					} else if (SQLTransformer.isSupportedJDKType(elementName) || TypeUtility.isByteArray(elementName)) {
						// scalar list
						selectResultType = SelectBuilderUtility.SelectType.LIST_SCALAR;
					} else {
						AssertKripton.failWithInvalidMethodSignException(true, method, "");
					}

				}
			} catch (Exception e) {
				// error
			}
		} else if (TypeUtility.isEquals(returnTypeName, entity)) {
			// return one element (no listener)
			selectResultType = SelectBuilderUtility.SelectType.BEAN;
		} else if (SQLTransformer.isSupportedJDKType(returnTypeName) || TypeUtility.isByteArray(returnTypeName)) {
			// return single value string, int, long, short, double, float,
			// String (no listener)
			selectResultType = SelectBuilderUtility.SelectType.SCALAR;
		}

		AssertKripton.assertTrueOrInvalidMethodSignException(selectResultType != null, method,"'%s' as return type is not supported", returnTypeName);

		// generate select method
		selectResultType.generate(elementUtils, builder, method, returnTypeName);
		
		if (method.contentProviderEntryPathEnabled) {
			// we need to generate UPDATE or DELETE for content provider to
			generateSelectForContentProvider(elementUtils, builder, method, selectResultType);
		}
	}
	
	/**
	 * <p>
	 * Generate select used in content provider class.
	 * </p>
	 * 
	 * @param elementUtils
	 * @param builder
	 * @param method
	 * @param selectResultType
	 */
	private static void generateSelectForContentProvider(Elements elementUtils, Builder builder,
			SQLiteModelMethod method, SelectType selectResultType) {
		final SQLiteDatabaseSchema schema = method.getParent().getParent();
		final SQLDaoDefinition daoDefinition = method.getParent();
		final SQLEntity entity = daoDefinition.getEntity();
		final Set<String> columns = new LinkedHashSet<>();
		final StringBuilder parametersBuilder = new StringBuilder();

		JQLChecker jqlChecker = JQLChecker.getInstance();

		// parameters extracted from query
		String whereStatement = jqlChecker.extractWhereStatement(method.jql.value);
		List<JQLPlaceHolder> placeHolders = jqlChecker.extractFromWhereCondition(whereStatement);
		// remove placeholder for dynamic where, we are not interested here
		placeHolders = SqlModifyBuilder.removeDynamicPlaceHolder(placeHolders);
		AssertKripton.assertTrue(placeHolders.size() == method.contentProviderUriVariables.size(),
				"In '%s.%s' content provider URI path variables and variables in where conditions are different",
				daoDefinition.getName(), method.getName());

		final One<Boolean> useColumns = new One<Boolean>(true);
		String resultForLog = jqlChecker.replace(method.jql, new JQLReplacerListener() {

			@Override
			public String onColumnName(String columnName) {
				String convertedColumnName = entity.get(columnName).columnName;

				if (useColumns.value0) {
					columns.add(convertedColumnName);
				}
				return convertedColumnName;
			}

			@Override
			public String onColumnValue(String columnValue) {
				JQLParameterName parameterName = JQLParameterName.parse(columnValue);

				String limit = "";
				SQLProperty property = daoDefinition.getEntity().get(parameterName.getValue());
				SQLColumnType columnType = SQLTransformer.columnType(property);
				switch (columnType) {
				case BLOB:
				case TEXT:
					limit = "'";
				case INTEGER:
				case REAL:
				default:
					;
				}

				parametersBuilder.append(", StringUtils.formatParam(contentValues.get(\"" + property.columnName
						+ "\"),\"" + limit + "\")");

				return "%s";
			}

			@Override
			public String onTableName(String tableName) {
				return schema.getEntityBySimpleName(tableName).getTableName();
			}

			@Override
			public void onWhereStatementBegin() {
				useColumns.value0 = false;

			}

			@Override
			public void onWhereStatementEnd() {
				useColumns.value0 = true;

			}
		});

		String sql = jqlChecker.replace(method.jql, new JQLReplacerListener() {

			@Override
			public String onTableName(String tableName) {
				return schema.getEntityBySimpleName(tableName).getTableName();
			}

			@Override
			public String onColumnValue(String columnValue) {
				return "${" + columnValue + "}";
			}

			@Override
			public String onColumnName(String columnName) {
				return entity.get(columnName).columnName;
			}

			@Override
			public void onWhereStatementBegin() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onWhereStatementEnd() {
				// TODO Auto-generated method stub

			}
		});

		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method.contentProviderMethodName);

		// params
		methodBuilder.addParameter(ParameterSpec.builder(Uri.class, "uri").build());
		methodBuilder.addParameter(ParameterSpec.builder(ArrayTypeName.of(String.class), "projection").build());
		methodBuilder.addParameter(ParameterSpec.builder(String.class, "selection").build());
		methodBuilder.addParameter(ParameterSpec.builder(ArrayTypeName.of(String.class), "selectionArgs").build());
		methodBuilder.addParameter(ParameterSpec.builder(String.class, "sortOrder").build());
		
		methodBuilder.returns(Cursor.class);

		methodBuilder.addCode("//$T and $T will be used to format SQL\n", SqlUtils.class, StringUtils.class);

		if (method.hasDynamicWhereConditions()) {
			final One<Integer> dynamicWhereCounter = new One<Integer>(0);
			whereStatement = jqlChecker.replaceFromWhere(whereStatement, new JQLPlaceHolderReplacerListener() {

				@Override
				public String onParameter(String placeHolder) {
					return "?";
				}

				@Override
				public String onDynamicSQL(String placeHolder) {
					dynamicWhereCounter.value0++;
					return "#{" + placeHolder + "}";
				}
			});
			AssertKripton.assertTrue(dynamicWhereCounter.value0 <= 1,
					"In %s.%s, @DynamicWhere variable are used more then one time", daoDefinition.getName(),
					method.getName()); //

			if (method.hasDynamicWhereConditions()) {
				whereStatement = whereStatement
						.replace(method.dynamicWherePrepend + " #{" + method.dynamicWhereParameterName + "}", "");
				whereStatement = whereStatement.replace("#{" + method.dynamicWhereParameterName + "}", "");

			}
			methodBuilder.addStatement("String whereCondition=$S", whereStatement);

			if (method.hasDynamicWhereConditions()) {
				methodBuilder.beginControlFlow("if ($T.hasText(selection))", StringUtils.class);
				methodBuilder.addStatement("whereCondition+=\"$L \" + $L", method.dynamicWherePrepend, "selection");
				methodBuilder.endControlFlow();
			}

		} else {
			methodBuilder.addStatement("String whereCondition=$S", whereStatement);
		}

		methodBuilder.addStatement("$T<String> whereParams=new $T<>()", ArrayList.class, ArrayList.class);

		int i = 0;
		// extract pathVariables
		// every controls was done in constructor of SQLiteModelMethod
		for (ContentUriPlaceHolder variable : method.contentProviderUriVariables) {
			AssertKripton.assertTrue(SqlModifyBuilder.validate(variable.value, placeHolders, i),
					"In '%s.%s' content provider URI path variables and variables in where conditions are different",
					daoDefinition.getName(), method.getName());

			SQLProperty entityProperty = entity.get(variable.value);

			if (entityProperty != null) {
				methodBuilder.addCode("// Add parameter $L at path segment $L\n", variable.value,
						variable.pathSegmentIndex);
				methodBuilder.addStatement("whereParams.add(uri.getPathSegments().get($L))", variable.pathSegmentIndex);
				AssertKripton.assertTrue(
						TypeUtility.isTypeIncludedIn(entityProperty.getPropertyType().getTypeName(), String.class,
								Long.class, Long.TYPE),
						"In '%s.%s' content provider URI path variables %s must be String of Long type",
						daoDefinition.getName(), method.getName(), entityProperty.getName());
			}

			i++;
		}

		if (method.hasDynamicWhereConditions()) {
			// ASSERT: only with dynamic where conditions
			methodBuilder.beginControlFlow("if ($T.hasText(selection) && selectionArgs!=null)", StringUtils.class);

			if (method.hasDynamicWhereConditions()) {
				methodBuilder.beginControlFlow("for (String arg: selectionArgs)");
				methodBuilder.addStatement("whereParams.add(arg)");
				methodBuilder.endControlFlow();
			}

			methodBuilder.endControlFlow();
		}

		methodBuilder.addStatement("String sql=$S",sql);
		methodBuilder.addStatement("Cursor result = database().rawQuery(sql, whereParams.toArray(new String[whereParams.size()]))");
		/*switch (selectResultType) {
		case BEAN:
		case DELETE_RAW:
			
			break;
		case UPDATE_BEAN:
		case UPDATE_RAW:
			SqlInsertBuilder.generateColumnCheckSet(elementUtils, builder, method, columns);
			SqlInsertBuilder.generateColumnCheck(method, methodBuilder, "UPDATE");
			methodBuilder.addStatement(
					"int result = database().update($S, contentValues, whereCondition, whereParams.toArray(new String[whereParams.size()]))",
					daoDefinition.getEntity().getTableName());
			break;
		}*/

		methodBuilder.addStatement("return result");

		// we add at last javadoc, because need info is built at last.

		// javadoc
		String operation = "SELECT";
		methodBuilder.addJavadoc("<h1>Content provider URI ($L operation):</h1>\n", operation);
		methodBuilder.addJavadoc("<pre>$L</pre>\n\n", method.contentProviderUriTemplate);

		if (method.contentProviderUriVariables.size() > 0) {
			methodBuilder.addJavadoc("<p>Path variables defined:</p>\n<ul>\n");
			for (ContentUriPlaceHolder variable : method.contentProviderUriVariables) {
				methodBuilder.addJavadoc("<li><strong>$${$L}</strong> at path segment $L</li>\n", variable.value,
						variable.pathSegmentIndex);
			}
			methodBuilder.addJavadoc("</ul>\n\n");
		}

		methodBuilder.addJavadoc("<h2>JQL $L for Content Provider</h2>\n", operation);
		methodBuilder.addJavadoc("<pre>$L</pre>\n\n", method.jql.value);
		methodBuilder.addJavadoc("<h2>SQL $L for Content Provider</h2>\n", operation);
		methodBuilder.addJavadoc("<pre>$L</pre>\n\n", sql);

		if (!method.hasDynamicWhereConditions()) {
			methodBuilder.addJavadoc(
					"<p><strong>Dynamic where statement is ignored, due no param with @$L was added.</strong></p>\n\n",
					BindSqlDynamicWhere.class.getSimpleName());
		}

		methodBuilder.addJavadoc("@param uri $S\n", method.contentProviderUriTemplate);
//		switch (selectResultType) {
//		case UPDATE_BEAN:
//		case UPDATE_RAW:
//			methodBuilder.addJavadoc("@param contentValues content values\n");
//			break;
//		default:
//			break;
//		}
		methodBuilder.addJavadoc("@param selection dynamic part of <code>where</code> statement $L\n",
				method.hasDynamicWhereConditions() ? "" : "<b>NOT USED</b>");
		methodBuilder.addJavadoc("@param selectionArgs arguments of dynamic part of <code>where</code> statement $L\n",
				method.hasDynamicWhereConditions() ? "" : "<b>NOT USED</b>");

		methodBuilder.addJavadoc("@return number of effected rows\n");

		builder.addMethod(methodBuilder.build());
	}





}
