package za.ac.cput.marginhotelmanagement.service;

import java.util.List;

public interface IService<T, ID> {
    T create(T t);
    T read(ID id);
    T update(T t);
    boolean delete(T t);
    List<T> findAll();
}
