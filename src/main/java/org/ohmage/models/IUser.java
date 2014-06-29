package org.ohmage.models;

import java.io.Serializable;

/**
 * Created by changun on 6/29/14.
 */
public interface IUser extends Serializable {
    String getId();

    @Override
    String toString();

    @Override
    int hashCode();

    @Override
    boolean equals(Object other);
}
