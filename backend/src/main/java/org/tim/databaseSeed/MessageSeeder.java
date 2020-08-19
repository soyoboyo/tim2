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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageSeeder {

	private final MessageRepository messageRepository;

	public Map<String, Message> initMessages(Map<String, Project> projects) {

		Map<String, Message> messages = new HashMap<>();

		ArrayList<LinkedHashMap<String, Object>> messagesArray = new ArrayList<>();
		try {
			FileReader fr = new FileReader("backend/src/main/resources/json-seed/projectP1/messagesP1.json");
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
			Message message = new Message(key, content, projects.get("projectP1"));
			message.setDescription(description);
			messages.put((String) m.get("uuid"), messageRepository.save(message));
		}

//		Message messageM1 = new Message("welcome1", "Groceries you’ll love, perfectly delivered.", projects.get("projectP1"));
//		messageM1.setDescription("The first page of the website for selling food and chemical products.");
//		messages.put("messageM1", messageRepository.save(messageM1));

//		Message messageM2 = new Message("welcome2", "Low Price Promise.", projects.get("projectP1"));
//		messageM2.setDescription("The first page of the website for selling food and chemical products.");
//		messages.put("messageM2", messageRepository.save(messageM2));


		Message pricing = new Message("pricing", "Pricing", projects.get("projectP1"));
		pricing.setDescription("Website title.");
		messages.put("pricing", messageRepository.save(pricing));
		Message signUp = new Message("signUp", "Sign up", projects.get("projectP1"));
		signUp.setDescription("Sign up button");
		messages.put("signUp", messageRepository.save(signUp));
		Message ocadoTechnology = new Message("ocadoTechnology", "Ocado Technology", projects.get("projectP1"));
		ocadoTechnology.setDescription("Company name");
		messages.put("ocadoTechnology", messageRepository.save(ocadoTechnology));
		Message featuresNav = new Message("featuresNav", "Features", projects.get("projectP1"));
		featuresNav.setDescription("Navbar link to see more information about features.");
		messages.put("featuresNav", messageRepository.save(featuresNav));
		Message enterpriseNav = new Message("enterpriseNav", "Enterprise ", projects.get("projectP1"));
		enterpriseNav.setDescription("Navbar link to see more information for enterprise clients.");
		messages.put("enterpriseNav", messageRepository.save(enterpriseNav));
		Message supportNav = new Message("supportNav", "Support ", projects.get("projectP1"));
		supportNav.setDescription("Navbar link to see help view.");
		messages.put("supportNav", messageRepository.save(supportNav));
		Message pricingNav = new Message("pricingNav", "Pricing ", projects.get("projectP1"));
		pricingNav.setDescription("Navbar link to see pricing information.");
		messages.put("pricingNav", messageRepository.save(pricingNav));

		Message freeCard = new Message("freeCard", "Free", projects.get("projectP1"));
		freeCard.setDescription("Information that product presented in this card is for free.");
		messages.put("freeCard", messageRepository.save(freeCard));
		Message maxFreeUsers = new Message("maxFreeUsers", "10 users included", projects.get("projectP1"));
		maxFreeUsers.setDescription("Information about maximal number of user that can use this product for free.");
		messages.put("maxFreeUsers", messageRepository.save(maxFreeUsers));
		Message maxFreeMemoryLimit = new Message("maxFreeMemoryLimit", "2 GB of storage", projects.get("projectP1"));
		maxFreeMemoryLimit.setDescription("Information about maximum memory limit that can be use for free.");
		messages.put("maxFreeMemoryLimit", messageRepository.save(maxFreeMemoryLimit));
		Message freeEmailSupport = new Message("freeEmailSupport", "Email support", projects.get("projectP1"));
		freeEmailSupport.setDescription("Information about email support for free account.");
		messages.put("freeEmailSupport", messageRepository.save(freeEmailSupport));
		Message freeHelpCenter = new Message("freeHelpCenter", "Help center access", projects.get("projectP1"));
		freeHelpCenter.setDescription("Information that user can use free help center for free account.");
		messages.put("freeHelpCenter", messageRepository.save(freeHelpCenter));
		Message signUpToAccess = new Message("signUpToAccess", "Sign up for free", projects.get("projectP1"));
		signUpToAccess.setDescription("Information that user need to create account to use this product.");
		messages.put("signUpToAccess", messageRepository.save(signUpToAccess));

		Message proCard = new Message("proCard", "Pro", projects.get("projectP1"));
		proCard.setDescription("Information about product type.");
		messages.put("proCard", messageRepository.save(proCard));
		Message maxProUsers = new Message("maxProUsers", "20 users included", projects.get("projectP1"));
		maxProUsers.setDescription("Information about maximal number of user that can use this product on pro type.");
		messages.put("maxProUsers", messageRepository.save(maxProUsers));
		Message maxProMemoryLimit = new Message("maxProMemoryLimit", "10 GB of storage", projects.get("projectP1"));
		maxProMemoryLimit.setDescription("Information about maximum memory limit that can be use on pro type.");
		messages.put("maxProMemoryLimit", messageRepository.save(maxProMemoryLimit));
		Message proEmailSupport = new Message("proEmailSupport", "Priority email support", projects.get("projectP1"));
		proEmailSupport.setDescription("Information about priority email support for pro account.");
		messages.put("proEmailSupport", messageRepository.save(proEmailSupport));
		Message proHelpCenter = new Message("proHelpCenter", "Help center access", projects.get("projectP1"));
		proHelpCenter.setDescription("Information that user can use free help center for pro account.");
		messages.put("proHelpCenter", messageRepository.save(proHelpCenter));
		Message getStarted = new Message("getStarted", "Get started", projects.get("projectP1"));
		getStarted.setDescription("Information that user need to create account to use this product.");
		messages.put("getStarted", messageRepository.save(getStarted));

		Message enterpriseCard = new Message("enterpriseCard", "Enterprise", projects.get("projectP1"));
		enterpriseCard.setDescription("Information about product type.");
		messages.put("enterpriseCard", messageRepository.save(enterpriseCard));
		Message maxEnterpriseUsers = new Message("maxEnterpriseUsers", "30 users included", projects.get("projectP1"));
		maxEnterpriseUsers.setDescription("Information about maximal number of user that can use this product on enterprise type.");
		messages.put("maxEnterpriseUsers", messageRepository.save(maxEnterpriseUsers));
		Message maxEnterpriseMemoryLimit = new Message("maxEnterpriseMemoryLimit", "15 GB of storage", projects.get("projectP1"));
		maxEnterpriseMemoryLimit.setDescription("Information about maximum memory limit that can be use on enterprise type.");
		messages.put("maxEnterpriseMemoryLimit", messageRepository.save(maxEnterpriseMemoryLimit));
		Message enterpriseEmailSupport = new Message("enterpriseEmailSupport", "Phone and email support", projects.get("projectP1"));
		enterpriseEmailSupport.setDescription("Information about phone and email support for enterprise account.");
		messages.put("enterpriseEmailSupport", messageRepository.save(enterpriseEmailSupport));
		Message enterpriseHelpCenter = new Message("enterpriseHelpCenter", "Help center access", projects.get("projectP1"));
		enterpriseHelpCenter.setDescription("Information that user can use free help center for pro account.");
		messages.put("enterpriseHelpCenter", messageRepository.save(enterpriseHelpCenter));
		Message contactUs = new Message("contactUs", "Contact us", projects.get("projectP1"));
		contactUs.setDescription("Information that user need to create account to use this product.");
		messages.put("contactUs", messageRepository.save(contactUs));

		Message featuresFooter = new Message("featuresFooter", "Features", projects.get("projectP1"));
		featuresFooter.setDescription("Footer links");
		messages.put("featuresFooter", messageRepository.save(featuresFooter));
		Message featuresFooter1 = new Message("featuresFooter1", "Cool stuff", projects.get("projectP1"));
		featuresFooter1.setDescription("Footer links");
		messages.put("featuresFooter1", messageRepository.save(featuresFooter1));
		Message featuresFooter2 = new Message("featuresFooter2", "Random feature", projects.get("projectP1"));
		featuresFooter2.setDescription("Footer links");
		messages.put("featuresFooter2", messageRepository.save(featuresFooter2));
		Message featuresFooter3 = new Message("featuresFooter3", "Team feature", projects.get("projectP1"));
		featuresFooter3.setDescription("Footer links");
		messages.put("featuresFooter3", messageRepository.save(featuresFooter3));
		Message featuresFooter4 = new Message("featuresFooter4", "Stuff for developers", projects.get("projectP1"));
		featuresFooter4.setDescription("Footer links");
		messages.put("featuresFooter4", messageRepository.save(featuresFooter4));
		Message featuresFooter5 = new Message("featuresFooter5", "Another one", projects.get("projectP1"));
		featuresFooter5.setDescription("Footer links");
		messages.put("featuresFooter5", messageRepository.save(featuresFooter5));
		Message featuresFooter6 = new Message("featuresFooter6", "Last time", projects.get("projectP1"));
		featuresFooter6.setDescription("Footer links");
		messages.put("featuresFooter6", messageRepository.save(featuresFooter6));

		Message resourcesFooter = new Message("resourcesFooter", "Resources", projects.get("projectP1"));
		resourcesFooter.setDescription("Footer links");
		messages.put("resourcesFooter", messageRepository.save(resourcesFooter));
		Message resourcesFooter1 = new Message("resourcesFooter1", "Resource", projects.get("projectP1"));
		resourcesFooter1.setDescription("Footer links");
		messages.put("resourcesFooter1", messageRepository.save(resourcesFooter1));
		Message resourcesFooter2 = new Message("resourcesFooter2", "Resource name", projects.get("projectP1"));
		resourcesFooter2.setDescription("Footer links");
		messages.put("resourcesFooter2", messageRepository.save(resourcesFooter2));
		Message resourcesFooter3 = new Message("resourcesFooter3", "Another resource", projects.get("projectP1"));
		resourcesFooter3.setDescription("Footer links");
		messages.put("resourcesFooter3", messageRepository.save(resourcesFooter3));
		Message resourcesFooter4 = new Message("resourcesFooter4", "Final resource", projects.get("projectP1"));
		resourcesFooter4.setDescription("Footer links");
		messages.put("resourcesFooter4", messageRepository.save(resourcesFooter4));

		Message aboutFooter = new Message("aboutFooter", "About", projects.get("projectP1"));
		aboutFooter.setDescription("Footer links");
		messages.put("aboutFooter", messageRepository.save(aboutFooter));
		Message aboutFooter1 = new Message("aboutFooter1", "Team", projects.get("projectP1"));
		aboutFooter1.setDescription("Footer links");
		messages.put("aboutFooter1", messageRepository.save(aboutFooter1));
		Message aboutFooter2 = new Message("aboutFooter2", "Locations", projects.get("projectP1"));
		aboutFooter2.setDescription("Footer links");
		messages.put("aboutFooter2", messageRepository.save(aboutFooter2));
		Message aboutFooter3 = new Message("aboutFooter3", "Privacy", projects.get("projectP1"));
		aboutFooter3.setDescription("Footer links");
		messages.put("aboutFooter3", messageRepository.save(aboutFooter3));
		Message aboutFooter4 = new Message("aboutFooter4", "Terms", projects.get("projectP1"));
		aboutFooter4.setDescription("Footer links");
		messages.put("aboutFooter4", messageRepository.save(aboutFooter4));


		Message messageM3 = new Message("business1", "Opracowaliśmy i stosujemy unikalny model biznesowy, który mocno nas pozycjonuje, " +
				"ponieważ coraz więcej konsumentów decyduje się na zakupy w Internecie.", projects.get("projectP2"));
		messageM3.setDescription("Pierwsza strona prezentująca przedsiębiorstwo.");
		messages.put("messageM3", messageRepository.save(messageM3));

		Message messageM4 = new Message("business2", "Naszym strategicznym celem jest dostosowanie interesów naszych klientów," +
				" inwestorów i innych interesariuszy do zapewnienia długoterminowej wartości dla akcjonariuszy.", projects.get("projectP2"));
		messageM4.setDescription("Pierwsza strona prezentująca przedsiębiorstwo.");
		messages.put("messageM4", messageRepository.save(messageM4));


		Message message1ForManyLocales = new Message("message1", "Short one.", projects.get("projectP3"));
		messageM4.setDescription("M is short.");
		messages.put("message1ForManyLocales", messageRepository.save(message1ForManyLocales));
		Message message2ForManyLocales = new Message("message2", "Looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong one.", projects.get("projectP3"));
		messageM4.setDescription("Message is long.");
		messages.put("message2ForManyLocales", messageRepository.save(message2ForManyLocales));

		Message categoriesTitle = new Message("categoriesTitle", "Categories", projects.get("projectP4"));
		categoriesTitle.setDescription("Categories title");
		messages.put("categoriesTitle", messageRepository.save(categoriesTitle));
		Message catHome = new Message("categoryHome", "Home", projects.get("projectP4"));
		messages.put("categoryHome", messageRepository.save(catHome));
		Message catEl = new Message("categoryElectronics", "Electronics", projects.get("projectP4"));
		messages.put("categoryElectronics", messageRepository.save(catEl));
		Message catVid = new Message("categoryVideoGames", "Video Games", projects.get("projectP4"));
		messages.put("categoryVideoGames", messageRepository.save(catVid));
		Message categorySel = new Message("selectedCategory", "Selected category", projects.get("projectP4"));
		messages.put("selectedCategory", messageRepository.save(categorySel));
		Message hotProduct = new Message("hotProduct", "Hot product", projects.get("projectP4"));
		messages.put("hotProduct", messageRepository.save(hotProduct));
		Message hotProductContent = new Message("hotProductContent", "<b>HOT</b> Phone XYZ only for <b>100$</b>", projects.get("projectP4"));
		messages.put("hotProductContent", messageRepository.save(hotProductContent));
		Message addToCart = new Message("addToCart", "Add to cart", projects.get("projectP4"));
		messages.put("addToCart", messageRepository.save(addToCart));
		Message featuredProducts = new Message("featuredProducts", "Featured products", projects.get("projectP4"));
		messages.put("featuredProducts", messageRepository.save(featuredProducts));
		Message productContent1 = new Message("productContent1", "Watch XYZ <b>90$</b>", projects.get("projectP4"));
		messages.put("productContent1", messageRepository.save(productContent1));
		Message productContent2 = new Message("productContent2", "Chair XYZ <b>40$</b>", projects.get("projectP4"));
		messages.put("productContent2", messageRepository.save(productContent2));
		Message productContent3 = new Message("productContent3", "Game console XYZ <b>60$</b>", projects.get("projectP4"));
		messages.put("productContent3", messageRepository.save(productContent3));
		Message showMore = new Message("showMore", "Show more", projects.get("projectP4"));
		messages.put("showMore", messageRepository.save(showMore));

		Message help = new Message("help", "Help", projects.get("projectP4"));
		messages.put("help", messageRepository.save(help));
		Message yOrders = new Message("yOrders", "Your orders", projects.get("projectP4"));
		messages.put("yOrders", messageRepository.save(yOrders));
		Message yAccount = new Message("yAccount", "Your account", projects.get("projectP4"));
		messages.put("yAccount", messageRepository.save(yAccount));
		Message helpCenter = new Message("helpCenter", "Help center", projects.get("projectP4"));
		messages.put("helpCenter", messageRepository.save(helpCenter));
		Message aboutUs = new Message("aboutUs", "About Us", projects.get("projectP4"));
		messages.put("aboutUs", messageRepository.save(aboutUs));
		Message careers = new Message("careers", "Careers", projects.get("projectP4"));
		messages.put("careers", messageRepository.save(careers));
		Message aboutExample = new Message("aboutExample", "About Example App", projects.get("projectP4"));
		messages.put("aboutExample", messageRepository.save(aboutExample));
		Message blog = new Message("blog", "Our blog", projects.get("projectP4"));
		messages.put("blog", messageRepository.save(blog));

		return messages;
	}
}
