package com.sparta2.springcore.controller;


import com.sparta2.springcore.dto.FolderCreateRequestDto;
import com.sparta2.springcore.exception.ApiException;
import com.sparta2.springcore.model.Folder;
import com.sparta2.springcore.model.Product;
import com.sparta2.springcore.security.UserDetailsImpl;
import com.sparta2.springcore.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class FolderController {
    // 멤버 변수 선언
    private final FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        // 멤버 변수 생성
        this.folderService = folderService;
    }

    // 회원이 등록한 모든 폴더 조회
    @GetMapping("/api/folders")
    public List<Folder> getFolders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return folderService.getFolders(userDetails.getUser());
    }

    // 회원이 폴더 추가
    @PostMapping("/api/folders")
    public List<Folder> addFolders(@RequestBody FolderCreateRequestDto folderCreateRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<String> folderNames = folderCreateRequestDto.getFolderNames();
        //유저 객체 자체를 넘김
        return folderService.createFolders(folderNames, userDetails.getUser());
    }


    // 회원의 폴더 별 관심상품 목록 조회
    @GetMapping("/api/folders/{folderId}/products")
    public Page<Product> getFolderProducts(
            @PathVariable Long folderId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        page = page - 1;
        return folderService.getFolderProduct(userDetails.getUser(), page , size, sortBy, isAsc,folderId);
    }
    //강의코드
    /*
    // 회원이 등록한 폴더 내 모든 상품 조회
    @GetMapping("/api/folders/{folderId}/products")
    public Page<Product> getProductsOnFolder(@PathVariable("folderId") Long folderId,
                                             @RequestParam("page") int page,
                                             @RequestParam("size") int size,
                                             @RequestParam("sortBy") String sortBy,
                                             @RequestParam("isAsc") boolean isAsc,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        page = page - 1;
        return folderService.getProductsOnFolder(userDetails.getUser(), page, size, sortBy, isAsc, folderId);
    }

     */
//    //공통적으로 이 컨트롤러에서 예외발생시 해당부분으로 들어올게
//    @ExceptionHandler({ IllegalArgumentException.class })
//    public ResponseEntity<Object> handle(IllegalArgumentException ex) {
//        // 처리는 여기서 해
//        ApiException apiException = new ApiException(
//                // 전달할 메시지
//                ex.getMessage(),
//                // HTTP 400 -> Client Error
//                // 클라이언트에서의 잘못된 인자로 인해 발생할 예상된 에러이다.
//                HttpStatus.BAD_REQUEST
//        );
//        // 이 결과를 클라이언트에게 보내줄게
//        // ResponseEntity : 응답을 해줌 - 클라이언트에게 전달된다.
//        return new ResponseEntity<>(
//                apiException,
//                HttpStatus.BAD_REQUEST ); }
}

