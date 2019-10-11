package com.githubquest.mauriciofeijo.githubquest.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;

import java.lang.reflect.Field;

public class HungarianFieldNamingStrategy implements FieldNamingStrategy
{
    @Override
    public String translateName(Field field)
    {
        String fieldName = FieldNamingPolicy.UPPER_CAMEL_CASE.translateName(field);
        if (fieldName.startsWith("M"))
        {
            fieldName = fieldName.substring(1,2).toLowerCase() + fieldName.substring(2);
        }
        return fieldName;
    }
}
