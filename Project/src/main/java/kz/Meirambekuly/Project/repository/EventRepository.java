package kz.Meirambekuly.Project.repository;

import kz.Meirambekuly.Project.domain.Event;
import kz.Meirambekuly.Project.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {
    @Query("from Event event where not(event.end < :from or event.start > :to) and event.eventOwner.id = :user_id")
    List<Event> findBetween(@Param("from") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
                                    LocalDateTime start, @Param("to") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                                    Long user_id);
//       @Query("from Event event where (event.user = user) ")
//    Collection<Event> getEventsOfUser(@Param("user") User user);

}
