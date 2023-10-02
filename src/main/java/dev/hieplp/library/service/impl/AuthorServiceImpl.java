package dev.hieplp.library.service.impl;

import dev.hieplp.library.common.entity.Author;
import dev.hieplp.library.common.enums.IdLength;
import dev.hieplp.library.common.enums.Nationality;
import dev.hieplp.library.common.enums.author.AuthorStatus;
import dev.hieplp.library.common.helper.AuthorHelper;
import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.GetListResponse;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.common.util.GeneratorUtil;
import dev.hieplp.library.common.util.SqlUtil;
import dev.hieplp.library.config.security.CurrentUser;
import dev.hieplp.library.payload.request.author.CreateAuthorRequest;
import dev.hieplp.library.payload.request.author.UpdateAuthorRequest;
import dev.hieplp.library.payload.response.author.AdminAuthorResponse;
import dev.hieplp.library.repository.AuthorRepository;
import dev.hieplp.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final CurrentUser currentUser;

    private final AuthorRepository authorRepo;

    private final AuthorHelper authorHelper;

    private final GeneratorUtil generatorUtil;
    private final DateTimeUtil dateTimeUtil;
    private final SqlUtil sqlUtil;

    @Override
    public AdminAuthorResponse createAuthorByAdmin(CreateAuthorRequest request) {
        log.info("Create author with request: {}", request);

        var nationality = Nationality.UNKNOWN;
        if (request.getNationality() != null) {
            nationality = Nationality.fromNationality(request.getNationality());
        }

        var authorId = generatorUtil.generateId(IdLength.AUTHOR_ID);
        var author = Author.builder()
                .authorId(authorId)
                .authorName(request.getAuthorName())
                .authorAvatar(request.getAuthorAvatar())
                .dateOfBirth(request.getDateOfBirth())
                .dateOfDeath(request.getDateOfDeath())
                .nationality(nationality.getValue())
                .biography(request.getBiography())
                .notableWorks(request.getNotableWorks())
                .status(AuthorStatus.ACTIVE.getStatus())
                .createdBy(currentUser.getUserId())
                .createdAt(dateTimeUtil.getCurrentTimestamp())
                .modifiedBy(currentUser.getUserId())
                .modifiedAt(dateTimeUtil.getCurrentTimestamp())
                .build();
        authorRepo.save(author);

        var response = new AdminAuthorResponse();
        BeanUtils.copyProperties(author, response);
        return response;
    }

    @Override
    public AdminAuthorResponse updateAuthorByAdmin(String authorId, UpdateAuthorRequest request) {
        log.info("Update author with id: {} and request: {}", authorId, request);

        var author = authorHelper.getAuthor(authorId);

        boolean isChanged = false;

        if (request.getAuthorName() != null && !request.getAuthorName().equals(author.getAuthorName())) {
            author.setAuthorName(request.getAuthorName());
            isChanged = true;
        }

        if (request.getAuthorAvatar() != null && !request.getAuthorAvatar().equals(author.getAuthorAvatar())) {
            author.setAuthorAvatar(request.getAuthorAvatar());
            isChanged = true;
        }

        if (request.getDateOfBirth() != null && !request.getDateOfBirth().equals(author.getDateOfBirth())) {
            author.setDateOfBirth(request.getDateOfBirth());
            isChanged = true;
        }

        if (request.getDateOfDeath() != null && !request.getDateOfDeath().equals(author.getDateOfDeath())) {
            author.setDateOfDeath(request.getDateOfDeath());
            isChanged = true;
        }

        if (request.getNationality() != null && !request.getNationality().equals(author.getNationality())) {
            var nationality = Nationality.fromNationality(request.getNationality());
            author.setNationality(nationality.getValue());
            isChanged = true;
        }

        if (request.getBiography() != null && !request.getBiography().equals(author.getBiography())) {
            author.setBiography(request.getBiography());
            isChanged = true;
        }

        if (request.getNotableWorks() != null && !request.getNotableWorks().equals(author.getNotableWorks())) {
            author.setNotableWorks(request.getNotableWorks());
            isChanged = true;
        }

        if (isChanged) {
            author
                    .setModifiedBy(currentUser.getUserId())
                    .setModifiedAt(dateTimeUtil.getCurrentTimestamp());
            authorRepo.save(author);
        }

        var response = new AdminAuthorResponse();
        BeanUtils.copyProperties(author, response);
        return response;
    }

    @Override
    public AdminAuthorResponse getAuthorByAdmin(String authorId) {
        log.info("Get author with id: {}", authorId);
        return authorHelper.getAuthor(authorId, AdminAuthorResponse.class);
    }

    @Override
    public GetListResponse<AdminAuthorResponse> getAuthorsByAdmin(GetListRequest request) {
        log.info("Get authors with request: {}", request);
        return sqlUtil.getList(request, authorRepo, AdminAuthorResponse.class);
    }
}
