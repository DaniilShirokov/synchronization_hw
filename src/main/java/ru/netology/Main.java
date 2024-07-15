package ru.netology;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        int numberOfRoutes = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfRoutes);

        for (int i = 0; i < numberOfRoutes; i++) {
            executor.submit(() -> {
                String route = generateRoute("RLRFR", 100);
                int countR = countOccurrences(route, 'R');
                synchronized (sizeToFreq) {
                    sizeToFreq.put(countR, sizeToFreq.getOrDefault(countR, 0) + 1);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        int mostFrequentCount = sizeToFreq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
        int mostFrequentCountFrequency = sizeToFreq.get(mostFrequentCount);

        System.out.println("Самое частое количество повторений " + mostFrequentCount + " (встретилось " + mostFrequentCountFrequency + " раз)");
        System.out.println("Другие размеры:");
        sizeToFreq.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    if (entry.getKey() != mostFrequentCount) {
                        System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
                    }
                });
    }


    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countOccurrences(String str, char ch) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == ch) {
                count++;
            }
        }
        return count;
    }
}