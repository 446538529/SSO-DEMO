package sso.demo.ssoserver.resources.util;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TokenUtil {
    private static ConcurrentHashMap<String,Object> tokenPool=new ConcurrentHashMap<>();
    public static String makeNormalToken(){
        String token = UUID.randomUUID().toString();
        tokenPool.put(token,new Object());
        return token;
    }
    public static boolean containsToken(String token){
        return tokenPool.containsKey(token);
    }
}
