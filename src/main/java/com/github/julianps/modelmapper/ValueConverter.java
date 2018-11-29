package com.github.julianps.modelmapper;

import io.vavr.Value;
import org.modelmapper.internal.typetools.TypeResolver;
import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.spi.PropertyInfo;

/**
 * @author jstuecker
 *
 * Implementation of a {@link ConditionalConverter} for {@link Value}
 */
class ValueConverter implements ConditionalConverter<Value, Value> {

    @Override
    public MatchResult match(Class<?> sourceType, Class<?> destinationType) {
        if (Value.class.isAssignableFrom(sourceType) &&
                Value.class.isAssignableFrom(destinationType)) {
            return MatchResult.FULL;
        } else {
            return MatchResult.NONE;
        }
    }

    @Override
    public Value convert(MappingContext<Value, Value> context) {
        final Value<?> source = (Value<?>) context.getSource();
        final PropertyInfo destInfo = context.getMapping().getLastDestinationProperty();
        final Class<?> destinationType = TypeResolver
                .resolveRawArgument(destInfo.getGenericType(), destInfo.getInitialType());
        return source
                .map(src -> context.create(src, destinationType))
                .map(ctx -> context.getMappingEngine().map(ctx));
    }
}
