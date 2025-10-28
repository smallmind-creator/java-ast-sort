package org.astonsorting.service.strategy;

import org.astonsorting.collection.CustomArrayList;

import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Самодостаточная многопоточная реализация стратегии сортировки слиянием.
 * Использует Fork/Join Framework для безопасного управления своей многопоточностью,
 * что позволяет ей корректно работать внутри любого внешнего ExecutorService.
 * @param <T> Тип сортируемых элементов.
 */
public class ParallelMergeSortStrategy<T> extends MergeSortStrategy<T> {

    private static final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    @Override
    public CustomArrayList<T> sort(CustomArrayList<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) {
            return list;
        }
        return forkJoinPool.invoke(new SortTask<>(list, comparator));
    }

    private class SortTask<E> extends RecursiveTask<CustomArrayList<E>> {
        private final CustomArrayList<E> list;
        private final Comparator<E> comparator;

        SortTask(CustomArrayList<E> list, Comparator<E> comparator) {
            this.list = list;
            this.comparator = comparator;
        }

        @Override
        protected CustomArrayList<E> compute() {
            if (list.size() <= 1) {
                return list;
            }

            int middle = list.size() / 2;
            CustomArrayList<E> leftHalf = new CustomArrayList<>();
            for (int i = 0; i < middle; i++) leftHalf.add(list.get(i));

            CustomArrayList<E> rightHalf = new CustomArrayList<>();
            for (int i = middle; i < list.size(); i++) rightHalf.add(list.get(i));

            SortTask<E> leftTask = new SortTask<>(leftHalf, comparator);
            SortTask<E> rightTask = new SortTask<>(rightHalf, comparator);

            invokeAll(leftTask, rightTask);

            return (CustomArrayList<E>) ParallelMergeSortStrategy.this.mergeSortList((CustomArrayList<T>) leftTask.join(), (CustomArrayList<T>) rightTask.join(), (Comparator<T>) comparator);
        }
    }
}