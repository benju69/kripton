/*******************************************************************************
 * Copyright 2015, 2016 Francesco Benincasa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
/**
 * 
 */
package com.abubusoft.kripton.processor.bind;

import static com.abubusoft.kripton.processor.core.reflect.TypeUtility.className;
import static com.abubusoft.kripton.processor.core.reflect.TypeUtility.typeName;

import java.io.IOException;
import java.util.Comparator;
import java.util.Stack;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;

import com.abubusoft.kripton.android.annotation.BindMap;
import com.abubusoft.kripton.annotation.BindType;
import com.abubusoft.kripton.binder.xml.XmlType;
import com.abubusoft.kripton.binder2.KriptonBinder2;
import com.abubusoft.kripton.binder2.context.JacksonContext;
import com.abubusoft.kripton.binder2.context.XmlBinderContext;
import com.abubusoft.kripton.binder2.core.AbstractMapper;
import com.abubusoft.kripton.binder2.persistence.JacksonWrapperParser;
import com.abubusoft.kripton.binder2.persistence.JacksonWrapperSerializer;
import com.abubusoft.kripton.binder2.persistence.XmlWrapperParser;
import com.abubusoft.kripton.binder2.persistence.XmlWrapperSerializer;
import com.abubusoft.kripton.escape.StringEscapeUtils;
import com.abubusoft.kripton.exception.KriptonRuntimeException;
import com.abubusoft.kripton.processor.bind.model.BindEntity;
import com.abubusoft.kripton.processor.bind.model.BindProperty;
import com.abubusoft.kripton.processor.bind.transform.BindTransform;
import com.abubusoft.kripton.processor.bind.transform.BindTransformer;
import com.abubusoft.kripton.processor.core.reflect.TypeUtility;
import com.abubusoft.kripton.processor.sqlite.core.JavadocUtility;
import com.abubusoft.kripton.processor.utils.AnnotationProcessorUtilis;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author xcesco
 *
 */
public class BindTypeBuilder {

	protected static Builder builder;

	protected static final String PREFIX = "";

	protected static final String SUFFIX = KriptonBinder2.MAPPER_CLASS_SUFFIX;

	/**
	 * Generate shared preference manager
	 * 
	 * @return name of generated class
	 * 
	 * @throws IOException
	 */
	public static String generate(Elements elementUtils, Filer filer, BindEntity item) throws IOException {
		String beanClassName = item.getSimpleName().toString();

		boolean needSuffix = true;
		if (beanClassName.endsWith(SUFFIX)) {
			needSuffix = false;
			// String msg = String.format("Class %s must have a name with suffix
			// \"%s\" to be used with @%s", beanClassName, SUFFIX,
			// BindSharedPreferences.class.getSimpleName());
			// throw (new InvalidNameException(msg));
		}

		String className = PREFIX + beanClassName + (needSuffix ? SUFFIX : "");
		//ModelAnnotation annotation = item.getAnnotation(BindType.class);
		//String bindTypeName = annotation.getAttribute(AnnotationAttributeType.ATTRIBUTE_VALUE);

		PackageElement pkg = elementUtils.getPackageOf(item.getElement());
		String packageName = pkg.isUnnamed() ? null : pkg.getQualifiedName().toString();

		AnnotationProcessorUtilis.infoOnGeneratedClasses(BindType.class, packageName, className);
		//@formatter:off
		builder = TypeSpec.classBuilder(className)
				.addAnnotation(BindMap.class)
				.addModifiers(Modifier.PUBLIC)
				.superclass(TypeUtility.parameterizedTypeName(className(AbstractMapper.class), typeName(item.getElement().asType())));
		//@formatter:on
		builder.addJavadoc("This class is the shared preference binder defined for $T\n\n", item.getElement());
		JavadocUtility.generateJavadocGeneratedBy(builder);
		builder.addJavadoc("@see $T\n", item.getElement());

		// createInstance
		generateCreateInstance(item);

		// order item by order, property name
		Collections.sort(item.getCollection(), new Comparator<BindProperty>() {

			@Override
			public int compare(BindProperty lhs, BindProperty rhs) {
				int c1 = lhs.order - rhs.order;
				if (c1 != 0)
					return c1;

				return lhs.getName().compareTo(rhs.getName());
			}
		});

		// generate serializeOnJackson
		generateSerializeOnJackson(item);

		// generate serializeOnJacksonAsString
		generateSerializeOnJacksonAsString(item);

		// order item by type (attribute, element, value), order, xmlName
		Collections.sort(item.getCollection(), new Comparator<BindProperty>() {

			@Override
			public int compare(BindProperty lhs, BindProperty rhs) {
				int c1 = lhs.xmlInfo.xmlType.ordinal() - rhs.xmlInfo.xmlType.ordinal();
				if (c1 != 0)
					return c1;

				c1 = lhs.order - rhs.order;
				if (c1 != 0)
					return c1;

				return lhs.xmlInfo.tag.compareTo(rhs.xmlInfo.tag);
			}
		});

		// generate serializeOnXml
		generateSerializeOnXml(item);

		// generate parseOnJackson
		generateParseOnJackson(item);

		// generate parseOnJacksonAsString
		generateParseOnJacksonAsString(item);

		// generate parseOnXml
		generateParseOnXml(item);

		TypeSpec typeSpec = builder.build();
		JavaFile.builder(packageName, typeSpec).build().writeTo(filer);

		return className;
	}

	/**
	 * @param item
	 */
	public static void generateCreateInstance(BindEntity item) {
		MethodSpec.Builder method = MethodSpec.methodBuilder("createInstance").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).addJavadoc("create new object instance\n").returns(typeName(item.getElement()));
		method.addStatement("return new $T()", typeName(item.getElement()));
		builder.addMethod(method.build());
	}

	/**
	 * <p>
	 * Generate method to parse xml stream.
	 * </p>
	 * 
	 * @param item
	 *            kind of object to manage
	 */
	private static void generateParseOnXml(BindEntity item) {
		// @formatter:off
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("parseOnXml")
				.addJavadoc("create new object instance\n")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.addParameter(typeName(XmlBinderContext.class), "context")
				.addParameter(typeName(XmlWrapperParser.class), "wrapper")
				.addParameter(typeName(Integer.TYPE), "currentEventType")
				.returns(typeName(item.getElement()));
		// @formatter:on

		methodBuilder.beginControlFlow("try");
		methodBuilder.addStatement("$T xmlParser = wrapper.xmlParser", XMLStreamReader2.class);
		methodBuilder.addStatement("$T instance = createInstance()", item.getElement());
		methodBuilder.addStatement("int eventType = currentEventType");
		methodBuilder.addStatement("boolean read=true");

		methodBuilder.addCode("\n");
		methodBuilder.beginControlFlow("if (currentEventType == 0)");
		methodBuilder.addStatement("eventType = xmlParser.next()");		
		methodBuilder.nextControlFlow("else");
		methodBuilder.addStatement("eventType = xmlParser.getEventType()");
		methodBuilder.endControlFlow();
		methodBuilder.addStatement("String currentTag = xmlParser.getName().toString()");

		methodBuilder.addStatement("$T<String> elementNameStack = new $T<>()", Stack.class, Stack.class);
		methodBuilder.addStatement("elementNameStack.push(currentTag)");

		generateParseOnXmlAttributes(methodBuilder, item);

		methodBuilder.addCode("\n");
		methodBuilder.addCode("//sub-elements\n");
		methodBuilder.beginControlFlow("while (xmlParser.hasNext() && !elementNameStack.isEmpty())");
		//methodBuilder.beginControlFlow("while (xmlParser.hasNext())");
		
		methodBuilder.beginControlFlow("if (read)");
			methodBuilder.addStatement("eventType = xmlParser.next()");
		methodBuilder.nextControlFlow("else");
			methodBuilder.addStatement("eventType = xmlParser.getEventType()");
		methodBuilder.endControlFlow();
		methodBuilder.addStatement("read=true");

		methodBuilder.beginControlFlow("switch(eventType)$>");
		methodBuilder.addCode("case $T.START_ELEMENT:\n$>", XMLEvent.class);
		generateParserOnXmlStartElement(methodBuilder, "instance", "xmlParser", item);
		methodBuilder.addStatement("$<break");

		methodBuilder.addCode("case $T.END_ELEMENT:\n$>", XMLEvent.class);
		generateParserOnXmlEndElement(methodBuilder, "instance", "xmlParser", item);
		methodBuilder.addStatement("$<break");

		methodBuilder.addCode("case $T.CDATA:\n", XMLEvent.class);
		methodBuilder.addCode("case $T.CHARACTERS:\n$>", XMLEvent.class);
		generateParserOnXmlCharacters(methodBuilder, "instance", "xmlParser", item);
		methodBuilder.addStatement("$<break");

		methodBuilder.addCode("default:\n$>");
		methodBuilder.addStatement("$<break");
		methodBuilder.addCode("$<");
		methodBuilder.endControlFlow();
		// @formatter:on

		methodBuilder.endControlFlow();

		methodBuilder.addStatement("return instance");
		methodBuilder.nextControlFlow("catch($T e)", typeName(XMLStreamException.class));
		methodBuilder.addStatement("e.printStackTrace()");
		methodBuilder.addStatement("throw (new $T(e))", typeName(KriptonRuntimeException.class));
		methodBuilder.endControlFlow();

		builder.addMethod(methodBuilder.build());
	}

	private static void generateParserOnXmlEndElement(MethodSpec.Builder methodBuilder, String instanceName, String parserName, BindEntity entity) {
		methodBuilder.addStatement("currentTag = elementNameStack.pop()");
		/*
		 * BindTransform bindTransform;
		 * 
		 * int count = 0; // count property to manage { // for each elements for (BindProperty property : entity.getCollection()) { if (property.xmlInfo.xmlType != XmlType.TAG) continue;
		 * 
		 * bindTransform = BindTransformer.lookup(property);
		 * 
		 * // here we manage only property of bean type if (bindTransform != null && !(bindTransform instanceof ObjectTransform)) { count++; } } }
		 * 
		 * if (count > 0) { methodBuilder.addStatement("currentTag = elementNameStack.pop()"); methodBuilder.beginControlFlow("switch(currentTag)$>");
		 * 
		 * for (BindProperty property : entity.getCollection()) { if (property.xmlInfo.xmlType != XmlType.TAG) continue;
		 * 
		 * bindTransform = BindTransformer.lookup(property); if (bindTransform instanceof ObjectTransform) continue;
		 * 
		 * methodBuilder.addCode("case $S:\n$>", property.xmlInfo.tagName); methodBuilder.addCode("// property $L\n", property.getName()); methodBuilder.beginControlFlow("if (!xmlParser.isEmptyElement())");
		 * bindTransform.generateParseOnXml(methodBuilder, "xmlParser", typeName(property.getPropertyType()), "instance", property); methodBuilder.endControlFlow();
		 * 
		 * methodBuilder.addStatement("$<break"); }
		 * 
		 * methodBuilder.addCode("default:\n$>"); methodBuilder.addStatement("$<break$<"); methodBuilder.endControlFlow(); }
		 */

	}

	private static void generateParseOnXmlAttributes(MethodSpec.Builder methodBuilder, BindEntity entity) {
		BindTransform bindTransform;

		int count = 0;
		// count property to manage
		{
			// for each elements
			for (BindProperty property : entity.getCollection()) {
				if (property.xmlInfo.xmlType != XmlType.ATTRIBUTE)
					continue;

				count++;
			}
		}

		if (count > 0) {
			// @formatter:off
			methodBuilder.addCode("\n// attributes \n");
			methodBuilder.addStatement("String attributeName = null");
			//methodBuilder.addStatement("String attributeValue = null");

			methodBuilder.addStatement("int attributesCount = xmlParser.getAttributeCount();");
			methodBuilder.beginControlFlow("for (int attributeIndex = 0; attributeIndex < attributesCount; attributeIndex++)");
			methodBuilder.addStatement("attributeName = xmlParser.getAttributeLocalName(attributeIndex)");
			//methodBuilder.addStatement("attributeValue = $T.unescapeXml(xmlParser.getAttributeValue(attributeIndex))", StringEscapeUtils.class);
			methodBuilder.beginControlFlow("switch(attributeName)$>");

			for (BindProperty property : entity.getCollection()) {
				if (property.xmlInfo.xmlType != XmlType.ATTRIBUTE)
					continue;

				methodBuilder.addCode("case $S:\n$>", property.xmlInfo.tag);

				bindTransform = BindTransformer.lookup(property);
				methodBuilder.addCode("// field $L\n", property.getName());
				bindTransform.generateParseOnXml(methodBuilder, "xmlParser", property.getPropertyType().getName(), "instance", property);

				methodBuilder.addStatement("$<break");
			}

			methodBuilder.addCode("default:\n$>");
			methodBuilder.addStatement("$<break$<");
			methodBuilder.endControlFlow();

			methodBuilder.endControlFlow();
			// @formatter:on
		} else {
			methodBuilder.addCode("// No attributes found\n");
		}
	}

	private static void generateParserOnXmlStartElement(MethodSpec.Builder methodBuilder, String instanceName, String parserName, BindEntity entity) {
		BindTransform bindTransform;
		// start and inner bean
		methodBuilder.addStatement("currentTag = xmlParser.getName().toString()");
		//methodBuilder.addStatement("elementNameStack.push(currentTag)");

		int count = 0;
		// count property to manage
		{
			// for each elements
			for (BindProperty property : entity.getCollection()) {
				if (property.xmlInfo.xmlType != XmlType.TAG)
					continue;

				bindTransform = BindTransformer.lookup(property);

				// here we manage only property of bean type
				if (bindTransform != null) {
					count++;
				}
			}
		}

		if (count > 0) {
			// switch for tag elements
			// @formatter:off
			methodBuilder.beginControlFlow("switch(currentTag)$>");

			// for each elements
			for (BindProperty property : entity.getCollection()) {
				if (property.xmlInfo.xmlType != XmlType.TAG)
					continue;

				bindTransform = BindTransformer.lookup(property);

				// here we manage only property of bean type
				if (bindTransform != null) {
					methodBuilder.addCode("case $S:\n$>", property.xmlInfo.tag);
					methodBuilder.addCode("// property $L\n", property.getName());

					//methodBuilder.beginControlFlow("if (!xmlParser.isEmptyElement())");
					bindTransform.generateParseOnXml(methodBuilder, "xmlParser", property.getPropertyType().getName(), "instance", property);					
					//methodBuilder.endControlFlow();

					methodBuilder.addStatement("$<break");
				}
			}

			methodBuilder.addCode("default:\n$>");
			methodBuilder.addStatement("$L.skipElement()", parserName);

			methodBuilder.addStatement("$<break");
			methodBuilder.endControlFlow();
		} else {
			methodBuilder.addCode("// No property to manage here\n");
		}
		// @formatter:on

	}

	/**
	 * Parse entity properties and write code to read only CData Text fields
	 * 
	 * @param methodBuilder
	 * @param entity
	 */
	private static void generateParserOnXmlCharacters(MethodSpec.Builder methodBuilder, String instanceName, String parserName, BindEntity entity) {
		BindTransform bindTransform;
		int count = 0;
		for (BindProperty property : entity.getCollection()) {
			if (property.xmlInfo.xmlType != XmlType.VALUE && property.xmlInfo.xmlType != XmlType.VALUE_CDATA)
				continue;

			count++;
			methodBuilder.beginControlFlow("if (elementNameStack.size()==1 && $L.hasText())", parserName);
			methodBuilder.addCode("// property $L\n", property.getName());
			bindTransform = BindTransformer.lookup(property);
			bindTransform.generateParseOnXml(methodBuilder, parserName, property.getPropertyType().getName(), "instance", property);
			methodBuilder.endControlFlow();
		}

		if (count == 0) {
			methodBuilder.addCode("// no property is binded to VALUE o CDATA ");
		}
	}

	private static void generateParseOnJackson(BindEntity entity) {
		// @formatter:off
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("parseOnJackson")
				.addJavadoc("create new object instance\n")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.addParameter(typeName(JacksonContext.class), "context")
				.addParameter(typeName(JacksonWrapperParser.class), "wrapper")
				.returns(typeName(entity.getElement()));
		// @formatter:on

		methodBuilder.beginControlFlow("try");
		methodBuilder.addStatement("$T jacksonParser = wrapper.jacksonParser", JsonParser.class);
		methodBuilder.addStatement("$T instance = createInstance()", entity.getElement());
		methodBuilder.addStatement("String fieldName");

		methodBuilder.beginControlFlow("if (jacksonParser.getCurrentToken() == null)");
		methodBuilder.addStatement("jacksonParser.nextToken()");
		methodBuilder.endControlFlow();

		methodBuilder.beginControlFlow("if (jacksonParser.getCurrentToken() != $T.START_OBJECT)", JsonToken.class);
		methodBuilder.addStatement("jacksonParser.skipChildren()");
		methodBuilder.addStatement("return instance");
		methodBuilder.endControlFlow();

		methodBuilder.beginControlFlow("while (jacksonParser.nextToken() != $T.END_OBJECT)", JsonToken.class);
		methodBuilder.addStatement("fieldName = jacksonParser.getCurrentName()");
		methodBuilder.addStatement("jacksonParser.nextToken()");

		methodBuilder.addCode("\n// Parse fields:\n");
		methodBuilder.beginControlFlow("switch (fieldName)$>");

		BindTransform bindTransform;
		for (BindProperty item : entity.getCollection()) {
			bindTransform = BindTransformer.lookup(item);

			methodBuilder.addCode("case $S:\n$>", item.getName());
			methodBuilder.addCode("// field $L\n", item.getName());
			bindTransform.generateParseOnJackson(methodBuilder, "jacksonParser", item.getPropertyType().getName(), "instance", item);
			methodBuilder.addCode("$<break;\n");
		}

		methodBuilder.addCode("default:$>\n");
		methodBuilder.addStatement("jacksonParser.skipChildren()");
		methodBuilder.addCode("$<break;");

		methodBuilder.addCode("$<");
		methodBuilder.endControlFlow();

		methodBuilder.endControlFlow();

		methodBuilder.addStatement("return instance");
		methodBuilder.nextControlFlow("catch ($T e)", IOException.class);
		methodBuilder.addStatement("e.printStackTrace()");
		methodBuilder.addStatement("throw new $T(e)", KriptonRuntimeException.class);
		methodBuilder.endControlFlow();
		builder.addMethod(methodBuilder.build());

	}

	private static void generateParseOnJacksonAsString(BindEntity entity) {
		// @formatter:off
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("parseOnJacksonAsString")
				.addJavadoc("create new object instance\n")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.addParameter(typeName(JacksonContext.class), "context")
				.addParameter(typeName(JacksonWrapperParser.class), "wrapper")
				.returns(typeName(entity.getElement()));
		// @formatter:on

		methodBuilder.beginControlFlow("try");
		methodBuilder.addStatement("$T jacksonParser = wrapper.jacksonParser", JsonParser.class);
		methodBuilder.addStatement("$T instance = createInstance()", entity.getElement());
		methodBuilder.addStatement("String fieldName");

		methodBuilder.beginControlFlow("if (jacksonParser.getCurrentToken() == null)");
		methodBuilder.addStatement("jacksonParser.nextToken()");
		methodBuilder.endControlFlow();

		methodBuilder.beginControlFlow("if (jacksonParser.getCurrentToken() != $T.START_OBJECT)", JsonToken.class);
		methodBuilder.addStatement("jacksonParser.skipChildren()");
		methodBuilder.addStatement("return instance");
		methodBuilder.endControlFlow();

		methodBuilder.beginControlFlow("while (jacksonParser.nextToken() != $T.END_OBJECT)", JsonToken.class);
		methodBuilder.addStatement("fieldName = jacksonParser.getCurrentName()");
		methodBuilder.addStatement("jacksonParser.nextToken()");

		methodBuilder.addCode("\n// Parse fields:\n");
		methodBuilder.beginControlFlow("switch (fieldName)$>");

		BindTransform bindTransform;
		for (BindProperty item : entity.getCollection()) {
			bindTransform = BindTransformer.lookup(item);

			methodBuilder.addCode("case $S:\n$>", item.getName());
			methodBuilder.addCode("// field $L\n", item.getName());
			bindTransform.generateParseOnJacksonAsString(methodBuilder, "jacksonParser", item.getPropertyType().getName(), "instance", item);
			methodBuilder.addCode("$<break;\n");
		}

		methodBuilder.addCode("default:$>\n");
		methodBuilder.addStatement("jacksonParser.skipChildren()");
		methodBuilder.addCode("$<break;");

		methodBuilder.addCode("$<");
		methodBuilder.endControlFlow();

		methodBuilder.endControlFlow();

		methodBuilder.addStatement("return instance");
		methodBuilder.nextControlFlow("catch ($T e)", IOException.class);
		methodBuilder.addStatement("e.printStackTrace()");
		methodBuilder.addStatement("throw new $T(e)", KriptonRuntimeException.class);
		methodBuilder.endControlFlow();
		builder.addMethod(methodBuilder.build());

	}

	private static void generateSerializeOnJackson(BindEntity entity) {
		// @formatter:off
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("serializeOnJackson")
				.addJavadoc("reset shared preferences\n")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)				
				.addParameter(typeName(JacksonContext.class), "context")
				.addParameter(typeName(entity.getElement()), "object")
				.addParameter(typeName(JacksonWrapperSerializer.class), "wrapper")
				.returns(Void.TYPE);
		// @formatter:on

		methodBuilder.beginControlFlow("try");
		methodBuilder.addStatement("$T jacksonSerializer = wrapper.jacksonGenerator", className(JsonGenerator.class));
		methodBuilder.addStatement("jacksonSerializer.writeStartObject()");

		BindTransform bindTransform;

		methodBuilder.addCode("\n");

		// fields
		methodBuilder.addCode("// Serialized Field:\n\n");
		for (BindProperty item : entity.getCollection()) {
			bindTransform = BindTransformer.lookup(item);

			methodBuilder.addCode("// field $L\n", item.getName());
			bindTransform.generateSerializeOnJackson(methodBuilder, "jacksonSerializer", item.getPropertyType().getName(), "object", item);
			methodBuilder.addCode("\n");
		}

		methodBuilder.addStatement("jacksonSerializer.writeEndObject()");

		methodBuilder.nextControlFlow("catch($T e)", typeName(IOException.class));
		methodBuilder.addStatement("e.printStackTrace()");
		methodBuilder.addStatement("throw (new $T(e))", typeName(KriptonRuntimeException.class));
		methodBuilder.endControlFlow();

		builder.addMethod(methodBuilder.build());
	}

	private static void generateSerializeOnJacksonAsString(BindEntity entity) {
		// @formatter:off
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("serializeOnJacksonAsString")
				.addJavadoc("reset shared preferences\n")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)				
				.addParameter(typeName(JacksonContext.class), "context")
				.addParameter(typeName(entity.getElement()), "object")
				.addParameter(typeName(JacksonWrapperSerializer.class), "wrapper")
				.returns(Void.TYPE);
		// @formatter:on

		methodBuilder.beginControlFlow("try");
		methodBuilder.addStatement("$T jacksonSerializer = wrapper.jacksonGenerator", className(JsonGenerator.class));
		methodBuilder.addStatement("jacksonSerializer.writeStartObject()");

		BindTransform bindTransform;

		methodBuilder.addCode("\n");

		// fields
		methodBuilder.addCode("// Serialized Field:\n\n");
		for (BindProperty item : entity.getCollection()) {
			bindTransform = BindTransformer.lookup(item);

			methodBuilder.addCode("// field $L\n", item.getName());
			bindTransform.generateSerializeOnJacksonAsString(methodBuilder, "jacksonSerializer", item.getPropertyType().getName(), "object", item);
			methodBuilder.addCode("\n");
		}

		methodBuilder.addStatement("jacksonSerializer.writeEndObject()");

		methodBuilder.nextControlFlow("catch($T e)", typeName(IOException.class));
		methodBuilder.addStatement("e.printStackTrace()");
		methodBuilder.addStatement("throw (new $T(e))", typeName(KriptonRuntimeException.class));
		methodBuilder.endControlFlow();

		builder.addMethod(methodBuilder.build());

	}

	private static void generateSerializeOnXml(BindEntity entity) {
		// @formatter:off
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("serializeOnXml")
				.addJavadoc("reset shared preferences\n")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)				
				.addParameter(typeName(XmlBinderContext.class), "context")
				.addParameter(typeName(entity.getElement()), "object")
				.addParameter(typeName(XmlWrapperSerializer.class), "wrapper")
				.addParameter(typeName(Integer.TYPE), "currentEventType")
				.returns(Void.TYPE);
		// @formatter:on

		methodBuilder.beginControlFlow("try");
		methodBuilder.addStatement("$T xmlSerializer = wrapper.xmlSerializer", className(XMLStreamWriter2.class));

		methodBuilder.beginControlFlow("if (currentEventType == 0)");
		methodBuilder.addStatement("xmlSerializer.writeStartElement(\"$L\")", entity.xmlInfo.tagName);
		methodBuilder.endControlFlow();

		BindTransform bindTransform;

		methodBuilder.addCode("\n");

		// attributes
		methodBuilder.addCode("// Persisted fields:\n\n");
		for (BindProperty item : entity.getCollection()) {
			bindTransform = BindTransformer.lookup(item);

			methodBuilder.addCode("// field $L\n", item.getName());
			bindTransform.generateSerializeOnXml(methodBuilder, "xmlSerializer", item.getPropertyType().getName(), "object", item);
			methodBuilder.addCode("\n");
		}

		methodBuilder.beginControlFlow("if (currentEventType == 0)");
		methodBuilder.addStatement("xmlSerializer.writeEndElement()");
		methodBuilder.endControlFlow();

		methodBuilder.nextControlFlow("catch($T e)", typeName(XMLStreamException.class));
		methodBuilder.addStatement("e.printStackTrace()");
		methodBuilder.addStatement("throw (new $T(e))", typeName(KriptonRuntimeException.class));
		methodBuilder.endControlFlow();

		builder.addMethod(methodBuilder.build());
	}

}
