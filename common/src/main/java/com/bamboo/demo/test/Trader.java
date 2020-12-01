package com.bamboo.demo.test;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Trader {
    private final String name;
    private final String city;
}
