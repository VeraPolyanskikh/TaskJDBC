package jm.task.core.jdbc;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {

       UserService us = new UserServiceImpl();
       us.createUsersTable();
       us.saveUser("Colin" ,"Farrell", (byte) 44);
       us.saveUser("Tom" ,"Hardy", (byte)43);
       us.saveUser(null ,"Пупкин",(byte) 22);
       us.saveUser("Christina" ,"Aguilera",(byte) 40);
       //us.removeUserById(5);
       //us.removeUserById(3);
       System.out.println("the table content:");
       us.getAllUsers().forEach(System.out::println);
       us.cleanUsersTable();
       us.dropUsersTable();
    }
}
