package org.ohmage.models;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
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
	private String auth_token;
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
	@JsonIgnore
	public List<OhmageClass> getClassList(){
		try{
			ObjectMapper mapper = new ObjectMapper();
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("client", OhmageServer.CLIENT_STRING);
			data.put("auth_token", this.getToken());
			InputStream res = HttpRequest.post(server.getUserInfoURL(), data, false).buffer();
			ObjectNode rootNode = mapper.readValue(res, ObjectNode.class);
			 Iterator<Map.Entry<String,JsonNode>> classes=  rootNode.get("data").get(this.getUsername()).get("classes").fields();
			ArrayList<OhmageClass> ret = new ArrayList<OhmageClass>();
			while(classes.hasNext()){
				Map.Entry<String,JsonNode> _class = classes.next(); 
				ret.add(new OhmageClass(_class.getKey(), _class.getValue().asText(), this.server));
			}
			return ret;
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	@JsonIgnore
	public String getToken(){
 		ObjectMapper mapper = new ObjectMapper();
		try{
			if(auth_token != null){
				// if we have a cached auth_token
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("auth_token", auth_token);
				data.put("client", OhmageServer.CLIENT_STRING);
				// query Who AM I to see if it is still valid
				InputStream res = HttpRequest.post(server.getWhoAmIURL(), data, false).stream();
				
				ObjectNode rootNode = mapper.readValue(res, ObjectNode.class);
				if(rootNode.get("result").asText().equals("success"))
					return auth_token;
			}
			// get a new token
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("user", username);
			data.put("password", password);
			data.put("client", OhmageServer.CLIENT_STRING);

			InputStream res = HttpRequest.post(server.getAuthenticateURL(), data, false).buffer();
			ObjectNode rootNode = mapper.readValue(res, ObjectNode.class);
			if(rootNode.get("result").asText().equals("success")){	
				auth_token = rootNode.get("token").asText();
				return auth_token;
			}
			else{
				throw new RuntimeException(new OhmageAuthenticationError(this));
			}
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	@JsonIgnore
	public List<OhmageClass> getPrevilegedClasses(){
		ArrayList<OhmageClass> ret = new ArrayList<OhmageClass>();
		for(OhmageClass _class:this.getClassList()){
			if(_class.getUserList(this.getToken()).get(this.username) == OhmagePermission.PRIVILEGED){
				ret.add(_class);
			}
		}
		return ret;
	}
	public Map<OhmageClass, List<String>> getAccessibleUsers(){
		HashMap<OhmageClass, List<String>> ret = new HashMap<OhmageClass, List<String>>();
		for(OhmageClass _class:this.getPrevilegedClasses()){
			List<String> users = new ArrayList<String>();
			for(String user: _class.getUserList(this.getToken()).keySet()){
				users.add(user);
			}
			ret.put(_class, users);
		}
		// add the inidividual class for the user himself
		ret.put(OhmageClass.getIndividualClass(), Arrays.asList(this.getUsername()));
		return ret;
	}
	public boolean hasAccessTo(String username){
		for(OhmageClass _class: this.getPrevilegedClasses()){
			if(_class.getUserList(this.getToken()).containsKey(username))
				return true;
		}
		return false;
	}
	@Override
	public String toString(){
		return String.format("User %s on %s", username, server);
	}
	@Override
	public int hashCode(){
		return String.format("%s:%s", username, server).hashCode();
	}
	static public class OhmageAuthenticationError extends Exception{
		OhmageUser user;
		OhmageAuthenticationError(OhmageUser u){
			this.user=u;
		}
	}
}
