package org.ohmage.sdk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ohmage.models.OhmageServer;
import org.ohmage.models.OhmageStream;
import org.ohmage.models.OhmageUser;
import org.ohmage.models.OhmageUser.OhmageAuthenticationError;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class OhmageStreamClient {

	OhmageUser requester;
	public OhmageStreamClient(OhmageUser requester) {
		super();
		this.requester = requester;
	}
	public OhmageStreamIterator.Builder getOhmageStreamIteratorBuilder(OhmageStream stream, OhmageUser user){
		OhmageStreamIterator.Builder builder = new OhmageStreamIterator.Builder();
		builder.requester(requester);
		builder.stream(stream);
		builder.owner(user);
		return builder;
	}
	public void upload(OhmageStream stream, List<ObjectNode> data) throws OhmageAuthenticationError, IOException{
		new OhmageStreamUploader(stream, requester).upload(data);
	}
	public void upload(OhmageStream stream, ObjectNode data) throws OhmageAuthenticationError, IOException{
		List<ObjectNode> nodeArray = new ArrayList<ObjectNode> ();
		nodeArray.add(data);
		new OhmageStreamUploader(stream, requester).upload(nodeArray);
	}
	public OhmageServer getServer(){
		return this.requester.getServer();
	}
	
}
