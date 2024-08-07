package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import config.ServerInfo;
import exception.DMLException;
import exception.DuplicateNoException;
import exception.HouseNotFoundException;
import exception.RecordNotFoundException;
import vo.GuestHouse;
import vo.Room;
import vo.Sales;


public class SellerServiceImpl implements SellerService{
	//싱글톤
	private static SellerServiceImpl service = new SellerServiceImpl();
	private SellerServiceImpl() {
		try {
			Class.forName(ServerInfo.DRIVER_NAME);
			System.out.println("1. 드라이버 로딩 성공");
			
		} catch (ClassNotFoundException e) {
			System.out.println("1. 드라이버 로딩 실패");
		}
	}
	public static SellerServiceImpl getInstance() {
		return service;
	}
	
	////////////////////////////////공통 로직(고정적인 부분) //////////////////////////////////
	public Connection getConnection() throws SQLException{
	Connection conn = DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASSWORD);
	return conn;
	}
	
	public void close(Connection conn) throws SQLException {
	if(conn != null) conn.close();
	}
	
	public void close(PreparedStatement ps) throws SQLException {
	if(ps != null) ps.close();
	}
	
	public void close(ResultSet rs) throws SQLException {
	if(rs != null) rs.close();
	}

	private void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException {
		if(rs!=null) rs.close();
		if (ps != null) ps.close();
		if (conn != null) conn.close();
	}

	////////////////////////////////비즈니스 로직(가변적인 부분) //////////////////////////////////
	//1. 게스트하우스 추가
	@Override
	public void addHouse(GuestHouse guestHouse) throws DuplicateNoException, DMLException{
		String query = "SELECT max(house_no) 최고값 FROM guesthouse";
		int maxNo = 0;
		
		try(Connection conn = getConnection(); 
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery()) {
			
			if(rs.next()) {
				maxNo = rs.getInt("최고값");
			}else {
				maxNo = 1;
			}
	
		}catch (SQLException e) {
			throw new DMLException("[ Result Error Message ] => "+"책 등록 중 문제가 발생해 등록이 이뤄지지 않았습니다."); 
		}
		
		for(int i=0; i<3; i++) {
			String query2 = "INSERT INTO guesthouse (house_no, type, sel_id, price, house_phone, house_name, location)"
							+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
	
			try(Connection conn = getConnection(); 
				PreparedStatement ps = conn.prepareStatement(query2)) {
				
				ps.setInt(1, maxNo+1);
				ps.setInt(2, guestHouse.getRooms().get(i).getType());
				ps.setString(3, guestHouse.getSellerId());
				ps.setInt(4, guestHouse.getRooms().get(i).getPrice());
				ps.setString(5, guestHouse.getHousePhone());
				ps.setString(6, guestHouse.getHouseName());
				ps.setString(7, guestHouse.getLocation());
				
				ps.executeUpdate();
			
				System.out.println("[ Result OK Message ] => "+"새 게스트하우스를 등록했습니다.");
			
			}catch (SQLIntegrityConstraintViolationException e) {
				throw new DuplicateNoException("[ Result Error Message ] => "+"이미 등록된 게스트하우스거나 판매자ID가 존재하지 않습니다. 다시 확인해주세요"); 
			}catch (SQLException e) {
				throw new DMLException("[ Result Error Message ] => "+"등록 중 문제가 발생해 등록이 이뤄지지 않았습니다."); 
			}
		}
	}
	
	//2. 게스트하우스 수정
	@Override
	public void updateHouse(int houseNo, List<Room> list) throws RecordNotFoundException, DMLException{
		for(int i=0; i<3; i++) {
//			String query = "UPDATE guesthouse SET sel_id=?, price=?, house_phone=?, house_name=?, location=? "
//							+ "WHERE house_no=? AND type=?";

			//수정할 값 : 방의 가격
			String query = "UPDATE guesthouse SET price = ? WHERE house_no=? AND type=?";
	
			try(Connection conn = getConnection(); 
				PreparedStatement ps = conn.prepareStatement(query)) {
				
				ps.setInt(1, list.get(i).getPrice());
				ps.setInt(2, houseNo);
				ps.setInt(3, list.get(i).getType());

				if(ps.executeUpdate() ==0)
					throw new RecordNotFoundException("[ Result Error Message ] => "+"정보를 수정할 게스트하우스가 존재하지 않거나 판매자 ID가 존재하지 않습니다.");

				System.out.println("[ Result OK Message ] => "+"게스트하우스 정보를 수정했습니다.");

			}catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new DMLException("[ Result Error Message ] => "+"수정 중 문제가 발생해 수정이 이뤄지지 않았습니다.");
			}
		}
	}
	
	//3. 게스트하우스 삭제
	@Override
	public void deleteHouse(int houseNo) throws RecordNotFoundException, DMLException{
		String query = "DELETE FROM guesthouse WHERE house_no=?";
		
		try(Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query))  {
			
			ps.setInt(1, houseNo);
			
			if(ps.executeUpdate() ==0) {
				throw new RecordNotFoundException("[ Result Error Message ] => "+"삭제할 게스트하우스가 존재하지 않습니다.");
			}
			System.out.println("[ Result OK Message ] => "+" 게스트하우스를 삭제했습니다.");

		}catch (SQLException e) {
			throw new DMLException("[ Result Error Message ] => "+"삭제 중 문제가 발생해 삭제가 이뤄지지 않았습니다.");
		}
	}

	@Override
	public GuestHouse findByHouseno(int houseno) throws SQLException, HouseNotFoundException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GuestHouse house = null;
		List<Room> rooms = new ArrayList<Room>();

		try {
			conn = getConnection();
			String query = "SELECT house_no, type, sel_id, price, house_phone, house_name, location FROM guesthouse WHERE house_no = ?";
			ps = conn.prepareStatement(query);

			ps.setInt(1, houseno);

			rs = ps.executeQuery();

			if(!rs.isBeforeFirst()) throw new HouseNotFoundException("[ Result Error Message ] => 해당 이름의 게스트하우스가 존재하지 않습니다.");
			else {
				while(rs.next()) {
					rooms.add(new Room(rs.getInt("type"),rs.getInt("price")));
					house = new GuestHouse(rs.getInt("house_no"), rs.getString("sel_id"), rs.getString("house_phone"), rs.getString("house_name"), rs.getString("location"), rooms);
				}
			}
			return house;

		} finally {
			closeAll(rs, ps, conn);
		}
	}

	//4. 판매자가 운영 중인 게스트하우스 목록 조회
	@Override
	public List<GuestHouse> findRegisterHouses(String sellerId) throws RecordNotFoundException, DMLException {
		List<GuestHouse> ghList = new ArrayList<>();
		List<Room> rList = new ArrayList<>();
		String query = "SELECT house_no, type, sel_id, price, house_phone, house_name, location FROM guesthouse WHERE sel_id=?";
		
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query);){
				
			ps.setString(1, sellerId);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				rList.add(new Room(rs.getInt("type"),rs.getInt("price")));

				ghList.add(new GuestHouse(rs.getInt("house_no"),
										rs.getString("sel_id"),
										rs.getString("house_phone"),
										rs.getString("house_name"),
										rs.getString("location"),
										rList));
			}
			
			if(ghList.isEmpty()) {
				throw new RecordNotFoundException("[ Result Error Message ] => "+"검색할 게스트하우스가 존재하지 않습니다.");
			}
			
		}catch (SQLException e) {
			throw new DMLException("[ Result Error Message ] => "+"게스트하우스 검색 중 문제가 발생해 검색이 이뤄지지 않았습니다.");
		}
		
		return ghList;
	}
	
	//5. 할인 적용 (선민님이 작성)
	@Override
	public boolean discount(int houseNo) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//메소드 추가 - 게하 이름 찾기
	public String getHouseName(int houseNo) throws SQLException{
		String hName = null;
		String query = "select distinct(house_name) house_name, house_no from guesthouse where house_no=?";
		
		try(Connection conn = getConnection(); 
			PreparedStatement ps = conn.prepareStatement(query)) {
				
			ps.setInt(1, houseNo);
				
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				hName = rs.getString("house_name");
			}
		}catch (SQLException e) {
			throw new SQLException("[ Result Error Message ] => "+"매출 계산 중 문제가 발생했습니다.");
		}
		return hName;
	}
	
	//6. 연매출
	@Override
	public List<Sales> searchSalesByYear(int houseNo) throws SQLException{
		List<Sales> yList = new ArrayList<>();
		String query = "SELECT year(start_date) 분기, sum(total_price) 합계 "
						+ "FROM receipt "
						+ "WHERE house_no=? "
						+ "group by 1";
		
		try(Connection conn = getConnection(); 
			PreparedStatement ps = conn.prepareStatement(query)) {
			
			ps.setInt(1, houseNo);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				yList.add(new Sales(rs.getInt("분기"), getHouseName(houseNo), rs.getLong("합계")));
			}
			if(yList.isEmpty()) {
				yList.add(new Sales(getHouseName(houseNo), 0));
			}

		}catch (SQLException e) {
			throw new SQLException("[ Result Error Message ] => "+"매출 계산 중 문제가 발생했습니다.");
		}
		return yList;
	}
	
	//7. 분기매출
	@Override
	public List<Sales> searchSalesByQuarter(int houseNo) throws SQLException{
		List<Sales> qList = new ArrayList<>();
		String query = "SELECT quarter(start_date) 분기, sum(total_price) 합계 "
						+ "FROM receipt "
						+ "WHERE house_no=? "
						+ "group by 1";
		
		try(Connection conn = getConnection(); 
			PreparedStatement ps = conn.prepareStatement(query)) {
			
			ps.setInt(1, houseNo);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				qList.add(new Sales(rs.getInt("분기"), getHouseName(houseNo), rs.getLong("합계")));
			}
			if(qList.isEmpty()) {
				qList.add(new Sales(getHouseName(houseNo), 0));
			}

		}catch (SQLException e) {
			throw new SQLException("[ Result Error Message ] => "+"매출 계산 중 문제가 발생했습니다.");
		}
		return qList;
	}
	
	//8. 월매출
	@Override
	public List<Sales> searchSalesByMonth(int houseNo) throws SQLException{
		List<Sales> mList = new ArrayList<>();
		String query = "SELECT month(start_date) 월, sum(total_price) 합계 "
						+ "FROM receipt "
						+ "WHERE house_no=? "
						+ "group by 1";
		
		try(Connection conn = getConnection(); 
			PreparedStatement ps = conn.prepareStatement(query)) {
			
			ps.setInt(1, houseNo);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				mList.add(new Sales(rs.getInt("월"), getHouseName(houseNo), rs.getLong("합계")));
			}
			if(mList.isEmpty()) {
				mList.add(new Sales(getHouseName(houseNo), 0));
			}

		}catch (SQLException e) {
			throw new SQLException("[ Result Error Message ] => "+"매출 계산 중 문제가 발생했습니다.");
		}
		return mList;
	}
}
