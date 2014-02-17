package org.ohmage.sdk.tests;

import java.io.IOException;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Test;
import org.ohmage.models.OhmageUser;
import org.ohmage.models.OhmageUser.OhmageAuthenticationError;
import org.ohmage.sdk.OhmageStreamIterator;
import org.ohmage.sdk.OhmageStreamIterator.SortOrder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StreamIteratorTest {

	@Test
	public void testMultiply() throws OhmageAuthenticationError, IOException {
		/* this test case queries the Mobility data for two users */
		
		// requester should have access to the requestee' data streams
		OhmageUser requester = new OhmageUser("https://test.ohmage.org", "requesterUserName", "requesterPassword");
		// requestee's password is not required
		OhmageUser requestee = new OhmageUser(requester.getServer(), "requesteeUserName", null);
		
		OhmageStreamIterator streamIterator = new OhmageStreamIterator.Builder()
			.observerId("edu.ucla.cens.Mobility")
			.observerVer("2012061300")
			.streamId("extended")
			.streamVer("2012050700")
			.requester(requester)
			.requestee(requestee)
			.order(SortOrder.Chronological) // order is optional, it is in Chronological order by default
			.startDate(new DateTime("2014-02-14T08:22:10+00:00"))  // start date is optional
			.endDate(new DateTime("2014-02-17T08:22:10+00:00")) // end date is optional
			.build(); 
		
		ObjectMapper mapper = new ObjectMapper();
		while(streamIterator.hasNext()){
			// the iterator returns each data point as a json node
			JsonNode node = streamIterator.next();
			// then do sth with it....
		}
		
		
		
	   
	 } 
}
