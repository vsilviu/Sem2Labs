package com.pao.rmi;

//interfata ce permite ambiguitate pe server in executia unei metode
//trebuie ca clientii sa implementeze aceasta interfata ptr ambiguitate
public interface Task<T> {
    T execute();
}