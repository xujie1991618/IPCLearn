package com.crayfish.ipclearn.provider;

/**
 * ============================
 * 作    者：crayfish(徐杰)
 * 创建日期：2017/12/18.
 * 描    述：
 * 修改历史：
 * ===========================
 */

public class User {
    public int id;
    public String name;
    public boolean sex;

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

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "id:" + id + "name:" + name + "sex:" + sex;
    }
}
