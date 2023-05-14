package gr.aueb.cf.movieapp.authentication;

import gr.aueb.cf.movieapp.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final IUserRepository userRepository;
    private final MessageSource messageSource;

    @Autowired
    public CustomAuthenticationProvider(IUserRepository userRepository, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }

    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        //userRepository.isUserValid(username,password)
        if (Objects.equals(userRepository.findUserByUsername(username).getPassword(), password)) {
            return new UsernamePasswordAuthenticationToken(username, password, Collections.<GrantedAuthority>emptyList());
        } else {
            throw new BadCredentialsException(accessor.getMessage("badCredentials"));
        }
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
