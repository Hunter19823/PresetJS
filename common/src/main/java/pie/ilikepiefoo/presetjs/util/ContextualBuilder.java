package pie.ilikepiefoo.presetjs.util;

@FunctionalInterface
public interface ContextualBuilder<C, R> {
    public R build(C context, R defaultReturnValue);
}
