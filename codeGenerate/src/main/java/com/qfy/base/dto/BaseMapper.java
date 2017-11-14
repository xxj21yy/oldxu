package com.qfy.base.dto;

import java.util.List;

public interface BaseMapper<T> {


    /**
     * 获取数据条数
     *
     * @param entity
     * @return
     */
    long count(T entity);

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    T get(long id);

    /**
     * 获取单条数据
     *
     * @param entity
     * @return
     */
    T get(T entity);

    /**
     * @param entity
     * @return
     */
    List<T> findList(T entity);

    /**
     * 查询所有数据列表
     *
     * @param entity
     * @return
     */
    List<T> findAllList(T entity);

    /**
     * 查询所有数据列表
     *
     * @return
     * @see List<T> findAllList(T entity)
     */
    List<T> findAllList();

    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    int insert(T entity);

    /**
     * 更新数据
     *
     * @param entity
     * @return
     */
    int update(T entity);

    /**
     * 删除数据（一般为逻辑删除，更新yn字段为1）
     *
     * @param id
     * @return
     * @see int delete(T entity)
     */
    int delete(long id);

    /**
     * 删除数据（一般为逻辑删除，更新yn字段为1）
     *
     * @param entity
     * @return
     */
    int delete(T entity);
}
