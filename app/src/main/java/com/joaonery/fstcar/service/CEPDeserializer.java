package com.joaonery.fstcar.service;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.joaonery.fstcar.model.Address;


import java.lang.reflect.Type;

public class CEPDeserializer implements JsonDeserializer{

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if(json.getAsJsonObject() != null){
            JsonElement element = json.getAsJsonObject();
            return (new Gson().fromJson(element, Address.class));
        }
        return null;
    }
}

