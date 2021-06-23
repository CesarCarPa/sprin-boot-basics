package com.cacp.learning.lesson_01.persistence;

import com.cacp.learning.lesson_01.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class UserHardcodePersistence {

    private static List<User> users = new ArrayList();
    private static int userCount;

    public UserHardcodePersistence(){
        users.add(new User(1, "Cesar", new Date()));
        users.add(new User(2, "Monica", new Date()));
        users.add(new User(3, "Camila", new Date()));
        userCount = users.size();
    }

    public List<User> findAll(){
        return users;
    }

    public User addUser(User user){
        user.setId(++userCount);
        users.add(user);
        return user;
    }

    public User findUser(int id){
        return users.stream().filter(u -> u.getId() == id).findAny().orElse(null);
    }

    public User deleteUser(int id){
        Iterator<User> iterator = users.iterator();
        while(iterator.hasNext()){
            User user = iterator.next();
            if(user.getId() == id){
                iterator.remove();
                return user;
            }
        }
        return null;
    }
}
