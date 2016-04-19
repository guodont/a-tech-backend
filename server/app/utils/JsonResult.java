package utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

public class JsonResult {

    private String type;

    private Object data;

    private String message;

    public JsonResult(String type) {
        this(type, null, null);
    }

    public JsonResult(String type, String message) {
        this(type, message, null);
    }

    public JsonResult(String type, String message, Object data) {
        this.type = type;
        this.data = data;
        this.message = message;
    }

    public ObjectNode toJsonResponse() {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.put(type, msg);
        return wrapper;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

}
