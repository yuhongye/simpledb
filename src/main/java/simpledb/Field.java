package simpledb;

import java.io.*;

/**
 * Interface for values of fields in tuples in SimpleDB.
 */
public interface Field {
    /**
     * Write the bytes representing this field to the specified DataOutputStream.
     * @see DataOutputStream
     * @param dos The DataOutputStream to write to.
     */
    void serialize(DataOutputStream dos) throws IOException;

    /**
     * Compare the value of this field object to the passed in value.
     * @param op The operator
     * @param value The value to compare this Field to
     * @return Whether or not the comparison yields true.
     */
    boolean compare(Predicate.Op op, Field value);

    /**
     * Returns the type of this field (see {@link Type#INT_TYPE} or {@link Type#FIXED_LENGTH_STRING_TYPE}
     * @return type of this field
     */
    Type getType();
    
    /**
     * Hash code.
     * Different Field objects representing the same value should probably
     * return the same hashCode.
     */
    @Override
    int hashCode();

    @Override
    boolean equals(Object field);

    @Override
    String toString();
}
