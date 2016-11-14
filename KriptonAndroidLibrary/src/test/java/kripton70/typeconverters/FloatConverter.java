package kripton70.typeconverters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonToken;

import kripton70.core.BinderGenerator;
import kripton70.core.BinderParser;

public class FloatConverter implements TypeConverter<Float> {

	@Override
	public Float parse(BinderParser parser) throws IOException {
		if (parser.getCurrentToken() == JsonToken.VALUE_NULL) {
			return null;
		} else if (parser.onlyText){
			return Float.valueOf(parser.getText());
		} else {
			return parser.getFloatValue();
		}
	}

	@Override
	public void serialize(BinderGenerator generator, boolean writeFieldNameForObject, String fieldName, Float value) throws IOException {
		generator.writeFieldName(fieldName);
		generator.writeNumber(value);
	}

}
