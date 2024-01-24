package cn.codedan.security.camelmergersecurity.service;

import cn.codedan.security.camelmergersecurity.model.entity.LoginUser;
import cn.codedan.security.camelmergersecurity.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Objects;

/**
 * @ClassName: UserDetailsServiceImpl
 * @Description: TODO
 * @Author: codedan
 * @Date: 2022/12/13 17:02
 * @Version: 1.0.0
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 查询用户信息(采用假数据)
        User user = new User();
        user.setId(1l);
        user.setUserName("zhangldc");
        user.setPassword(passwordEncoder.encode("12345"));
        if(Objects.isNull(user) ){
            throw new RuntimeException("用户名密码错误");
        }
        return new LoginUser(user);
    }
}
