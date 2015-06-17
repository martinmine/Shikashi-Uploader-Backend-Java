package me.shikashi.img.representations;

import org.restlet.ext.gson.GsonRepresentation;

import java.lang.annotation.Annotation;

/**
 * Creates a GSON representation for generic types.
 */
public final class RepresentationFactory {
    private RepresentationFactory() {
    }

    /**
     * Creates a gson representation for a class and filters out fields that
     * should not be present.
     * 
     * @param subject
     *            Object to serialize to gson.
     * @param filterAnnotation
     *            The annotation type which selects fields to serialize.
     * @return A gson representation of the object.
     */
    public static <T> GsonRepresentation<T> makeRepresentation(T subject, Class<? extends Annotation> filterAnnotation) {
        GsonRepresentation<T> representation = new GsonRepresentation<>(subject);
        GenericFilteringStrategy filter = new GenericFilteringStrategy(filterAnnotation);
        representation.getBuilder().setDateFormat("yyyy-MM-dd'T'HH:mmZ");
        representation.getBuilder().setExclusionStrategies(filter);
        representation.getBuilder().registerTypeAdapterFactory(new MethodSerializerAdapterFactory(filterAnnotation));
        return representation;
    }
}