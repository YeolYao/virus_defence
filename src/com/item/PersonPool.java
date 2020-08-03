package com.item;

import java.util.ArrayList;
import java.util.List;

//多人信息
public class PersonPool {
    private static PersonPool personPool=new PersonPool();
    private PersonPool(){}
    public static PersonPool getPersonPool(){
        return personPool;
    }

    private List<Person> personList=new ArrayList<Person>();
    private List<Person> virusList=new ArrayList<Person>();
    private List<Person> observerList=new ArrayList<Person>();

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public List<Person> getVirusList() {
        return virusList;
    }

    public void setVirusList(List<Person> virusList) {
        this.virusList = virusList;
    }

    public List<Person> getObserverList() {
        return observerList;
    }

    public void setObserverList(List<Person> observerList) {
        this.observerList = observerList;
    }
}
