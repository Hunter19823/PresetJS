package pie.ilikepiefoo.presetjs.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public record ConditionalProcedure<T, R>(
    Predicate<T> condition,
    @Nullable
    Function<T, R> alternativeProcedure,
    NullHandleStrategy nullHandleStrategy
) implements ContextualBuilder<T, R> {
    public ConditionalProcedure {
        // Default to always true condition
        if (condition == null) {
            condition = context -> true;
        }
        // Default to no alternative procedure
        if (nullHandleStrategy == null) {
            nullHandleStrategy = NullHandleStrategy.ON_NULL_FALLBACK_TO_DEFAULT;
        }
    }

    public ConditionalProcedure(@Nullable Function<T, R> alternativeProcedure, NullHandleStrategy nullHandleStrategy) {
        this(null, alternativeProcedure, nullHandleStrategy);
    }

    public static <T, U, R> ConditionalProcedure<T, R> of(Function<? super @NotNull T, ? extends @Nullable U> mapper, Predicate<? super @NotNull U> condition, Function<@NotNull U, ? extends @Nullable R> alternativeProcedure, NullHandleStrategy nullHandleStrategy) {
        return new ConditionalProcedure<>(
            context -> Optional.ofNullable(context).map(mapper).map(condition::test).orElse(false),
            context -> Optional.ofNullable(context).map(mapper).map(alternativeProcedure).orElse(null),
            nullHandleStrategy
        );
    }

    public static <T, U, R> ConditionalProcedure<T, R> of(Function<@NotNull T, @Nullable U> mapper, Function<@NotNull U, @Nullable R> alternativeProcedure, NullHandleStrategy nullHandleStrategy) {
        return new ConditionalProcedure<>(
            context -> Optional.ofNullable(context).map(mapper).map(alternativeProcedure).orElse(null),
            nullHandleStrategy
        );
    }

    public static <T, U, R> ConditionalProcedure<T, R> fallbackWhenNull(Function<? super @NotNull T, ? extends @Nullable U> mapper, Predicate<? super @NotNull U> condition, Function<@NotNull U, ? extends @Nullable R> alternativeProcedure) {
        return new ConditionalProcedure<>(
            context -> Optional.ofNullable(context).map(mapper).map(condition::test).orElse(false),
            context -> Optional.ofNullable(context).map(mapper).map(alternativeProcedure).orElse(null),
            NullHandleStrategy.ON_NULL_FALLBACK_TO_DEFAULT
        );
    }

    public static <T, U, R> ConditionalProcedure<T, R> returnNull(Function<? super @NotNull T, ? extends @NotNull U> mapper, Predicate<? super @NotNull U> condition, Function<@NotNull U, ? extends @Nullable R> alternativeProcedure) {
        return new ConditionalProcedure<>(
            context -> Optional.ofNullable(context).map(mapper).map(condition::test).orElse(false),
            context -> Optional.ofNullable(context).map(mapper).map(alternativeProcedure).orElse(null),
            NullHandleStrategy.ON_NULL_RETURN_NULL
        );
    }

    public static <T, R> ConditionalProcedure<T, R> of(Predicate<? super @NotNull T> condition, Function<? super @NotNull T, ? extends @Nullable R> alternativeProcedure, NullHandleStrategy nullHandleStrategy) {
        return new ConditionalProcedure<>(
            context -> Optional.ofNullable(context).map(condition::test).orElse(false),
            context -> Optional.ofNullable(context).map(alternativeProcedure).orElse(null),
            nullHandleStrategy
        );
    }

    public ConditionalProcedure<T, R> and(Predicate<? super T> additionalCondition) {
        return new ConditionalProcedure<>(condition.and(additionalCondition), alternativeProcedure, nullHandleStrategy);
    }

    public ConditionalProcedure<T, R> or(Predicate<? super T> additionalCondition) {
        return new ConditionalProcedure<>(condition.or(additionalCondition), alternativeProcedure, nullHandleStrategy);
    }

    public ConditionalProcedure<T, R> not() {
        return new ConditionalProcedure<>(condition.negate(), alternativeProcedure, nullHandleStrategy);
    }

    public ConditionalProcedure<T, R> andNot(Predicate<? super T> additionalCondition) {
        return new ConditionalProcedure<>(condition.and(additionalCondition.negate()), alternativeProcedure, nullHandleStrategy);
    }

    public ConditionalProcedure<T, R> orNot(Predicate<? super T> additionalCondition) {
        return new ConditionalProcedure<>(condition.or(additionalCondition.negate()), alternativeProcedure, nullHandleStrategy);
    }

    public <U> ConditionalProcedure<T, R> and(Function<? super T, U> mapper, Predicate<? super U> mappedCondition) {
        return new ConditionalProcedure<>(condition.and(t -> mappedCondition.test(mapper.apply(t))), alternativeProcedure, nullHandleStrategy);
    }

    public <U> ConditionalProcedure<T, R> or(Function<? super T, U> mapper, Predicate<? super U> mappedCondition) {
        return new ConditionalProcedure<>(condition.or(t -> mappedCondition.test(mapper.apply(t))), alternativeProcedure, nullHandleStrategy);
    }

    @Override
    @NotNull
    public R build(@NotNull T buildContext, @NotNull R defaultReturnValue) {
        return nullHandleStrategy.build(this, buildContext, defaultReturnValue);
    }

    public enum NullHandleStrategy {
        ON_NULL_FALLBACK_TO_DEFAULT,
        ON_NULL_RETURN_NULL,
        ON_NULL_THROW_EXCEPTION
        ;

        public <T, R> R build(ConditionalProcedure<T, ? extends R> procedure, T context, R defaultReturnValue) {
            return switch (this) {
                case ON_NULL_FALLBACK_TO_DEFAULT -> {
                    if (procedure.alternativeProcedure == null) {
                        yield defaultReturnValue;
                    }
                    if (!procedure.condition.test(context)) {
                        yield defaultReturnValue;
                    }
                    R result = procedure.alternativeProcedure.apply(context);
                    yield result != null ? result : defaultReturnValue;
                }
                case ON_NULL_RETURN_NULL -> {
                    if (procedure.alternativeProcedure == null) {
                        yield defaultReturnValue;
                    }
                    if (!procedure.condition.test(context)) {
                        yield defaultReturnValue;
                    }
                    yield procedure.alternativeProcedure.apply(context);
                }
                case ON_NULL_THROW_EXCEPTION -> {
                    if (procedure.alternativeProcedure == null) {
                        throw new IllegalStateException("Alternative procedure is null");
                    }
                    if (!procedure.condition.test(context)) {
                        yield defaultReturnValue;
                    }
                    R result = procedure.alternativeProcedure.apply(context);
                    if (result == null) {
                        throw new IllegalStateException("Alternative procedure returned null");
                    }
                    yield result;
                }
            };
        }
    }
}
