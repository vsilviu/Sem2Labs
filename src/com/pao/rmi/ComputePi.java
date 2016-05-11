package com.pao.rmi;

import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ComputePi {
    public static void main(String args[]) {
//        System.setProperty("java.security.policy","/Users/Silviu/TCLab1/src/com/pao/rmi/policy.txt");
        //important: nu e nevoie de SecurityManager cand lucrez pe localhost
//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }
        try {
            //numele stub-ului ce contine metodele remote
            String name = "Compute";
            //iau registrul RMI din jvm
            Registry registry = LocateRegistry.getRegistry();
            //caut stub-ul pus in registru dupa nume
            Compute comp = (Compute) registry.lookup(name);
            //instantiez task-ul la nivel de client
            Pi task = new Pi(Integer.parseInt("2"));
            //rezultatul se obtine prin executia taskului pe server
            BigDecimal pi = comp.executeTask(task);
            System.out.println(pi);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }
}