package com.bamboo.demo.test;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Transaction {

    private final Trader trader;
    private final int year;
    private final int value;
}
