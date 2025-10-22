package org.astonsorting.collection;

import java.lang.Iterable;
import java.util.Iterator;
import java.util.*;

/*Реализация пользовательского варианта коллекции
* через использование обычного массива
* Класс поддерживает дженерики и итерацию
* */
public class CustomArrayList<T> implements Iterable<T> {
    private Object[] list;
    private int containerSize;

    public CustomArrayList() {
        this.list = new Object[16];
        this.containerSize = 0;
    }

    public void add(T element) {
        if(element == null) throw new IllegalArgumentException("An element for the list is null");

        resize();

        this.list[containerSize] = element;
        this.containerSize++;
    }

    public T get(int index) {
        if(index < 0 || index >= containerSize) throw new IndexOutOfBoundsException("index is invalid value");

        return (T) list[index];
    }

    public int size() {
        return this.containerSize;
    }

    public void clear() {
        for(int i = 0; i < containerSize; i++) {
            this.list[i] = null;
        }
    }

    public boolean isEmpty() {
        return this.containerSize < 0;
    }

    private void resize() {
        if(this.containerSize >= list.length) {
            Object[] extendedList = new Object[(int) (list.length * 1.5) + 1];

            for(int i = 0; i < this.containerSize; i++) {
                extendedList[i] = this.list[i];
            }

            this.list = extendedList;
        }
    }
/*
* Преобразование массива в коллекцию
* (игнорируя неиспользуемые ячейки)
* */
    @Override
    public Iterator<T> iterator() {
        return Arrays.asList((T[]) Arrays.copyOf(this.list, this.containerSize)).iterator();
    }
}