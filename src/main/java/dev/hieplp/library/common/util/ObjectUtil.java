package dev.hieplp.library.common.util;

import dev.hieplp.library.common.exception.UnknownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

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
}
