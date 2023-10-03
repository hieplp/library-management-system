package dev.hieplp.library.common.helper;

import dev.hieplp.library.common.entity.Author;

public interface AuthorHelper {
    Author getAuthor(String authorId);

    <T> T getAuthor(String authorId, Class<T> clazz);

    <T> T getActiveAuthor(String authorId, Class<T> clazz);
}
