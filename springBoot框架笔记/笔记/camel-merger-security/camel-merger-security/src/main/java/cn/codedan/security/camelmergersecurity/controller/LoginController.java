package cn.codedan.security.camelmergersecurity.controller;

import cn.codedan.security.camelmergersecurity.model.dto.ResponseReusltDTO;
import cn.codedan.security.camelmergersecurity.model.entity.User;
import cn.codedan.security.camelmergersecurity.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: LoginController
 * @Description: TODO
 * @Author: codedan
 * @Date: 2022/12/13 14:56
 * @Version: 1.0.0
 **/
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    public ResponseReusltDTO login(@RequestBody User user){
        return loginService.login(user);
    }

}
