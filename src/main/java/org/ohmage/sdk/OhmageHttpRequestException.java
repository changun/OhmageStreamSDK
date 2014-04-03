package org.ohmage.sdk;

import java.io.IOException;

import com.github.kevinsawicki.http.HttpRequest;

public class OhmageHttpRequestException extends IOException{
	final HttpRequest req;
	OhmageHttpRequestException(HttpRequest req){
		super();
		this.req = req;
	}
	public HttpRequest getReq() {
		return req;
	}
	
}