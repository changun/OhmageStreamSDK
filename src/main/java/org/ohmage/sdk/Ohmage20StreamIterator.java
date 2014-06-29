package org.ohmage.sdk;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kevinsawicki.http.HttpRequest;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.ohmage.models.IUser;
import org.ohmage.models.Ohmage20Stream;
import org.ohmage.models.Ohmage20User;
import org.ohmage.models.Ohmage20User.OhmageAuthenticationError;
import org.ohmage.models.OhmageServer;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Ohmage20StreamIterator implements Iterator<ObjectNode>, Closeable {
    public enum SortOrder {
        Chronological, ReversedChronological
    }

    private JsonFactory factory = new MappingJsonFactory();
    private JsonParser curParser;
    private InputStream curInputStream;
    private ObjectNode nextNode;
    private String nextURL;

    Ohmage20Stream stream;
    Ohmage20User requester;
    IUser owner;

    DateTime startDate;
    DateTime endDate;
    int numberToReturn;
    SortOrder order;
    String columnList;

    private void init() throws OhmageAuthenticationError, IOException {
        String token = requester.getToken();
        Map<String, String> params = getRequestParams(token);
        HttpRequest request = HttpRequest.post(
                requester.getServer().getStreamReadURL(), params, false);
        if (!request.ok()) {
            throw new OhmageHttpRequestException(request);
        }
        InputStream buf = request.stream();
        curParser = factory.createParser(buf);
        forwardToStartOfDataAndSetNextURL(curParser, buf);
        advance();
    }

    private Map<String, String> getRequestParams(String token) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("auth_token", token);
        params.put("observer_id", stream.getObserverId());
        params.put("stream_id", stream.getId());

        params.put("observer_version", stream.getObserverVer());
        params.put("stream_version", stream.getVersion());

        params.put("client", OhmageServer.CLIENT_STRING);
        params.put("username", owner.getId());


        params.put("chronological",
                this.order == SortOrder.Chronological ? "true" : "false");
        if (columnList != null)
            params.put("column_list", columnList);
        if (startDate != null)
            params.put("start_date", startDate.toString());
        if (endDate != null)
            params.put("start_date", startDate.toString());
        if (endDate != null)
            params.put("end_date", endDate.toString());
        if (numberToReturn != -1)
            params.put("num_to_return", String.valueOf(numberToReturn));
        return params;
    }

    void forwardToStartOfDataAndSetNextURL(JsonParser jp, InputStream input)
            throws IOException {

        jp.nextToken(); // START {
        jp.nextToken(); // FIELD NAME
        String fieldName = jp.getCurrentName();
        assert fieldName.equals("result");
        jp.nextToken(); // VALUE
        // move from field name to field value
        String result = jp.getText();
        if (result.equals("success")) {
            jp.nextToken(); // FIELD NAME
            fieldName = jp.getCurrentName();
            assert fieldName.equals("metadata");
            jp.nextToken();
            ObjectNode metadata = jp.readValueAsTree();
            jp.nextToken(); // FIELD NAME
            fieldName = jp.getCurrentName();
            assert fieldName.equals("data");
            // skip Array starting token (i.e. [)
            jp.nextToken(); // ARRAY START
            if (metadata.get("next") != null && metadata.get("count").asInt() != 0) {
                this.nextURL = metadata.get("next").asText();
            } else {
                this.nextURL = null;
            }
            return;
        }
        // something wrong wit the returned results
        throw new IOException("Stream Read failed:"
                + IOUtils.toString(input, "UTF-8"));
    }

    private void advance() throws IOException {
        nextNode = null;
        JsonToken curToken = curParser.nextToken();
        if (curToken == JsonToken.START_OBJECT) {
            nextNode = curParser.readValueAsTree();
            return;
        } else {
            if (nextURL == null) {
                curParser.close();
                return;
            } else {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", this.owner.getId());
                curInputStream = HttpRequest.get(nextURL, params, false).stream();
                curParser = factory.createParser(curInputStream);
                forwardToStartOfDataAndSetNextURL(curParser, curInputStream);
                advance();
            }
        }

        // when we get here, we should be ready to read the next node
        // move to next token if we are at STA


    }

    public boolean hasNext() {
        return nextNode != null;
    }

    public ObjectNode next() {
        ObjectNode ret = nextNode;
        try {
            advance();
        } catch (IOException e) {
            System.err.println(e);
        }
        return ret;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public static class Builder {
        private Ohmage20Stream stream;
        private Ohmage20User requester;
        private DateTime startDate;
        private DateTime endDate;
        private int numberToReturn = -1;
        private SortOrder order = SortOrder.Chronological;
        private IUser owner;
        private String columnList;

        protected Builder stream(Ohmage20Stream stream) {
            this.stream = stream;
            return this;
        }

        protected Builder requester(Ohmage20User requester) {
            this.requester = requester;
            return this;
        }

        public Builder startDate(DateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(DateTime endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder numberToReturn(int numberToReturn) {
            this.numberToReturn = numberToReturn;
            return this;
        }

        public Builder order(SortOrder order) {
            this.order = order;
            return this;
        }

        protected Builder owner(IUser user) {
            this.owner = user;
            return this;
        }

        public Builder columnList(String columnList) {
            this.columnList = columnList;
            return this;
        }

        public Ohmage20StreamIterator build() throws OhmageAuthenticationError, IOException {
            return new Ohmage20StreamIterator(this);
        }


    }

    private Ohmage20StreamIterator(Builder builder) throws OhmageAuthenticationError, IOException {
        if (builder.stream == null || builder.requester == null || builder.owner == null)
            throw new IllegalArgumentException();
        this.stream = builder.stream;
        this.requester = builder.requester;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.numberToReturn = builder.numberToReturn;
        this.order = builder.order;
        this.owner = builder.owner;
        this.columnList = builder.columnList;
        init();
    }

    public void close() throws IOException {
        if (this.curParser != null && !this.curParser.isClosed()) {
            this.curParser.close();
        }
        if (this.curInputStream != null) {
            this.curInputStream.close();
        }

    }
}
