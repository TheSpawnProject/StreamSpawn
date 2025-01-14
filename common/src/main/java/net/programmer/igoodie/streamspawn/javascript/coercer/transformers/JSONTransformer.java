package net.programmer.igoodie.streamspawn.javascript.coercer.transformers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;

import java.util.Iterator;

public class JSONTransformer {

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
