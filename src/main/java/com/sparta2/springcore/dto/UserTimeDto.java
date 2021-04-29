package com.sparta2.springcore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class UserTimeDto {
    String username;
    long totalTime;
}