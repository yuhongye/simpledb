package simpledb;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Stream;

/**
 * TupleDesc describes the schema of a tuple.
 */
@Slf4j
public class TupleDesc {
    private List<FieldDesc> fields;

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields
     * fields, with the first td1.numFields coming from td1 and the remaining
     * from td2.
     * @param td1 The TupleDesc with the first fields of the new TupleDesc
     * @param td2 The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc combine(TupleDesc td1, TupleDesc td2) {
        List<FieldDesc> newFields = new ArrayList<>(td1.fields.size() + td2.fields.size());
        newFields.addAll(td1.fields);
        newFields.addAll(td2.fields);
        return new TupleDesc(newFields);
    }

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     *
     * @param typeAr array specifying the number of and types of fields in
     *        this TupleDesc. It must contain at least one entry.
     * @param fieldAr array specifying the names of the fields. Note that names may be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        Preconditions.checkArgument(typeAr != null && Stream.of(typeAr).anyMatch(Objects::nonNull));
        Preconditions.checkArgument(fieldAr == null || fieldAr.length == typeAr.length);

        List<FieldDesc> fieldDescs = new ArrayList<>(typeAr.length);
        for (int i = 0; i < typeAr.length; i++) {
            fieldDescs.add(new FieldDesc(typeAr[i], fieldAr == null ? null : fieldAr[i]));
        }
        this.fields = fieldDescs;
        log.debug("Construct tuple desc, fields desc: {}", fields);
    }

    /**
     * Constructor.
     * Create a new tuple desc with typeAr.length fields with fields of the
     * specified types, with anonymous (unnamed) fields.
     *
     * @param typeAr array specifying the number of and types of fields in
     *        this TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        this(typeAr, null);
    }

    private TupleDesc(List<FieldDesc> fieldDescs) {
        this.fields = fieldDescs;
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        return fields.size();
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     *
     * @param i index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        if (i >= fields.size()) {
            throw new NoSuchElementException("Only has " + fields.size() + " fields, but get " + i);
        }
        return fields.get(i).name;
    }

    /**
     * Find the index of the field with a given name.
     *
     * @param name name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException if no field with a matching name is found.
     * @throws NullPointerException if name is null
     */
    public int nameToId(String name) throws NoSuchElementException {
        Preconditions.checkNotNull(name);
        for (int i = 0; i < fields.size(); i++) {
            if (name.equals(fields.get(i).name)) {
                return i;
            }
        }
        throw new NoSuchElementException("Cannot found field of name: " + name);
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     *
     * @param i The index of the field to get the type of. It must be a valid index.
     * @return the type of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public Type getType(int i) throws NoSuchElementException {
        if (i >= fields.size()) {
            throw new NoSuchElementException("Only has " + fields.size() + " fields, but get " + i);
        }
        return fields.get(i).type;
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     * Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        return fields.stream().map(f -> f.type).mapToInt(Type::getLen).sum();
    }

    /**
     * Compares the specified object with this TupleDesc for equality.
     * Two TupleDescs are considered equal if they are the same size and if the
     * n-th type in this TupleDesc is equal to the n-th type in td.
     *
     * @param o the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (! (o instanceof TupleDesc)) {
            return false;
        }

        TupleDesc other = (TupleDesc) o;
        if (this.fields.size() != other.fields.size()) {
            return false;
        }

        for (int i = 0; i < this.fields.size(); i++) {
            if (getType(i) != other.getType(i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * @return String describing this descriptor.
     */
    @Override
    public String toString() {
        return fields.toString();
    }

    @AllArgsConstructor
    private static class FieldDesc {
        Type type;
        // may be null, means the field with anonymous (unnamed)
        String name;

        @Override
        public String toString() {
            return type.toString() + "(" + (name == null ? "null" : name) + ")";
        }
    }
}
