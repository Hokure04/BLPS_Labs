package org.example.blps_lab1.core.ports.auth;

import org.example.blps_lab1.core.domain.auth.User;
import org.example.blps_lab1.core.domain.auth.UserXml;
import org.example.blps_lab1.core.domain.course.nw.NewCourse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    User add(User user);

    User getUserByEmail(String username);

    User updateUser(User user);

    boolean isExist(String username);

    void enrollUser(User user, NewCourse course);

    UserDetailsService getUserDetailsService();
}
