package com.system.common.rest;

import com.github.pagehelper.PageInfo;
import com.system.base.domain.CurrentUser;
import com.system.common.domain.dto.user.UpdatePasswordDTO;
import com.system.common.domain.model.UserModel;
import com.system.common.domain.qo.user.LoginQO;
import com.system.common.domain.qo.user.UserQO;
import com.system.common.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/user/login")
    public CurrentUser login(HttpServletRequest request,
                             @RequestBody LoginQO loginQO) {
        return userService.login(request, loginQO);
    }

    @GetMapping("/user/current")
    public CurrentUser getCurrentUser(HttpServletRequest request) {
        return userService.getCurrentUser(request);
    }

    @GetMapping("/user/logout")
    public Boolean logout(HttpServletRequest request) {
        return userService.logout(request);
    }

    @PostMapping("/user/updatePassword")
    public Boolean updatePassword(@RequestBody UpdatePasswordDTO dto) {
        return userService.updatePassword(dto);
    }

    @GetMapping("/user/info")
    public PageInfo<UserModel> pageUser(UserQO qo) {
        return userService.pageUser(qo);
    }

    @PostMapping("/user/info")
    public Boolean addUser(@RequestBody UserModel model) {
        return userService.addUser(model);
    }

    @PutMapping("/user/info")
    public Boolean updateUser(@RequestBody UserModel model) {
        return userService.updateUser(model);
    }

    @DeleteMapping("/user/info")
    public Boolean deleteUser(@RequestParam String username) {
        return userService.deleteUser(username);
    }
}
