package org.komusubi.feeder.model.airline;

import org.komusubi.feeder.model.airline.Segment.Type;

/**
 * 
 * @author jun.ozeki 2013/11/28
 */
public abstract class Tree<T> {
    private Node<T> start;
//    private Node<T> end;
//    private Node<T> endOfLeft;
//    private Node<T> endOfRight;
    private Node<T> current;
    
    @SuppressWarnings("hiding")
    private class Node<T> {
        private Node<T> left;
        private Node<T> right;
        private Node<T> previous;
        private Node<T> next;
        private T element;

        public Node(Node<T> prev, Node<T> next, Node<T> left, Node<T> right, T element) {
            this.previous = prev;
            this.next = next;
            this.left = left;
            this.right = right;
            this.element = element;
        }
    }

    public Tree() {
        start = new Node<T>(null, null, null, null, null); // starting point.
        current = start;
    }

    public Tree<T> setNext(T element) {
        Node<T> node = new Node<T>(current, null, null, null, element);
        while (current.next != null)
            current = current.next;
        current.next = node;
        current = node;
        return this;
    }

    public Tree<T> setPrev(T element) {
        Node<T> node = new Node<T>(null, current, null, null, element);
        while (current.previous != null) 
            current = current.previous;
        current.previous = node;
        current = node;
        return this;
    }

    public Tree<T> setLeft(T element) {
        Node<T> node = new Node<T>(null, null, null, current, element);
        while (current.left != null)
            current = current.left;
        current.left = node;
        current = node;
        return this;
    }
    
    public Tree<T> setRight(T element) {
        Node<T> node = new Node<T>(null, null, current, null, element);
        while (current.right != null)
            current = current.right;
        current.right = node;
        current = node;
        return this;
    }

    public T getElement() {
        return current.element;
    }

    public boolean next() {
        if (current.next == null)
            return false;
        current = current.next;
        return true;
    }

    public boolean prev() {
        if (current.previous == null)
            return false;
        current = current.previous;
        return true;
    }

    public boolean left() {
        if (current.left == null)
            return false;
        current = current.left;
        return true;
    }

    public boolean right() {
        if (current.right == null)
            return false;
        current = current.right;
        return true;
    }

    public void rewind() {
        current = start;
    }

    public T[] findNode(Type type) {
        // TODO return array?
        throw new UnsupportedOperationException("not implemented");
//        return null;
    }
}
