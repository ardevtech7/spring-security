package com.ardevtech.springsecurity.note;

import com.ardevtech.springsecurity.user.User;
import com.ardevtech.springsecurity.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
class NoteServiceTest {
    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("노트 조회 - 유저가 게시물 조회")
    void findByUser_유저가_게시물조회() {
        // Given
        User user = userRepository.save(new User("username", "password", "ROLE_USER"));
        noteRepository.save(new Note("title1", "content1", user));
        noteRepository.save(new Note("title2", "content2", user));

        // When
        List<Note> notes = noteService.findByUser(user);

        // Then
        then(notes.size()).isEqualTo(2);
        Note note1 = notes.get(0);
        Note note2 = notes.get(1);

        // note1 = title2
        then(note1.getUser().getUsername()).isEqualTo("username");
        then(note1.getTitle()).isEqualTo("title2"); // 가장 늦게 insert 된 데이터가 먼저 나와야한다.
        then(note1.getContent()).isEqualTo("content2");

        // note2 = title1
        then(note2.getUser().getUsername()).isEqualTo("username");
        then(note2.getTitle()).isEqualTo("title1");
        then(note2.getContent()).isEqualTo("content1");
    }

    @Test
    @DisplayName("노트 조회 - 어드민이 게시물 조회")
    void findByUser_어드민이_게시물조회() {
        // Given
        User admin = userRepository.save(new User("admin", "password", "ROLE_ADMIN"));
        User user1 = userRepository.save(new User("username", "password", "ROLE_USER"));
        User user2 = userRepository.save(new User("username2", "password", "ROLE_USER"));
        noteRepository.save(new Note("title1", "content1", user1));
        noteRepository.save(new Note("title2", "content2", user1));
        noteRepository.save(new Note("title3", "content3", user2));

        // When
        List<Note> notes = noteService.findByUser(admin);

        // Then
        then(notes.size()).isEqualTo(3);
        Note note1 = notes.get(0);
        Note note2 = notes.get(1);
        Note note3 = notes.get(2);

        // note1 = title3
        then(note1.getUser().getUsername()).isEqualTo("username2");
        then(note1.getTitle()).isEqualTo("title3");
        then(note1.getContent()).isEqualTo("content3");

        // note2 = title2
        then(note2.getUser().getUsername()).isEqualTo("username");
        then(note2.getTitle()).isEqualTo("title2");
        then(note2.getContent()).isEqualTo("content2");

        // note3 = title1
        then(note3.getUser().getUsername()).isEqualTo("username");
        then(note3.getTitle()).isEqualTo("title1");
        then(note3.getContent()).isEqualTo("content1");
    }

    @Test
    @DisplayName("노트 저장")
    void saveNote() {
        // Given
        User user = userRepository.save(new User("username", "password", "ROLE_USER"));

        // When
        noteService.saveNote(user,"title1", "content1");

        // Then
        then(noteRepository.count()).isOne();
    }

    @Test
    @DisplayName("노트 삭제")
    void deleteNote() {
        // Given
        User user = userRepository.save(new User("username", "password", "ROLE_USER"));
        Note note = noteRepository.save(new Note("title1", "content1", user));

        // when
        noteService.deleteNote(user, note.getId());

        // Then
        then(noteRepository.count()).isZero();
    }

}