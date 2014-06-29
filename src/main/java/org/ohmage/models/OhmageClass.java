package org.ohmage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kevinsawicki.http.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OhmageClass {

    private static String INDIVIDUAL_CLASS_NAME = "Yourself";
    private String urn;
    private String name;


    private OhmageServer server;

    @JsonProperty
    public String getUrn() {
        return urn;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    /**
     * @return the server
     */
    @JsonIgnore
    public OhmageServer getServer() {
        return server;
    }

    /**
     * @param server the server to set
     */
    @JsonIgnore
    public void setServer(OhmageServer server) {
        this.server = server;
    }

    /**
     * @param urn the urn to set
     */
    public void setUrn(String urn) {
        this.urn = urn;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }


    static private ObjectMapper mapper = new ObjectMapper();

    /**
     * @param token the auth_token
     * @return a map of all the users in this class and their permission
     */
    public Map<String, OhmagePermission> getUserList(String token) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("client", OhmageServer.CLIENT_STRING);
        data.put("auth_token", token);
        data.put("class_urn_list", this.urn);
        try {
            InputStream res = HttpRequest.post(server.getClassReadURL(), data, false).buffer();
            ObjectNode rootNode = mapper.readValue(res, ObjectNode.class);
            Iterator<Map.Entry<String, JsonNode>> userNodes = rootNode.get("data").get(this.urn).get("users").fields();
            HashMap<String, OhmagePermission> ret = new HashMap<String, OhmagePermission>();
            while (userNodes.hasNext()) {
                Map.Entry<String, JsonNode> userNode = userNodes.next();
                String user = userNode.getKey();
                OhmagePermission permission = (userNode.getValue().asText().equals("privileged")) ?
                        OhmagePermission.PRIVILEGED : OhmagePermission.RESTRICTED;

                ret.put(user, permission);
            }
            return ret;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public OhmageClass(String id, String name, OhmageServer server) {
        this.urn = id;
        this.name = name;
        this.server = server;
    }

    public OhmageClass() {
    }

    static public OhmageClass getIndividualClass() {
        return new OhmageClass(INDIVIDUAL_CLASS_NAME, INDIVIDUAL_CLASS_NAME, null);
    }
}
