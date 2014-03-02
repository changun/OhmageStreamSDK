OhmageStreamSDK
===============

A simple SDK for accessing ohmage stream API


Data download example
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

Data upload example
```java
		
		/*** setup the target stream and user ***/
		OhmageStream stream = new OhmageStream.Builder()
						.observerId("edu.ucla.cens.Mobility")
						.observerVer("2012061300")
						.streamId("regular")
						.streamVer("2012050700").build();
				
		// the user to which the record is uploaded to
		OhmageUser user= new OhmageUser("https://test.ohmage.org", "username", "password");
		// create a ohmage stream client
		OhmageStreamClient client = new OhmageStreamClient(user);
		
		/*** create a record of which the fields comply with the stream's schema ***/
		
		ObjectMapper mapper = new ObjectMapper();

		// populate metadata fields ....
		ObjectNode metadata = mapper.createObjectNode();
		metadata.put(...)
		
		// populate data fields
		ObjectNode data = mapper.createObjectNode();
		data.put(...)
		
		
		ObjectNode record = mapper.createObjectNode();
		record.put("data", data);
		record.put("metadata", metadata);
		
		/*** upload the record ***/
		client.upload(targetStream, record);
		
		// you can also upload in batch
		client.upload(targetStream, List<ObjectNode> records);
			
```