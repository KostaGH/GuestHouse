package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import config.ServerInfo;
import exception.CanNotReserveException;
import exception.HouseNotFoundException;
import exception.NeedMoneyException;
import exception.ReceiptNotFoundException;
import vo.GuestHouse;
import vo.Receipt;
import vo.Room;

public class CustomerServiceImpl implements CustomerService{

	private static CustomerServiceImpl service = new CustomerServiceImpl();
	
	private CustomerServiceImpl(){
		try {
			Class.forName(ServerInfo.DRIVER_NAME);
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패..");
		}
	}
	
	public static CustomerServiceImpl getInstance() {
		return service;
	}
	
	@Override
	public Connection getConnect() throws SQLException {
		Connection conn = DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASSWORD);
		return conn;
	}

	@Override
	public void closeAll(PreparedStatement ps, Connection conn) throws SQLException {
		if(ps!=null) ps.close();
		if(conn!=null) conn.close();
	}

	@Override
	public void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException {
		if(rs!=null) rs.close();
		closeAll(ps, conn);
	}
	
	@Override
	public List<GuestHouse> findALLGuestHouse() throws SQLException, HouseNotFoundException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String bname = "";
		String nname = "";
		List<GuestHouse> houses = new ArrayList<GuestHouse>();
		GuestHouse house = null;
		List<Room> rooms = new ArrayList<Room>();
		
		try {
			conn = getConnect();
			String query = "SELECT house_no, type, sel_id, price, house_phone, house_name, location FROM guesthouse";
			ps = conn.prepareStatement(query);
			
			
			rs = ps.executeQuery();
			
			if(!rs.isBeforeFirst()) throw new HouseNotFoundException("[ Result Error Message ] => 해당 이름의 게스트하우스가 존재하지 않습니다.");
			else {
				while(rs.next()) {
					nname = rs.getString("house_no");
					if(!(nname.equals(bname))&&!(bname.equals(""))) {
						List<Room> r = new ArrayList<Room>();
						r.addAll(rooms);
						house.setRooms(r);
						houses.add(house);
						rooms.clear();
					}
					rooms.add(new Room(rs.getInt("type"),rs.getInt("price")));
					bname = nname;
					house = new GuestHouse(rs.getInt("house_no"), rs.getString("sel_id"), rs.getString("house_phone"), rs.getString("house_name"), rs.getString("location"), rooms);
				}
				houses.add(house);
			}
			return houses;
			
		} finally {			
			closeAll(rs, ps, conn);
		}
	}
	
	@Override
	public List<GuestHouse> findByName(String name) throws SQLException, HouseNotFoundException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String bname = "";
		String nname = "";
		List<GuestHouse> houses = new ArrayList<GuestHouse>();
		GuestHouse house = null;
		List<Room> rooms = new ArrayList<Room>();
		
		try {
			conn = getConnect();
			String query = "SELECT house_no, type, sel_id, price, house_phone, house_name, location FROM guesthouse WHERE house_name LIKE ?";
			ps = conn.prepareStatement(query);
			
			ps.setString(1, "%" + name + "%");
			
			rs = ps.executeQuery();
			
			if(!rs.isBeforeFirst()) throw new HouseNotFoundException("[ Result Error Message ] => 해당 이름의 게스트하우스가 존재하지 않습니다.");
			else {
				while(rs.next()) {
					nname = rs.getString("house_no");
					if(!(nname.equals(bname))&&!(bname.equals(""))) {
						List<Room> r = new ArrayList<Room>();
						r.addAll(rooms);
						house.setRooms(r);
						houses.add(house);
						rooms.clear();
					}
					rooms.add(new Room(rs.getInt("type"),rs.getInt("price")));
					bname = nname;
					house = new GuestHouse(rs.getInt("house_no"), rs.getString("sel_id"), rs.getString("house_phone"), rs.getString("house_name"), rs.getString("location"), rooms);
				}
				houses.add(house);
			}
			return houses;
			
		} finally {			
			closeAll(rs, ps, conn);
		}
	}

	@Override
	public List<GuestHouse> findByGrade() throws SQLException, HouseNotFoundException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String bname = "";
		String nname = "";
		List<GuestHouse> houses = new ArrayList<GuestHouse>();
		GuestHouse house = null;
		List<Room> rooms = new ArrayList<Room>();
		
		try {
			conn = getConnect();
			String query = "SELECT g.house_no, g.type, g.sel_id, g.price, g.house_phone, g.house_name, g.location, a.avggrade "
					+ "FROM guesthouse g, (SELECT house_no, round(avg(grade),1) avggrade FROM receipt GROUP BY house_no) a "
					+ "WHERE g.house_no=a.house_no ORDER BY a.avggrade DESC";
			ps = conn.prepareStatement(query);
			
			rs = ps.executeQuery();
			
			if(!rs.isBeforeFirst()) throw new HouseNotFoundException("[ Result Error Message ] => 게스트하우스가 존재하지 않습니다.");
			else {
				while(rs.next()) {
					nname = rs.getString("house_no");
					if(!(nname.equals(bname))&&!(bname.equals(""))) {
						List<Room> r = new ArrayList<Room>();
						r.addAll(rooms);
						house.setRooms(r);
						houses.add(house);
						rooms.clear();
					}
					rooms.add(new Room(rs.getInt("g.type"),rs.getInt("g.price")));
					bname = nname;
					house = new GuestHouse(rs.getInt("g.house_no"), rs.getString("g.sel_id"), rs.getString("g.house_phone"), rs.getString("g.house_name"), rs.getString("g.location"), rs.getDouble("a.avggrade"), rooms);
				}
				houses.add(house);
			}
			return houses;
			
		} finally {			
			closeAll(rs, ps, conn);
		}
	}

	@Override
	public List<GuestHouse> findByPrice(int sPrice, int ePrice) throws SQLException, HouseNotFoundException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String bname = "";
		String nname = "";
		List<GuestHouse> houses = new ArrayList<GuestHouse>();
		GuestHouse house = null;
		List<Room> rooms = new ArrayList<Room>();
		
		try {
			conn = getConnect();
			String query = "SELECT house_no, type, sel_id, price, house_phone, house_name, location FROM guesthouse WHERE price BETWEEN ? AND ?";
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, sPrice);
			ps.setInt(2, ePrice);
			
			rs = ps.executeQuery();
			
			if(!rs.isBeforeFirst()) throw new HouseNotFoundException("[ Result Error Message ] => 해당 가격대의 게스트하우스가 존재하지 않습니다.");
			else {
				while(rs.next()) {
					nname = rs.getString("house_no");
					if(!(nname.equals(bname))&&!(bname.equals(""))) {
						List<Room> r = new ArrayList<Room>();
						r.addAll(rooms);
						house.setRooms(r);
						houses.add(house);
						rooms.clear();
					}
					rooms.add(new Room(rs.getInt("type"),rs.getInt("price")));
					bname = nname;
					house = new GuestHouse(rs.getInt("house_no"), rs.getString("sel_id"), rs.getString("house_phone"), rs.getString("house_name"), rs.getString("location"), rooms);
				}
				houses.add(house);
			}
			return houses;
			
		} finally {			
			closeAll(rs, ps, conn);
		}
	}

	@Override
	public List<GuestHouse> findByLocation(String location) throws SQLException, HouseNotFoundException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String bname = "";
		String nname = "";
		List<GuestHouse> houses = new ArrayList<GuestHouse>();
		GuestHouse house = null;
		List<Room> rooms = new ArrayList<Room>();
		
		try {
			conn = getConnect();
			String query = "SELECT house_no, type, sel_id, price, house_phone, house_name, location FROM guesthouse WHERE location = ?";
			ps = conn.prepareStatement(query);
			
			ps.setString(1, location);
			
			rs = ps.executeQuery();
			
			if(!rs.isBeforeFirst()) throw new HouseNotFoundException("[ Result Error Message ] => 해당 가격대의 게스트하우스가 존재하지 않습니다.");
			else {
				while(rs.next()) {
					nname = rs.getString("house_no");
					if(!(nname.equals(bname))&&!(bname.equals(""))) {
						List<Room> r = new ArrayList<Room>();
						r.addAll(rooms);
						house.setRooms(r);
						houses.add(house);
						rooms.clear();
					}
					rooms.add(new Room(rs.getInt("type"),rs.getInt("price")));
					bname = nname;
					house = new GuestHouse(rs.getInt("house_no"), rs.getString("sel_id"), rs.getString("house_phone"), rs.getString("house_name"), rs.getString("location"), rooms);
				}
				houses.add(house);
			}
			return houses;
			
		} finally {			
			closeAll(rs, ps, conn);
		}
	}
	
	@Override
	public boolean isDangol(String custId, int houseno) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean dangol = false;
		
		try {
			conn = getConnect();
			String query = "SELECT count(reserv_no) FROM receipt WHERE cust_id = ? AND house_no = ?";
			ps = conn.prepareStatement(query);
			
			ps.setString(1, custId);
			ps.setInt(2, houseno);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				if(rs.getInt(1)>=2) dangol = true;
			}
			return dangol;
			
		} finally {			
			closeAll(rs, ps, conn);
		}
	}

	@Override
	public void reserve(Receipt receipt, int discount) throws SQLException, NeedMoneyException, CanNotReserveException {
		List<Receipt> receipts = searchReserveByHouseno(receipt.getHouseNo(), receipt.getType());
		if(!receipts.isEmpty()) {
			for(Receipt r : receipts) {
				if(r.geteDate().compareTo(receipt.geteDate())>=0&&receipt.geteDate().compareTo(r.getsDate())>=0) throw new CanNotReserveException();
				else if(r.geteDate().compareTo(receipt.getsDate())>=0&&receipt.getsDate().compareTo(r.getsDate())>=0) throw new CanNotReserveException();
				else if(receipt.geteDate().compareTo(r.geteDate())>=0&&r.getsDate().compareTo(receipt.getsDate())>=0) throw new CanNotReserveException();
			}
		}
		int totalprice = receipt.geteDate().compareTo(receipt.getsDate())*searchRoomPrice(receipt.getHouseNo(), receipt.getType());
		totalprice -= totalprice*discount;
		if(totalprice > searchBalance(receipt.getCustId())) throw new NeedMoneyException();
		updateCustBalance(receipt.getCustId(), totalprice);
		updateSelBalance(searchSelId(receipt.getHouseNo()), totalprice);
		
		receipt.setTotalPrice(totalprice);
		receipt.setbDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		String query = "INSERT INTO receipt(house_no, type, cust_id, start_date, end_date, buy_date, total_price) VALUES(?,?,?,?,?,?,?)";
		try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)){
			ps.setInt(1, receipt.getHouseNo());
			ps.setInt(2, receipt.getType());
			ps.setString(3, receipt.getCustId());
			ps.setString(4, receipt.getsDate());
			ps.setString(5, receipt.geteDate());
			ps.setString(6, receipt.getbDate());
			ps.setInt(7, receipt.getTotalPrice());
			
			ps.executeUpdate();
		}
		
	}
	
	@Override
	public int searchRoomPrice(int houseno, int type) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnect();
			String query = "SELECT price FROM guesthouse WHERE house_no = ? AND type=?";
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, houseno);
			ps.setInt(2, type);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("price");				
			}
			return 0;
			
		} finally {			
			closeAll(rs, ps, conn);
		}
	}
	
	@Override
	public int searchBalance(String custId) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnect();
			String query = "SELECT cust_balance FROM customer WHERE cust_id = ?";
			ps = conn.prepareStatement(query);
			
			ps.setString(1, custId);
			
			rs = ps.executeQuery();
			if(rs.next()) {
				return rs.getInt("cust_balance");				
			}
			return 0;
		} finally {			
			closeAll(rs, ps, conn);
		}
	}

	@Override
	public void reserveCancel(int reserveNo) throws SQLException, ReceiptNotFoundException {
		String query = "DELETE FROM receipt WHERE reserv_no=?";
		try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)){
			ps.setInt(1, reserveNo);
			Receipt receipt = searchReserve(reserveNo);
			if(ps.executeUpdate()==0) throw new ReceiptNotFoundException("[ Result Error Message ] => 해당 예약내역이 존재하지 않습니다.");
			chargeBalance(receipt.getCustId(), receipt.getTotalPrice());
			chargeSelBalance(searchSelId(receipt.getHouseNo()), receipt.getTotalPrice());
		}
		
	}

	@Override
	public void chargeBalance(String custId, int balance) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnect();
			String query = "UPDATE customer SET cust_balance = cust_balance + ? WHERE cust_id = ?";
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, balance);
			ps.setString(2, custId);
			
			ps.executeUpdate();
			
		} finally {			
			closeAll(ps, conn);
		}		
	}
	
	@Override
	public void updateCustBalance(String custId, int totalprice) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnect();
			String query = "UPDATE customer SET cust_balance = cust_balance - ? WHERE cust_id = ?";
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, totalprice);
			ps.setString(2, custId);
			
			ps.executeUpdate();
			
		} finally {			
			closeAll(ps, conn);
		}
	}
	
	@Override
	public void updateSelBalance(String selId, int totalprice) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnect();
			String query = "UPDATE seller SET sel_balance = sel_balance + ? WHERE sel_id = ?";
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, totalprice);
			ps.setString(2, selId);
			
			ps.executeUpdate();
			
		} finally {			
			closeAll(ps, conn);
		}
	}
	
	@Override
	public void chargeSelBalance(String selId, int totalprice) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnect();
			String query = "UPDATE seller SET sel_balance = sel_balance - ? WHERE sel_id = ?";
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, totalprice);
			ps.setString(2, selId);
			
			ps.executeUpdate();
			
		} finally {			
			closeAll(ps, conn);
		}
	}
	
	@Override
	public String searchSelId(int houseno) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnect();
			String query = "SELECT sel_Id FROM guesthouse WHERE house_no = ?";
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, houseno);
			
			rs = ps.executeQuery();
			if(rs.next()) {
				return rs.getString("sel_Id");
			}
			return "";
		} finally {			
			closeAll(rs, ps, conn);
		}
	}

	@Override
	public String descHouse(int houseNo) {
		
		return null;
	}

	@Override
	public int rateRevisit(int houseNo) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnect();
			String query = "SELECT count(d)\n"
					+ "FROM (SELECT cust_id, count(reserv_no) d FROM receipt WHERE house_no = ? GROUP BY cust_id) f\n"
					+ "WHERE d >= 2";
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, houseNo);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				return 0;
			}
			return 0;
			
		} finally {			
			closeAll(rs, ps, conn);
		}
	}

	@Override
	public int rateGender(int houseNo) {
		
		return 0;
	}

	@Override
	public int marketPrice(int houseNo) {
		
		return 0;
	}

	@Override
	public double showGrade(int houseNo) {
		
		return 0;
	}

	@Override
	public int visitCount(int houseNo) {
		
		return 0;
	}

	@Override
	public void grader(int reserveNo, int grade) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnect();
			String query = "UPDATE receipt SET grade = ? WHERE reserv_no = ?";
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, grade);
			ps.setInt(2, reserveNo);
			
			ps.executeUpdate();
			
		} finally {			
			closeAll(ps, conn);
		}
	}

	@Override
	public List<Receipt> listReserve(String custId) throws SQLException, ReceiptNotFoundException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Receipt> receipts = new ArrayList<Receipt>();
		
		try {
			conn = getConnect();
			String query = "SELECT reserv_no, house_no, type, cust_id, grade, start_date, end_date, buy_date, total_price FROM receipt WHERE cust_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, custId);
			
			rs = ps.executeQuery();
			
			if(!rs.isBeforeFirst()) throw new ReceiptNotFoundException("[ Result Error Message ] => 예약내역이 존재하지 않습니다.");
			else {
				while(rs.next()) {
					receipts.add(new Receipt(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9)));
				}
			}
			return receipts;
			
		} finally {			
			closeAll(rs, ps, conn);
		}
	}
	
	@Override
	public Receipt searchReserve(int reserveno) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Receipt receipt = null;
		
		try {
			conn = getConnect();
			String query = "SELECT reserv_no, house_no, type, cust_id, grade, start_date, end_date, buy_date, total_price FROM receipt WHERE reserv_no = ?";
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, reserveno);
			
			rs = ps.executeQuery();
			if(rs.next()) {
				receipt = new Receipt(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9));
			}
			return receipt;
		} finally {			
			closeAll(rs, ps, conn);
		}
	}
	
	@Override
	public List<Receipt> searchReserveByHouseno(int houseno, int type) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Receipt> receipts = new ArrayList<Receipt>();
		
		try {
			conn = getConnect();
			String query = "SELECT start_date, end_date FROM receipt WHERE house_no = ? AND type=?";
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, houseno);
			ps.setInt(2, type);
			
			rs = ps.executeQuery();
			if(!rs.isBeforeFirst()) return receipts;
			else {
				while(rs.next()) {
					receipts.add(new Receipt(rs.getString(1), rs.getString(2)));
				}
			}
			return receipts;
		} finally {			
			closeAll(rs, ps, conn);
		}
	}

}
