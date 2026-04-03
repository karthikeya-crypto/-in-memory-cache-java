package com.cache.core;
import java.util.Map;
public class EvictionManager {
      DoublyLinkedList dll;
      Map<Long,Node> cacheMap;
      // constructor
    public EvictionManager(DoublyLinkedList dll,Map<Long,Node> cacheMap){
        this.dll = dll;
        this.cacheMap = cacheMap;

    }
    public void evictLRU(){
        Node node = dll.removeTail(); // when capacity is full cache asks dll to remove least recently used
        if(node != null){
            cacheMap.remove(node.key);// when node is there it should remove in hash map too
        }
    }
}
