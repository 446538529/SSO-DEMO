package sso.demo.ssoclient1.resource.interceptor;

import com.fasterxml.jackson.databind.util.JSONPObject;
import jdk.nashorn.internal.scripts.JS;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private String SSO_SERVER_LOGIN ="http://localhost:9000/sso-server/passport";
    private String SSO_SERVER_VERIFY_TOKEN ="http://localhost:9000/sso-server/verify/token";
    @Autowired
    private RestTemplate restTemplate;
    public LoginInterceptor() {
        super();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object loginFlag = session.getAttribute("loginFlag");
        if(loginFlag!=null){
            Cookie[] cookies = request.getCookies();
            if(cookies.length>0){
                for (Cookie cookie : cookies) {
                    if(cookie.getName().equals("JSESSIONID")){
                        cookie.setMaxAge(30*60);
                        cookie.setPath("/sso-client1");
                        response.addCookie(cookie);
                        break;
                    }
                }
            }
            return super.preHandle(request, response, handler);
        }
        String url = request.getRequestURL().toString();
        String token = request.getParameter("token");
        if(token==null){
            response.sendRedirect(SSO_SERVER_LOGIN +"?url="+url);
            return false;
        }else{
            /*JSONObject jsonObject=new JSONObject();
            jsonObject.put("url",url);
            jsonObject.put("token",token);*/
            try {
                ResponseEntity<JSONObject> verifyResult = restTemplate.getForEntity(SSO_SERVER_VERIFY_TOKEN + "?url=" + url + "&token=" + token, JSONObject.class);
                if(verifyResult.getStatusCodeValue()==200&&verifyResult.getBody().get("verifyResult").equals(true)){
                    session.setAttribute("loginFlag",token);
                    return super.preHandle(request,response,handler);
                }else{
                    response.sendRedirect(SSO_SERVER_LOGIN +"?url="+url);
                    return false;
                }
            } catch (Exception e) {
                response.sendRedirect(SSO_SERVER_LOGIN +"?url="+url);
                return false;
            }
        }

    }

}
