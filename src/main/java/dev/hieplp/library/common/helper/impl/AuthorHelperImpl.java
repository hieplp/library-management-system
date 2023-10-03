package dev.hieplp.library.common.helper.impl;

import dev.hieplp.library.common.entity.Author;
import dev.hieplp.library.common.enums.author.AuthorStatus;
import dev.hieplp.library.common.exception.NotFoundException;
import dev.hieplp.library.common.helper.AuthorHelper;
import dev.hieplp.library.common.util.ObjectUtil;
import dev.hieplp.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorHelperImpl implements AuthorHelper {

    private final AuthorRepository authorRepo;

    private final ObjectUtil objectUtil;

    @Override
    public Author getAuthor(String authorId) {
        log.info("Get author with authorId: {}", authorId);
        return authorRepo.findById(authorId)
                .orElseThrow(() -> {
                    var message = String.format("Author with authorId: %s not found", authorId);
                    log.warn(message);
                    return new NotFoundException(message);
                });
    }

    @Override
    public <T> T getAuthor(String authorId, Class<T> clazz) {
        log.info("Get author with authorId: {}", authorId);
        var author = getAuthor(authorId);
        return objectUtil.parse(author, clazz);
    }

    @Override
    public <T> T getActiveAuthor(String authorId, Class<T> clazz) {
        log.info("Get active author with authorId: {}", authorId);

        var author = getAuthor(authorId);
        if (AuthorStatus.ACTIVE.getStatus().equals(author.getStatus())) {
            return objectUtil.parse(author, clazz);
        }

        var message = String.format("Author with authorId: %s is not active", authorId);
        log.warn(message);
        throw new NotFoundException(message);
    }
}
