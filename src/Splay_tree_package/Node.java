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
public class Node {
    private Record record;
    private Node leftChild;
    private Node rightChild;
    private Node father;
    private int balanceFactor;

    public Node(Record record, Node leftChild, Node rightChild, Node father, int balanceFactor) {
        this.record = record;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.father = father;
        this.balanceFactor = balanceFactor;
    }
    
    public boolean isLeaf(){
        if (this == null) {
            System.out.println("blbost");
        }
        
        if (leftChild == null && rightChild == null)
            return true;
        return false;
    }
    
    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public Node getFather() {
        return father;
    }

    public void setFather(Node father) {
        this.father = father;
    }

    public int getBalanceFactor() {
        return balanceFactor;
    }

    public void setBalanceFactor(int balanceFactor) {
        this.balanceFactor = balanceFactor;
    }
}