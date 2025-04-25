package ru.sirius.hl.service;

import org.springframework.beans.BeanUtils;
import java.beans.PropertyDescriptor;
import java.util.Arrays;



public class BeanUtilsHelper {

    public static String[] getNullPropertyNames(Object source) {
        return Arrays.stream(BeanUtils.getPropertyDescriptors(source.getClass()))
                .filter(pd -> {
                    try {
                        return pd.getReadMethod().invoke(source) == null;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .map(PropertyDescriptor::getName)
                .toArray(String[]::new);
    }
}