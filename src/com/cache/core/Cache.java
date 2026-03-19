package com.cache.core;
import java.util.*;
public class Cache {

     int capacity;
     Map<Long,Node> cacheMap;
      DoublyLinkedList dll;
      EvictionManager evictionManager;

  public Cache(int capacity) {
      this.capacity = capacity;
      this.cacheMap = new HashMap<>();
      this.dll = new DoublyLinkedList();

      this.evictionManager = new EvictionManager(dll, cacheMap);
  }
  public long get(long key){

      if(cacheMap.containsKey(key)){
          Node node= cacheMap.get(key);
          dll.moveToHead(node);
          return node.getValue();

      }
      else
          return -1;

  }
  public void set(long key,long value) {

      if (cacheMap.containsKey(key)) {
          Node node =cacheMap.get(key);
          dll.moveToHead(node);
          node.setValue(value);

      } else {
          if (capacity == cacheMap.size()) {
              evictionManager.evictLRU();
             addNode(key, value);
          } else {

            addNode(key ,value);


          }
      }
  }


     private void addNode(long key,long value){
          Node node=new Node(key, value);
          dll.addToHead(node);
          cacheMap.put(key,node);
      }












}
