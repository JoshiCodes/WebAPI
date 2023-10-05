package de.joshicodes.webapi.event;

import de.joshicodes.webapi.event.events.Event;

/**
 *  This interface is just to handle the events.
 *  To listen to a specific event, implement this interface and create a method with the {@link EventHandler} annotation.
 *  The name of the method doesn't matter, but the parameters must only be an instance of {@link Event}.
 *  <br><br>
 *  Inspired by the Minecraft Bukkit API.
 */
public interface ListenerAdapter {

    // Nothing to see here

}
