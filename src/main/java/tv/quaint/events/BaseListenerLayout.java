package tv.quaint.events;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.events.components.BaseEvent;
import tv.quaint.events.processing.BaseProcessor;
import tv.quaint.events.processing.exception.BaseEventException;
import tv.quaint.events.processing.BaseEventExecutor;
import tv.quaint.objects.Eventable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Simple interface for tagging all EventListeners
 */
public class BaseListenerLayout {
    @Getter @Setter
    private int index;
    @Getter @Setter
    private List<Method> methods = new ArrayList<>();
    @Getter @Setter
    private BaseEventListener listener;

    public BaseListenerLayout(BaseEventListener listener) {
        setListener(listener);
        setIndex(BaseEventHandler.getListeners().size());
        BaseEventHandler.getListeners().put(getIndex(), getListener());
        setMethods(new ArrayList<>(getConcurrentMethods().values()));
    }

    public BaseListenerLayout(List<Method> methods) {
        setListener(listener);
        setIndex(BaseEventHandler.getListeners().size());
        BaseEventHandler.getListeners().put(getIndex(), getListener());
        setMethods(methods);
    }

    public ConcurrentSkipListMap<String, Method> getConcurrentMethods() {
        ConcurrentSkipListMap<String, Method> r = new ConcurrentSkipListMap<>();

        for (Method method : getListener().getClass().getMethods()) {
            r.put(method.getName(), method);
        }
        for (Method method : getListener().getClass().getDeclaredMethods()) {
            r.put(method.getName(), method);
        }

        return r;
    }

    public ConcurrentHashMap<Class<? extends BaseEvent>, ConcurrentSkipListSet<RegisteredListener<?>>> setUp(Eventable eventable) {
        ConcurrentHashMap<Class<? extends BaseEvent>, ConcurrentSkipListSet<RegisteredListener<?>>> ret = new ConcurrentHashMap<>();

        for (final Method method : getMethods()) {
            final BaseProcessor eh = method.getAnnotation(BaseProcessor.class);
            if (eh == null) continue;
            // Do not register bridge or synthetic methods to avoid event duplication
            // Fixes SPIGOT-893
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }
            final Class<?> checkClass;
            if (method.getParameterTypes().length != 1 || ! BaseEvent.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                continue;
            }
            final Class<? extends BaseEvent> eventClass = checkClass.asSubclass(BaseEvent.class);
            method.setAccessible(true);
            ConcurrentSkipListSet<RegisteredListener<?>> eventSet = ret.computeIfAbsent(eventClass, k -> new ConcurrentSkipListSet<>());

            for (Class<?> clazz = eventClass; BaseEvent.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
                // This loop checks for extending deprecated events
                if (clazz.getAnnotation(Deprecated.class) != null) {
                    break;
                }
            }

            BaseEventExecutor executor = (listener1, event) -> {
                try {
                    if (!eventClass.isAssignableFrom(event.getClass())) {
                        return;
                    }
                    method.invoke(listener1, event);
                } catch (InvocationTargetException ex) {
                    throw new BaseEventException(ex.getCause());
                } catch (Throwable t) {
                    throw new BaseEventException(t);
                }

                event.setCompleted(true);
            };

            eventSet.add(new RegisteredListener<>(getListener(), executor, eh.priority(), eventable, eh.ignoreCancelled()));
        }
        return ret;
    }
}
