package com.georgiana.ojoc.repo;

public interface AbstractFactory<Type> {
    Type create(String method);
}
