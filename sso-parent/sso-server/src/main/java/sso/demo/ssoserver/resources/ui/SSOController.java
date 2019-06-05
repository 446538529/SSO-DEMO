package sso.demo.ssoserver.resources.ui;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sso.demo.ssoserver.resources.util.TokenUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class SSOController {
    @GetMapping(value ="/passport")
    public String loginUI(@RequestParam("url") String url, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        HttpSession session = request.getSession();
        Object loginFlag = session.getAttribute("loginFlag");
        if(loginFlag==null){
            model.addAttribute("url",url);
            return "site/login";
        }
        response.sendRedirect(url+"?token="+ TokenUtil.makeNormalToken());
        return null;
    }
    @PostMapping("/login")
    public String login(String name,String password,String url,HttpServletResponse response,HttpServletRequest request,Model model) throws IOException {
        if("admin".equals(name)&&"123".equals(password)){
            String token = TokenUtil.makeNormalToken();
            request.getSession().setAttribute("loginFlag",token);
            Cookie[] cookies = request.getCookies();
            if(cookies.length>0){
                for (Cookie cookie : cookies) {
                    if(cookie.getName().equals("JSESSIONID")){
                        cookie.setMaxAge(30*60);
                        response.addCookie(cookie);
                        break;
                    }
                }
            }
            response.sendRedirect(url+"?token="+token) ;
            return null;
        }else{
            model.addAttribute("msg","incorrect username or password");
            return "site/login";
        }
    }
    @GetMapping("/verify/token")
    public String verifyToken(String url,String token,HttpServletResponse response,Model model) throws IOException {
        if(TokenUtil.containsToken(token)){
            JSONObject result=new JSONObject();
            result.put("verifyResult",true);
            result.put("url",url);
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(JSONUtils.valueToString(result));
            writer.close();
            return null;
        }else{
            model.addAttribute("url",url);
            return "site/login";
        }

    }
}
