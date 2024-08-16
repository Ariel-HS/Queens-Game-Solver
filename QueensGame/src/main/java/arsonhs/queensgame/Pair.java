/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package arsonhs.queensgame;

/**
 *
 * @author ariel
 */
public class Pair<T,U> {
    private T key;
    private U value;
    
    public Pair() {
    }
    
    public Pair(T key, U value) {
        this.key = key;
        this.value = value;
    }
    
    public void setKey(T key) {
        this.key = key;
    }
    
    public void setValue(U value) {
        this.value = value;
    }
    
    public T getKey() {
        return this.key;
    }
    
    public U getValue() {
        return this.value;
    }
}
