package org.astonsorting.service;

import org.astonsorting.collection.CustomArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CollectionCounterService {

    /**
     * Подсчитывает количество вхождений указанного элемента в CustomArrayList, используя многопоточность.
     *
     * @param list    Коллекция, в которой производится поиск.
     * @param element Элемент для подсчета.
     * @param <T>     Тип элементов в коллекции.
     * @return Количество вхождений элемента.
     */
    public <T> int countOccurrences(CustomArrayList<T> list, T element) {
        if (list == null || list.isEmpty()) {
            return 0;
        }

        int numThreads = Runtime.getRuntime().availableProcessors();
        int totalOccurrences;
        try (ExecutorService executor = Executors.newFixedThreadPool(numThreads)) {
            List<Future<Integer>> futures = new ArrayList<>();
            totalOccurrences = 0;

            try {
                int partSize = list.size() / numThreads;
                for (int i = 0; i < numThreads; i++) {
                    final int start = i * partSize;
                    final int end = (i == numThreads - 1) ? list.size() : (i + 1) * partSize;

                    Callable<Integer> task = () -> {
                        int localCount = 0;
                        for (int j = start; j < end; j++) {
                            if (element.equals(list.get(j))) {
                                localCount++;
                            }
                        }
                        return localCount;
                    };
                    futures.add(executor.submit(task));
                }

                for (Future<Integer> future : futures) {
                    try {
                        totalOccurrences += future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        System.err.println("Ошибка при выполнении задачи подсчета: " + e.getMessage());
                        Thread.currentThread().interrupt();
                        return -1;
                    }
                }
            } finally {
                executor.shutdown();
            }
        }

        return totalOccurrences;
    }
}