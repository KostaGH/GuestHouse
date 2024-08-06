package service;

import vo.GuestHouse;

import java.util.List;

public interface SellerService {
    void addHouse(GuestHouse guestHouse);
    void updateHouse(GuestHouse guestHouse);
    void deleteHouse(int houseNo);
    List<GuestHouse> findRegisterHouses(String sellerId);
    boolean discount(int houseNo);
    int searchSalesByYear(int houseNo);
    int searchSalesByQuarter(int houseNo);
    int searchSalesByMonth(int houseNo);
}
