package asm.utils;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Collection<T> {

    protected List<T> items;

    public Collection(List<T> items) {
        this.items = items;
    }

    public Collection(Collection<T> collection) {
        this.items = collection.items;
    }

    public Collection() {
        this.items = new java.util.ArrayList<T>();
    }

    public void replace(T item, int index) {
        items.set(index, item);
    }

    public T shift() {
        return items.remove(0);
    }

    public void unshift(T item) {
        items.add(0, item);
    }

    public void push(T item) {
        items.add(item);
    }

    public T pop() {
        return items.remove(items.size() - 1);
    }

    public void sort(Comparator<T> comparator) {
        java.util.Collections.sort(items, comparator);
    }

    public Collection<T> filter(Predicate<T> predicate) {
        Collection<T> collection = new Collection<T>();

        for (T item : items) {
            if (predicate.test(item)) {
                collection.push(item);
            }
        }

        return collection;
    }

    public T find(Predicate<T> predicate) {
        for (T item : items) {
            if (predicate.test(item)) {
                return item;
            }
        }

        return null;
    }

    public int count() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<T> all() {
        return items;
    }

    public void clear() {
        items.clear();
    }

        public void each(Consumer<T> consumer) {
        for (T item : items) {
            consumer.accept(item);
        }
    }

    public int indexOf(T item) {
        return items.indexOf(item);
    }

    public void remove(T item) {
        items.remove(item);
    }

    public void remove(int index) {
        items.remove(index);
    }

    public T first() {
        return items.get(0);
    }

    public T last() {
        return items.get(items.size() - 1);
    }

    public Collection<T> clone() {
        return new Collection<T>(this);
    }

    public <R> R reduce(R initial, BiFunction<R, T, R> reducer) {
        R result = initial;

        for (T item : items) {
            result = reducer.apply(result, item);
        }

        return result;
    }

        public <R> Collection<R> map(BiFunction<T, Integer, R> mapper) {
        Collection<R> collection = new Collection<R>();

        for (int i = 0; i < items.size(); i++) {
            collection.push(mapper.apply(items.get(i), i));
        }

        return collection;
    }

    @SuppressWarnings("unchecked")
    public T[] toArray() {
        return (T[]) items.toArray();
    }
}
