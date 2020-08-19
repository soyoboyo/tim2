package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.stereotype.Service;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.repositories.MessageRepository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageSeeder {

	private final MessageRepository messageRepository;

	public Map<String, Message> initMessages(Map<String, Project> projects) {

		Map<String, Message> messages = new HashMap<>();
		List<String> projectIds = Arrays.asList("P1", "P2", "P3", "P4");

		for (String id : projectIds) {
			ArrayList<LinkedHashMap<String, Object>> messagesArray = new ArrayList<>();
			try {
				FileReader fr = new FileReader("backend/src/main/resources/json-seed/project" + id + "/messages" + id + ".json");
				JSONParser parser = new JSONParser(fr);
				messagesArray = (ArrayList<LinkedHashMap<String, Object>>) parser.parse();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			for (LinkedHashMap<String, Object> m : messagesArray) {
				String key = (String) m.get("key");
				String content = (String) m.get("content");
				String description = (String) m.get("description");
				Message message = new Message(key, content, projects.get("project" + id));
				message.setDescription(description);
				String uuid = (String) m.get("uuid");
				if (uuid != null) {
					messages.put(uuid, messageRepository.save(message));
				} else {
					messages.put(key, messageRepository.save(message));
				}
			}
		}

		return messages;
	}
}
