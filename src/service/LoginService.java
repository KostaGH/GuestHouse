package service;

import vo.User;

public interface LoginService {
    void addUser(User user);
    User login(String id, String password);
    void deleteUser(String userId, String password);
    User updateUser(User user);
    String findUserId(String ssn);
    boolean findPass(String id, String ssn);
    void updatePass(String password);
}
