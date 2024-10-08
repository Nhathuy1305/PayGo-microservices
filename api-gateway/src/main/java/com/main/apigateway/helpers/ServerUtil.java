package com.main.apigateway.helpers;

import com.main.apigateway.exception.SystemClientIpError;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import java.net.InetAddress;

@UtilityClass
public class ServerUtil {

    public static String LOCAL_IP_ADDRESS = null;
    public static String LOCAL_HOST_NAME = null;
    public static String LOCAL_ADDRESS = null;

    static {
        try {
            InetAddress ownIP = InetAddress.getLocalHost();
            LOCAL_IP_ADDRESS = ownIP.getHostAddress();
            LOCAL_HOST_NAME = ownIP.getHostName();
            LOCAL_ADDRESS = LOCAL_IP_ADDRESS + "@" + LOCAL_HOST_NAME;
        } catch (Exception e) {
            throw new SystemClientIpError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
    }

}
