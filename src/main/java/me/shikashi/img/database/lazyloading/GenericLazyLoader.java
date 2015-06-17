package me.shikashi.img.database.lazyloading;

/**
 * Defines which members each lazy loader must support.
 */
public interface GenericLazyLoader<T> {
    public T assertLoaded(T proxy);
}
