package de.joshicodes.webapi.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    EventPriority priority() default EventPriority.NORMAL;


    static enum EventPriority {
        LOWEST(0),
        LOW(1),
        NORMAL(2),
        HIGH(3),
        HIGHEST(4);

        private final int value;

        private EventPriority(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
