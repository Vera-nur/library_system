package com.library.library_system.service;

import com.library.library_system.dto.DashboardStats;
import com.library.library_system.entity.BorrowDetailsView;
import com.library.library_system.repository.BorrowBookRepository;
import com.library.library_system.repository.BorrowViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowBookRepository borrowBookRepository;
    private final BorrowViewRepository borrowViewRepository;

    // 1) Kitap ödünç verme (SP çağırıyor)
    public void giveBookToUser(Integer userId, Integer bookId) {
        // SP stok kontrolünü yapar — stok yoksa hata fırlatır
        borrowBookRepository.borrowBook(userId, bookId);
    }

    // 2) Dashboard kartları için istatistikler (SP sp_GetDashboardStats)
    public DashboardStats getDashboardStats() {
        return borrowBookRepository.getDashboardStatistics();
    }

    // 3) Dashboard altındaki "Son 10 Ödünç Alma İşlemi" tablosu
    public List<BorrowDetailsView> getLast10Borrows() {
        return borrowViewRepository.findTop10ByOrderByStartDateDesc();
    }


    // 4) Kitabı geri teslim alma
    public void returnBook(Integer borrowId) {
        // Return SP stok +1 artırır ve durumu değiştirir
        borrowBookRepository.returnBook(borrowId);
    }

    // 🔹 Kullanıcı paneli: giriş yapan kişinin ödünç aldığı kitaplar
    public List<BorrowDetailsView> getBorrowsForUser(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return List.of();   // güvenlik için boş liste
        }
        return borrowViewRepository.findByFullNameOrderByStartDateDesc(fullName);
        // İstersen sadece aktifleri göstermek için:
        // return borrowViewRepository.findByFullNameAndStatusOrderByStartDateDesc(fullName, "Ödünçte");
    }


}