package wisoft.io.miseprep_api.global.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Async
    public void sendCartInvitation(String toEmail, String inviterName, String cartName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("[MisePrep] " + inviterName + "님이 장바구니에 초대했습니다");
            helper.setText(buildInviteHtml(inviterName, cartName), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            // 이메일 발송 실패해도 초대 자체는 저장됨
        }
    }

    private String buildInviteHtml(String inviterName, String cartName) {
        return """
                <div style="font-family: sans-serif; max-width: 480px; margin: 0 auto;">
                  <h2 style="color: #1a1a1a;">장바구니 초대</h2>
                  <p><strong>%s</strong>님이 <strong>%s</strong> 장바구니에 초대했습니다.</p>
                  <p>아래 버튼을 눌러 초대를 확인하세요.</p>
                  <a href="%s/invitations"
                     style="display:inline-block; padding: 12px 24px; background:#1a1a1a; color:#fff;
                            text-decoration:none; border-radius:6px; margin-top:8px;">
                    초대 확인하기
                  </a>
                </div>
                """.formatted(inviterName, cartName, frontendUrl);
    }
}
