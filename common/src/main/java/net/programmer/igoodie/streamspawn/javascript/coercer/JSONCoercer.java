package net.programmer.igoodie.streamspawn.javascript.coercer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import java.util.Iterator;

public class JSONCoercer {

    public static class Object extends Coercer<JSONObject, NativeObject> {

        public static Object INSTANCE = new Object();

        @Override
        public Class<?> getId() {
            return JSONObject.class;
        }

        @Override
        public NativeObject coerceValue(JSONObject jsonObject, Scriptable scope) {
            try {
                NativeObject nativeObject = new NativeObject();
                nativeObject.setParentScope(scope);

                for (Iterator<?> it = jsonObject.keys(); it.hasNext(); ) {
                    java.lang.Object key = it.next();
                    java.lang.Object value = jsonObject.get(key.toString());

                    if (value instanceof JSONObject) {
                        value = this.coerceValue((JSONObject) value, scope);
                    } else if (value instanceof JSONArray) {
                        value = JSONCoercer.Array.INSTANCE.coerceValue((JSONArray) value, scope);
                    }

                    nativeObject.defineProperty(key.toString(), value, 0);
                }

                return nativeObject;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static class Array extends Coercer<JSONArray, NativeArray> {

        public static JSONCoercer.Array INSTANCE = new JSONCoercer.Array();

        @Override
        public Class<?> getId() {
            return JSONArray.class;
        }

        @Override
        public NativeArray coerceValue(JSONArray jsonArray, Scriptable scope) {
            java.lang.Object[] array = new java.lang.Object[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                java.lang.Object value;

                try {
                    value = jsonArray.get(i);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                if (value instanceof JSONObject) {
                    value = JSONCoercer.Object.INSTANCE.coerceValue((JSONObject) value, scope);
                } else if (value instanceof JSONArray) {
                    value = this.coerceValue((JSONArray) value, scope);
                }

                array[i] = value;
            }

            NativeArray nativeArray = new NativeArray(array);
            nativeArray.setParentScope(scope);

            return nativeArray;
        }

    }

}
