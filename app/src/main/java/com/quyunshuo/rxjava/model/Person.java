package com.quyunshuo.rxjava.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: QuYunShuo
 * @Time: 2020/4/17
 * @Class: Person
 * @Remark:
 */
public class Person {

    private String name;
    private List<Plan> planList = new ArrayList<>();

    public Person(String name, List<Plan> planList) {
        this.name = name;
        this.planList = planList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Plan> getPlanList() {
        return planList;
    }

    public void setPlanList(List<Plan> planList) {
        this.planList = planList;
    }
}
