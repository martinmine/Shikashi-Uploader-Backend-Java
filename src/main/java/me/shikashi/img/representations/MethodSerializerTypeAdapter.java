package me.shikashi.img.representations;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.shikashi.img.representations.annotations.ExposedMethod;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Adds the return type of annotated functions, the return type of the function is
 * added together with the name of the function to the JSON object unless a name
 * is specified together with the annotation through the "value" field.
 */
public class MethodSerializerTypeAdapter<T> extends TypeAdapter<T> {
    private static final Logger LOGGER = Logger.getLogger(MethodSerializerAdapterFactory.class.getSimpleName());
    private final TypeAdapter<T> adapter;
    private final Class<? extends Annotation> annotation;
    private final Gson gson;

    /**
     * Creates a new type adapter.
     * @param adapter The type adapter.
     * @param annotation Annotation to look for members/functions to serialize.
     * @param gson The GSON object.
     */
    public MethodSerializerTypeAdapter(TypeAdapter<T> adapter, Class<? extends Annotation> annotation, Gson gson) {
        this.adapter = adapter;
        this.annotation = annotation;
        this.gson = gson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T read(JsonReader reader) throws IOException {
        return adapter.read(reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(JsonWriter writer, T value) throws IOException {
        JsonElement tree = adapter.toJsonTree(value);
        if (value != null && tree instanceof JsonObject) {
            for (Method method : value.getClass().getMethods()) {
                serializeMethodReturnValue(value, (JsonObject) tree, method);
            }
        }

        gson.getAdapter(JsonElement.class).write(writer, tree);
    }

    private void serializeMethodReturnValue(T value, JsonObject obj, Method method) {
        ExposedMethod methodAnnotation;
        if ((methodAnnotation = method.getAnnotation(ExposedMethod.class)) != null
                && method.getAnnotation(annotation) != null) {
            try {
                Object result = method.invoke(value);
                String fieldName = methodAnnotation.value().isEmpty() ? method.getName() : methodAnnotation.value();

                if (result instanceof Number) {
                    obj.addProperty(fieldName, (Number) result);
                } else if (result instanceof Boolean) {
                    obj.addProperty(fieldName, (Boolean) result);
                } else if (result instanceof Character) {
                    obj.addProperty(fieldName, (Character) result);
                } else {
                    obj.addProperty(fieldName, result.toString());
                }

            } catch (InvocationTargetException | IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }
}