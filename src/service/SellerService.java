package service;

import vo.GuestHouse;

import java.sql.SQLException;
import java.util.List;

import exception.DMLException;
import exception.DuplicateNoException;
import exception.RecordNotFoundException;

public interface SellerService {
    void addHouse(GuestHouse guestHouse) throws DuplicateNoException, DMLException; //예외 추가 (지남)
    void updateHouse(GuestHouse guestHouse) throws RecordNotFoundException, DMLException;
    void deleteHouse(int houseNo) throws RecordNotFoundException, DMLException;
    List<GuestHouse> findRegisterHouses(String sellerId) throws RecordNotFoundException, DMLException;
    boolean discount(int houseNo);
    long searchSalesByYear(int houseNo) throws SQLException; //반환타입 변경
    long searchSalesByQuarter(int houseNo) throws SQLException; //반환타입 변경
    long searchSalesByMonth(int houseNo) throws SQLException; //반환타입 변경
}
