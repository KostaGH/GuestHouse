package service;

import java.sql.SQLException;
import java.util.List;

import exception.DMLException;
import exception.DuplicateNoException;
import exception.RecordNotFoundException;
import vo.GuestHouse;
import vo.Sales;

public interface SellerService {
    void addHouse(GuestHouse guestHouse) throws DuplicateNoException, DMLException; //예외 추가 (지남)
    void updateHouse(GuestHouse guestHouse) throws RecordNotFoundException, DMLException;
    void deleteHouse(int houseNo) throws RecordNotFoundException, DMLException;
    List<GuestHouse> findRegisterHouses(String sellerId) throws RecordNotFoundException, DMLException;
    String getHouseName(int houseNo) throws SQLException ; //메소드 추가 (지남)
    boolean discount(int houseNo);
    List<Sales> searchSalesByYear(int houseNo) throws SQLException; //반환타입 변경
    List<Sales> searchSalesByQuarter(int houseNo) throws SQLException; //반환타입 변경
    List<Sales> searchSalesByMonth(int houseNo) throws SQLException; //반환타입 변경
}
