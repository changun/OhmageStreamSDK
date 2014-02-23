package org.ohmage.sdk.tests;

import java.io.IOException;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Test;
import org.ohmage.models.OhmageStream;
import org.ohmage.models.OhmageUser;
import org.ohmage.models.OhmageUser.OhmageAuthenticationError;
import org.ohmage.sdk.OhmageStreamClient;
import org.ohmage.sdk.OhmageStreamIterator;
import org.ohmage.sdk.OhmageStreamIterator.SortOrder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StreamIteratorTest {

	@Test
	public void testMultiply() throws OhmageAuthenticationError, IOException {
		/* this test case queries the Mobility data for two users */
		
		
		// targeted ohmage stream
		OhmageStream stream = new OhmageStream.Builder()
				.observerId("edu.ucla.cens.Mobility")
				.observerVer("2012061300")
				.streamId("regular")
				.streamVer("2012050700").build();
		
		// requester should have access to the requestee' data streams
		OhmageUser requester = new OhmageUser("https://test.ohmage.org", "LifestreamsTest1", "1qaz@WSX");
		// create a ohmage stream client
		OhmageStreamClient client = new OhmageStreamClient(requester);
		
		// requestee's password is not required
		OhmageUser requestee = new OhmageUser(requester.getServer(), "LifestreamsTest1", null);

		
		OhmageStreamIterator streamIterator = client.getOhmageStreamIteratorBuilder(stream, requestee)
			.order(SortOrder.Chronological) // order is optional, it is in Chronological order by default
			.build(); 
		
		ObjectMapper mapper = new ObjectMapper();
		while(streamIterator.hasNext()){
			// the iterator returns each data point as a json node
			JsonNode node = streamIterator.next();
			break;
			// then do sth with it....
		}
		
		
		
	   
	 } 
}
