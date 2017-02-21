package com.sokoloff06.mcprices.client;

import com.sokoloff06.mcprices.entities.LastUpdate;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Vitaly Sokolov on 14.02.2017.
 */
public interface LastUpdateRepository extends JpaRepository<LastUpdate, Integer> {
}
