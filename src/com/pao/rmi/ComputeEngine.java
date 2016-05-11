package com.pao.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ComputeEngine implements Compute {

    public ComputeEngine() {
        super();
    }

    public <T> T executeTask(Task<T> t) {
        return t.execute();
    }

    public static void main(String[] args) {
//        System.setProperty("java.security.policy","/Users/Silviu/TCLab1/src/com/pao/rmi/policy.txt");
//        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
//        System.setProperty("java.rmi.server.codebase","file:/Users/Silviu/TCLab1/src/");

//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }
        try {
            //nume pt recording in RMIRegistry
            String name = "Compute";
            //va fi stub-ul pus pe registry; pun interfata, deoarece nu pot accesa decat metodele ei
            Compute engine = new ComputeEngine();
            //creez registru nou corelat cu jvm-ul curent, pt a fixa problema cu CLASSPATH
            LocateRegistry.createRegistry(1099);
            //stub-ul propriu zis, wrapped, pregatit ptr un port anonim
            Compute stub =
                    (Compute) UnicastRemoteObject.exportObject(engine, 0);
            //registrul creat mai sus, nu unul cu propriul sau CLASSPATH
            Registry registry = LocateRegistry.getRegistry(1099);
            //punerea stub-ului in registru
            registry.bind(name, stub);
            System.out.println("ComputeEngine bound");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }
    }
}