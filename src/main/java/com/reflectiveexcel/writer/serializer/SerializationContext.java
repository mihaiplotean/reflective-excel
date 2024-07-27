package com.reflectiveexcel.writer.serializer;

import com.reflectiveexcel.writer.WritingContext;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Class responsible for storing the serializers and the serialization process itself.
 */
public interface SerializationContext {

    /**
     * Serializes a given value to a value that will be written to the Excel cell.
     * If the returned type is not supported by Apache POI, {@link Object#toString()} will be used to convert the type
     * to a string representation. Those supported types can be found in {@link com.reflectiveexcel.writer.WritableCell#writeTo(Cell)}.
     *
     * @param clazz the type to be serialized.
     * @param value the value to be serialized.
     * @return the serialized value.
     */
    <T> Object serialize(WritingContext context, Class<T> clazz, T value);

    /**
     * Registers a serializer for a given type.
     *
     * @param clazz      the type the serializer is registered for.
     * @param serializer the serializer to be used for the given type.
     */
    <T> void registerSerializer(Class<T> clazz, CellSerializer<T> serializer);
}
