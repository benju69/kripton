package com.abubusoft.kripton.binder2.persistence;

import org.codehaus.stax2.XMLStreamReader2;

import com.abubusoft.kripton.binder2.context.BinderContext;
import com.abubusoft.kripton.binder2.core.BinderType;

public class XmlWrapperParser implements ParserWrapper {

	public XMLStreamReader2 xmlParser;

	public XmlWrapperParser(BinderContext<XmlWrapperSerializer, XmlWrapperParser> context, XMLStreamReader2 xmlStreamReader, BinderType supportedFormat) {
		this.xmlParser = xmlStreamReader;
	}

}