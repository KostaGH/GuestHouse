package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.ServerInfo;
import vo.Customer;
import vo.User;

public class LoginServiceImpl implements LoginService {
    // customer, seller 구분해주는 변수
    static int userType;

    // 싱글톤
    private static LoginServiceImpl service = new LoginServiceImpl();
    private LoginServiceImpl() {
        try {
            Class.forName(ServerInfo.DRIVER_NAME);
//            System.out.println("드라이버 로딩 성공");
        }catch(ClassNotFoundException e) {
            System.out.println("드라이버 로딩 실패");
        }
    }
    public static LoginServiceImpl getInstance() {
        return service;
    }

    // 공통로직
    public Connection getConnect() throws SQLException {
        Connection conn = DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASSWORD);
        return conn;
    }
    public void close(Connection conn) throws SQLException {
        if (conn != null) conn.close();
    }
    public void close(PreparedStatement ps) throws SQLException {
        if (ps != null) ps.close();
    }
    public void close(ResultSet rs) throws SQLException {
        if (rs != null) rs.close();
    }

    // 비즈니스로직
    @Override
    public void addUser(int num, User user) {
        // customer
        if (userType == num) {
            String query = "INSERT INTO customer (cust_id, cust_name, cust_password, cust_ssn, cust_phone, cust_balance, cust_gender) VALUES(?,?,?,?,?,?,?)";

            try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setString(1, user.getUserId());
                ps.setString(2, user.getUserName());
                ps.setString(3, user.getPw());
                ps.setString(4, user.getSsn());
                ps.setString(5, user.getPhoneNum());
                ps.setInt(6, user.getBalance());
                ps.setString(7, ((Customer) user).getGender());

                ps.executeUpdate();

            } catch (SQLException e) {
                System.out.println("[Error] add customer");
            }
        //seller
        } else {
            String query = "INSERT INTO seller (sel_id, sel_name, sel_password, sel_ssn, sel_phone, sel_balance) VALUES(?,?,?,?,?,?)";

            try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setString(1, user.getUserId());
                ps.setString(2, user.getUserName());
                ps.setString(3, user.getPw());
                ps.setString(4, user.getSsn());
                ps.setString(5, user.getPhoneNum());
                ps.setInt(6, user.getBalance());

                ps.executeUpdate();

            } catch (SQLException e) {
                System.out.println("[Error] add seller");
            }
        }
    }

    @Override
    public User login (int num, String userId, String pw) {
        User user = null;
        Customer cust = null;
        ResultSet rs = null;

        // customer
        if (userType == num) {
            String query = "SELECT cust_id, cust_name, cust_password, cust_ssn, cust_phone, cust_balance, cust_gender FROM customer WHERE cust_id = ? AND cust_password = ?";
            try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, userId);
                    ps.setString(2, pw);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        cust = new Customer(rs.getString("cust_id"),
                        		rs.getString("cust_name"),
                                rs.getString("cust_password"),
                                rs.getString("cust_ssn"),
                                rs.getString("cust_phone"),
                                rs.getInt("cust_balance"),
                                rs.getString("cust_gender"));
                        return cust;
                    }
            } catch (SQLException e) {
                System.out.println("[Error] customer login");
            }
        //seller
        } else if (userType+1 == num) {
            String query = "SELECT sel_id, sel_name, sel_password, sel_ssn, sel_phone, sel_balance FROM seller WHERE sel_id = ? AND sel_password = ?";
            try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, userId);
                    ps.setString(2, pw);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        user = new User(rs.getString("sel_id"),
                                rs.getString("sel_name"),
                                rs.getString("sel_password"),
                                rs.getString("sel_ssn"),
                                rs.getString("sel_phone"),
                                rs.getInt("sel_balance"));
                        return user;
                    }
            } catch (SQLException e) {
                System.out.println("[Error] seller login");
            }
        } else {
            System.out.println("[Error] login");
        }
        return null;
    }

    @Override
    public void deleteUser(int num, String userId, String password) {
        // customer
        if (userType == num) {
            String query = "DELETE FROM customer WHERE cust_id = ? AND cust_password = ?";

            try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setString(1, userId);
                ps.setString(2, password);

                ps.executeUpdate();

            } catch (SQLException e) {
                System.out.println("[Error] delete customer");
            }
            //seller
        } else {
            String query = "DELETE FROM seller WHERE sel_id = ? AND sel_password = ?";

            try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setString(1, userId);
                ps.setString(2, password);

                ps.executeUpdate();

            } catch (SQLException e) {
                System.out.println("[Error] delete seller");
            }
        }
    }

    @Override
    public User updateUser(int num, User user) {
        User newUser = null;
        Customer cust = null;
        ResultSet rs = null;
        // customer
        if (userType == num) {
            String query = "UPDATE customer SET cust_name = ?, cust_password = ?, cust_phone = ?, cust_gender = ? WHERE cust_ssn = ?";

            try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setString(1, user.getUserName());
                ps.setString(2, user.getPw());
                ps.setString(3, user.getPhoneNum());
                ps.setString(4, ((Customer) user).getGender());
                ps.setString(5, user.getSsn());

                ps.executeUpdate();

                return findUserBySsn(num, user.getSsn());

            } catch (SQLException e) {
                System.out.println("[Error] update customer");
            }
        //seller
        } else {
            String query = "UPDATE seller SET sel_name = ?, sel_password = ?, sel_phone = ? WHERE sel_ssn = ?";

            try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setString(1, user.getUserName());
                ps.setString(2, user.getPw());
                ps.setString(3, user.getPhoneNum());
                ps.setString(4, user.getSsn());

                ps.executeUpdate();

                return findUserBySsn(num, user.getSsn());
            } catch (SQLException e) {
                System.out.println("[Error] update seller");
            }
        }
        return user;
    }

    public User findUserBySsn(int num, String ssn) {
        Customer cust = null;
        User user = null;

        // customer
        if (userType == num) {
            String query = "SELECT cust_id, cust_name, cust_password, cust_ssn, cust_phone, cust_balance, cust_gender FROM customer WHERE cust_ssn = ?";

            try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, ssn);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    cust = new Customer(rs.getString("cust_id"),
                            rs.getString("cust_name"),
                            rs.getString("cust_password"),
                            rs.getString("cust_ssn"),
                            rs.getString("cust_phone"),
                            rs.getInt("cust_balance"),
                            rs.getString("cust_gender"));
                    return cust;
                }
            } catch (SQLException e) {
                System.out.println("[Error] findUserBySsn customer");
            }
            return null;

        //seler
        } else {
            String query = "SELECT sel_id, sel_name, sel_password, sel_ssn, sel_phone, sel_balance FROM seller WHERE sel_ssn = ?";
            try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, ssn);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    user = new User(rs.getString("sel_id"),
                            rs.getString("sel_name"),
                            rs.getString("sel_password"),
                            rs.getString("sel_ssn"),
                            rs.getString("sel_phone"),
                            rs.getInt("sel_balance"));
                    return user;
                }
        } catch (SQLException e) {
            System.out.println("[Error] findUserBySsn seller");
        }
        return null;
        }
    }


    public static void main(String[] args) {
        LoginServiceImpl service = LoginServiceImpl.getInstance();
//        System.out.println("&&&&&&&&&&&&&login&&&&&&&&&&&&&&");
        System.out.println(service.login(0, "BAEK", "2222"));
//        System.out.println(service.login(1, "helpgod", "6666"));
//        System.out.println("&&&&&&&&&&&&&addUser&&&&&&&&&&&&&&");
//        service.addUser(0, new Customer("naflnafl", "butterfly", "8888", "888888-8888888", "010-8878-7788", 0, "M"));
//        service.addUser(1, new User("nine", "dragon", "9999", "909090-9090909", "010-9090-9090", 0));
//        System.out.println("&&&&&&&&&&&&&deleteUser&&&&&&&&&&&&&&");
//        service.deleteUser(0, "naflnafl", "8888");
//        service.deleteUser(1, "nine", "9999");
//        System.out.println("&&&&&&&&&&&&&updateUser&&&&&&&&&&&&&&");
//        System.out.println(service.updateUser(0, new Customer("believeme", "3030", "333-3333-3333", "F", "333333-3333333")));
//        System.out.println(service.updateUser(1, new User("pleasegod", "6666", "666-6666-6666", "446644-4444444")));
//        System.out.println(service.findUserId(0,"111111-1111111"));
//        System.out.println(service.findUserId(1,"446644-4444444"));
    }
}
