package net.programmer.igoodie.javascript.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.NativeObject;

import java.util.Iterator;

public class NativeJsObject extends NativeObject {

    public static NativeJsObject fromJSONObject(JSONObject jsonObject) {
        try {
            NativeJsObject nativeObject = new NativeJsObject();

            for (Iterator<?> it = jsonObject.keys(); it.hasNext(); ) {
                Object key = it.next();
                Object value = jsonObject.get(key.toString());

                if (value instanceof JSONObject) {
                    value = fromJSONObject((JSONObject) value);
                } else if (value instanceof JSONArray) {
                    value = NativeJsArray.fromJSONArray((JSONArray) value);
                }

                nativeObject.defineProperty(key.toString(), value, 0);
            }

            return nativeObject;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{ ");

        Object[] ids = this.getIds();
        for (int i = 0; i < ids.length; i++) {
            Object id = ids[i];
            if (i != 0) sb.append(", ");
            Object value = get(id);
            sb.append(id).append(": ");
            if(value instanceof String) sb.append('"');
            sb.append(value.toString());
            if(value instanceof String) sb.append('"');
        }

        sb.append(" }");
        return sb.toString();
    }

}
