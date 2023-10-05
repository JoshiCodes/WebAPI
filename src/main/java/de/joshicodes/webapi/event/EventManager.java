package de.joshicodes.webapi.event;

import com.sun.net.httpserver.HttpServer;
import de.joshicodes.webapi.Webserver;
import de.joshicodes.webapi.event.events.Event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class EventManager {

    private final Webserver server;
    private final HttpServer httpServer;

    private final List<ListenerAdapter> listeners;

    public EventManager(Webserver server, HttpServer httpServer) {
        this.server = server;
        this.httpServer = httpServer;
        listeners = new ArrayList<>();
    }

    public void registerListener(ListenerAdapter listener) {
        listeners.add(listener);
    }

    public void unregisterListener(ListenerAdapter listener) {
        listeners.remove(listener);
    }

    public void callEvent(Event event) {
        HashMap<ListenerAdapter, HashMap<Method, EventHandler.EventPriority>> methodsToCall = new HashMap<>();
        for (ListenerAdapter listener : listeners) {
            Method[] methods = listener.getClass().getDeclaredMethods();
            HashMap<Method, EventHandler.EventPriority> methodsToCallForListener = new HashMap<>();
            for (Method method : methods) {
                if (method.isAnnotationPresent(EventHandler.class)) {
                    if (method.getParameterCount() == 1) {
                        // Check if the parameter is an instance of Event (or a subclass of Event)
                        if (Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                            // Calls if the parameter is an instance of Event (or a subclass of Event)
                            EventHandler eventHandler = method.getAnnotation(EventHandler.class);
                            methodsToCallForListener.put(method, eventHandler.priority());
                        }
                    }
                }
            }
            methodsToCall.put(listener, methodsToCallForListener);
        }
        // Sort the methods by their priority
        HashMap<Method, Object[]> methodsToSort = new HashMap<>();
        for (ListenerAdapter listener : methodsToCall.keySet()) {
            for (Method method : methodsToCall.get(listener).keySet()) {
                methodsToSort.put(method, new Object[]{listener, methodsToCall.get(listener).get(method)});
            }
        }
        HashMap<Method, ListenerAdapter> methodsToCallSorted = new HashMap<>();
        methodsToSort.entrySet().stream().sorted((o1, o2) -> {
            EventHandler.EventPriority priority1 = (EventHandler.EventPriority) o1.getValue()[1];
            EventHandler.EventPriority priority2 = (EventHandler.EventPriority) o2.getValue()[1];
            return Integer.compare(priority1.getValue(), priority2.getValue());
        }).forEachOrdered(method -> methodsToCallSorted.put(method.getKey(), (ListenerAdapter) method.getValue()[0]));

        // Call the methods
        for (Method method : methodsToCallSorted.keySet()) {
            try {
                method.setAccessible(true);
                method.invoke(methodsToCallSorted.get(method), event);
            } catch (Exception e) {
                Logger.getGlobal().warning("An error occurred while calling an event: " + e.getMessage());
            }
        }
    }

}
