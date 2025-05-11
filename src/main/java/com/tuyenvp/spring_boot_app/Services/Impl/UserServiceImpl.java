package com.tuyenvp.spring_boot_app.Services.Impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.tuyenvp.spring_boot_app.Model.UserDtls;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.UserService;
import com.tuyenvp.spring_boot_app.Util.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;



@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private DbConnect DbConnect;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDtls saveUser(UserDtls user) {
        user.setRole("ROLE_USER");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        UserDtls saveUser = DbConnect.userRepo.save(user);
        return saveUser;
    }

    @Override
    public UserDtls getUserByEmail(String email) {
        return DbConnect.userRepo.findByEmail(email);
    }

    @Override
    public UserDtls getUserByName(String name) {
        UserDtls user = DbConnect.userRepo.findByName(name);
        return user;
    }

    @Override
    public List<UserDtls> getUsers(String role) {
        return DbConnect.userRepo.findByRole(role);
    }

    @Override
    public Boolean updateAccountStatus(Integer id, Boolean status) {

        Optional<UserDtls> findByuser = DbConnect.userRepo.findById(id);

        if (findByuser.isPresent()) {
            UserDtls userDtls = findByuser.get();
            userDtls.setIsEnable(status);
            DbConnect.userRepo.save(userDtls);
            return true;
        }

        return false;
    }

    @Override
    public void increaseFailedAttempt(UserDtls user) {
        int attempt = user.getFailedAttempt() + 1;
        user.setFailedAttempt(attempt);
        DbConnect.userRepo.save(user);
    }

    @Override
    public void userAccountLock(UserDtls user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        DbConnect.userRepo.save(user);
    }

    @Override
    public boolean unlockAccountTimeExpired(UserDtls user) {

        long lockTime = user.getLockTime().getTime();
        long unLockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;

        long currentTime = System.currentTimeMillis();

        if (unLockTime < currentTime) {
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            DbConnect.userRepo.save(user);
            return true;
        }

        return false;
    }

    @Override
    public void resetAttempt(int userId) {

    }

    @Override
    public void updateUserResetToken(String email, String resetToken) {
        UserDtls findByEmail = DbConnect.userRepo.findByEmail(email);
        findByEmail.setResetToken(resetToken);
        DbConnect.userRepo.save(findByEmail);
    }

    @Override
    public UserDtls getUserByToken(String token) {
        return DbConnect.userRepo.findByResetToken(token);
    }

    @Override
    public UserDtls updateUser(UserDtls user) {
        return DbConnect.userRepo.save(user);
    }

    @Override
    public UserDtls updateUserProfile(UserDtls user, MultipartFile img) {

        UserDtls dbUser = DbConnect.userRepo.findById(user.getId()).get();

        /*if (!img.isEmpty()) {
            dbUser.setProfileImage(img.getOriginalFilename());
        }*/

        if (!ObjectUtils.isEmpty(dbUser)) {

            dbUser.setName(user.getName());
            dbUser.setPhone(user.getPhone());
            dbUser.setBirthday(user.getBirthday());
            dbUser.setGender(user.getGender());
            dbUser = DbConnect.userRepo.save(dbUser);
        }

        try {
            if (!img.isEmpty()) {
                File saveFile = new ClassPathResource("static/be/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
                        + img.getOriginalFilename());

			System.out.println(path);
                Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dbUser;
    }

    @Override
    public UserDtls saveAdmin(UserDtls user) {
        user.setRole("ROLE_ADMIN");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        UserDtls saveUser = DbConnect.userRepo.save(user);
        return saveUser;
    }

    @Override
    public Boolean existsEmail(String email) {
        return DbConnect.userRepo.existsByEmail(email);
    }

    @Override
    public long getTotalUsers() {
        return DbConnect.userRepo.countByRole("ROLE_USER");
    }

    public Page<UserDtls> getUsersByRole(String role, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 8);
        return DbConnect.userRepo.findByRolePage(role, pageable);
    }

    public Page<UserDtls> searchUser(String keyword, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 8);
        return DbConnect.userRepo.searchUser(keyword, pageable);
    }


}

