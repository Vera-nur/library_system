package com.library.library_system.service;

import com.library.library_system.entity.AccessLog;
import com.library.library_system.entity.Parameter;
import com.library.library_system.repository.AccessLogRepository;
import com.library.library_system.repository.ParameterRepository;
import com.library.library_system.repository.UserRepository;
import com.library.library_system.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogService {

    private final AccessLogRepository accessLogRepository;
    private final ParameterRepository parameterRepository;
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;

    /**
     * @param actionValue : Parameter tablosundaki 'value' (Örn: "create_user", "DOWNLOAD")
     * @param userId      : İşlemi yapan Üye ID (Yoksa null)
     * @param workerId    : İşlemi yapan Çalışan ID (Yoksa null)
     */
    public void log(String actionValue, Integer userId, Integer workerId) {

        // 1. İşlem Tipini Bul
        System.out.println("🔍 LOG SERVICE BAŞLADI: " + actionValue);
        Parameter actionParam = parameterRepository.findByValue(actionValue);

        if (actionParam != null) {
            AccessLog log = new AccessLog();
            log.setAccessType(actionParam); // Entity'deki yeni ismimiz
            log.setCreatedAt(LocalDateTime.now());

            // 2. User varsa ayarla (getReferenceById performanslıdır, DB sorgusu atmaz)
            if (userId != null) {
                log.setUser(userRepository.getReferenceById(userId));
            }

            // 3. Worker varsa ayarla
            if (workerId != null) {
                log.setWorker(workerRepository.getReferenceById(workerId));
            }

            // 4. Kaydet
            accessLogRepository.save(log);
            System.out.println("✅ LOG: " + actionValue + " işlemi kaydedildi.");
        } else {
            System.err.println("⚠️ HATA: '" + actionValue + "' parametre tablosunda bulunamadı!");
        }
    }
}