package org.example.ecommerce.user.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class SecurityUser extends User {
    private String uuidUser;
    private String uuidCart;
    public SecurityUser(String username,
                        String password,
                        Collection<? extends GrantedAuthority> authorities,
                        String uuidUser,
                        String uuidCart) {
        super(username, password, authorities);
        this.uuidUser = uuidUser;
        this.uuidCart = uuidCart;
    }
}
