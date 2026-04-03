package com.cache.core;

public class DoublyLinkedList {
   public Node head ;
   private Node tail ;
    public void addToHead(Node node) {
        if(head == null){
            head= tail=node;

            return;
        }
        node.next = head;
        head.prev = node;
        head = node;
    }
    public void  remove(Node node){
        if(node==head && node == tail){
            node.prev = node.next =null;
            head= tail=null;
            return;
        }
        else if(node == head){

            head=head.next;
            node.prev = null;

            node.next=null;
            if(head!=null){
            head.prev=null;}
        }
        else if (node == tail){
            tail = tail.prev;
            if(tail!=null) {
                tail.next = null;
            }

            node.prev =node.next=null;


        }
        else{
            node.prev.next=node.next;
            node.next.prev=node.prev;
            node.prev = node.next = null;
        }

    }  public Node removeTail(){

        Node node= tail;
        if(node==null)
        {
            return node;
        }
       remove(node);
        return node;
    }
    public void moveToHead(Node node){
        if(node==head){
            return;
        }
        remove(node);
        addToHead(node);
    }

}


