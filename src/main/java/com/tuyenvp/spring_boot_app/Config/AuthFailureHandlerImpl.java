package com.tuyenvp.spring_boot_app.Config;

import com.tuyenvp.spring_boot_app.Model.UserDtls;
import com.tuyenvp.spring_boot_app.Repository.UserRepo;
import com.tuyenvp.spring_boot_app.Services.Impl.UserServiceImpl;
import com.tuyenvp.spring_boot_app.Util.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private UserRepo userRepository;

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		String email = request.getParameter("username");

		UserDtls userDtls = userRepository.findByEmail(email);

		if (userDtls != null) {

			if (userDtls.getIsEnable()) {

				if (userDtls.getAccountNonLocked()) {

					if (userDtls.getFailedAttempt() < AppConstant.ATTEMPT_TIME) {
						userServiceImpl.increaseFailedAttempt(userDtls);
					} else {
						userServiceImpl.userAccountLock(userDtls);
						exception = new LockedException("Tài khoản của bạn đã bị đóng !! failed attempt 3");
					}
				} else {

					if (userServiceImpl.unlockAccountTimeExpired(userDtls)) {
						exception = new LockedException("Tài khoản của bạn đã được mở !! Vui lòng đăng nhập");
					} else {
						exception = new LockedException("Tài khoản của bạn đã bị khoá !! Vui lòng thử lại sau vài giây");
					}
				}

			} else {
				exception = new LockedException("Tài khoản của bạn không hoạt động");
			}
		} else {
			exception = new LockedException("Email & Mật khẩu không đúng");
		}

		super.setDefaultFailureUrl("/login?error");
		super.onAuthenticationFailure(request, response, exception);
	}

}
