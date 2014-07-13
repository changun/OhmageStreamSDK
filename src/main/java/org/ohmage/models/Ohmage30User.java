package org.ohmage.models;

/**
 * Created by changun on 6/27/14.
 */
public class Ohmage30User implements IUser {
    String id;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ohmage30User that = (Ohmage30User) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Ohmage30User(String id) {
        this.id = id;
    }

    public Ohmage30User() {
    }
}
