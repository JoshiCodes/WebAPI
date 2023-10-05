package de.joshicodes.webapi.event.events;

import de.joshicodes.webapi.Webserver;

public abstract class Event {

    private final Webserver server;

    private final boolean canBeCancelled;

    private boolean cancelled;

    public Event(Webserver server, boolean canBeCancelled) {
        this.server = server;
        this.canBeCancelled = canBeCancelled;
        cancelled = false;
    }

    public Event(Webserver server) {
        this(server, false);
    }

    public void setCancelled(boolean cancelled) {
        if(!canBeCancelled) throw new UnsupportedOperationException("This event cannot be cancelled!");
        this.cancelled = cancelled;
    }

    public Webserver getServer() {
        return server;
    }

    public boolean canBeCancelled() {
        return canBeCancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
