package com.ardevtech.springsecurity.config;

import com.ardevtech.springsecurity.note.NoteService;
import com.ardevtech.springsecurity.notice.NoticeService;
import com.ardevtech.springsecurity.user.User;
import com.ardevtech.springsecurity.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 초기 상태 등록 Config
 */
@Configuration
@RequiredArgsConstructor
@Profile(value = "!test") // test 에서는 제외
public class InitializeDefaultConfig {
    private final UserService userService;
    private final NoteService noteService;
    private final NoticeService noticeService;

    /**
     * 유저 등록, note 4개 등록
     */
    @Bean
    public void initializeDefaultUser() {
        User user = userService.signup("user", "user");
        noteService.saveNote(user, "테스트1", "테스트1");
        noteService.saveNote(user, "테스트2", "테스트2");
        noteService.saveNote(user, "테스트3", "테스트3");
        noteService.saveNote(user, "테스트4", "테스트4");
    }

    /**
     * 어드민 등록, 공지사항 4개 등록
     */
    @Bean
    public void initializeDefaultAdmin() {
        userService.signupAdmin("admin", "admin");
        noticeService.saveNotice("환영합니다.", "환영합니다 여러분");
        noticeService.saveNotice("노트 작업 방법 공지", "1. 회원가입\n2. 로그인\n3. 노트 작성\n4. 저장\n* 본인 외에는 게시글을 볼 수 없습니다.");
    }

}
