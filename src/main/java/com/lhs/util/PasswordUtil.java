package com.lhs.util;

import java.util.Random;

// 비밀번호 찾기시 6자리 임의 숫자
// Passwordchick 메서드
public class PasswordUtil {
    public static String generateRandomPassword() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10)); // 0부터 9까지의 숫자 중 하나를 랜덤으로 선택하여 추가
        }
        return sb.toString();
    }
}
