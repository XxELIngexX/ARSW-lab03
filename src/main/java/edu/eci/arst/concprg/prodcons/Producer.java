/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class Producer extends Thread {

    private Queue<Integer> queue = null;

    private int dataSeed = 0;
    private Random rand=null;
    private final long stockLimit;

    public Producer(Queue<Integer> queue,long stockLimit) {
        this.queue = queue;
        rand = new Random(System.currentTimeMillis());
        this.stockLimit=stockLimit;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (queue) {
                try {
                    dataSeed = dataSeed + rand.nextInt(100);
                    queue.add(dataSeed);
                    queue.notifyAll();
                    System.out.println("Producer added " + dataSeed);

//                    while (queue.size() == stockLimit) {
//                        queue.wait(); // Espera si la cola está llena
//                    }




                    Thread.sleep(1000); // Simulación de tiempo de producción
                } catch (InterruptedException ex) {
                    Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
