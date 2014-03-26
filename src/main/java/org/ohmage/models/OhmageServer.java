package org.ohmage.models;

import java.io.Serializable;
import java.net.URL;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class OhmageServer implements Serializable {
	String baseURL;
	public final static String CLIENT_STRING = "lifestreams";
	
	public String getAuthenticateURL(){
		return baseURL + "app/user/auth_token";
	}
	
	public String getStreamReadURL(){
		return baseURL + "app/stream/read";
	}
	public String getStreamUploadURL(){
		return baseURL + "app/stream/upload";
	}
	public String getClassReadURL(){
		return baseURL + "app/class/read";
	}
	public String getUserInfoURL(){
		return baseURL + "app/user_info/read";
	}
	public String getWhoAmIURL(){
		return baseURL + "app/user/whoami";
	}
	public String toString(){
		return this.baseURL;
	}
	
	public OhmageServer(){}
	
	public String getBaseURL() {
		return baseURL;
	}
	@JsonProperty
	public String getURL() {
		return baseURL;
	}
	public OhmageServer(String base){
		if( base.charAt(base.length()-1) != '/')
			base += "/";
		this.baseURL = base;
	}
	
}
