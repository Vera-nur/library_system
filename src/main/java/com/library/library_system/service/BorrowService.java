package com.library.library_system.service;

import com.library.library_system.repository.BorrowBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowBookRepository borrowBookRepository;

    public void giveBookToUser(Integer userId, Integer bookId) {
        // Burada SP'yi çağırıyoruz.
        // SP içinde stok kontrolü yapıldığı için ekstra if/else yazmamıza gerek yok.
        // Eğer stok yoksa SP hata fırlatır, Spring bunu yakalar.
        borrowBookRepository.borrowBook(userId, bookId);
    }
}