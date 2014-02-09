package cs.java.json;

import static cs.java.lang.Lang.*;

public abstract class JsonArrayImplBase extends JSONTypeImpl implements JSONArray {
    public JsonArrayImplBase(Object value) {
        super(value);
    }

    @Override
		public void add(boolean value) {
        add(json().create(value));
    }

    @Override
		public void add(double value) {
        add(json().create(value));
    }

    @Override
		public void add(String value) {
        add(json().create(value));
    }

    @Override
		public JSONArray getArray(int index) {
        JSONType value = get(index);
        if (is(value)) {
            JSONArray typevalue = value.asArray();
            if (is(typevalue)) return typevalue;
            throw exception("Expected JSONArray, found ", value.getValue());
        }
        return null;
    }

    @Override
		public Boolean getBoolean(int index) {
        JSONType value = get(index);
        if (is(value)) {
            JSONBoolean typevalue = value.asJSONBoolean();
            if (is(typevalue)) return typevalue.get();
            throw exception("Expected Boolean, found ", value.getValue());
        }
        return null;
    }

    @Override
		public Number getNumber(int index) {
        JSONType value = get(index);
        if (is(value)) {
            JSONNumber typevalue = value.asJSONNumber();
            if (is(typevalue)) return typevalue.get();
            throw exception("Expected Number, found ", value.getValue());
        }
        return null;
    }

    @Override
		public JSONObject getObject(int index) {
        JSONType value = get(index);
        if (is(value)) {
            JSONObject typevalue = value.asObject();
            if (is(typevalue)) return typevalue;
            throw exception("Expected JSONObject, found ", value.getValue());
        }
        return null;
    }

    @Override
		public String getString(int index) {
        JSONType value = get(index);
        if (is(value)) {
            JSONString typevalue = value.asJSONString();
            if (is(typevalue)) return typevalue.get();
            throw exception("Expected String, found ", value.getValue());
        }
        return null;
    }

}