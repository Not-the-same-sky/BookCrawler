package com.weapes.bookcrawler.util;

import com.weapes.bookcrawler.mapper.BookMapper;
import com.weapes.bookcrawler.store.Book;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2017/5/19.
 */
public class SqlHelper {
    private static  SqlSessionFactory sqlSessionFactory;
    private static ThreadLocal<SqlSession> sqlSessionThreadLocal = new ThreadLocal<>();
    private static final String MYBATIS_CONFIG_PATH="resources/config/mybatis-config.xml";
    static {
        try (InputStream inputStream = Resources.getResourceAsStream(MYBATIS_CONFIG_PATH)) {
            assert inputStream != null;
            sqlSessionFactory = new SqlSessionFactoryBuilder()
                    .build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static SqlSession openSqlSession(){
        return sqlSessionFactory.openSession(true);
    }
    public static SqlSession openThreadSqlSession(){
        //从当前线程获取
        SqlSession sqlSession =sqlSessionThreadLocal.get();
        if(sqlSession == null){
            sqlSession = sqlSessionFactory.openSession(true);
            System.out.println("新建SqlSession");
            //将sqlSession与当前线程绑定
            sqlSessionThreadLocal.set(sqlSession);
        }
        return sqlSession;
    }
    public static void closeSqlSession(){
        //从当前线程获取
        SqlSession sqlSession =sqlSessionThreadLocal.get();
        if(sqlSession != null){
            sqlSession.close();
            sqlSessionThreadLocal.remove();
        }
    }
//    private static final SqlSessionFactory sqlSessionFactory=initSqlSessionFactory();
//    private static final SqlSession sqlSession=initSqlSession();
//    private static final String MYBATIS_CONFIG_PATH="resources/config/mybatis-config.xml";
//
//    public static SqlSessionFactory initSqlSessionFactory() {
//        try (InputStream inputStream = Resources.getResourceAsStream(MYBATIS_CONFIG_PATH)) {
//            assert inputStream != null;
//            //获取Configuration实例的样例
//            TransactionFactory transactionFactory = new JdbcTransactionFactory();//定义事务工厂
//            Environment environment =
//                    new Environment("development", transactionFactory, dataSource());
//            Configuration configuration = new Configuration(environment);
//            configuration.addMapper(BookMapper.class);
//
//            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
//                            .build(configuration);
//            inputStream.close();
//            return sqlSessionFactory;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//    private static DataSource dataSource(){
//        HikariDataSource dataSource=new HikariDataSource();
////        dataSource.setDriverClassName("");
////        dataSource.setJdbcUrl("");
////        dataSource.setUsername("");
////        dataSource.setPassword("");
////        dataSource.setMaximumPoolSize(5);
//        Properties properties=new Properties();
//        InputStream inputStream=SqlHelper.class.getClassLoader()
//                .getResourceAsStream("resources/config/hikariConfig.properties");
//        try {
//            properties.load(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        dataSource.setDataSourceProperties(properties);
//        return dataSource;
//    }
//    public static SqlSession initSqlSession() {
//       return sqlSessionFactory.openSession();
//    }
//    public static SqlSession getSqlSession() {
//        return sqlSession;
//    }
    public static void saveBook(Book book){
        SqlSession sqlSession =sqlSessionThreadLocal.get();
        BookMapper bookMapper=sqlSession.getMapper(BookMapper.class);
        bookMapper.saveBook(book);
        sqlSession.commit();
       // sqlSession.close();
        System.out.println("当前已保存"+
                Helper.getBookNumbers().addAndGet(1)+"篇图书信息");
    }
}
