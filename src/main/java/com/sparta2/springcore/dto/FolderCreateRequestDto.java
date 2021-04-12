package com.sparta2.springcore.dto;


import lombok.Getter;
import java.util.List;

//폴더를 만들 때 요청하는 DTO
// 리스트로 폴더들을 받음
@Getter
public class FolderCreateRequestDto {
    List<String> folderNames;
}