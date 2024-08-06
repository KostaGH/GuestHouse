package service;

import vo.GuestHouse;
import vo.Receipt;

import java.util.List;

public interface CustomerService {
    List<GuestHouse> findByName(String name);
    List<GuestHouse> findByGrade();
    List<GuestHouse> findByPrice(int sPrice, int ePrice);
    List<GuestHouse> findByLocation(String location);

    void reserve(Receipt receipt);
    void reserveCancel(int reserveNo);
    void chargeBalance(int custId, int balance);

    String descHouse(int houseNo);
    int rateRevisit(int houseNo);
    int rateGender(int houseNo);
    int marketPrice(int houseNo);
    double showGrade(int houseNo);
    int visitCount(int houseNo);

    void grader(int reserveNo, int grade);
    List<Receipt> listReserve(String custId);

}
