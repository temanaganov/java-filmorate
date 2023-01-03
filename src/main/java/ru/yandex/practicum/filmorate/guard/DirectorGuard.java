package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;

@Component
@RequiredArgsConstructor
public class DirectorGuard extends Guard<Director> {
    private final DirectorRepository directorRepository;

    @Override
    protected String getGuardClass() {
        return "Director";
    }

    @Override
    protected Director checkMethod(int id) {
        return directorRepository.getById(id);
    }
}
