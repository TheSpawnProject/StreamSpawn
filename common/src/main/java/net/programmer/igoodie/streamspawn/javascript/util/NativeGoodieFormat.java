package net.programmer.igoodie.streamspawn.javascript.util;

import net.programmer.igoodie.goodies.exception.GoodieParseException;
import net.programmer.igoodie.goodies.exception.YetToBeImplementedException;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.TypeUtilities;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Undefined;

public class NativeGoodieFormat extends GoodieFormat<NativeObject, GoodieObject> {

    public static final NativeGoodieFormat INSTANCE = new NativeGoodieFormat();

    private NativeGoodieFormat() {}

    /* --------------------------------- */

    @Override
    public GoodieObject writeToGoodie(NativeObject nativeObject) {
        return convertNativeObject(nativeObject);
    }

    public static GoodieElement convert(Object scriptable) {
        if (scriptable instanceof NativeObject)
            return convertNativeObject(((NativeObject) scriptable));
        if (scriptable instanceof NativeArray)
            return convertNativeArray(((NativeArray) scriptable));
        if (scriptable instanceof Undefined)
            return GoodieNull.INSTANCE;
        if (TypeUtilities.isPrimitive(scriptable.getClass()))
            return GoodieElement.from(scriptable);

        return null;
    }

    public static GoodieObject convertNativeObject(NativeObject nativeObject) {
        GoodieObject goodieObject = new GoodieObject();

        for (Object id : nativeObject.getAllIds()) {
            Object value = nativeObject.get(id);

            String propertyName = id.toString();
            GoodieElement goodieElement = convert(value);

            goodieObject.put(propertyName, goodieElement);
        }

        return goodieObject;
    }

    public static GoodieArray convertNativeArray(NativeArray nativeArray) {
        GoodieArray goodieArray = new GoodieArray();

        for (Object element : nativeArray) {
            goodieArray.add(convert(element));
        }

        return goodieArray;
    }

    /* --------------------------------- */

    @Override
    public NativeObject readFromGoodie(GoodieObject goodieObject) {
        throw new YetToBeImplementedException();
    }

    /* --------------------------------- */

    @Override
    public String writeToString(NativeObject nativeObject, boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NativeObject readFromString(String s) throws GoodieParseException {
        throw new UnsupportedOperationException();
    }

}
