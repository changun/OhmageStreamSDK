package org.ohmage.models;


public class Ohmage20Stream implements IStream {

    String observerId;
    String streamId;
    String observerVer;
    String streamVer;

    public Ohmage20Stream(String observerId, String streamId, String observerVer,
                          String streamVer) {
        super();
        this.observerId = observerId;
        this.streamId = streamId;
        this.observerVer = observerVer;
        this.streamVer = streamVer;
    }

    @Override
    public String toString() {
        return String.format("%s&%s&%s&%s", observerId, streamId, observerVer, streamVer);
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

    @Override
    public String getId() {
        return streamId;
    }

    public String getObserverVer() {
        return observerVer;
    }

    @Override
    public String getVersion() {
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

        public Ohmage20Stream build() {
            return new Ohmage20Stream(this);
        }
    }

    private Ohmage20Stream(Builder builder) {
        this.observerId = builder.observerId;
        this.streamId = builder.streamId;
        this.observerVer = builder.observerVer;
        this.streamVer = builder.streamVer;
    }
}
