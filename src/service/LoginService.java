package service;

import vo.Customer;
import vo.User;

public interface LoginService {
    void addUser(int num, User user);
    User login(int num, String id, String password);
    void deleteUser(int num, String userId, String password);
    User updateUser(int num, User user);
}
