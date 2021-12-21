package com.oga.notificationservvice.Repository;

import com.oga.notificationservvice.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity,Long> {

    public List<NotificationEntity> findByType2(String string);
    public List<NotificationEntity> findByUserIdAndType2(long id,String string);
    public List<NotificationEntity> findByTypeAndType2(String d,String t);

}
