package dev.hieplp.library.controller.admin;

import dev.hieplp.library.common.enums.response.SuccessCode;
import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.payload.request.author.CreateAuthorRequest;
import dev.hieplp.library.payload.request.author.UpdateAuthorRequest;
import dev.hieplp.library.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/authors")
@RequiredArgsConstructor
public class AdminAuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<CommonResponse> createAuthorByAdmin(@Valid @RequestBody CreateAuthorRequest request) {
        log.info("Create author with request: {}", request);
        var response = authorService.createAuthorByAdmin(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }


    @PatchMapping("/{authorId}")
    public ResponseEntity<CommonResponse> updateAuthorByAdmin(@PathVariable String authorId,
                                                              @Valid @RequestBody UpdateAuthorRequest request) {
        log.info("Update author with id: {} and request: {}", authorId, request);
        var response = authorService.updateAuthorByAdmin(authorId, request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<CommonResponse> getAuthorByAdmin(@PathVariable String authorId) {
        log.info("Get author with id: {}", authorId);
        var response = authorService.getAuthorByAdmin(authorId);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAuthorsByAdmin(GetListRequest request) {
        log.info("Get authors with request: {}", request);
        var response = authorService.getAuthorsByAdmin(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }
}
