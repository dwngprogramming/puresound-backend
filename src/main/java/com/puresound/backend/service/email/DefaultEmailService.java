package com.puresound.backend.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class DefaultEmailService implements EmailService {
    JavaMailSender javaMailSender;

    @Override
    public void sendOtp(String to, String otp, long expMins) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        String htmlTemplate = getOtpTemplate(otp, expMins);

        helper.setTo(to);
        helper.setSubject("PureSound - Mã xác thực: " + otp);
        helper.setText(htmlTemplate, true);
        helper.setFrom("PureSound <dddhandicraft.contact@gmail.com>");

        javaMailSender.send(message);
    }

    private String getOtpTemplate(String otp, long expMins) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="UTF-8">
                  <title>PureSound - Mã xác thực của bạn</title>
                  <style>
                    body {
                      font-family: Arial, sans-serif;
                      background-color: #7DA0CA;
                      padding: 0;
                      margin: 0;
                    }
                    .container {
                      background-color: #ffffff;
                      max-width: 700px;
                      margin: 40px auto;
                      padding: 0;
                      border-radius: 12px;
                      box-shadow: 0 8px 24px rgba(2, 16, 36, 0.25);
                      overflow: hidden;
                    }
                    .header {
                      background: linear-gradient(to bottom, #052659 0%, #021024 100%);
                      color: white;
                      text-align: center;
                      padding: 30px 20px;
                      position: relative;
                    }
                    .logo {
                      width: 60px;
                      height: 60px;
                      margin: 0 auto 15px;
                      display: block;
                      border-radius: 50%;
                      background-color: rgba(255, 255, 255, 0.15);
                      padding: 10px;
                    }
                    .header h2 {
                      margin: 0;
                      font-size: 24px;
                      font-weight: 600;
                    }
                    .content {
                      padding: 30px;
                      background-color: #f0f0f0;
                    }
                    .greeting {
                      color: #021024;
                      font-size: 16px;
                      margin-bottom: 20px;
                    }
                    .otp-section {
                      text-align: center;
                      margin: 30px 0;
                    }
                    .otp-label {
                      color: #052659;
                      font-size: 14px;
                      font-weight: 600;
                      margin-bottom: 15px;
                      text-transform: uppercase;
                      letter-spacing: 1px;
                    }
                    .otp-code {
                      font-size: 32px;
                      font-weight: bold;
                      color: #021024;
                      padding: 12px 30px;
                      display: inline-block;
                      letter-spacing: 6px;
                    }
                    .warning {
                      background-color: #C1E8FF;
                      border-left: 4px solid #052659;
                      padding: 15px 20px;
                      margin: 25px 0;
                      border-radius: 0 8px 8px 0;
                    }
                    .warning p {
                      margin: 0;
                      color: #021024;
                      font-size: 14px;
                      line-height: 1.5;
                    }
                    .footer {
                      background-color: #021024;
                      color: #7DA0CA;
                      font-size: 12px;
                      text-align: center;
                      padding: 20px;
                    }
                    .footer a {
                      color: #C1E8FF;
                      text-decoration: none;
                    }
                    .security-note {
                      color: #052659;
                      font-size: 13px;
                      text-align: center;
                      margin-top: 20px;
                      font-style: italic;
                    }
                  </style>
                </head>
                <body>
                  <div class="container">
                    <div class="header">
                      <img src="https://drive.usercontent.google.com/download?id=1LK0VI5M2ScDOO8jmBXVuxugtEdJpmSOl&export=view&authuser=0" alt="PureSound Logo" class="logo">
                      <h2>Xác thực Email</h2>
                    </div>
                
                    <div class="content">
                      <div class="greeting">
                        <p>Xin chào quý khách hàng,</p>
                        <p>Chúng tôi đã nhận được yêu cầu xác thực của bạn. Vui lòng sử dụng mã xác thực bên dưới để hoàn tất quá trình này.</p>
                      </div>
                
                      <div class="otp-section">
                        <div class="otp-label">Mã xác thực của bạn</div>
                        <div class="otp-code">{OTP_CODE}</div>
                      </div>
                
                      <div class="warning">
                        <p><strong>Quan trọng:</strong> Mã này sẽ hết hạn sau <strong>{EXP_MINUTES} phút</strong>. Vui lòng sử dụng ngay để xác thực tài khoản của bạn.</p>
                      </div>
                
                      <div class="security-note">
                        Nếu bạn không yêu cầu xác thực này, vui lòng bỏ qua email này hoặc liên hệ với đội ngũ hỗ trợ của chúng tôi.
                      </div>
                    </div>
                
                    <div class="footer">
                      <p>&copy; 2025 <strong>PureSound</strong>. Tất cả quyền được bảo lưu.</p>
                      <p>Cần hỗ trợ? <a href="mailto:dddhandicraft.contact@gmail.com">Liên hệ hỗ trợ</a></p>
                    </div>
                  </div>
                </body>
                </html>
                """
                .replace("{OTP_CODE}", otp)
                .replace("{EXP_MINUTES}", String.valueOf(expMins));
    }
}
