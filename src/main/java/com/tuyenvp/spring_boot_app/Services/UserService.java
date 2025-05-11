package com.tuyenvp.spring_boot_app.Services;

import java.util.List;

import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.UserDtls;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    public UserDtls saveUser(UserDtls user);

    public UserDtls getUserByEmail(String email);

    public UserDtls getUserByName(String name);

    public List<UserDtls> getUsers(String role);

    public Boolean updateAccountStatus(Integer id, Boolean status);

    public void increaseFailedAttempt(UserDtls user);

    public void userAccountLock(UserDtls user);

    public boolean unlockAccountTimeExpired(UserDtls user);

    public void resetAttempt(int userId);

    public void updateUserResetToken(String email, String resetToken);

    public UserDtls getUserByToken(String token);

    public UserDtls updateUser(UserDtls user);

    public UserDtls updateUserProfile(UserDtls user, MultipartFile img);

    public UserDtls saveAdmin(UserDtls user);

    public Boolean existsEmail(String email);

    public long getTotalUsers();

   /* List<UserDtls> searchUser(String keyword, Integer pageNo);
    Page<UserDtls> getUserByRole(Integer pageNo, String role);
    Page<UserDtls> searchUser(String keyword, String role, Integer pageNo);*/

    public Page<UserDtls> getUsersByRole(String role, int pageNo);
    public Page<UserDtls> searchUser(String keyword, int pageNo);
}

