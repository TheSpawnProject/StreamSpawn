package net.programmer.igoodie.javascript.network;

import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSGetter;

public class TestWebsocket extends ScriptableObject {

    @Override
    public String getClassName() {
        return "Websocket";
    }

    public TestWebsocket() {}

    @JSConstructor
    public TestWebsocket(String url) {
        System.out.println("Websocket: " + url);
    }

    @JSGetter
    public int getCount() {
        return 1;
    }

}
