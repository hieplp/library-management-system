package dev.hieplp.library.service;

import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.GetListResponse;
import dev.hieplp.library.payload.request.author.CreateAuthorRequest;
import dev.hieplp.library.payload.request.author.UpdateAuthorRequest;
import dev.hieplp.library.payload.response.author.AdminAuthorResponse;
import dev.hieplp.library.payload.response.author.UserAuthorResponse;

public interface AuthorService {
    // ------------------- ADMIN : Author -------------------
    AdminAuthorResponse createAuthorByAdmin(CreateAuthorRequest request);

    AdminAuthorResponse updateAuthorByAdmin(String authorId, UpdateAuthorRequest request);

    AdminAuthorResponse getAuthorByAdmin(String authorId);

    GetListResponse<AdminAuthorResponse> getAuthorsByAdmin(GetListRequest request);

    // ------------------- USER -------------------
    UserAuthorResponse getAuthorByUser(String authorId);

    GetListResponse<UserAuthorResponse> getAuthorsByUser(GetListRequest request);
}
