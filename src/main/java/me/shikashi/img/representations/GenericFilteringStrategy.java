package me.shikashi.img.representations;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.lang.annotation.Annotation;

/**
 * Utility class for gson that decides which fields to implement.
 */
public class GenericFilteringStrategy implements ExclusionStrategy {
    private Class<? extends Annotation> annotation;

    /**
     * Creates a new filtering strategy.
     * 
     * @param annotation
     *            Annotation that highlights the type that should be serialized.
     */
    public GenericFilteringStrategy(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(annotation) == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
