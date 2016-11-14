package kripton70.typeconverters;

import java.io.IOException;

import kripton70.core.BinderGenerator;
import kripton70.core.BinderParser;

import com.fasterxml.jackson.core.JsonToken;

public class ByteConverter implements TypeConverter<Byte> {

	@Override
	public Byte parse(BinderParser parser) throws IOException {
		if (parser.getCurrentToken() == JsonToken.VALUE_NULL) {
			return null;
		} else if (parser.onlyText){
			return Byte.valueOf(parser.getText());
		} else {
			return parser.getByteValue();
			
		}
	}

	@Override
	public void serialize(BinderGenerator generator, boolean writeFieldNameForObject, String fieldName, Byte value) throws IOException {
		generator.writeFieldName(fieldName);
		generator.writeNumber(value);
		
	}

	

}
