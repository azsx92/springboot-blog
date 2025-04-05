package com.springboot.blog;

import org.junit.jupiter.api.*;

public class JunitTest {
    @DisplayName("1 + 2는 3이다")
    @Test
    public void junitTest() {
        int a = 1;
        int b = 2;
        int sum = 3;

        Assertions.assertEquals(a + b, sum);
    }
}
