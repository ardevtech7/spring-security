package com.ardevtech.springsecurity.note;

import com.ardevtech.springsecurity.user.User;
import com.ardevtech.springsecurity.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;

    /**
     * 노트 조회
     * 유저는 본인의 노드만 조회할 수 있다.
     * 어드민은 모든 노트를 조회할 수 있다.
     *
     * @param user 노트를 찾을 유저
     * @return 유저가 조회할 수 있는 모든 노트 List
     */
    @Transactional(readOnly = true)
    public List<Note> findByUser(User user) {
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (user.isAdmin()) {
            return noteRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        }
        return noteRepository.findByUserOrderByIdDesc(user);
    }

    public Note saveNote(User user, String title, String content) {
        if (user == null) {
            throw new UserNotFoundException();
        }
        return noteRepository.save(new Note(title, content, user));
    }

    public void deleteNote(User user, Long noteId) {
        if (user == null) {
            throw new UserNotFoundException();
        }
        Note note = noteRepository.findByIdAndUser(noteId, user);
        if (note != null) {
            noteRepository.delete(note);
        }
    }
}
