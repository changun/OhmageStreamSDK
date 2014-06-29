package org.ohmage.models;

import java.io.Serializable;

/**
 * Created by changun on 6/29/14.
 */
public interface IStream extends Serializable {
    String toString();

    String getId();

    String getVersion();
}
