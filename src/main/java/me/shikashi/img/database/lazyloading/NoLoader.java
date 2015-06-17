package me.shikashi.img.database.lazyloading;

/**
 * This lazy loader is so lazy it doesn't even do anything. The purpose of this class
 * it to only return the member being loaded as no database operations are required
 * for loading the object, as it is already loaded.
 */
public class NoLoader<T> implements GenericLazyLoader<T> {
    /**
     * Returns the object, as it is already loaded.
     * @param proxy Member to load.
     * @return The member loaded.
     */
    @Override
    public T assertLoaded(T proxy) {
        return proxy;
    }
}
