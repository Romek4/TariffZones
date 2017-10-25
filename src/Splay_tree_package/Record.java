/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Splay_tree_package;

/**
 *
 * @author Roman
 */
public class Record <K extends Comparable, V> {
    /**
     * returns unique identification key of Record
     * @return 
     */
    
    private K key;
    private V value;

    public Record(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
