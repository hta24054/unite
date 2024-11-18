package com.hta2405.unite.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hta2405.unite.enums.DocType;

import java.lang.reflect.Type;

public class DocTypeAdapter implements JsonSerializer<DocType> {

    @Override
    public JsonElement serialize(DocType src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getType());
    }
}