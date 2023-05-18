package gr.aueb.cf.movieapp.controller;

import gr.aueb.cf.movieapp.authentication.CustomAuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        request.getSession().setAttribute(CustomAuthenticationSuccessHandler.REDIRECT_URL_SESSION_ATTRIBUTE_NAME, referer);

        return principal == null ? "login" : "redirect:/movieapp";
    }

    @GetMapping(path = "/")
    String root(Model model, Principal principal, HttpServletRequest request) throws Exception {
        return principal == null ? "login" : "redirect:/movieapp";
    }

    @GetMapping(path = "/movieapp")
    String movieApp() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLoggedInUser = authentication.getName();

        System.out.println("Remember to impl proper user greeting/display on .html");
        System.out.println("Welcome " + currentLoggedInUser);
        return "index";
    }
}
