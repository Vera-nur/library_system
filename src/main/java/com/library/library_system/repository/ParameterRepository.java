// ParameterRepository.java
package com.library.library_system.repository;

import com.library.library_system.entity.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParameterRepository extends JpaRepository<Parameter, Integer> {
    // KÜTÜPHANE: Bir grup içindeki TÜM parametreleri çekmek için (ör: BOOK_STATUS listesi)
    List<Parameter> findByParameterDef_Id(Integer parameterDefId);

    // DİJİTAL: Belirli bir grup + value ile TEK parametre bulmak için
    // ör: ( "BOOK_STATUS", "AVAILABLE" )
    Optional<Parameter> findByParameterDef_NameAndValue(String defName, String value);
}