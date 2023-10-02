package dev.hieplp.library.common.util;

import dev.hieplp.library.common.exception.UnknownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ObjectUtil {
    public <T> List<T> copyProperties(List<?> source, Class<T> clazz) {
        try {
            log.info("Copy properties from {} to {}", source.getClass().getName(), clazz.getName());
            var target = new ArrayList<T>();
            for (var item : source) {
                var obj = clazz.getConstructor().newInstance();
                BeanUtils.copyProperties(item, obj);
                target.add(obj);
            }
            return target;
        } catch (Exception e) {
            log.error("Copy properties failed: {}", e.getMessage());
            throw new UnknownException("Copy properties failed");
        }

    }

    public <T> T parse(Object source, Class<T> type) {
        try {
            var response = type.getConstructor().newInstance();
            BeanUtils.copyProperties(source, response);
            return response;
        } catch (InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException e) {
            log.error(e.getMessage());
            throw new UnknownException(e.getMessage());
        }
    }
}
