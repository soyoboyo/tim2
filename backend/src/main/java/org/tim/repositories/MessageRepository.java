package org.tim.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.Message;

import java.util.List;

@Repository
public interface MessageRepository extends ElasticsearchRepository<Message, String> {

    List<Message> findMessagesByProjectIdAndIsArchivedFalse(String projectId);

}
