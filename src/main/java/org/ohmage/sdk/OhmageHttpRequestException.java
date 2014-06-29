package org.ohmage.sdk;

import com.github.kevinsawicki.http.HttpRequest;

import java.io.IOException;

public class OhmageHttpRequestException extends IOException {
    final HttpRequest req;

    OhmageHttpRequestException(HttpRequest req) {
        super();
        this.req = req;
    }

    public HttpRequest getReq() {
        return req;
    }

}