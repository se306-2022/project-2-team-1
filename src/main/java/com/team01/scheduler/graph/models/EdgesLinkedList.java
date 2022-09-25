package com.team01.scheduler.graph.models;

import com.team01.scheduler.graph.exceptions.InvalidPositionException;

import java.util.Iterator;

public class EdgesLinkedList implements Iterable<Edge> {

    private Edge head;

    public EdgesLinkedList() {
        head = null;
    }

    /**
     * Method to add an edge to the start of the list
     * @param edge being added
     */
    public void prepend(Edge edge) {
        Edge temp = head;
        this.head = edge;
        edge.setNext(temp);
    }

    /**
     * Method to add an edge to the end of the list
     * @param edge
     */
    public void append(Edge edge) {
        int listSize = size();

        if (listSize == 0) {
            head = edge;
        } else {
            Edge temp = get(listSize - 1);
            temp.setNext(edge);
        }
    }

    /**
     * Method to get an edge at a given position in the list
     * @param pos: integer position
     * @return edge at position
     * @throws InvalidPositionException
     */
    public Edge get(int pos) throws InvalidPositionException {
        if (pos < 0 || pos > size() - 1) {
            throw new InvalidPositionException("Position " + pos + " is invalid");
        }

        int count = 0;
        Edge currentEdge = head;
        if (pos == 0) {
            return head;
        } else {
            while (currentEdge != null) {
                if (count == pos) {
                    return currentEdge;
                }
                currentEdge = currentEdge.getNext();
                count++;
            }
        }

        return null;
    }

    /**
     * Method to insert an edge at a given position in the list
     * @param pos: integer position
     * @param edge to insert
     * @throws InvalidPositionException
     */
    public void insert(int pos, Edge edge) throws InvalidPositionException {
        if (pos < 0 || pos > size() - 1) {
            throw new InvalidPositionException("Position " + pos + " is invalid");
        }

        if (size() == 0) {
            throw new InvalidPositionException("Can't insert in an empty list");
        }

        if (pos == 0) {
            edge.setNext(head);
        } else {
            Edge leftEdge = get(pos - 1);
            Edge rightEdge = get(pos);

            edge.setNext(rightEdge);
            leftEdge.setNext(edge);
        }
    }

    /**
     * Method to remove an edge at a position in the list
     * @param pos: integer position
     * @throws InvalidPositionException
     */
    public void remove(int pos) throws InvalidPositionException {
        if (pos < 0 || pos > size() - 1) {
            throw new InvalidPositionException("Position " + pos + " is invalid");
        }

        if (pos == 0) {
            head = get(pos + 1);
        } else if (pos == size() - 1) {
            Edge leftEdge = get(pos - 1);
            leftEdge.setNext(null);
        } else {
            Edge leftEdge = get(pos - 1);
            Edge rightEdge = get(pos + 1);

            leftEdge.setNext(rightEdge);
        }
    }

    /**
     * Method to
     * @return
     */
    public int size() {
        int count = 0;
        Edge edge = head;
        while(edge != null) {
            count++;
            edge = edge.getNext();
        }
        return count;
    }

    /**
     * Allows using EdgesLinkedList in a for/foreach loop.
     *
     * @return New iterator instance
     */
    @Override
    public Iterator<Edge> iterator() {
        return new EdgeIterator();
    }

    /**
     * Iterator implementation for EdgesLinkedList
     */
    class EdgeIterator implements Iterator<Edge> {

        private int index = 0;

        /**
         * Checks if there is another item in the iterable
         *
         * @return True if not the last item
         */
        @Override
        public boolean hasNext() {
            return index < size();
        }

        /**
         * Get the next item in the iterable
         *
         * @return The next edge
         */
        @Override
        public Edge next() {
            return get(index++);
        }
    }
}