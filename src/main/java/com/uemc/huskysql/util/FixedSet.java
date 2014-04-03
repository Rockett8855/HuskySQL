package com.uemc.huskysql.util;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FixedSet<E> extends AbstractSet<E> implements Set<E> {
    private final int size;
    
    public FixedSet() {
        this(10);
    }
    
    public FixedSet(int size) {
        this.size = size;
        map = new HashMap<>(size);
    }
    
    private transient Map<E, Object> map;
    
    private static final Object PRESENT = new Object();

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public boolean add(E e) {
        if(map.size() > size) {
            return false;
        } else {
            map.put(e, PRESENT);
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o)==PRESENT;
    }

    @Override
    public void clear() {
        map.clear();
    }
}
