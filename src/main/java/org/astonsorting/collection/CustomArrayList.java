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

        ensureCapacity(containerSize + 1);

        this.list[containerSize] = element;
        this.containerSize++;
    }

    public void addAll(CustomArrayList<? extends T> otherList) {
        if (otherList == null) {
            throw new NullPointerException("The specified list is null");
        }

        int numNew = otherList.size();
        if (numNew == 0) {
            return;
        }

        ensureCapacity(this.containerSize + numNew);

        System.arraycopy(otherList.list, 0, this.list, this.containerSize, numNew);

        this.containerSize += numNew;
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
        this.containerSize = 0;
    }

    public boolean isEmpty() {
        return this.containerSize == 0;
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

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > list.length) {
            int newCapacity = (int) (list.length * 1.5) + 1;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] extendedList = new Object[newCapacity];
            if (this.containerSize >= 0) System.arraycopy(this.list, 0, extendedList, 0, this.containerSize);
            this.list = extendedList;
        }
    }
/*
* Преобразование массива в коллекцию
* (игнорируя неиспользуемые ячейки)
* */
    @Override
    public Iterator<T> iterator() {
        return Arrays.stream(this.list, 0, containerSize).map(e -> (T) e).iterator();
    }
}