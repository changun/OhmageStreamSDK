package org.ohmage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by changun on 6/28/14.
 */
public class Ohmage30Server {
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
