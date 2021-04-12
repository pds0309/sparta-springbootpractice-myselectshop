package com.sparta2.springcore.service;


import com.sparta2.springcore.model.Folder;
import com.sparta2.springcore.model.Product;
import com.sparta2.springcore.model.User;
import com.sparta2.springcore.repository.FolderRepository;
import com.sparta2.springcore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FolderService {
    // 멤버 변수 선언
    private final FolderRepository folderRepository;

    //
    private final ProductRepository productRepository;

    // 생성자: ProductFolderService() 가 생성될 때 호출됨
    @Autowired
    public FolderService(FolderRepository folderRepository , ProductRepository productRepository) {
        // 멤버 변수 생성
        this.folderRepository = folderRepository;

        this.productRepository = productRepository;
    }

    // 회원 ID 에 대한 해당 회원의 등록된 모든 폴더 조회
    public List<Folder> getFolders(User user) {
        return folderRepository.findAllByUser(user);
    }



    public List<Folder> createFolders(List<String> folderNameList, User user) {

        folderNameList = folderNameList.stream().distinct().collect(Collectors.toList());

        List<Folder> folderList = new ArrayList<>();
        List<Folder> existList = folderRepository.findByUserAndNameIsIn(user,folderNameList);
        List<String> existName = new ArrayList<>();

        for(int i = 0 ; i < existList.size(); i ++){
            existName.add(existList.get(i).getName() );
        }
        for (String folderName : folderNameList) {
            Folder folder = new Folder(folderName, user);
            // 해당 유저의 폴더들 중 없는 이름일 때만 추가해준다.
            if(!existName.contains(folderName)){
                folderList.add(folder);
            }
        }
        folderList = folderRepository.saveAll(folderList);
        return folderList;
    }

    //폴더별 관심상품 조회
    public Page<Product> getFolderProduct(User user, int page,
                                          int size, String sortBy, boolean isAsc, Long folderId){
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> list = productRepository.findAllByUserIdAndFolderList_Id(user.getId(),folderId,pageable) ;
        return list;
    }
/* 강의 코드
// 회원 ID 가 소유한 폴더에 저장되어 있는 상품들 조회
    public Page<Product> getProductsOnFolder(User user, int page, int size, String sortBy, boolean isAsc, Long folderId) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAllByUserIdAndFolderList_Id(user.getId(), folderId, pageable);
    }
 */
}


