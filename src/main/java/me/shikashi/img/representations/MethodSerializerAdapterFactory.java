package me.shikashi.img.representations;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;

/**
 * Creates adapters for serializing objects with functions annotated with @ExposedMethod.
 */
public class MethodSerializerAdapterFactory implements TypeAdapterFactory {
    private final Class<? extends Annotation> annotation;

    public MethodSerializerAdapterFactory(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> tokenType) {
        final TypeAdapter<T> adapter = gson.getDelegateAdapter(this, tokenType);
        return new MethodSerializerTypeAdapter<>(adapter, annotation, gson);
    }
}