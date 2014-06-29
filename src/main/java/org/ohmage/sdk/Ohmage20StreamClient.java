package org.ohmage.sdk;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ohmage.models.IUser;
import org.ohmage.models.Ohmage20Stream;
import org.ohmage.models.Ohmage20User;
import org.ohmage.models.Ohmage20User.OhmageAuthenticationError;
import org.ohmage.models.OhmageServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ohmage20StreamClient {

    Ohmage20User requester;

    public Ohmage20StreamClient(Ohmage20User requester) {
        super();
        this.requester = requester;
    }

    public Ohmage20StreamIterator.Builder getOhmageStreamIteratorBuilder(Ohmage20Stream stream, IUser user) {
        Ohmage20StreamIterator.Builder builder = new Ohmage20StreamIterator.Builder();
        builder.requester(requester);
        builder.stream(stream);
        builder.owner(user);
        return builder;
    }

    public void upload(Ohmage20Stream stream, List<ObjectNode> data) throws OhmageAuthenticationError, IOException {
        new Ohmage20StreamUploader(stream, requester).upload(data);
    }

    public void upload(Ohmage20Stream stream, ObjectNode data) throws OhmageAuthenticationError, IOException {
        List<ObjectNode> nodeArray = new ArrayList<ObjectNode>();
        nodeArray.add(data);
        new Ohmage20StreamUploader(stream, requester).upload(nodeArray);
    }

    public OhmageServer getServer() {
        return this.requester.getServer();
    }

}
