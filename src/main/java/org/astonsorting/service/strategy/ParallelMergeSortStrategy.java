package org.astonsorting.service.strategy;

import org.astonsorting.collection.CustomArrayList;

import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * Многопоточная реализация стратегии сортировки слиянием.
 * Расширяет однопоточную версию, переопределяя метод sort для распараллеливания
 * рекурсивных вызовов с помощью ExecutorService.
 * @param <T> Тип сортируемых элементов.
 */
public class ParallelMergeSortStrategy<T> extends MergeSortStrategy<T> {

    private final ExecutorService executorService;

    public ParallelMergeSortStrategy(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public CustomArrayList<T> sort(CustomArrayList<T> list, Comparator<T> comparator) {
        if (list == null) return null;
        try {
            return parallelSortInternal(list, comparator).get();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Ошибка во время параллельной сортировки: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<CustomArrayList<T>> parallelSortInternal(CustomArrayList<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) {
            return CompletableFuture.completedFuture(list);
        }

        // Ручное разделение списка на две половины.
        int middle = list.size() / 2;
        CustomArrayList<T> leftHalf = new CustomArrayList<>();
        for (int i = 0; i < middle; i++) {
            leftHalf.add(list.get(i));
        }

        CustomArrayList<T> rightHalf = new CustomArrayList<>();
        for (int i = middle; i < list.size(); i++) {
            rightHalf.add(list.get(i));
        }

        CompletableFuture<CustomArrayList<T>> leftFuture = parallelSortInternal(leftHalf, comparator);
        CompletableFuture<CustomArrayList<T>> rightFuture = parallelSortInternal(rightHalf, comparator);

        return leftFuture.thenCombineAsync(
                rightFuture,
                (sortedLeft, sortedRight) -> super.mergeSortList(sortedLeft, sortedRight, comparator),
                executorService
        );
    }
}