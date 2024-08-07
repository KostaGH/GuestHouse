package test;

import exception.*;
import service.CustomerService;
import service.CustomerServiceImpl;
import service.SellerService;
import service.SellerServiceImpl;
import vo.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GuestHouseTest {

    static int userType;
    static Scanner sc;
    static boolean loginFlag;

    public static void main(String[] args) {
        sc = new Scanner(System.in);

        //전체 서비스
        while (true) {

            // 로그인 및 회원가입
            User user = loginService();

            while (loginFlag) {
                //유저 타입이 Customer 일 경우 -> Customer 메뉴
                if (userType == 1) {
                    //서비스 생성
                    CustomerServiceImpl customService = CustomerServiceImpl.getInstance();

                    //화면 출력 및 메뉴 선택
                    printMain();
                    int menu = sc.nextInt();

                    switch (menu) {
                        case 1:
                            System.out.println("예약하실 날짜를 입력하세요.");
                            System.out.print("입실 날짜  ex) 2024-05-09 : ");
                            String sDate = sc.next();
                            System.out.print("퇴실 날짜  ex) 2024-05-12 : ");
                            String eDate = sc.next();

                            // 리스트 출력(list)
                            try {
                                List<GuestHouse> allHouse = customService.findALLGuestHouse();
                                for (GuestHouse gh : allHouse) {
                                    System.out.print("[" + gh.getHouseNo() + "] 숙소명: " + gh.getHouseName() + ", 숙소 위치: " + gh.getLocation() + ", 숙소 전화번호: " + gh.getHousePhone());
                                    System.out.println();
                                }
                            } catch (SQLException | HouseNotFoundException e) {
                                throw new RuntimeException(e);
                            }

                            System.out.println("예약하실 숙소 번호와 방 타입을 선택해주세요");
                            System.out.print("예약하실 숙소 번호 : ");
                            int roomNo = sc.nextInt();
                            int roomType = 0;

                            try {
                                System.out.println(customService.descHouse(roomNo, user.getUserId()));

                                for (int i = 0; i < 3; i++) {
                                    GuestHouse house = customService.findByHouseno(roomNo);
                                    System.out.println("[" + house.getRooms().get(i).getType() + "] 숙소명: " + house.getHouseName() + ", 방 타입: " + house.getRooms().get(i).getType() + " 타입, 가격: " + house.getRooms().get(i).getPrice() + "원");
                                }
                                System.out.print("예약하실 방 타입 : ");
                                roomType = sc.nextInt();

                                //예약
                                customService.reserve(new Receipt(roomNo, roomType, user.getUserId(), sDate, eDate), 0);
                                System.out.println("예약 완료");
                            } catch (SQLException | HouseNotFoundException | NeedMoneyException |
                                     CanNotReserveException | ReceiptNotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 2:
                            System.out.println("*** 검색 종류를 선택해주세요. ***");
                            System.out.print("[1] 숙소명 검색  ");
                            System.out.print("[2] 평점순 검색  ");
                            System.out.print("[3] 가격별 검색  ");
                            System.out.println("[4] 지역별 검색");
                            int searchType = sc.nextInt();

                            switch (searchType) {
                                case 1:
                                    System.out.print("검색하실 숙소명을 입력해주세요: ");
                                    String houseName = sc.next();
                                    // List<GuestHouse> list = 숙소명 검색(houseName);
                                    try {
                                        List<GuestHouse> allHouse = customService.findByName(houseName);

                                        for (GuestHouse gh : allHouse) {
                                            System.out.print("[" + gh.getHouseNo() + "] 숙소명: " + gh.getHouseName() + ", 숙소 위치: " + gh.getLocation() + ", 숙소 전화번호: " + gh.getHousePhone());
                                            System.out.println();
                                        }

                                        System.out.print("[1] 예약 하기   ");
                                        System.out.println("[2] 메인 메뉴로 이동");
                                        System.out.print("선택: ");
                                        int select = sc.nextInt();

                                        if (select == 1) //예약 로직
                                            reserve(customService, user);

                                    } catch (SQLException | HouseNotFoundException | NeedMoneyException |
                                             CanNotReserveException | ReceiptNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;

                                case 2:
                                    // 평점순 출력
                                    try {
                                        List<GuestHouse> allHouse = customService.findByGrade();

                                        for (GuestHouse gh : allHouse) {
                                            System.out.print("[" + gh.getHouseNo() + "] 숙소명: " + gh.getHouseName() + ", 숙소 위치: " + gh.getLocation() + ", 숙소 전화번호: " + gh.getHousePhone() + ", 숙소 평점: " + gh.getAvgGrade());
                                            System.out.println();
                                        }

                                        System.out.print("[1] 예약 하기   ");
                                        System.out.println("[2] 메인 메뉴로 이동");
                                        System.out.print("선택: ");
                                        int select = sc.nextInt();

                                        if (select == 1) //예약 로직
                                            reserve(customService, user);

                                    } catch (SQLException | HouseNotFoundException | NeedMoneyException |
                                             CanNotReserveException | ReceiptNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;

                                case 3:
                                    System.out.print("숙소 예약 최소금액을 입력 해주세요 >> ");
                                    int minPrice = sc.nextInt();
                                    System.out.println("숙소 예약 최대금액을 입력 해주세요 >>");
                                    int maxPrice = sc.nextInt();

                                    // 가격별 검색 메소드(minPrice, maxPrice); -> 문제 있는듯?
                                    try {
                                        List<GuestHouse> allHouse = customService.findByPrice(minPrice, maxPrice);
                                        for (GuestHouse gh : allHouse) {
                                            for (Room r : gh.getRooms()) {
                                                System.out.print("[" + gh.getHouseNo() + "] 숙소명: " + gh.getHouseName() + ", 방 타입: " + r.getType() + "타입, 가격 : " + r.getPrice() + "원, 숙소 위치: " + gh.getLocation() + " , 숙소 전화번호: " + gh.getHousePhone());
                                                System.out.println();
                                            }

                                        }
                                    } catch (SQLException | HouseNotFoundException e) {
                                        throw new RuntimeException(e);
                                    }

                                    break;

                                case 4:
                                    System.out.print("검색하실 지역을 입력하세요 [서울] [인천] [경기도] [강원도] >> ");
                                    String location = sc.next();
                                    //지역 검색 메소드(location);
                                    try {
                                        List<GuestHouse> allHouse = customService.findByLocation(location);
                                        for (GuestHouse gh : allHouse) {
                                            System.out.print("[" + gh.getHouseNo() + "] 숙소명: " + gh.getHouseName() + ", 숙소 위치: " + gh.getLocation() + ", 숙소 전화번호: " + gh.getHousePhone());

                                            System.out.println();
                                        }

                                        System.out.print("[1] 예약 하기   ");
                                        System.out.println("[2] 메인 메뉴로 이동");
                                        System.out.print("선택: ");
                                        int select = sc.nextInt();

                                        if (select == 1) //예약 로직
                                            reserve(customService, user);

                                    } catch (SQLException | HouseNotFoundException | NeedMoneyException |
                                             CanNotReserveException | ReceiptNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                            }
                            break;
                        case 3: //마이 페이지
                            System.out.println("\n[1] 예약 내역 확인하기   [2] 잔액 충전하기");
                            int menu3 = sc.nextInt();

                            switch (menu3) {
                                case 1:
                                    try {
                                        //예약 내역 확인하기 -> 평점 및 예약 취소
                                        searchReserve(customService, user);
                                    } catch (SQLException | ReceiptNotFoundException | HouseNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 2:
                                    //잔액 충전
                                    chargeBalance(customService, user);
                                    break;
                            }
                            break;
                        case 4:
                            loginFlag = false;
                            break;
                    }

                } else { //유저 타입 Seller 일 경우 -> Seller 메뉴
                    //서비스 생성
                    SellerServiceImpl sellService = SellerServiceImpl.getInstance();

                    //화면 출력
                    printMain();

                    int menu = sc.nextInt();

                    switch (menu) {
                        case 1:
                            //숙소 등록
                            addHouse(sellService, user);
                            break;
                        case 2:
                            //내가 등록한 숙소 조회
                            findRegisterHouse(sellService, user);

                            System.out.println("\n[1] 숙소 수정하기  [2] 숙소 삭제하기  [3] 숙소 매출 조회  [4] 뒤로 가기");
                            System.out.print("메뉴 입력 : ");
                            int menuSelect = sc.nextInt();
                            switch (menuSelect) {
                                case 1:
                                    //숙소 아이디 및 방 타입 수정은 불가능.. -> 수정 로직 (미)
                                    System.out.print("수정하실 숙소의 번호를 입력하세요 : ");
                                    int updateHouseNo = sc.nextInt();

                                    try {
                                        GuestHouse house = sellService.findByHouseno(updateHouseNo);
                                        List<Room> list = new ArrayList<>();

                                    for (int i = 0; i < 3; i++) {
                                        System.out.print("[수정] " + (i+1) + " 번방의 가격을 입력해주세요 (현재가격 : " + house.getRooms().get(i).getPrice() + "원) : ");
                                        int roomPrice = sc.nextInt();
                                        list.add(new Room((i+1), roomPrice));
                                    }

                                    sellService.updateHouse(updateHouseNo, list);

                                    System.out.println();
                                    } catch (SQLException | HouseNotFoundException | RecordNotFoundException | DMLException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 2:
                                    System.out.print("삭제하실 숙소의 번호를 입력하세요 : ");
                                    int deleteNum = sc.nextInt();
                                    try {
                                        sellService.deleteHouse(deleteNum);
                                    } catch (RecordNotFoundException | DMLException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 3:
                                    System.out.print("매출 조회하려고 하는 숙소 번호를 입력하세요 : ");
                                    int salesHouseNo = sc.nextInt();

                                    System.out.println("[1] 연 매출 조회    [2] 월 매출 조회    [3] 분기 매출 조회");
                                    int selectNum = sc.nextInt();

                                    switch (selectNum) {
                                        case 1:
                                            try {
                                                Sales sales = sellService.searchSalesByYear(salesHouseNo);
                                                System.out.println(sales);
                                            } catch (SQLException e) {
                                                System.out.println(e.getMessage());
                                            }
                                            break;
                                        case 2:
                                            try {
                                                Sales sales = sellService.searchSalesByMonth(salesHouseNo);
                                                System.out.println(sales);
                                            } catch (SQLException e) {
                                                System.out.println(e.getMessage());
                                            }
                                            break;
                                        case 3:
                                            try {
                                                Sales sales = sellService.searchSalesByQuarter(salesHouseNo);
                                                System.out.println(sales);
                                            } catch (SQLException e) {
                                                System.out.println(e.getMessage());
                                            }
                                            break;
                                    }
                                    break;
                                case 4:
                                    break;
                            }
                            break;
                        case 3:
                            loginFlag = false;
                            break;
                    }
                }
            }
         } //전체 서비스 끝
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 로그인 및 회원가입 기능
    static User loginService() {
        User user = null;

        do {
            System.out.println("\n********************************");
            System.out.println("*                              *");
            System.out.println("*    Welcome to Guest House    *");
            System.out.println("*                              *");
            System.out.println("********************************");
            System.out.println("             /\\                 ");
            System.out.println("            /  \\               ");
            System.out.println("           /    \\              ");
            System.out.println("          /______\\             ");
            System.out.println("         /        \\            ");
            System.out.println("        /          \\           ");
            System.out.println("       /            \\          ");
            System.out.println("      /______________\\         ");
            System.out.println();
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("3. 종료");
            System.out.print("메뉴를 선택하세요: ");
            int menu = sc.nextInt();

            switch (menu) {
                case 1:
                    System.out.println("\n********************************");
                    System.out.println("*                              *");
                    System.out.println("*        로그인 시스템         *");
                    System.out.println("*                              *");
                    System.out.println("********************************");
                    System.out.print("사용자 ID :");
                    String userId = sc.next();
                    System.out.print("사용자 Password :");
                    String userPass = sc.next();
                    // User user = 로그인 메소드(userId, userPass);

                    loginFlag = true;
                    if (userId.equals("BAEK")) {
                        user = new Customer("BAEK", "Dohyun", "2222", "222222-2222222", "010-5656-1234", 0, "M");
                        userType = 1;
                    } else {
                        user = new Seller("helpgod", "midmid", "4444", "446644-4444444", "010-7834-9255", 0);
                        userType = 2;
                    }

                    /*if (user != null) {
                        loginFlag = true;
                        //userType 설정..
                        userType = 2;
                    }*/

                    break;
                case 2:
                    System.out.println("\n*** 회원가입 하려고 하시는 유형을 선택하세요 ***");
                    System.out.println(" [1]. Customer    [2]. Seller");
                    userType = sc.nextInt();

                    System.out.println("\n********************************");
                    System.out.println("*                              *");
                    System.out.println("*        회원가입 시스템         *");
                    System.out.println("*                              *");
                    System.out.println("********************************");
                    System.out.print("사용자 ID :");
                    String registerId = sc.next();
                    System.out.print("사용자 Password :");
                    String registerPass = sc.next();
                    // 회원가입 메소드(registerId, registerPass);
                    System.out.println("OOO 님 회원가입이 완료되었습니다.");
                    break;
                case 3: // 프로그램 종료
                    System.exit(1);
            }
        } while(!loginFlag); //로그인에 성공하면 다음 STEP...

        return user;
    }

    static void printMain() {
        if (userType == 1) {
            System.out.println("\n*********************************************");
            System.out.println("*                                            *");
            System.out.println("*       Welcome to Guest House by Customer   *");
            System.out.println("*                                            *");
            System.out.println("*********************************************");
            System.out.println("             ____                 ");
            System.out.println("            /    \\               ");
            System.out.println("           /______\\              ");
            System.out.println("          |  .--.  |             ");
            System.out.println("          |  |  |  |             ");
            System.out.println("          |__|  |__|             ");
            System.out.println();
            System.out.println("1. 예약 하기");
            System.out.println("2. 검색 하기");
            System.out.println("3. 마이 페이지");
            System.out.println("4. 로그아웃");
            System.out.print("메뉴를 선택하세요: ");
        } else {
            System.out.println("\n*********************************************");
            System.out.println("*                                            *");
            System.out.println("*       Welcome to Guest House by Seller   *");
            System.out.println("*                                            *");
            System.out.println("*********************************************");
            System.out.println("             ____                 ");
            System.out.println("            /    \\               ");
            System.out.println("           /______\\              ");
            System.out.println("          |  .--.  |             ");
            System.out.println("          |  |  |  |             ");
            System.out.println("          |__|  |__|             ");
            System.out.println();
            System.out.println("1. 숙소 등록");
            System.out.println("2. 내가 등록한 숙소");
            System.out.println("3. 로그아웃");
            System.out.print("메뉴를 선택하세요: ");
        }
    }

    //잔액 충전
    static void chargeBalance(CustomerService customService, User user) {
        try {
            int curBalance = customService.searchBalance(user.getUserId());
            System.out.println("현재 " + user.getUserName() + "님의 보유 잔액은 " + curBalance + "원 입니다.");
            System.out.print("얼마를 충전하시겠습니까? 입력: ");
            int chargeBalance = sc.nextInt();
            customService.chargeBalance(user.getUserId(), chargeBalance);
            int curBalance2 = customService.searchBalance(user.getUserId());
            System.out.println("충전이 완료 되었습니다. 현재 잔액은 " + curBalance2 + "원 입니다.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static void addHouse(SellerService sellService, User user) {
        System.out.println("등록할 숙소의 정보를 입력합니다.");
        System.out.print("등록할 숙소의 이름을 적어주세요 : ");
        String houseName = sc.next();
        System.out.print("등록할 숙소의 위치를 입력하세요 : ");
        String location = sc.next();
        System.out.print("숙소의 전화번호를 적어주세요 ex) 032-111-2222  : ");
        String housePhone = sc.next();

        System.out.print("숙소의 '1'타입 방의 가격을 설정하세요 : ");
        int roomPrice1 = sc.nextInt();
        System.out.print("숙소의 '2'타입 방의 가격을 설정하세요 : ");
        int roomPrice2 = sc.nextInt();
        System.out.print("숙소의 '3'타입 방의 가격을 설정하세요 : ");
        int roomPrice3 = sc.nextInt();

        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(1, roomPrice1));
        rooms.add(new Room(2, roomPrice2));
        rooms.add(new Room(3, roomPrice3));

        //숙소 등록 메소드(...);
        //String sellerId, String housePhone, String houseName, String location, List<Room> rooms
        try {
            sellService.addHouse(new GuestHouse(user.getUserId(), housePhone, houseName, location, rooms));
        } catch (DuplicateNoException | DMLException e) {
            System.out.println(e.getMessage());
        }
    }

    //내가 등록한 숙소 검색
    static void findRegisterHouse(SellerService sellService, User user) {
        try {
            List<GuestHouse> registerHouses = sellService.findRegisterHouses(user.getUserId());
            int idx = 0;
            for (GuestHouse gh : registerHouses) {
                System.out.print("[" + gh.getHouseNo() + "]  숙소명: " + gh.getHouseName() + ", 방 타입: "  + gh.getRooms().get(idx).getType() + "타입, 가격: " + gh.getRooms().get(idx).getPrice() +
                        ", 숙소 위치: " +  gh.getLocation() + ", 숙소 전화번호: " + gh.getHousePhone());
                System.out.println();
                idx++;
            }
        } catch (RecordNotFoundException | DMLException e) {
            System.out.println(e.getMessage());
        }
    }

    //예약하기 -> 날짜선택, 숙소 번호와 방 타입 선택, 예약 로직까지
    //이 로직 위에 선택 가능한 숙소 리스트 표출...
    static void reserve(CustomerService customService, User user) throws SQLException, HouseNotFoundException, NeedMoneyException, CanNotReserveException, ReceiptNotFoundException {
        System.out.println("예약하실 날짜를 입력하세요.");
        System.out.print("입실 날짜  ex) 2024-05-09 : ");
        String sDate = sc.next();
        System.out.print("퇴실 날짜  ex) 2024-05-12 : ");
        String eDate = sc.next();

        System.out.println("예약하실 숙소 번호와 방 타입을 선택해주세요");
        System.out.print("예약하실 숙소 번호 : ");
        int roomNo = sc.nextInt();
        int roomType = 0;

        System.out.println(customService.descHouse(roomNo, user.getUserId()));

        for (int i = 0; i < 3; i++) {
            GuestHouse house = customService.findByHouseno(roomNo);
            System.out.println("[" + house.getRooms().get(i).getType() + "]  숙소명: " + house.getHouseName() + ", 방 타입: " + house.getRooms().get(i).getType() + " 타입, 가격: " + house.getRooms().get(i).getPrice() + "원");
        }

        System.out.print("예약하실 방 타입 : ");
        roomType = sc.nextInt();

        //예약
        customService.reserve(new Receipt(roomNo, roomType, user.getUserId(), sDate, eDate), 0);
        System.out.println("**** 예약 완료 ****");
    }

    //예약 내역 확인하기 기능
    static void searchReserve(CustomerService customService, User user) throws SQLException, ReceiptNotFoundException, HouseNotFoundException {
        List<Receipt> receipts = customService.listReserve(user.getUserId());
        for (Receipt receipt : receipts) {
            GuestHouse house = customService.findByHouseno(receipt.getHouseNo());
            System.out.println("[숙소번호 " + house.getHouseNo() + "번],  예약번호 " + receipt.getReserveNo() + "번 " + "숙소명 : " + house.getHouseName() + ", 방 타입 : " + receipt.getType() +
                    ", 입실 날짜 : " + receipt.getsDate() + ", 퇴실 날짜 : " + receipt.geteDate() + ", 결제일 : " + receipt.getbDate() + ", 결제 금액 : " + receipt.getTotalPrice() + ", 평점 : " + receipt.getGrade());
        }

        System.out.println("[1] 평점 등록   [2] 예약 취소");
        int selectNum = sc.nextInt();

        if (selectNum == 1) {
            System.out.print("평점을 등록할 예약 번호를 입력해주세요 : ");
            int gradeNo = sc.nextInt();
            System.out.print("평점 입력 (0점 ~ 5점) : ");
            int grade = sc.nextInt();
            customService.grader(gradeNo, grade);
            System.out.println("평점 등록 완료!");
        } else if (selectNum == 2) {
            System.out.print("예약 취소할 예약 번호를 입력해주세요 : ");
            int cancelNo = sc.nextInt();
            customService.reserveCancel(cancelNo);
            System.out.println(cancelNo + "번 예약이 취소되었습니다.");
        }
    }
}
