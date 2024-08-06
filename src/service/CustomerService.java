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

public interface CustomerService {
    List<GuestHouse> findByName(String name) throws SQLException, HouseNotFoundException;
    List<GuestHouse> findByGrade() throws SQLException, HouseNotFoundException;
    List<GuestHouse> findByPrice(int sPrice, int ePrice) throws SQLException, HouseNotFoundException;
    List<GuestHouse> findByLocation(String location) throws SQLException, HouseNotFoundException;

    boolean isDangol(String custId, int houseno) throws SQLException, HouseNotFoundException;
    void reserve(Receipt receipt, int discount) throws SQLException, NeedMoneyException, CanNotReserveException;
    int searchRoomPrice(int houseno, int type) throws SQLException;
    int searchBalance(String custId) throws SQLException;
    void reserveCancel(int reserveNo) throws SQLException, ReceiptNotFoundException;
    void chargeBalance(String custId, int balance) throws SQLException;
    void updateCustBalance(String custId, int totalprice) throws SQLException;
    void updateSelBalance(String custId, int totalprice) throws SQLException;
    void chargeSelBalance(String selId, int totalprice) throws SQLException;
    String searchSelId(int houseno) throws SQLException;

    String descHouse(int houseNo);
    int rateRevisit(int houseNo) throws SQLException;
    int rateGender(int houseNo);
    int marketPrice(int houseNo);
    double showGrade(int houseNo);
    int visitCount(int houseNo);

    void grader(int reserveNo, int grade) throws SQLException;
    List<Receipt> listReserve(String custId) throws SQLException, ReceiptNotFoundException;
	Connection getConnect() throws SQLException;
	void closeAll(PreparedStatement ps, Connection conn) throws SQLException;
	void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException;
	List<GuestHouse> findALLGuestHouse() throws SQLException, HouseNotFoundException;
	Receipt searchReserve(int reserveno) throws SQLException;
	List<Receipt> searchReserveByHouseno(int houseno, int type) throws SQLException;

}
