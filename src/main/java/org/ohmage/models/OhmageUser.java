package org.ohmage.models;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kevinsawicki.http.HttpRequest;

public class OhmageUser implements Serializable{
	OhmageServer server;
	String username;
	
	public String getPassword() {
		return password;
	}
	

	String password;

	public OhmageServer getServer(){
		return server;
	}
	public OhmageUser(String server, String username, String password) {
		this(new OhmageServer(server), username, password);
	}
	public OhmageUser(){};
	public OhmageUser(OhmageServer server, String username, String password) {
		super();
		this.server = server;
		this.username = username;
		this.password = password;
	}
	public String getUsername(){
		return username;
	}
	@JsonIgnoreProperties
	public String getToken() throws OhmageAuthenticationError{
		 		ObjectMapper mapper = new ObjectMapper();
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("user", username);
				data.put("password", password);
				data.put("client", OhmageServer.CLIENT_STRING);
				try{
					InputStream res = HttpRequest.post(server.getAuthenticateURL(), data, false).buffer();
					ObjectNode rootNode = mapper.readValue(res, ObjectNode.class);
					if(rootNode.get("result").asText().equals("success")){	
						return rootNode.get("token").asText();
					}
					else{
						throw new OhmageAuthenticationError(this);
					}
				}
				catch(IOException e){
					throw new OhmageAuthenticationError(this);
				}
			
	}

	@Override
	public String toString(){
		return String.format("User %s on %s", username, server);
	}
	
	public class OhmageAuthenticationError extends Exception{
		OhmageUser user;
		OhmageAuthenticationError(OhmageUser u){
			this.user=u;
		}
	}
}
