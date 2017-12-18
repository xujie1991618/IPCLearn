package com.crayfish.ipclearn;

import java.io.Serializable;

/**
 * ============================
 * 作    者：crayfish(徐杰)
 * 创建日期：2017/12/13.
 * 描    述：
 * 修改历史：
 * ===========================
 */

public class SerializableItem implements Serializable {
    private static final long serialVersionUID = -2623171117769186822L;

    private int id;
    private String name;

    public SerializableItem(int id ,String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
