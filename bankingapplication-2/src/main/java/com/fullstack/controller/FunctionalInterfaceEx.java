package com.fullstack.controller;

import java.security.SecureRandom;

public class FunctionalInterfaceEx {

    static void main() {

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 1; i <= 100000; i++){
            IO.println(1000 + secureRandom.nextInt(9000));
        }

    }
}
