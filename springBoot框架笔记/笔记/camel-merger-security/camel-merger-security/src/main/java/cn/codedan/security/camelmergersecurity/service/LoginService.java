package cn.codedan.security.camelmergersecurity.service;

import cn.codedan.security.camelmergersecurity.model.dto.ResponseReusltDTO;
import cn.codedan.security.camelmergersecurity.model.entity.User;

/**
 * @ClassName: LoginService
 * @Description: TODO
 * @Author: codedan
 * @Date: 2022/12/13 17:30
 * @Version: 1.0.0
 **/
public interface LoginService {

    ResponseReusltDTO login(User user);

}
