package org.ohmage.models;

import java.io.Serializable;
import java.net.URL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
		return baseURL + "app/class";
	}

	public String toString(){
		return this.baseURL;
	}
	
	public OhmageServer(){}
	
	public String getBaseURL() {
		return baseURL;
	}
	public OhmageServer(String base){
		if( base.charAt(base.length()-1) != '/')
			base += "/";
		this.baseURL = base;
	}
	
}
