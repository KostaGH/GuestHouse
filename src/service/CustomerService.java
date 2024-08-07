package service;

import vo.GuestHouse;
import vo.Receipt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import exception.CanNotReserveException;
import exception.HouseNotFoundException;
import exception.NeedMoneyException;
import exception.ReceiptNotFoundException;

public interface CustomerService {List<GuestHouse> findByName(String name) throws SQLException, HouseNotFoundException;
	GuestHouse findByHouseno(int houseno) throws SQLException, HouseNotFoundException;
    List<GuestHouse> findByGrade() throws SQLException, HouseNotFoundException;
    List<GuestHouse> findByPrice(int sPrice, int ePrice) throws SQLException, HouseNotFoundException;
    List<GuestHouse> findByLocation(String location) throws SQLException, HouseNotFoundException;

    void reserve(Receipt receipt, int discount) throws SQLException, NeedMoneyException, CanNotReserveException;
    int searchBalance(String custId) throws SQLException;
    void reserveCancel(int reserveNo) throws SQLException, ReceiptNotFoundException;
    void chargeBalance(String custId, int balance) throws SQLException;

    String descHouse(int houseNo, String custId) throws SQLException, ReceiptNotFoundException, HouseNotFoundException;

    void grader(int reserveNo, int grade) throws SQLException;
    List<Receipt> listReserve(String custId) throws SQLException, ReceiptNotFoundException;
	List<GuestHouse> findALLGuestHouse() throws SQLException, HouseNotFoundException;
	Receipt searchReserve(int reserveno) throws SQLException;

}
