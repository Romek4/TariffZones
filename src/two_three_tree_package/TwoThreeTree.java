/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package two_three_tree_package;

import java.util.Stack;

/**
 *
 * @author Roman
 */
public class TwoThreeTree <K extends Comparable, V> {
    private Node root;
    private Node actual;
    private int numberOfRecords = 0;
    //public int pocetDup = 0;

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    public Node getRoot() {
        return root;
    }
    
    public boolean insertData(K key, V value){
        Record <K, V> data = new Record<>(key, value);
        
        if (root == null) {
            Node node = new Node(data, null, null, null, null, null);
            root = node;
            //pocetDat++;
            return true;
        }
        else {
            if (findData(data.getKey()) != null) {
                //System.out.println("Duplicita!");
                //pocetDup++;
                return false;
            }
            
            actual = getLeafNode(data);
            
            if (actual == null) {
                //System.err.println("ZĂˇznam s tĂ˝mto kÄľĂşÄŤom uĹľ existuje - nie je moĹľnĂ© pridaĹĄ.");
                return false;
            }           
            
            boolean cont = true;
            Node newNode = null, newNode2 = null, lMin = null, lMax = null;
            while (cont) {
                if (actual == null) {
                    Node n = new Node(data, null, newNode, actual, newNode2, null);
                    newNode.setFather(n);
                    newNode2.setFather(n);
                    root = n;
                    numberOfRecords++;
                    return true;
                }
                
                /*if (actual.getData1().compareKeys(data.getKey()) == 0 || actual.getData2().compareKeys(data.getKey()) == 0) {
                    System.err.println("Chyba!");
                    return;
                }*/

                //ak sa tam zmestĂ­
                if (actual.getData2() == null) {                    
                    if (actual.getData1().getKey().compareTo(data.getKey()) == -1) {
                        actual.setData2(data);
                    }
                    else {
                        Record pomData = actual.getData1();
                        actual.setData1(data);
                        actual.setData2(pomData);
                    }
                    
                    if (lMin != null && lMax != null) {
                        lMin.setFather(actual);
                        lMax.setFather(actual);
                        if (lMin.getData1().getKey().compareTo(actual.getData1().getKey()) == -1 ) {
                            actual.setLeftChild(lMin);
                            actual.setMidChild(lMax);
                        }
                        else {
                            actual.setMidChild(lMin);
                            actual.setRightChild(lMax);
                        }
                    }
                    cont = false;
                }
                //ak sa tam nezmestĂ­
                else {
                    Record mid = getMidRecord(actual.getData1(), data, actual.getData2());
                    Record min = getMinRecord(actual.getData1(), data, actual.getData2());
                    Record max = getMaxRecord(actual.getData1(), data, actual.getData2());

                    if (newNode != null && newNode2 != null ) {
                        //ÄľavĂ˝
                        if (actual.getData1().getKey().compareTo(data.getKey()) == 1) {
                            lMin = new Node(min, null, newNode, null, newNode2, null);
                            lMax = new Node(max, null, actual.getMidChild(), null, actual.getRightChild(), null); //tu som opravil getLeftChild na getRightChild
                            newNode.setFather(lMin);
                            newNode2.setFather(lMin);
                            actual.getMidChild().setFather(lMax);
                            actual.getRightChild().setFather(lMax);
                        }
                        //pravĂ˝ 
                        else if (actual.getData2().getKey().compareTo(data.getKey()) == -1){
                            lMin = new Node(min, null, actual.getLeftChild(), null, actual.getMidChild(), null);
                            lMax = new Node(max, null, newNode, null, newNode2, null);
                            newNode.setFather(lMax);
                            newNode2.setFather(lMax);
                            actual.getLeftChild().setFather(lMin);
                            actual.getMidChild().setFather(lMin);
                        }
                        //stred
                        else {
                            lMin = new Node(min, null, actual.getLeftChild(), null, newNode, null);
                            lMax = new Node(max, null, newNode2, null, actual.getRightChild(), null);
                            newNode.setFather(lMin);
                            newNode2.setFather(lMax);
                            actual.getLeftChild().setFather(lMin);
                            actual.getRightChild().setFather(lMax);
                        }
                    }
                    else {
                        lMin = new Node(min, null, null, null, null, null);
                        lMax = new Node(max, null, null, null, null, null);
                    }

                    data = mid;

                    //ak otec existuje
                    if (actual.getFather() != null) {
                        actual = actual.getFather();
                        //ak je v otcovi miesto => choÄŹ odznova vo whily
                        if (actual.getData2() == null) {
                            cont = true;
                        }
                        //ak nie je v otcovi miesto
                        else {
                            data = mid;
                            mid = getMidRecord(actual.getData1(), data, actual.getData2());
                            min = getMinRecord(actual.getData1(), data, actual.getData2());
                            max = getMaxRecord(actual.getData1(), data, actual.getData2());
                            data = mid;

                            //ÄľavĂ˝
                            if (actual.getData1().getKey().compareTo(lMin.getData1().getKey()) == 1) {
                                newNode = new Node(min, null, lMin, null, lMax, null);                            
                                newNode2 = new Node(max, null, actual.getMidChild(), null, actual.getRightChild(), null);
                                lMin.setFather(newNode);
                                lMax.setFather(newNode);
                                actual.getMidChild().setFather(newNode2);
                                actual.getRightChild().setFather(newNode2);
                            }
                            //pravĂ˝
                             else if (actual.getData2().getKey().compareTo(lMin.getData1().getKey()) == -1){
                                newNode = new Node(min, null, actual.getLeftChild(), null, actual.getMidChild(), null);                            
                                newNode2 = new Node(max, null, lMin, null, lMax, null);
                                lMin.setFather(newNode2);
                                lMax.setFather(newNode2);
                                actual.getLeftChild().setFather(newNode);    
                                actual.getMidChild().setFather(newNode);
                            }
                            //stred
                            else {
                                newNode = new Node(min, null, actual.getLeftChild(), null, lMin, null);                            
                                newNode2 = new Node(max, null, lMax, null, actual.getRightChild(), null);
                                lMin.setFather(newNode);
                                lMax.setFather(newNode2);
                                actual.getLeftChild().setFather(newNode);    
                                actual.getRightChild().setFather(newNode2);
                            }
                            
                            actual = actual.getFather();

                            lMin = newNode;
                            lMax = newNode2;
                            cont = true;
                        }
                    }
                    //ak otec neexistuje - vznik noveho korena
                    else {
                        Node newRoot = new Node(mid, null, lMin, null, lMax, null);
                        lMin.setFather(newRoot);
                        lMax.setFather(newRoot);
                        root = newRoot;
                        cont = false;
                    }
                }
            }
            numberOfRecords++;
            return true;
        }
    }
    
    public Record <K, V> findData(K keyToFind){
        Record <K, V> record = null;
        actual = root;
        while (actual != null) {            
            //actual je 2-vrchol
            if (actual.getData2() == null) {
                //ak sa kÄľĂşÄŤe rovnajĂş => naĹˇiel som, ÄŤo som hÄľadal
                if (actual.getData1().getKey().compareTo(keyToFind) == 0) {
                    record = actual.getData1();
                    return record;
                }
                //ak kÄľĂşÄŤ recordu je menĹˇĂ­ ako hÄľadanĂ˝
                else if (actual.getData1().getKey().compareTo(keyToFind) == 1) {
                    actual = actual.getLeftChild();
                }
                //ak kÄľĂşÄŤ recordu je vĂ¤ÄŤĹˇĂ­ ako hÄľadanĂ˝
                else {
                    actual = actual.getRightChild();
                }
            }
            //actual je 3-vrchol
            else {
                //ak som naĹˇiel v 1. zĂˇzname
                if (actual.getData1().getKey().compareTo(keyToFind) == 0) {
                    record = actual.getData1();
                    return record;
                }
                //ak som naĹˇiel v 2. zĂˇzname
                else if (actual.getData2().getKey().compareTo(keyToFind) == 0) {
                    record = actual.getData2();
                    return record;
                }
                //ak som nenaĹˇiel ani v jednom zĂˇzname, zistĂ­m kam ÄŹalej
                //doprava
                else if (actual.getData2().getKey().compareTo(keyToFind) == -1) {
                    actual = actual.getRightChild();
                }
                //doÄľava
                else if (actual.getData1().getKey().compareTo(keyToFind) == 1) {
                    actual = actual.getLeftChild();
                }
                //stred
                else {
                    actual = actual.getMidChild();
                }
            }
        }
        return null;
    }
    
    public boolean deleteData(K key){
        Node v = getNode(key); //najdem si node, do ktoreho patri record, ktory chcem vymazat - v
        
        if (v == null) {
            System.out.println("Nepodarilo sa vymazaĹĄ - takĂ˝ zĂˇznam neexistuje!");
            return false;
        }
        
        if (!v.isLeaf()) //ak v (vrchol, v ktorom je Record, ktorĂ˝ chcem vymazaĹĄ) nie je listom
        {
            Node node = getNodeWithInOrderSuccessor(v, key);
            Record r;
            if (v.getData1().getKey().compareTo(key) == 0)
            {
                r = v.getData1();
                v.setData1(node.getData1());
                node.setData1(r);
            }
            else 
            {
                r = v.getData2();
                v.setData2(node.getData1());
                node.setData1(r);
            }
            v = node; // po tomto ife budem maĹĄ 'v' node nastavenĂ˝ na list, v ktorom data1 = record, kt. chcem  vymazaĹĄ
        }
        
        if (v.getData2() != null) { //v je 3-vrchol - jednoducho odoberiem ten record, kt.chcem a koniec
            if (v.getData1().getKey().compareTo(key) == 0) {
                v.setData1(v.getData2());
                v.setData2(null);
            }
            else {
                v.setData2(null);
            }
            return true;
        }
        
        //v je 2-vrchol a po odstrĂˇnenĂ­ recordu zostane prĂˇzdny
        Node vBro;
        Record k0;
        while (true)
        {
            if (v == root) //ak v je koreĹ�om
            {
                if (v.getData1() == null && v.getData2() == null) { //ak v koreni nie je Ĺľiadny zĂˇznam
                    
                    if (v.getLeftChild().getData1() != null) {
                        root = v.getLeftChild();
                    }
                    else {
                        root = v.getRightChild();
                    }
                    root.setFather(null);
                }
                else {
                    root = null;
                }
                return true;
            }
            
            if (v == v.getFather().getLeftChild()) //v je ÄľavĂ˝ syn
            {
                if (v.getFather().getData2() == null) //otec je 2-vrchol
                {
                    vBro = v.getFather().getRightChild();
                }
                else //otec je 3-vrchol
                {
                    vBro = v.getFather().getMidChild();
                }
                
                k0 = v.getFather().getData1();
                
                
                //zistĂ­m, ÄŤi brat je 2-vrchol alebo 3-vrchol
                if (vBro.getData2() == null) //vBro je 2-vrchol
                {
                    vBro.setData2(vBro.getData1());
                    vBro.setData1(k0);
                    
                    if (v.getFather().getData2() == null) { //ak otec je 2-vrchol
                        v.getFather().setData1(null);
                    }
                    else {
                        v.getFather().setData1(v.getFather().getData2());
                        v.getFather().setData2(null);
                    }
                    
                    if (v.isLeaf()) {
                        v.setData1(null);
                    }
                    else {
                        vBro.setMidChild(vBro.getLeftChild());
                        if (v.getLeftChild().getData1() != null) {
                            vBro.setLeftChild(v.getLeftChild());
                        }
                        else {
                            vBro.setLeftChild(v.getRightChild());
                        }
                        vBro.getLeftChild().setFather(vBro);
                    }
                }
                else //vBro je 3-vrchol
                {
                    v.setData1(k0);
                    v.getFather().setData1(vBro.getData1());
                    vBro.setData1(vBro.getData2());
                    vBro.setData2(null);
                    if (!v.isLeaf()) {
                        if (v.getRightChild().getData1() != null) {
                            v.setLeftChild(v.getRightChild());
                        }
                        v.setRightChild(vBro.getLeftChild());
                        v.getRightChild().setFather(v);
                        vBro.setLeftChild(vBro.getMidChild());
                        vBro.setMidChild(null);
                    }
                }
            }
            else if (v == v.getFather().getRightChild()) //v je pravĂ˝ syn
            {
                if (v.getFather().getData2() == null) //otec je 2-vrchol
                {
                    vBro = v.getFather().getLeftChild();
                    k0 = v.getFather().getData1();
                }
                else //otec je 3-vrchol
                {
                    vBro = v.getFather().getMidChild();
                    k0 = v.getFather().getData2();
                }
                
                
                //zistĂ­m, ÄŤi brat je 2-vrchol alebo 3-vrchol
                if (vBro.getData2() == null) //vBro je 2-vrchol
                {
                    vBro.setData2(k0);
                    
                    if (v.getFather().getData2() == null) { //otec je 2-vrchol                        
                        v.getFather().setData1(null);
                    }
                    else { //otec je 3-vrchol
                        v.getFather().setData2(null);
                    }
                    
                    if (v.isLeaf()) {
                        v.setData1(null);
                    }
                    else {
                        vBro.setMidChild(vBro.getRightChild());
                        if (v.getLeftChild().getData1() != null) {
                            vBro.setRightChild(v.getLeftChild());
                        }
                        else {
                            vBro.setRightChild(v.getRightChild());
                        }
                        vBro.getRightChild().setFather(vBro);
                    }
                }
                else //vBro je 3-vrchol
                {
                    if (v.getFather().getData2() == null) {
                        v.setData1(k0);
                        v.getFather().setData1(vBro.getData2());
                        vBro.setData2(null);
                    }
                    else {
                        v.setData1(k0);
                        v.getFather().setData2(vBro.getData2());
                        vBro.setData2(null);
                    }
                   
                    if (!v.isLeaf()) {
                        if (v.getLeftChild().getData1() != null) {
                            v.setRightChild(v.getLeftChild());
                        }
                        v.setLeftChild(vBro.getRightChild());
                        v.getLeftChild().setFather(v);
                        vBro.setRightChild(vBro.getMidChild());
                        vBro.setMidChild(null);
                    }
                }
            }
            else //v je strednĂ˝ syn(OTEC bude urÄŤite 3-vrchol) - Ĺ PECIĂ�LNY PRĂŤPAD - mĂ´Ĺľem si vybraĹĄ, ÄŤi chcem ÄľavĂ©ho brata alebo pravĂ©ho - vĂ˝hodnejĹˇĂ­ by bol brat 3-vrchol asi
            { //defaultne beriem ÄľavĂ˝ch bratov
                if (v.getFather().getLeftChild().getData2() != null) {
                    vBro = v.getFather().getLeftChild();
                    k0 = v.getFather().getData1();
                }
                else if (v.getFather().getRightChild().getData2() != null) {
                    vBro = v.getFather().getRightChild();
                    k0 = v.getFather().getData2();
                }
                else {
                    vBro = v.getFather().getLeftChild();
                    k0 = v.getFather().getData1();
                }
                
                //zistĂ­m, ÄŤi brat je 2-vrchol alebo 3-vrchol
                if (vBro.getData2() == null) //vBro je 2-vrchol - urÄŤite to bude ÄľavĂ˝ brat
                {
                    vBro.setData2(k0);
                    vBro.getFather().setData1(v.getFather().getData2());
                    vBro.getFather().setData2(null);
                    if (!v.isLeaf()) {
                        vBro.setMidChild(vBro.getRightChild());
                        if (v.getLeftChild().getData1() != null) {
                            vBro.setRightChild(v.getLeftChild());
                        }
                        else {
                            vBro.setRightChild(v.getRightChild());
                        }
                        vBro.getRightChild().setFather(vBro);
                    }
                }
                else //vBro je 3-vrchol
                {
                    if (vBro == v.getFather().getLeftChild()) { //vBro je ÄľavĂ˝ brat
                        v.setData1(k0);
                        v.getFather().setData1(vBro.getData2());
                        vBro.setData2(null);
                        if (!v.isLeaf()) {
                            if (v.getLeftChild().getData1() != null) {
                                v.setRightChild(v.getLeftChild());
                            }
                            v.setLeftChild(vBro.getRightChild());
                            v.getLeftChild().setFather(v);
                            vBro.setRightChild(vBro.getMidChild());
                            vBro.setMidChild(null);
                        }
                    }
                    else { //vBro je pravĂ˝ brat
                        v.setData1(k0);
                        v.getFather().setData2(vBro.getData1());
                        vBro.setData1(vBro.getData2());
                        vBro.setData2(null);
                        if (!v.isLeaf()) {
                            if (v.getRightChild().getData1() != null) {
                                v.setLeftChild(v.getRightChild());
                            }
                            v.setRightChild(vBro.getLeftChild());
                            v.getRightChild().setFather(v);
                            vBro.setLeftChild(vBro.getMidChild());
                            vBro.setMidChild(null);
                        }
                    }
                }
            }
            v = v.getFather();
            
            if (v.getData1() != null) {
                if (v.getLeftChild().getData1() == null) {
                    v.setLeftChild(vBro);
                    vBro.setFather(v);
                    v.setMidChild(null);
                }
                else if (v.getRightChild().getData1() == null){
                    v.setRightChild(vBro);
                    vBro.setFather(v);
                    v.setMidChild(null);
                }
                return true;
            }
        }
    }
    
    //int pocetVyp;
    
    public String inorderTraversal(){
        //pocetVyp = 0;
        String inorder = "";
        actual = root;
        Stack stack = new Stack();
        Node previous, node = null;
        
        while(!stack.isEmpty() || actual != null)
        {
            if (actual != null) {
                stack.push(actual);
                actual = actual.getLeftChild();
            }
            else {
                previous = node;
                node = (Node) stack.pop();
                
                if (node.getData2() == null) {
                    inorder += node.getData1().toString() + " ";
                    actual = node.getRightChild();
                    //pocetVyp++;
                }
                else {
                    if (node.isLeaf()) {
                        inorder += node.getData1().toString()+ " ";
                        actual = node.getMidChild();
                        inorder += node.getData2().toString() + " ";
                        //pocetVyp = pocetVyp + 2;
                    }
                    else {
                        if (previous != null) {
                            if (previous.getData1().getKey().compareTo(node.getData1().getKey()) == -1) {
                                inorder += node.getData1().toString() + " ";
                                actual = node.getMidChild();
                                stack.push(node);
                                //pocetVyp++;
                            }
                            else {
                                inorder += node.getData2().toString() + " ";
                                actual = node.getRightChild();
                                //pocetVyp++;
                            }
                        }
                        else {
                            inorder += node.getData1().toString() + " ";
                            actual = node.getMidChild();
                            stack.push(node);
                            //pocetVyp++;
                        }
                    }
                }
            }
        }
        return inorder;
    }
    
    private Node getNodeWithInOrderSuccessor(Node node, K key){ //tuto metodu budem potrebovat
        if (node.getData2() == null) {
            node = node.getRightChild();
        }
        else {
            if (node.getData1().getKey().compareTo(key) == 0) {
                node = node.getMidChild();
            }
            else {
                node = node.getRightChild();
            }
        }
        
        while (!node.isLeaf()){
            node = node.getLeftChild();
        }
        return node;
    }
    
    public Node getNode(K key){
        actual = root;
        while(actual != null){
            
            if (actual.getData2() == null) {
                if (actual.getData1().getKey().compareTo(key) == 0) {
                    return actual;
                }
                else if (actual.getData1().getKey().compareTo(key) == 1) {
                    actual = actual.getLeftChild();
                }
                else {
                    actual = actual.getRightChild();
                } 
            }
            else {
                if (actual.getData1().getKey().compareTo(key) == 0 || actual.getData2().getKey().compareTo(key) == 0) {
                    return actual;
                }
                else if (actual.getData2().getKey().compareTo(key) == -1) {
                    actual = actual.getRightChild();
                }
                else if (actual.getData1().getKey().compareTo(key) == 1) {
                    actual = actual.getLeftChild();
                }
                else {
                    actual = actual.getMidChild();
                }
            }
        }
        return null;
    }
    
    private Node getLeafNode(Record data){
        //tĂ˝mto whilom sa dostanem do listu, kam by mal patriĹĄ vkladanĂ˝ prvok
        actual = root;
        while (!actual.isLeaf()) {            
            if (actual.getData2() == null) {
                if (actual.getData1().getKey().compareTo(data.getKey()) == 0) {
                    return null;
                }
                
                if (actual.getData1().getKey().compareTo(data.getKey()) == 1) {
                    actual = actual.getLeftChild();
                }
                else {
                    actual = actual.getRightChild();
                } 
            }
            else {
                if (actual.getData1().getKey().compareTo(data.getKey()) == 0 || actual.getData2().getKey().compareTo(data.getKey()) == 0) {
                    return null;
                }
                
                if (actual.getData2().getKey().compareTo(data.getKey()) == -1) {
                    actual = actual.getRightChild();
                }
                else if (actual.getData1().getKey().compareTo(data.getKey()) == 1) {
                    actual = actual.getLeftChild();
                }
                else {
                    actual = actual.getMidChild();
                }
            }
        }
        return actual;
    }
    
    private Record getMidRecord(Record a, Record b, Record c){
        if (a == b || a == c || b == c) {
            return null;
        }
        else {
            if (b.getKey().compareTo(a.getKey()) == -1 && a.getKey().compareTo(c.getKey()) == -1) {
                return a;
            }
            else if (a.getKey().compareTo(b.getKey()) == -1 && b.getKey().compareTo(c.getKey()) == -1) {
                return b;
            }
            return c;
        }
    }
    
    private Record getMinRecord(Record a, Record b, Record c){
        if (a == b || a == c || b == c) {
            return null;
        }
        else {
            if (a.getKey().compareTo(b.getKey()) == -1 && a.getKey().compareTo(c.getKey()) == -1) {
                return a;
            }
            else if (b.getKey().compareTo(a.getKey()) == -1 && b.getKey().compareTo(c.getKey()) == -1) {
                return b;
            }
            return c;
        }
    }
    
    private Record getMaxRecord(Record a, Record b, Record c){
        if (a == b || a == c || b == c) {
            return null;
        }
        else {
            if (a.getKey().compareTo(b.getKey()) == 1 && a.getKey().compareTo(c.getKey()) == 1) {
                return a;
            }
            else if (b.getKey().compareTo(a.getKey()) == 1 && b.getKey().compareTo(c.getKey()) == 1) {
                return b;
            }
            return c;
        }
    }
}