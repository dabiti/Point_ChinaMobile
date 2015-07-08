package com.point.web.util;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


/**
 * @Title:Gson工具类
 * @Description:
 * @Since: 2015年7月06日上午11:17:35
 * @author wangchunlong
 */
public class GsonTool {
	
    public static String objectToJsonDateSerializer(Object ts,    
            final String dateformat) {    
        String jsonStr = null;    
        Gson gson = new GsonBuilder()    
                .registerTypeHierarchyAdapter(Date.class,    
                        new JsonSerializer<Date>() {    
                            public JsonElement serialize(Date src,    
                                    Type typeOfSrc,    
                                    JsonSerializationContext context) {    
                                SimpleDateFormat format = new SimpleDateFormat(    
                                        dateformat);    
                                return new JsonPrimitive(format.format(src));    
                            }    
                        }).setDateFormat(dateformat).create();    
        if (gson != null) {    
            jsonStr = gson.toJson(ts);    
        }    
        return jsonStr;    
    }    
    
    
    @SuppressWarnings("unchecked")    
    public static <T> T jsonToBeanDateSerializer(String jsonStr, Class<T> cl,    
            final String pattern) {    
        Object obj = null;    
        Gson gson = new GsonBuilder()    
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {    
                    public Date deserialize(JsonElement json, Type typeOfT,    
                            JsonDeserializationContext context)    
                            throws JsonParseException {    
                        SimpleDateFormat format = new SimpleDateFormat(pattern);    
                        String dateStr = json.getAsString();    
                        try {    
                            return format.parse(dateStr);    
                        } catch (ParseException e) {    
                            e.printStackTrace();    
                        }    
                        return null;    
                    }    
                }).setDateFormat(pattern).create();    
        if (gson != null) {    
            obj = gson.fromJson(jsonStr, cl);    
        }    
        return (T) obj;    
    }    
}

