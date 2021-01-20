package jm.task.core.jdbc;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {

       UserService us = new UserServiceImpl();
       us.createUsersTable();
       us.saveUser("Colin" ,"Farrell", (byte) 44);
       us.saveUser("Tom" ,"Hardy", (byte)43);
       us.saveUser("Вася" ,"Пупкин",(byte) 22);
       us.saveUser("Christina" ,"Aguilera",(byte) 40);
       us.getAllUsers().forEach(System.out::println);
       us.cleanUsersTable();
       us.dropUsersTable();
    }
}
