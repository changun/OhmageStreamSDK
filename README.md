OhmageStreamSDK
===============

A simple SDK for accessing ohmage stream API


```java
		// targeted ohmage stream
		OhmageStream stream = new OhmageStream.Builder()
				.observerId("edu.ucla.cens.Mobility")
				.observerVer("2012061300")
				.streamId("regular")
				.streamVer("2012050700").build();
		
		// requester should have access to the requestee' data streams
		OhmageUser requester = new OhmageUser("https://test.ohmage.org", "username", "password");
		// create a ohmage stream client
		OhmageStreamClient client = new OhmageStreamClient(requester);
		
		// requestee's password is not required
		OhmageUser requestee = new OhmageUser(requester.getServer(), "requestee_username", null);

		
		OhmageStreamIterator streamIterator = client.getOhmageStreamIteratorBuilder(stream, requestee)
			.order(SortOrder.Chronological) // order is optional, it is in Chronological order by default
			.build(); 
		
		ObjectMapper mapper = new ObjectMapper();
		while(streamIterator.hasNext()){
			// the iterator returns each data point as a json node
			JsonNode node = streamIterator.next();
			// then do sth with it....
		}
```