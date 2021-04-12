package com.sparta2.springcore.repository;


import com.sparta2.springcore.model.Folder;
import com.sparta2.springcore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findAllByUser(User user);

    // 하나의 유저에서 동일한 이름의 폴더가 여러개 생성되어서는 안된다.

    List<Folder> findByUserAndNameIsIn(User user , List<String> folderNameList);
}