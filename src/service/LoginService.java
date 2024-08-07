package service;

import vo.Customer;
import vo.User;

import java.sql.SQLException;

public interface LoginService {
    void addUser(int num, User user) throws SQLException;
    User login(int num, String id, String password) throws Exception;
    void deleteUser(int num, String userId, String password);
    User updateUser(int num, User user);
}
