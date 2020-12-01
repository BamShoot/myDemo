package com.bamboo.demo.test;


import java.util.*;
import java.util.stream.Collectors;

public class Z {

    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );


        List<Transaction> c1 = transactions.stream()
                .filter(transaction -> transaction.getYear() == 2011)
                .sorted(Comparator.comparingInt(Transaction::getValue))
                .collect(Collectors.toList());

        Set<String> c2 = transactions.stream()
                .map(Transaction -> Transaction.getTrader().getCity())
                .collect(Collectors.toSet());

        List<Trader> c3 = transactions.stream()
                .map(Transaction::getTrader)
                .filter(Trader -> "Cambridge".equals(Trader.getCity()))
                .distinct()
                .sorted(Comparator.comparing(Trader::getName))
                .collect(Collectors.toList());

        String c4 = transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getName)
                .distinct().sorted()
                .collect(Collectors.joining());

        boolean c5 = transactions.stream()
                .map(Transaction::getTrader)
                .anyMatch(trader -> "Milan".equals(trader.getCity()));


        transactions.stream()
                .filter(Transaction -> "Cambridge".equals(Transaction.getTrader().getCity()))
                .map(Transaction::getValue)
                .forEach(System.out::println);

        Optional<Integer> c7 = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::max);

        transactions.stream()
                .sorted(Comparator.comparing(Transaction::getValue))
                .findFirst();

    }


}
