package tv.quaint.events;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.events.components.BaseEvent;
import tv.quaint.events.components.FunctionedCall;
import tv.quaint.objects.handling.IEventable;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * A list of event handlers, stored per-event.
 */
public class BaseEventHandler {
    @Getter @Setter
    private static ConcurrentSkipListSet<FunctionedCall<BaseEvent>> functions = new ConcurrentSkipListSet<>();

    @Getter @Setter
    private static ConcurrentSkipListMap<Integer, BaseEventListener> listeners = new ConcurrentSkipListMap<>();

    @Getter @Setter
    private static ConcurrentHashMap<Class<? extends BaseEvent>, ConcurrentSkipListSet<RegisteredListener<?>>> regularEvents = new ConcurrentHashMap<>();

    public static String getMethodNamed(Method method) {
        return method.getDeclaringClass().getSimpleName() + "::" + method.getName();
    }

    public static void put(Class<? extends BaseEvent> aClass, RegisteredListener<?>... registeredListener) {
        ConcurrentSkipListSet<RegisteredListener<?>> listeners = getRegularEvents().get(aClass);
        if (listeners == null) listeners = new ConcurrentSkipListSet<>();
        listeners.addAll(Arrays.stream(registeredListener).collect(Collectors.toList()));
        getRegularEvents().put(aClass, listeners);
    }

    public static void unput(Class<? extends BaseEvent> aClass, RegisteredListener<?>... registeredListener) {
        ConcurrentSkipListSet<RegisteredListener<?>> listeners = getRegularEvents().get(aClass);
        if (listeners == null) listeners = new ConcurrentSkipListSet<>();
        Arrays.stream(registeredListener).collect(Collectors.toList()).forEach(listeners::remove);
        getRegularEvents().put(aClass, listeners);
    }

    public static void bake(BaseEventListener listener, IEventable eventable) {
        BaseListenerLayout listenerLayout = new BaseListenerLayout(listener);
        listenerLayout.setUp(eventable).forEach((aClass, registeredListeners) -> {
            put(aClass, registeredListeners.toArray(new RegisteredListener<?>[0]));
        });
    }

    public static <T extends IEventable> void unbake(T eventable) {
        getRegularEvents().forEach((aClass, registeredListeners) -> {
            registeredListeners.forEach(registeredListener -> {
                if (registeredListener.getEventable().equals(eventable)) getRegularEvents().get(aClass).remove(registeredListener);
            });
        });
    }

    public static ConcurrentSkipListSet<RegisteredListener<?>> getRegularListeners(Class<? extends BaseEvent> event) {
        ConcurrentSkipListSet<RegisteredListener<?>> listeners = new ConcurrentSkipListSet<>();

        getRegularEvents().forEach((aClass, registeredListeners) -> {
            if (event.equals(aClass)) listeners.addAll(registeredListeners);
        });

        return listeners;
    }

    public static void fireEvent(BaseEvent event) {
        getRegularListeners(event.getClass()).forEach(registeredListener -> {
            try {
                registeredListener.callEvent(event);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        });

        getFunctions().forEach((function) -> {
            if (function.getClazz().isAssignableFrom(event.getClass())) function.fire(event);
        });
    }

    public static <T extends BaseEvent> void loadFunction(FunctionedCall<T> functionedCall) {
        getFunctions().add((FunctionedCall<BaseEvent>) functionedCall);
    }
}
