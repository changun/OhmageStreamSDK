package org.ohmage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.kevinsawicki.http.HttpRequest;

import java.io.Serializable;

/**
 * Created by changun on 6/28/14.
 */
public class Ohmage30Server implements Serializable {
    String baseURL;

    public final static String CLIENT_STRING = "lifestreams";

    @JsonIgnore
    public String getOAuthAuthURL() {
        return baseURL + "ohmage/oauth/authorize";
    }

    @JsonIgnore
    public String getOAuthTokenURL() {
        return baseURL + "ohmage/oauth/token";
    }

    public String getStreamDataURL(Ohmage30Stream stream){
        return baseURL + "ohmage/streams/" + stream.getId() + "/" + stream.getVersion()+"/data";
    }

    public HttpRequest getHttpRequest(String url, String method, String token){
        return new HttpRequest(url, method).header("Authorization", "ohmage " + token);
    }
    public String toString() {
        return this.baseURL;
    }

    public Ohmage30Server() {
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public Ohmage30Server(String base) {
        if (base.charAt(base.length() - 1) != '/')
            base += "/";
        this.baseURL = base;
    }
}
