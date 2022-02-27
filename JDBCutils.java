package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCutils {
    public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("properties");
        Properties properties = new Properties();
        properties.load(is);
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");
        //加载驱动
        Class.forName(driverClass);
        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;

    }

    public static void closeResource(Connection connection, Statement preparedStatement){
        try {
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //通用的增删改操作
    //注：SQL的占位符个数与args可变形参的长度一致
    public static void update(String sql,Object...args) throws SQLException, IOException, ClassNotFoundException {
        //1.获取数据库连接
        Connection connection = JDBCutils.getConnection();
        //2.编译SQL语言
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //3.填充占位符
        for(int i= 0;i<args.length;i++){
            preparedStatement.setObject(i+1,args[i]);
        }
        //4.执行
        preparedStatement.execute();
        //5.关闭流
        JDBCutils.closeResource(connection,preparedStatement);
    }
}
