package org.ohmage.models;

import java.io.Serializable;


public class OhmageStream  implements Serializable {
	
	String observerId;
	String streamId;
	String observerVer;
	String streamVer;
	
	public OhmageStream(String observerId, String streamId, String observerVer,
			String streamVer) {
		super();
		this.observerId = observerId;
		this.streamId = streamId;
		this.observerVer = observerVer;
		this.streamVer = streamVer;
	}
	public String toString(){
		return String.format("ObserverId:%s(%s) StreamId:%s(%s)", observerId, observerVer, streamId, streamVer);
	}
	public void setObserverId(String observerId) {
		this.observerId = observerId;
	}

	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}

	public void setObserverVer(String observerVer) {
		this.observerVer = observerVer;
	}

	public void setStreamVer(String streamVer) {
		this.streamVer = streamVer;
	}

	public String getObserverId() {
		return observerId;
	}

	public String getStreamId() {
		return streamId;
	}

	public String getObserverVer() {
		return observerVer;
	}

	public String getStreamVer() {
		return streamVer;
	}

	public static class Builder {

		private String observerId;
		private String streamId;
		private String observerVer;
		private String streamVer;


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

		public OhmageStream build() {
			return new OhmageStream(this);
		}
	}

	private OhmageStream(Builder builder) {
		this.observerId = builder.observerId;
		this.streamId = builder.streamId;
		this.observerVer = builder.observerVer;
		this.streamVer = builder.streamVer;
	}
}
