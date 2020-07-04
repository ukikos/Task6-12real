package com.company;

import java.util.ArrayList;

public class MyMap<K extends Comparable<K>, V extends Comparable<V>> {
    private int size = 0;
    private Node keyRoot = null;
    private Node valueRoot = null;
    private Node balanceRoot = null;

    public boolean add(K key, V value) {
        if(size == 0) {
            size ++;
            keyRoot = new Node(key, value);
            valueRoot = new Node(key, value);
            return  true;
        }

        Node parent = searchByKey(key);
        switch (parent.getKey().compareTo(key)) {
            case 0 : {
                return false;
            }
            case 1 : {
                parent.setLeft(new Node(key, value));
                balanceRoot = keyRoot;
                balance(parent);
                break;
            }
            case -1 : {
                parent.setRight(new Node(key, value));
                balanceRoot = keyRoot;
                balance(parent);
            }
        }
        size++;

        parent = searchByValue(value);
        switch (parent.getValue().compareTo(value)) {
            case 1 : {
                parent.setLeft(new Node(key, value));
                balanceRoot = valueRoot;
                balance(parent);
                break;
            }
            case -1 : {
                parent.setRight(new Node(key, value));
                balanceRoot = valueRoot;
                balance(parent);
            }
        }
        return true;
    }

    public V get(K key) {
        Node temp = searchByKey(key);
        return temp.getKey().compareTo(key) == 0 ? searchByKey(key).getValue() : null;
    }

    public boolean contains(V value) {
        return searchByValue(value).getValue().compareTo(value) == 0;
    }

    public int size() {
        return size;
    }

    public ArrayList<V> toArray(){
        ArrayList<Node> values = new ArrayList<>();
        getNodeList(values, valueRoot);
        ArrayList<V> result = new ArrayList<>();
        for (Node temp: values) {
            result.add(temp.getValue());
        }
        return result;
    }

    public void clear() {
        size = 0;
        keyRoot = null;
        valueRoot = null;
    }

    public boolean remove(V value) {
        if(searchByValue(value).getValue().compareTo(value) != 0)
            return false;

        removeNode(searchByValue(value));
        size --;

        ArrayList<Node> keys = new ArrayList<>();
        getNodeList(keys, keyRoot);
        for (Node temp : keys) {
            if(temp.getValue().compareTo(value) == 0)
                removeNode(temp);
        }

        return true;
    }

    private void removeNode(Node temp) {
        Node paste = temp.getRight();
        if(paste != null)
            while (paste.getLeft() != null)
                paste = paste.getLeft();
        else{
            paste = temp.getLeft();
            if(paste != null)
                while (paste.getRight() != null)
                    paste = paste.getRight();
        }

        Node parent = null;
        if(paste != null) {
            parent = paste.getParent();
            paste.setParent(null);
            if(temp.getLeft() != paste)
                paste.setLeft(temp.getLeft());
            if(temp.getRight() != paste)
                paste.setRight(temp.getRight());
            if(parent.getLeft() == paste)
                parent.setLeft(null);
            else
                parent.setRight(null);
        }

        if(temp != this.balanceRoot) {
            if (temp.getValue().compareTo(temp.getParent().getValue()) == 1)
                temp.getParent().setRight(paste);
            else
                temp.getParent().setLeft(paste);
        }
        else
        if(balanceRoot == keyRoot)
            keyRoot = paste;
        else
            valueRoot = paste;
        balanceRoot = paste;
        if(parent != temp && parent != null)
            balance(parent);
        else balance(temp.getParent());
        size--;

    }

    private void getNodeList(ArrayList<Node> result, Node temp) {
        result.add(temp);
        if(temp.getLeft() != null)
            getNodeList(result, temp.getLeft());
        if(temp.getRight() != null)
            getNodeList(result, temp.getRight());
    }

    private void balance(Node leaf) {
        do{
            int a = (leaf.getRight() != null) ? leaf.getRight().getHeight() : -1;
            int b = (leaf.getLeft() != null) ? leaf.getLeft().getHeight() : -1;
            if (a - b == 2) {
                if(((leaf.getRight().getRight() != null) ? leaf.getRight().getRight().getHeight() : -1) <
                        ((leaf.getRight().getLeft() != null) ? leaf.getRight().getLeft().getHeight() : -1))
                    rightRotation(leaf.getRight());
                leftRotation(leaf);
            }
            if (a - b == -2) {
                if(((leaf.getLeft().getLeft() != null) ? leaf.getLeft().getLeft().getHeight() : -1) <
                        ((leaf.getLeft().getRight() != null) ? leaf.getLeft().getRight().getHeight() : -1))
                    leftRotation(leaf.getLeft());
                rightRotation(leaf);
            }
            leaf = leaf.getParent();
        }while (leaf != null);

    }

    private void rightRotation(Node root) {
        Node left = root.getLeft(), parent = root.getParent();
        left.setParent(null);
        root.setLeft(left.getRight());
        left.setRight(root);
        if(parent == null) {
            if(balanceRoot == keyRoot)
                keyRoot = left;
            else
                valueRoot = left;
            this.balanceRoot = left;
            left.setParent(null);
        }
        else
        {
            if(parent.getValue().compareTo(root.getValue()) == 1)
                parent.setLeft(left);
            else
                parent.setRight(left);
        }
    }

    private void leftRotation(Node root) {
        Node right = root.getRight(), parent = root.getParent();
        right.setParent(null);
        root.setRight(right.getLeft());
        right.setLeft(root);
        if(parent == null){
            if(balanceRoot == keyRoot)
                keyRoot = right;
            else
                valueRoot = right;
            this.balanceRoot = right;
            right.setParent(null);
        }
        else
        {
            if(parent.getValue().compareTo(root.getValue()) == 1)
                parent.setLeft(right);
            else
                parent.setRight(right);
        }
    }

    private Node searchByKey(K key) {
        Node temp = keyRoot;
        while (key.compareTo(temp.getKey()) != 0) {
            int a = key.compareTo(temp.getKey());
            if(a == -1)
                if(temp.getLeft() != null)
                    temp = temp.getLeft();
                else
                    break;
            else
            if(temp.getRight() != null)
                temp = temp.getRight();
            else
                break;
        }
        return temp;
    }

    private Node searchByValue(V value) {
        Node temp = valueRoot;
        while (value.compareTo(temp.getValue()) != 0) {
            int a = value.compareTo(temp.getValue());
            if(a == -1)
                if(temp.getLeft() != null)
                    temp = temp.getLeft();
                else
                    break;
            else
            if(temp.getRight() != null)
                temp = temp.getRight();
            else
                break;
        }
        return temp;
    }

    private class Node{
        private K key;
        private V value;
        private int height;
        private Node left;
        private Node right;
        private Node parent;

        public K getKey() {
            return key;
        }

        V getValue() {
            return value;
        }
        int getHeight() {
            return height;
        }
        Node getRight() {
            return right;
        }
        Node getLeft() {
            return left;
        }
        Node getParent() {
            return parent;
        }

        void setHeight(int height) {
            this.height = height;
        }
        void setLeft(Node node) {
            left = node;
            if(node != null) {
                node.setParent(this);
            }
            updateParameters();
        }
        void setRight(Node node) {
            right = node;
            if(node != null) {
                node.setParent(this);
            }
            updateParameters();
        }
        void setParent(Node node) {
            parent = node;
        }

        private void updateParameters() {
            Node parent = this;
            do {
                int a = parent.getLeft() != null ? parent.getLeft().getHeight() : -1;
                int b = parent.getRight() != null ? parent.getRight().getHeight() : -1;
                parent.setHeight(Math.max(a, b) + 1);
                parent = parent.getParent();
            } while (parent != null);
        }

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            height = 0;
        }
    }
}
