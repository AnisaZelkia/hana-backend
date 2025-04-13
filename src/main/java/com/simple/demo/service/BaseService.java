package com.simple.demo.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, D> {

	    T save(T entity);          

	    T update(D id, T entity);       

	    void delete(D id);               

	    Optional<T> getById(D id);         

	    List<T> getAll();                   

}
