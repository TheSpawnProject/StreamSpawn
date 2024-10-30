package net.programmer.igoodie.javascript.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.NativeArray;

public class NativeJsArray extends NativeArray {

    public static NativeJsArray fromJSONArray(JSONArray jsonArray) {
        Object[] array = new Object[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            Object value;

            try {
                value = jsonArray.get(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            if (value instanceof JSONObject) {
                value = NativeJsObject.fromJSONObject((JSONObject) value);
            } else if (value instanceof JSONArray) {
                value = fromJSONArray((JSONArray) value);
            }

            array[i] = value;
        }

        return new NativeJsArray(array);
    }

    public NativeJsArray(Object[] arr) {
        super(arr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < this.size(); i++) {
            if (i != 0) sb.append(", ");
            Object value = get(i);
            if(value instanceof String) sb.append('"');
            sb.append(value);
            if(value instanceof String) sb.append('"');
        }
        sb.append("]");
        return sb.toString();
    }

}
