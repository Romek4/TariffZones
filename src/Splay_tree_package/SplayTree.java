/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Splay_tree_package;

import java.util.Stack;

/**
 *
 * @author Roman
 */
public class SplayTree <K extends Comparable, V>
{
    private Node root = null, actual = null;

    public Node getRoot() {
        return root;
    }
    
    private void splay(Node n)
    {
        while (n != root)
        {            
            //n nemá prarodiča -> jeho otec je root
            if (n.getFather() == root)
            {
                if (n == n.getFather().getLeftChild()) //n je ľavým synom
                {
                    rightRotation(n.getFather());
                }
                else //n je pravým synom
                {
                    leftRotation(n.getFather());
                }
            }
            else //n má prarodiča
            {
                if (n == n.getFather().getLeftChild() && n.getFather() == n.getFather().getFather().getLeftChild()) //n aj jeho otec sú ľavými synmi
                {
                    rightRotation(n.getFather().getFather());
                    rightRotation(n.getFather());
                }
                else if (n == n.getFather().getRightChild() && n.getFather() == n.getFather().getFather().getRightChild()) //n aj jeho otec sú pravými synmi
                {
                    leftRotation(n.getFather().getFather());
                    leftRotation(n.getFather());
                }
                else if (n == n.getFather().getLeftChild() && n.getFather() == n.getFather().getFather().getRightChild()) //n je ľavý syn a jeho otec je pravý syn
                {
                    rightRotation(n.getFather());
                    leftRotation(n.getFather());
                }
                else //n je pravý syn a jeho otec je ľavý syn
                {
                    leftRotation(n.getFather());
                    rightRotation(n.getFather());
                }
            }
        }
    }
    
    public boolean deleteData(K key)
    {
        //if (findData(key) == null) { return false; } //ak nenašlo záznam s takýmto kľúčom, nemám čo vymazať-končím
        actual = root;
        Node n = null;
        while (actual != null)
        {   
            if (actual.getRecord().getKey().compareTo(key) == 0) //ak sa kľúče rovnajú => našiel som, čo som hľadal
            {
                break;
            }
            else if (actual.getRecord().getKey().compareTo(key) < 0) //ak kľúč recordu je menší ako hľadaný
            {
                n = actual;
                actual = actual.getRightChild();
            }
            else //ak kľúč recordu je väčší ako hľadaný
            {
                n = actual;
                actual = actual.getLeftChild();
            }
        }
        
        if (actual == null) {
            splay(n);
            return false;
        }
        
        if (actual.isLeaf()) //ak je actual list(nemá synov), zruším ho
        {
            if (actual == root)
            {
                root = null; //strom je prázdny
            }
            else
            {
                Node father = actual.getFather();
                if (actual == father.getLeftChild())
                {
                    father.setLeftChild(null);
                }
                else
                {
                    father.setRightChild(null);
                }
                splay(father);
            }
        }//ak to nie je list
        else if (actual.getLeftChild() != null && actual.getRightChild() != null) //ak uzol, ktorý má byť vymazaný má dvoch synov
        {
            n = getLargestNodeFromLeftSubTree(actual);
            actual.setRecord(n.getRecord()); //vymením recordy
            if (n.isLeaf()) {
                Node father = n.getFather();
                if (n == father.getLeftChild())
                {
                    father.setLeftChild(null);
                }
                else
                {
                    father.setRightChild(null);
                }
                splay(father);
            }
            else
            {
                if (n == actual.getLeftChild()) {
                    actual.setLeftChild(n.getLeftChild());
                    n.getLeftChild().setFather(actual);
                    splay(actual);
                }
                else
                {
                    n.getFather().setRightChild(n.getLeftChild());
                    n.getLeftChild().setFather(n.getFather());
                    splay(actual);
                }
            }
        }
        else //ak uzol, ktorý má byť vymazaný má len jedného syna
        {
            if (actual.getLeftChild() == null) //ak má len pravého syna
            {
                n = actual.getRightChild();
            }
            else //ak má len ľavého syna
            {
                n = actual.getLeftChild();
            }
            
            if (actual.getFather() == null) //ak idem vymazať koreň -> netreba volať splay - volal by som ho aj tak nad vrchol, ktorý je už rootom
            {
                root = n;
                n.setFather(null);
                return true;
            }
            else
            {
                if (actual == actual.getFather().getLeftChild()) //ak vymazávaný je ľavým synom
                {
                    actual.getFather().setLeftChild(n);
                    n.setFather(actual.getFather());
                }
                else //ak vymazávaný je pravým synom
                {
                    actual.getFather().setRightChild(n);
                    n.setFather(actual.getFather());
                }
                splay(n.getFather());
            }
        }
        return true;
    }
    
    private Node getLargestNodeFromLeftSubTree(Node node)//tu sa ešte zamyslieť
    {
        Node n = node.getLeftChild();
        while (n.getRightChild() != null)
        {            
            n = n.getRightChild();
        }
        
        return n;
    }
    
    public boolean insertData(Record record)
    {
        if (root == null) //if i'm not having root yet -> i create new one
        {
            Node node = new Node(record, null, null, null, 0);
            root = node;
            return true;
        }
        
        actual = getLeafNode(record);
        if (actual == null) { return false; }
        
        Node insertNode = new Node(record, null, null, actual, 0);
        
        if (actual.getRecord().getKey().compareTo(record.getKey()) == 0) { return false; } //duplicity found in leaf
        else if (actual.getRecord().getKey().compareTo(record.getKey()) < 0)
        {
            actual.setRightChild(insertNode);
        }
        else
        {
            actual.setLeftChild(insertNode);
        }
        splay(insertNode);
        return true;
    }
    
    private void leftRotation(Node node)
    {
        Node v = node.getRightChild(); //v bude ľavý syn toho, okolo ktorého budem rotovať do ľava(node) a stane sa jeho otcom
        
        node.setRightChild(v.getLeftChild());
        if (v.getLeftChild() != null) //ak v má ľavého syna nastavím mu otca
        {
            v.getLeftChild().setFather(node);
        }
                     
        if (node.getFather() != null) //ak bol node predtým root
        {
            if (node == node.getFather().getLeftChild()) //zistím, ktorý syn bol node a podľa toho nastavím nového syna(v)
            {
                node.getFather().setLeftChild(v);
            }
            else
            {
                node.getFather().setRightChild(v);
            }
        }
        
        v.setFather(node.getFather()); //vrcholu v nastavím otca, ktorého mal predtým jeho otec(node)
        if (v.getFather() == null) //ak je jeho nový otec null -> stáva sa rootom
        {
            root = v;
        }
        
        v.setLeftChild(node);
        node.setFather(v);
    }
    
    private void rightRotation(Node node)
    {
        Node v = node.getLeftChild(); //v bude ľavý syn toho, okolo ktorého budem rotovať do prava(node) a stane sa jeho otcom
        
        node.setLeftChild(v.getRightChild()); //node preberie pravého syna od svojho ľavého syna(v)
        if (v.getRightChild() != null) //ak v má pravého syna nastavím mu otca
        {
            v.getRightChild().setFather(node);
        }
        
        if (node.getFather() != null) //ak bol node predtým root
        {
            if (node == node.getFather().getLeftChild()) //zistím, ktorý syn bol node a podľa toho nastavím nového syna(v)
            {
                node.getFather().setLeftChild(v);
            }
            else
            {
                node.getFather().setRightChild(v);
            }
        }
        
        v.setFather(node.getFather()); //vrcholu v nastavím otca, ktorého mal predtým jeho otec(node)
        if (v.getFather() == null) //ak je jeho nový otec null -> stáva sa rootom
        {
            root = v;
        }
                
        v.setRightChild(node); //node sa stane pravým synom v
        node.setFather(v); //v sa stane jeho otcom
    }
    
    
    public Record findData(K keyToFind)
    {
        if (root == null) { return null; }
        actual = root;
        Node n = null;
        int a = 0;
        while (actual != null)
        {   
            if (actual.getRecord().getKey().compareTo(keyToFind) == 0) //ak sa kľúče rovnajú => našiel som, čo som hľadal
            {
                splay(actual);
                return actual.getRecord();
            }
            else if (actual.getRecord().getKey().compareTo(keyToFind) < 0) //ak kľúč recordu je menší ako hľadaný
            {
                n = actual;
                actual = actual.getRightChild();
            }
            else //ak kľúč recordu je väčší ako hľadaný
            {
                n = actual;
                actual = actual.getLeftChild();
            }
        }
        splay(n);
        actual = n;
        return null;
    }
    
    private Node getLeafNode(Record record)
    {
        Node leafNode = root;
        while (!leafNode.isLeaf()) {            
            if (leafNode.getRecord().getKey().compareTo(record.getKey()) < 0)
            {
                if (leafNode.getRightChild() == null)
                {
                    return leafNode;
                }
                leafNode = leafNode.getRightChild();
            }
            else if (leafNode.getRecord().getKey().compareTo(record.getKey()) > 0)
            {
                if (leafNode.getLeftChild() == null)
                {
                    return leafNode;
                }
                leafNode = leafNode.getLeftChild();
            }
            else { return null; }
        }
        return leafNode;
    }
    
    public String inorderTraversal()
    {
        String inorder = "";
        actual = root;
        Stack stack = new Stack();
        
        while(!stack.isEmpty() || actual != null)
        {
            if (actual != null) {
                stack.push(actual);
                actual = actual.getLeftChild();
            }
            else {
                Node node = (Node) stack.pop();
                
                inorder += node.getRecord().toString() + "\n";
                actual = node.getRightChild();
            }
        }
        return inorder;
    }
    
    public String getNextSuccessors(int number)
    {
        String s = "";
        Stack stack = new Stack();
        int found = 0;
        if (actual.getRightChild() == null) {
            s += actual.getRecord().toString();
            return s;
        }
        
        actual = actual.getRightChild();
        
        while(found <= number && (!stack.isEmpty() || actual != null))
        {
            if (actual != null) {
                stack.push(actual);
                actual = actual.getLeftChild();
            }
            else {
                Node node = (Node) stack.pop();
                
                s += node.getRecord().toString() + "\n";
                found++;
                actual = node.getRightChild();
            }
        }
        return s;
    }
}
