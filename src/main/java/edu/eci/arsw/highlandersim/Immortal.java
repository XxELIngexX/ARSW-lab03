package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback=null;
    
    private int health;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());

    private static final Object lock = new Object();
    
    private boolean execution;
    
    private static boolean paused = false;


    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue=defaultDamageValue;
    }

    public void run() {

        while (true) {
            synchronized (lock){
                while (paused){
                    try {
                        lock.wait();
                    }catch (Exception e){
                        Thread.currentThread().interrupt();
                    }
                }

            }
            Immortal im;

            int myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            //avoid self-fight
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);

            this.fight(im);

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void fight(Immortal i2) {
        Immortal first = this.getHealth() < i2.getHealth() ? this : i2;
        Immortal second = this.getHealth() >= i2.getHealth() ? this : i2;


        // Adquiere el bloqueo en el orden de los inmortales
        if (first.hashCode() > second.hashCode()) {
            Immortal aux = first;
            first = second;
            second = aux;

        }

            synchronized (first) {
                synchronized (second) {
                    if (i2.getHealth() > 0) {
                        i2.changeHealth(i2.getHealth() - defaultDamageValue);
                        this.health += defaultDamageValue;
                        updateCallback.processReport("Fight: " + this + " vs " + i2 + "\n");
                    } else {
                        updateCallback.processReport(this + " says: " + i2 + " is already dead!\n");
                    }
                }
            }

    }



    public void changeHealth(int v) {
        health = v;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

    public void setExecution(boolean execution) {
        this.execution = execution;
    }


    public static void pause() {
       synchronized (lock){
           paused = true;
       }
    }

        public static void resumeGame() {
            synchronized (lock){
                paused = false;
                lock.notifyAll();
            }
        }

}



