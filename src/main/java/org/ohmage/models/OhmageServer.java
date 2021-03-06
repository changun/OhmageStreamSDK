package org.ohmage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class OhmageServer implements Serializable {
    String baseURL;

    public final static String CLIENT_STRING = "lifestreams";

    @JsonIgnore
    public String getAuthenticateURL() {
        return baseURL + "app/user/auth_token";
    }

    @JsonIgnore
    public String getStreamReadURL() {
        return baseURL + "app/stream/read";
    }

    @JsonIgnore
    public String getStreamUploadURL() {
        return baseURL + "app/stream/upload";
    }

    @JsonIgnore
    public String getClassReadURL() {
        return baseURL + "app/class/read";
    }

    @JsonIgnore
    public String getUserInfoURL() {
        return baseURL + "app/user_info/read";
    }

    @JsonIgnore
    public String getUserReadURL() {
        return baseURL + "app/user/read";
    }

    @JsonIgnore
    public String getWhoAmIURL() {
        return baseURL + "app/user/whoami";
    }

    public String toString() {
        return this.baseURL;
    }

    public OhmageServer() {
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public OhmageServer(String base) {
        if (base.charAt(base.length() - 1) != '/')
            base += "/";
        this.baseURL = base;
    }

}
