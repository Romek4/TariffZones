/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package two_three_tree_package;

/**
 *
 * @author Roman
 */
public class Node <K extends Comparable, V>{
    private Record <K, V> data1;
    private Record <K, V> data2;
    private Node leftChild;
    private Node midChild;
    private Node rightChild;
    private Node father;

    public Node(Record data1, Record data2, Node leftChild, Node midChild, Node rightChild, Node father) {
        this.data1 = data1;
        this.data2 = data2;
        this.leftChild = leftChild;
        this.midChild = midChild;
        this.rightChild = rightChild;
        this.father = father;
    }
    
    public boolean isLeaf(){
        if (this == null) {
            System.out.println("blbost");
        }
        
        if (leftChild == null && midChild == null && rightChild == null)
            return true;
        return false;
    }
    
    public Record getData1() {
        return data1;
    }

    public void setData1(Record data1) {
        this.data1 = data1;
    }

    public Record getData2() {
        return data2;
    }

    public void setData2(Record data2) {
        this.data2 = data2;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getMidChild() {
        return midChild;
    }

    public void setMidChild(Node midChild) {
        this.midChild = midChild;
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
}