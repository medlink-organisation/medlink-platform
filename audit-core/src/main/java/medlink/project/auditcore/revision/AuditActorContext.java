package medlink.project.auditcore.revision;

import medlink.project.auditcore.dto.ActorType;

import java.util.Optional;

/**
 * Context reserved for trusted Kafka/gRPC/workflow adapters. Values must only be
 * populated after the adapter has authenticated the caller; never from raw headers.
 */
public final class AuditActorContext {

    private static final ThreadLocal<Actor> ACTOR = new ThreadLocal<>();

    private AuditActorContext() {
    }

    public static Scope open(String userId, String username, ActorType actorType, String correlationId) {
        Actor previous = ACTOR.get();
        ACTOR.set(new Actor(userId, username, actorType, correlationId));
        return () -> {
            if (previous == null) {
                ACTOR.remove();
            } else {
                ACTOR.set(previous);
            }
        };
    }

    static Optional<Actor> current() {
        return Optional.ofNullable(ACTOR.get());
    }

    public record Actor(String userId, String username, ActorType actorType, String correlationId) {
    }

    @FunctionalInterface
    public interface Scope extends AutoCloseable {
        @Override
        void close();
    }
}
