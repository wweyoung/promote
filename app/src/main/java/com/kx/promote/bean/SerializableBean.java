package com.kx.promote.bean;

import java.io.Serializable;

public class SerializableBean<T> implements Serializable {
    private T bean;
    public SerializableBean(T bean){
        this.bean = bean;
    }

    public T getBean() {
        return bean;
    }

    public void setBean(T bean) {
        this.bean = bean;
    }
}
