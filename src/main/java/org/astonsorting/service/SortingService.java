package org.astonsorting.service;

import org.astonsorting.collection.CustomArrayList;
import org.astonsorting.service.strategy.Sorter;

import java.util.Comparator;
import java.util.concurrent.*;

public class SortingService<T> {
    private final ExecutorService service;

    public SortingService() {
        this.service = Executors.newFixedThreadPool(2);
    }

    public void sort(CustomArrayList<T> list, Sorter<T> sorter, Comparator<T> comparator) {
        Callable<CustomArrayList<T>> task = () -> sorter.sort(list, comparator);

        Future<CustomArrayList<T>> future = this.service.submit(task);

        try{
            CustomArrayList<T> sortedList = future.get();

            list.clear();

            for(var item : sortedList) {
                list.add(item);
            }
        } catch(ExecutionException e) {
            System.out.println("Failed with sorted list from Future");
            throw new RuntimeException(e);
        }
        catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        if(!this.service.isShutdown()) this.service.shutdown();
    }
}
