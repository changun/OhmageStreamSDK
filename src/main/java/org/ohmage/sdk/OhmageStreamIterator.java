package org.ohmage.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.ohmage.models.OhmageServer;
import org.ohmage.models.OhmageUser;
import org.ohmage.models.OhmageUser.OhmageAuthenticationError;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kevinsawicki.http.HttpRequest;

public class OhmageStreamIterator implements Iterator<JsonNode> {
	public enum SortOrder {
		Chronological, ReversedChronological
	}

	private JsonFactory factory = new MappingJsonFactory();
	private JsonParser curParser;
	private JsonNode nextNode;
	private String nextURL;

	OhmageUser requestee;
	OhmageUser requester;
	String observerId;
	String streamId;
	String observerVer;
	String streamVer;
	DateTime startDate;
	DateTime endDate;
	int numberToReturn;
	SortOrder order;

	private void init() throws OhmageAuthenticationError, IOException {
		String token = requester.getToken();
		Map<String, String> params = getRequestParams(token);
		InputStream buf = HttpRequest.post(
				requester.getServer().getStreamReadURL(), params, false)
				.buffer();
		curParser = factory.createParser(buf);
		forwardToStartOfDataAndSetNextURL(curParser, buf);
		advance();

	}

	Map<String, String> getRequestParams(String token) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("auth_token", token);
		params.put("observer_id", observerId);
		params.put("stream_id", streamId);
		params.put("start_date", startDate.toString());
		params.put("observer_version", observerVer);
		params.put("stream_version", streamVer);
		params.put("client", OhmageServer.CLIENT_STRING);
		params.put("username", requestee.getUsername());
		params.put("chronological",
				this.order == SortOrder.Chronological ? "true" : "false");
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
			if (metadata.get("next") != null) {
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
		if (curParser.nextToken() != JsonToken.START_OBJECT) {
			if (nextURL == null) {
				return;
			} else {
				InputStream buf = HttpRequest.get(nextURL).buffer();
				curParser = factory.createParser(buf);
				forwardToStartOfDataAndSetNextURL(curParser, buf);
			}
		}
		// when we get here, we should be ready to read the next node
		nextNode = curParser.readValueAsTree();
	}

	public static class Builder {
		private OhmageUser requestee;
		private String observerId;
		private String streamId;
		private String observerVer;
		private String streamVer;
		private OhmageUser requester;
		private DateTime startDate;
		private DateTime endDate;
		private int numberToReturn = -1;
		private SortOrder order = SortOrder.Chronological;

		public Builder requestee(OhmageUser requestee) {
			this.requestee = requestee;
			return this;
		}

		public Builder observerId(String observerId) {
			this.observerId = observerId;
			return this;
		}

		public Builder streamId(String streamId) {
			this.streamId = streamId;
			return this;
		}

		public Builder observerVer(String observerVer) {
			this.observerVer = observerVer;
			return this;
		}

		public Builder streamVer(String streamVer) {
			this.streamVer = streamVer;
			return this;
		}

		public Builder requester(OhmageUser requester) {
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

		public OhmageStreamIterator build() throws OhmageAuthenticationError,
				IOException {
			if (this.observerId == null || this.requestee == null
					|| this.streamId == null || this.streamVer == null
					|| this.requester == null) {
				throw new IllegalArgumentException(
						"Missing some of the required arguments");
			}
			return new OhmageStreamIterator(this);
		}
	}

	private OhmageStreamIterator(Builder builder)
			throws OhmageAuthenticationError, IOException {
		this.requestee = builder.requestee;
		this.observerId = builder.observerId;
		this.streamId = builder.streamId;
		this.observerVer = builder.observerVer;
		this.streamVer = builder.streamVer;
		this.requester = builder.requester;
		this.startDate = builder.startDate;
		this.endDate = builder.endDate;
		this.numberToReturn = builder.numberToReturn;
		this.order = builder.order;

		init();
	}

	public boolean hasNext() {
		return nextNode != null;
	}

	public JsonNode next() {
		JsonNode ret = nextNode;
		try {
			advance();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
