package simple.web.app;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import java.util.Properties;

@Controller
public class HelloController {

    @GetMapping({"/", "/hello"})
    public String showLoginView(Model model) throws IOException {
        List<LocaleDTO> locales = getLocales();
        return setParametersToModel(locales.get(locales.size()-1).getName(), model);
    }

    @GetMapping("/locale")
    public String changeLanguage(Model model, @RequestParam String locale) throws IOException {
        return setParametersToModel(locale, model);
    }

    private String setParametersToModel(String locale, Model model) throws IOException {
        List<LocaleDTO> localeDTO = getLocales();
        LocaleDTO selectedLocale = null;
        for(LocaleDTO l: localeDTO) {
            if(l.getName().equals(locale))
                selectedLocale = l;
        }
        if(selectedLocale != null) {
            localeDTO.remove(selectedLocale);
            localeDTO.add(selectedLocale);
        }
        setProperties(locale);
        Properties properties = getProperties();
        model.addAttribute("locales", localeDTO);
        model.addAttribute("welcome1", properties.getProperty("welcome1"));
        model.addAttribute("welcome2", properties.getProperty("welcome2"));
        model.addAttribute("pricing", properties.getProperty("pricing"));
        model.addAttribute("signUp", properties.getProperty("signUp"));
        model.addAttribute("ocadoTechnology", properties.getProperty("ocadoTechnology"));
        model.addAttribute("featuresNav", properties.getProperty("featuresNav"));
        model.addAttribute("enterpriseNav", properties.getProperty("enterpriseNav"));
        model.addAttribute("supportNav", properties.getProperty("supportNav"));
        model.addAttribute("pricingNav", properties.getProperty("pricingNav"));
        model.addAttribute("freeCard", properties.getProperty("freeCard"));
        model.addAttribute("maxFreeUsers", properties.getProperty("maxFreeUsers"));
        model.addAttribute("maxFreeMemoryLimit", properties.getProperty("maxFreeMemoryLimit"));
        model.addAttribute("freeEmailSupport", properties.getProperty("freeEmailSupport"));
        model.addAttribute("freeHelpCenter", properties.getProperty("freeHelpCenter"));
        model.addAttribute("signUpToAccess", properties.getProperty("signUpToAccess"));
        model.addAttribute("proCard", properties.getProperty("proCard"));
        model.addAttribute("maxProUsers", properties.getProperty("maxProUsers"));
        model.addAttribute("maxProMemoryLimit", properties.getProperty("maxProMemoryLimit"));
        model.addAttribute("proEmailSupport", properties.getProperty("proEmailSupport"));
        model.addAttribute("proHelpCenter", properties.getProperty("proHelpCenter"));
        model.addAttribute("getStarted", properties.getProperty("getStarted"));
        model.addAttribute("enterpriseCard", properties.getProperty("enterpriseCard"));
        model.addAttribute("maxEnterpriseUsers", properties.getProperty("maxEnterpriseUsers"));
        model.addAttribute("maxEnterpriseMemoryLimit", properties.getProperty("maxEnterpriseMemoryLimit"));
        model.addAttribute("enterpriseEmailSupport", properties.getProperty("enterpriseEmailSupport"));
        model.addAttribute("enterpriseHelpCenter", properties.getProperty("enterpriseHelpCenter"));
        model.addAttribute("contactUs", properties.getProperty("contactUs"));
        model.addAttribute("featuresFooter", properties.getProperty("featuresFooter"));
        model.addAttribute("featuresFooter1", properties.getProperty("featuresFooter1"));
        model.addAttribute("featuresFooter2", properties.getProperty("featuresFooter2"));
        model.addAttribute("featuresFooter3", properties.getProperty("featuresFooter3"));
        model.addAttribute("featuresFooter4", properties.getProperty("featuresFooter4"));
        model.addAttribute("featuresFooter5", properties.getProperty("featuresFooter5"));
        model.addAttribute("featuresFooter6", properties.getProperty("featuresFooter6"));
        model.addAttribute("resourcesFooter", properties.getProperty("resourcesFooter"));
        model.addAttribute("resourcesFooter1", properties.getProperty("resourcesFooter1"));
        model.addAttribute("resourcesFooter2", properties.getProperty("resourcesFooter2"));
        model.addAttribute("resourcesFooter3", properties.getProperty("resourcesFooter3"));
        model.addAttribute("resourcesFooter4", properties.getProperty("resourcesFooter4"));
        model.addAttribute("aboutFooter", properties.getProperty("aboutFooter"));
        model.addAttribute("aboutFooter1", properties.getProperty("aboutFooter1"));
        model.addAttribute("aboutFooter2", properties.getProperty("aboutFooter2"));
        model.addAttribute("aboutFooter3", properties.getProperty("aboutFooter3"));
        model.addAttribute("aboutFooter4", properties.getProperty("aboutFooter4"));
        return "hello";
    }

    private List<LocaleDTO> getLocales() {
        final String uri = "http://localhost:8081/api/v1/exportCI/locales/getByProject/1";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        Gson gson = new Gson();
        List<LocaleDTO> availableLocales = gson.fromJson(result, new TypeToken<List<LocaleDTO>>(){}.getType());
        return availableLocales;
    }

    private void setProperties(String locale)  throws IOException {
        final String uri = "http://localhost:8081/api/v1/exportCI/message/getByLocale/1?locale=" + locale;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        Resource resource = new ClassPathResource("main-content.properties");
        FileWriter fileWriter = new FileWriter(resource.getURI().getPath());
        fileWriter.write(result);
        fileWriter.close();
    }

    private Properties getProperties() throws IOException {
        Resource resource = new ClassPathResource("main-content.properties");
        FileReader reader = new FileReader(resource.getURI().getPath());
        Properties properties = new Properties();
        properties.load(reader);
        return properties;
    }
}
