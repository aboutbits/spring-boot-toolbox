package it.aboutbits.springboot.toolbox.util;

import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import it.aboutbits.springboot.toolbox.type.identity.Identified;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.util.Streamable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NullMarked
public class MapByIdentity<ID extends EntityId<?>, E extends Identified<ID>> extends HashMap<ID, E> {
    public MapByIdentity(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MapByIdentity(int initialCapacity) {
        super(initialCapacity);
    }

    public MapByIdentity() {
        super();
    }

    public MapByIdentity(Map<? extends ID, ? extends E> m) {
        super(m);
    }

    public static <ID extends EntityId<?>, E extends Identified<ID>> MapByIdentity<ID, E> of(Collection<? extends E> items) {
        var map = new MapByIdentity<ID, E>(items.size());
        items.forEach(item -> map.put(item.getId(), item));
        return map;
    }

    public static <ID extends EntityId<?>, E extends Identified<ID>> MapByIdentity<ID, E> of(Stream<? extends E> items) {
        var collected = items.collect(Collectors.toMap(
                Identified::getId,
                Function.identity()
        ));

        return new MapByIdentity<>(collected);
    }

    public static <ID extends EntityId<?>, E extends Identified<ID>> MapByIdentity<ID, E> of(Streamable<? extends E> items) {
        return of(items.toList());
    }
}
