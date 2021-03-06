package org.ohmage.models;

/**
 * Created by changun on 6/28/14.
 */
public class Ohmage30Stream implements IStream {
    final private String id;
    final private String version;

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getReadScopeName() {
        return getBaseScopeName() + "read";
    }

    public String getWriteScopeName() {
        return getBaseScopeName() + "write";
    }

    private String getBaseScopeName() {
        return "/streams" + "/" + id + "/" + version + "/";
    }

    public Ohmage30Stream(String id, String version) {
        this.id = id;
        this.version = version;
    }

    public Ohmage30Stream(String id) {
        this.id = id;
        this.version = "*";
    }
}
