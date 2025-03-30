package net.programmer.igoodie.streamspawn.javascript.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Listeners<E, L> {

    protected Map<E, List<L>> listeners = new HashMap<>();

    public void forEach(BiConsumer<E, L> consumer) {
        listeners.forEach((event, listeners) ->
                listeners.forEach(listener -> consumer.accept(event, listener)));
    }

    public List<L> getListeners(E event) {
        return listeners.computeIfAbsent(event, key -> new ArrayList<>());
    }

    public void subscribe(E event, L listener) {
        getListeners(event).add(listener);
    }

    public void unsubscribe(E event, L listener) {
        getListeners(event).remove(listener);
    }

    public void unsubscribeAll(E event) {
        getListeners(event).clear();
    }

    public void invoke(E event, Consumer<L> invoker) {
        getListeners(event).forEach(invoker);
    }

    public interface GenericListener {
        void call(Object... args);
    }

}
