package net.programmer.igoodie.streamspawn.javascript.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Undefined;

import java.util.Iterator;

public class NativeFormat {

    public static class JSON {

        public static NativeObject toObject(JSONObject jsonObject) {
            try {
                NativeObject nativeObject = new NativeObject();

                for (Iterator<?> it = jsonObject.keys(); it.hasNext(); ) {
                    Object key = it.next();
                    Object value = jsonObject.get(key.toString());

                    if (value instanceof JSONObject) {
                        value = toObject((JSONObject) value);
                    } else if (value instanceof JSONArray) {
                        value = toArray((JSONArray) value);
                    }

                    nativeObject.defineProperty(key.toString(), value, 0);
                }

                return nativeObject;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        public static NativeArray toArray(JSONArray jsonArray) {
            Object[] array = new Object[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                Object value;

                try {
                    value = jsonArray.get(i);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                if (value instanceof JSONObject) {
                    value = toObject((JSONObject) value);
                } else if (value instanceof JSONArray) {
                    value = toArray((JSONArray) value);
                }

                array[i] = value;
            }

            return new NativeArray(array);
        }

    }

    public static String toString(Object obj) {
        if (obj instanceof NativeObject) {
            NativeObject nativeObject = (NativeObject) obj;
            StringBuilder sb = new StringBuilder("{ ");

            Object[] ids = nativeObject.getIds();
            for (int i = 0; i < ids.length; i++) {
                Object id = ids[i];
                if (i != 0) sb.append(", ");
                Object value = nativeObject.get(id);
                sb.append(id).append(": ");
                if (value instanceof String) sb.append('"');
                sb.append(toString(value));
                if (value instanceof String) sb.append('"');
            }

            sb.append(" }");
            return sb.toString();
        }

        if (obj instanceof NativeArray) {
            NativeArray nativeArray = (NativeArray) obj;
            StringBuilder sb = new StringBuilder("[");

            for (int i = 0; i < nativeArray.size(); i++) {
                if (i != 0) sb.append(", ");
                Object value = nativeArray.get(i);
                if (value instanceof String) sb.append('"');
                sb.append(toString(value));
                if (value instanceof String) sb.append('"');
            }

            sb.append("]");
            return sb.toString();
        }

        if (obj instanceof Undefined)
            return "undefined";

        if (obj == null)
            return "null";

        return obj.toString();
    }

}
