package com.huihuang.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtils {

    private ObjectUtils(){};

    public static String object2String(Object o){
        String className = o.getClass().getName();
        StringBuilder builder = new StringBuilder(className);
        builder.append("[");
        Map<String, String> map = mappingFields(o);
        if (!map.isEmpty()){
            int index = 0;
            int size = map.size();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.append(entry.getKey());
                builder.append(":");
                builder.append(entry.getValue());
                if (++index != size){
                    builder.append(";");
                }
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private static Map<String, String> mappingFields(Object o){
        Map<String, String> result = new HashMap<String, String>();
        for (Field field : o.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                result.put(field.getName(), object2Str(field.get(o)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static String object2Str(Object object){
        if (object instanceof Date){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format((Date) object);
        }else if (object instanceof BigDecimal){
            return object.toString();
        }
        return String.valueOf(object);
    }

    public static Object String2Object(String value){
        if (null == value || "".equals(value)){
            return null;
        }
        String[] values = value.split("\\[");
        String[] fieldValues = value.substring(value.indexOf("[") + 1, value.indexOf("]")).split(";");
        String className = values[0];
        Object result = null;
        try {
            Class<?> clazz = Class.forName(className);
            result = clazz.newInstance();
            for (String fieldValue : fieldValues) {
                String[] val = fieldValue.split(":");
                Field field = clazz.getDeclaredField(fieldValue.split(":")[0]);
                field.setAccessible(true);
                field.set(result, typeConversion(field.getType(), val[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    private static Object typeConversion(Class<?> clazz,String value){
        if (clazz.equals(Date.class)){
            SimpleDateFormat sdf = new SimpleDateFormat();
            return sdf.format(value);
        }else if (clazz.equals(BigDecimal.class)){
            return new BigDecimal(value);
        }else if (clazz.equals(Integer.class)){
            return Integer.valueOf(value);
        }
        return value;
    }
}
