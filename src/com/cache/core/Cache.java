package com.cache.core;
import java.util.*;
import java.util.concurrent.*;

public class Cache {

     int capacity;
     Map<Long,Node> cacheMap;
      DoublyLinkedList dll;
      EvictionManager evictionManager;
    PriorityQueue<Node> expiryQueue;


  public Cache(int capacity) {
      this.capacity = capacity;
      this.cacheMap = new HashMap<>();
      this.dll = new DoublyLinkedList();

      this.evictionManager = new EvictionManager(dll, cacheMap);

      this.expiryQueue = new PriorityQueue<>(
              (Node a, Node b) -> Long.compare(a.getExpiryTime(), b.getExpiryTime())
      );

      ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

      scheduler.scheduleAtFixedRate(() -> {
          cleanExpiredUsingPQ();
      }, 2, 2, TimeUnit.SECONDS);
  }
  public long get(long key){

      if(cacheMap.containsKey(key)){
          Node node= cacheMap.get(key);
          if(node.getExpiryTime()!=-1 &&
                  System.currentTimeMillis()>node.getExpiryTime()){
              dll.remove(node);
              cacheMap.remove(key);
              return -1;
          }
          dll.moveToHead(node);
          return node.getValue();
      }
      else
          return -1;

  }
  public void set(long key,long value) {

      if (cacheMap.containsKey(key)) {
          Node old = cacheMap.get(key);
          dll.remove(old);
          cacheMap.remove(key);

          addNode(key, value);

      } else {
          if (capacity == cacheMap.size()) {
              cleanExpired();
              if (capacity == cacheMap.size()){
              evictionManager.evictLRU();}
             addNode(key, value);
          } else {

            addNode(key ,value);


          }
      }
  }
  //overloading for extra ttl version
    public void set(long key,long value,long ttl) {

        if (cacheMap.containsKey(key)) {
            Node old = cacheMap.get(key);
            dll.remove(old);
            cacheMap.remove(key);

            addNode(key, value, ttl);

        } else {
            if (capacity == cacheMap.size()) {

                cleanExpired();

                if (capacity == cacheMap.size()) {
                    evictionManager.evictLRU();
                }
            }

                addNode(key ,value,ttl);



        }
    }



    private void addNode(long key,long value){
          Node node=new Node(key, value);
          dll.addToHead(node);
          cacheMap.put(key,node);
      }
    private void addNode(long key,long value,long ttl){
      long exptime=(ttl>0)?System.currentTimeMillis()+ttl*1000:-1;
        Node node=new Node(key, value,exptime);
        dll.addToHead(node);
        cacheMap.put(key,node);
        if (exptime != -1) {
            expiryQueue.offer(node);
        }

    }
    void cleanExpired(){
      int checks=2;
      Node node=dll.getTail();
      while(node!=null&&checks-->0){
          Node temp=node.prev;
          if(node.getExpiryTime()!=-1&&System.currentTimeMillis()>node.getExpiryTime()){
              dll.remove(node);
              cacheMap.remove(node.getKey());
          }
          node=temp;
      }
    }
    void cleanExpiredUsingPQ() {
        long now = System.currentTimeMillis();

        while (!expiryQueue.isEmpty()) {
            Node node = expiryQueue.peek();

            if (node.getExpiryTime() > now) {
                break; // nothing more expired
            }

            expiryQueue.poll(); // remove from PQ


            if (cacheMap.get(node.getKey()) == node) {
                dll.remove(node);
                cacheMap.remove(node.getKey());
            }
        }
    }












}
