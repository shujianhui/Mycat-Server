package io.mycat.backend.jdbc.mongodb;

import com.mongodb.MongoClientURI;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * 功能详细描述
 *
 * @author sohudo[http://blog.csdn.net/wind520]
 * @version 0.0.1
 * @create 2014年12月19日 下午6:50:23
 */
@Deprecated
public class MongoDriver implements Driver {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MongoDriver.class);
    static final String PREFIX = "mongodb://";
    private DriverPropertyInfoHelper propertyInfoHelper = new DriverPropertyInfoHelper();

    static {
        try {
            DriverManager.registerDriver(new MongoDriver());
        } catch (SQLException e) {
            LOGGER.error("initError", e);
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        MongoClientURI mcu = null;
        if ((mcu = parseURL(url, info)) == null) {
            return null;
        }

        MongoConnection result = null;
        //System.out.print(info);
        try {
            result = new MongoConnection(mcu, url);
        } catch (Exception e) {
            throw new SQLException("Unexpected exception: " + e.getMessage(), e);
        }

        return result;
    }

    private MongoClientURI parseURL(String url, Properties defaults) {
        if (url == null) {
            return null;
        }

        if (!StringUtils.startsWithIgnoreCase(url, PREFIX)) {
            return null;
        }

        //删掉开头的 jdbc:
        //url = url.replace(URL_JDBC, "");

        try {
            //FIXME 判断defaults中的参数,写入URL中?
            return new MongoClientURI(url);
        } catch (Exception e) {
            LOGGER.error("parseURLError", e);
            return null;
        }

    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        if (StringUtils.startsWithIgnoreCase(url, PREFIX)) {
            return true;
        }
        return false;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
            throws SQLException {

        return propertyInfoHelper.getPropertyInfo();
    }


    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return true;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

}
