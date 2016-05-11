package com.pao.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

//metodele acestei interfete sunt accesibile clientilor
//clientii trebuie sa implementeze interfata Task ptr a putea apela metoda remote
//metoda remote doar apeleaza metoda execute a interfetei Task
//codul de executie e incarcat dinamic in server cand clientul apeleaza metoda prin RMI
public interface Compute extends Remote {
    <T> T executeTask(Task<T> t) throws RemoteException;
}