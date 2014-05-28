package org.ohmage.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ohmage.models.OhmageServer;
import org.ohmage.models.OhmageStream;
import org.ohmage.models.OhmageUser;
import org.ohmage.models.OhmageUser.OhmageAuthenticationError;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kevinsawicki.http.HttpRequest;

public class OhmageStreamUploader {
	
	OhmageStream stream;
	OhmageUser requester;
	public OhmageStreamUploader(OhmageStream stream, OhmageUser requester) {
		super();
		this.stream = stream;
		this.requester = requester;
	}

	private Map<String, String> getRequestParams() throws OhmageAuthenticationError {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("auth_token", requester.getToken());
		params.put("observer_id", stream.getObserverId());
		params.put("observer_version", stream.getObserverVer());
		params.put("client", OhmageServer.CLIENT_STRING);
		params.put("username", requester.getUsername());
		return params;
	}
	private final ObjectMapper mapper = new ObjectMapper();
	public void upload(List<ObjectNode> data) throws OhmageAuthenticationError, IOException{
		Map<String, String> params = getRequestParams();
		
		for(ObjectNode node: data){
			node.put("stream_id", stream.getStreamId());
			node.put("stream_version", Integer.valueOf(stream.getStreamVer()));
		}
		params.put("data", mapper.writeValueAsString(data));
		
		
		HttpRequest request = HttpRequest.post(
				requester.getServer().getStreamUploadURL()).form(params);

		if(request.ok()){
			InputStream buf = request.buffer();
			ObjectNode ret = mapper.readValue(buf, ObjectNode.class);
			if(!ret.get("result").asText().equals("success")){
				throw new IOException(mapper.writeValueAsString(ret));
			}
			else if(ret.get("invalid_points").size() != 0 ){
				throw new IOException(mapper.writeValueAsString(ret.get("invalid_points")));
			}
		}
		else
			throw new IOException("Upload request failed. Code:" + request.code());
	}
}
