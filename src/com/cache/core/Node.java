package com.cache.core ;
public class Node {
    public long key;
    private long value;
    private long expiryTime;
     Node prev;
     Node next;
    public Node ( long key,long value){
        this.key = key;
        this.value = value;
        this.expiryTime=-1;

    }
    public Node(long key,long value,long expirytime){
        this.key = key;
        this.value= value;
        this.expiryTime = expirytime;
    }
    long getExpiryTime(){
        return expiryTime;
    }
    public long getValue(){
        return value;
    }
    public long getKey(){
        return key;
    }
    public void setValue(long value){
        this.value= value;
    }
    public void setValue(long key,long value){
        this.key=key;
        this.value= value;
    }




}