package com.abubusoft.kripton.android;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.abubusoft.kripton.binder.BinderWriter;
import com.abubusoft.kripton.binder.Format;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.DEFAULT)
public class BinderTest {

	@Test
	public void testChatWriter() {
		
		Format format=new Format(true);
		BinderWriter writer = BinderFactory.getJSONWriter();
		try {

			File file = new File("src/test/java/list_message.json");

			ChatMessage message = new ChatMessage();
			//message.rawValue="ciao".getBytes();
			message.setType(ChatMessageType.SAY);
			message.setUid("asdfa");
			message.setType(ChatMessageType.LIST);
			//message.rawValue=Base64.encode("bla bla bla".getBytes());

			ChatMessageArray array = new ChatMessageArray();
			array.add(message);
			array.add(message);

			System.out.println(writer.write(array));

			/*
			 * File fileOutput=new
			 * File("src/test/java/argon_settings_test.xml"); BinderWriter
			 * writer = BinderFactory.getXMLWriter(); writer.write(settings, new
			 * FileOutputStream(fileOutput));
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
