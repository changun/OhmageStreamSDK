package org.ohmage.models;

import java.io.Serializable;
import java.net.URL;

public class OhmageServer implements Serializable {
	String _baseURL;
	public final static String CLIENT_STRING = "lifestreams";
	public String getAuthenticateURL(){
		return _baseURL + "app/user/auth_token";
	}
	public String getStreamReadURL(){
		return _baseURL + "app/stream/read";
	}
	public String getClassReadURL(){
		return _baseURL + "app/class";
	}
	public String toString(){
		return this._baseURL;
	}
	public OhmageServer(){}
	public OhmageServer(String base){
		if( base.charAt(base.length()-1) != '/')
			base += "/";
		this._baseURL = base;
	}
	
}
