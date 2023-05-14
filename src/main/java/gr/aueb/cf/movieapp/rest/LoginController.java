package gr.aueb.cf.movieapp.rest;

import gr.aueb.cf.movieapp.authentication.CustomAuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class LoginController {

    @GetMapping(path = "/login")
    String login(Model model, Principal principal, HttpServletRequest request) throws Exception {

        String referer = request.getHeader("Referer");
        request.getSession().setAttribute(CustomAuthenticationSuccessHandler.REDIRECT_URL_SESSION_ATTRIBUTE_NAME,
                referer);
        return principal == null ? "login" : "redirect:/moviesapp";
    }

    @GetMapping(path = "/")
    String root(Model model, Principal principal, HttpServletRequest request) throws Exception {
        return principal == null ? "login" : "redirect:/moviesapp";
    }

    @GetMapping(path = "/moviesapp")
    String landingPage() throws Exception {
        return "index";
    }
}
