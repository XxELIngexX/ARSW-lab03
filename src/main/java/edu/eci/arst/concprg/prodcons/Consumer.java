/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class Consumer extends Thread{
    
    private Queue<Integer> queue;
    
    
    public Consumer(Queue<Integer> queue){
        this.queue=queue;        
    }
    
    @Override
    public void run() {
        while (true) {
            try {
               
                    while (queue.isEmpty()){
                        System.out.println("sin productos");
                        queue.wait();
                    }
                 synchronized (queue) {
                    int elem=queue.poll();
                    System.out.println("Consumer consumes "+elem);
                     queve.notify()


                }
            }catch (Exception e){}



        }
    }
}
