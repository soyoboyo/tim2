package org.tim.controllers;

import org.tim.configuration.SpringTestsCustomExtension;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.any;

public class ProjectControllerTestIT extends SpringTestsCustomExtension {

//	private MockMvc mockMvc;
//	private static ObjectMapper mapper;
//
//	private ProjectDTO projectDTO;
//	private static Project expectedProject;
//
//	private final String BASE_URL = "http://localhost:8081";
//	private final String UPDATE_PROJECT = "/update/1";
//
//	@Mock
//	private ProjectService projectService;
//
//	@InjectMocks
//	private ProjectController projectController;
//
//	@BeforeAll
//	public static void init() {
//		mapper = new ObjectMapper();
//		expectedProject = new Project("name", new Locale("pl", "PL"));
//		expectedProject.addTargetLocale(Arrays.asList(
//				new LocaleWrapper(new Locale("pl", "PL")),
//				new LocaleWrapper(Locale.UK),
//				new LocaleWrapper(Locale.US),
//				new LocaleWrapper(Locale.GERMANY)));
//		expectedProject.updateSubstituteLocale(new LocaleWrapper(
//				new Locale("pl", "PL")), new LocaleWrapper(Locale.UK));
//		expectedProject.updateSubstituteLocale(new LocaleWrapper(Locale.UK), new LocaleWrapper(Locale.US));
//		expectedProject.updateSubstituteLocale(new LocaleWrapper(Locale.US), new LocaleWrapper(Locale.GERMANY));
//	}
//
//	@BeforeEach
//	public void setUp() {
//		mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
//		Map<String, String> replaceableLocaleToItsSubstituteString = new HashMap<>();
//		replaceableLocaleToItsSubstituteString.put("pl_PL", "en_GB");
//		replaceableLocaleToItsSubstituteString.put("en_GB", "en_US");
//		replaceableLocaleToItsSubstituteString.put("en_US", "de_DE");
//		projectDTO = new ProjectDTO("name", "pl_PL",
//				Arrays.asList("pl_PL", "en_GB", "en_US", "de_DE"), replaceableLocaleToItsSubstituteString);
//	}
//
//	@Test
//	void createNewProject_DataCorrect_Success() throws Exception {
//		when(projectService.createProject(any())).thenReturn(expectedProject);
//		String jsonRequest = mapper.writeValueAsString(projectDTO);
//		LocaleWrapper[] targetLocalesArray = expectedProject.getTargetLocales().toArray(new LocaleWrapper[0]);
//		mockMvc.perform(post(BASE_URL + API_VERSION + PROJECT + CREATE)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isOk())
//				.andDo(print())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(expectedProject.getName())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.sourceLocale", Matchers.is(
//						expectedProject.getSourceLocale().toString())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.targetLocales[0].locale", Matchers.is(targetLocalesArray[0].getLocale().toString())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.targetLocales[1].locale", Matchers.is(targetLocalesArray[1].getLocale().toString())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.targetLocales[2].locale", Matchers.is(targetLocalesArray[2].getLocale().toString())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.targetLocales[3].locale", Matchers.is(targetLocalesArray[3].getLocale().toString())));
//	}
//
//	@Test
//	void updateProject_DataCorrect_Success() throws Exception {
//		when(projectService.updateProject(any(), any())).thenReturn(expectedProject);
//		String jsonRequest = mapper.writeValueAsString(projectDTO);
//		LocaleWrapper[] targetLocalesArray = expectedProject.getTargetLocales().toArray(new LocaleWrapper[0]);
//		mockMvc.perform(post(BASE_URL + API_VERSION + PROJECT + UPDATE_PROJECT)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isOk())
//				.andDo(print())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(expectedProject.getName())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.sourceLocale", Matchers.is(
//						expectedProject.getSourceLocale().toString())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.targetLocales[0].locale", Matchers.is(targetLocalesArray[0].getLocale().toString())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.targetLocales[1].locale", Matchers.is(targetLocalesArray[1].getLocale().toString())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.targetLocales[2].locale", Matchers.is(targetLocalesArray[2].getLocale().toString())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.targetLocales[3].locale", Matchers.is(targetLocalesArray[3].getLocale().toString())));
//	}
//
//	@Test
//	void createNewProject_ProjectNameBlank_Failure() throws Exception {
//		projectDTO.setName("");
//		String jsonRequest = mapper.writeValueAsString(projectDTO);
//		mockMvc.perform(post(BASE_URL + API_VERSION + PROJECT + CREATE)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isBadRequest())
//				.andDo(print());
//	}
//
//	@Test
//	void updateProject_ProjectNameBlank_Failure() throws Exception {
//		projectDTO.setName("");
//		String jsonRequest = mapper.writeValueAsString(projectDTO);
//		mockMvc.perform(post(BASE_URL + API_VERSION + PROJECT + UPDATE_PROJECT)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isBadRequest())
//				.andDo(print());
//	}
//
//	@Test
//	void createNewProject_SourceLocaleBlank_Failure() throws Exception {
//		projectDTO.setSourceLocale("");
//		String jsonRequest = mapper.writeValueAsString(projectDTO);
//		mockMvc.perform(post(BASE_URL + API_VERSION + PROJECT + CREATE)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isBadRequest())
//				.andDo(print());
//	}
//
//	@Test
//	void updateProject_SourceLocaleBlank_Failure() throws Exception {
//		projectDTO.setSourceLocale("");
//		String jsonRequest = mapper.writeValueAsString(projectDTO);
//		mockMvc.perform(post(BASE_URL + API_VERSION + PROJECT + UPDATE_PROJECT)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isBadRequest())
//				.andDo(print());
//	}
//
//	@Test
//	void createNewProject_ReplaceableLocaleToItsSubstituteNull_Failure() throws Exception {
//		projectDTO = new ProjectDTO("name", "pl_PL",
//				Arrays.asList("pl_PL", "en_GB", "en_US", "de_DE"), null);
//		String jsonRequest = mapper.writeValueAsString(projectDTO);
//		mockMvc.perform(post(BASE_URL + API_VERSION + PROJECT + CREATE)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isBadRequest())
//				.andDo(print());
//	}
//
//	@Test
//	void updateProject_ReplaceableLocaleToItsSubstituteNull_Failure() throws Exception {
//		projectDTO = new ProjectDTO("name", "pl_PL",
//				Arrays.asList("pl_PL", "en_GB", "en_US", "de_DE"), null);
//		String jsonRequest = mapper.writeValueAsString(projectDTO);
//		mockMvc.perform(post(BASE_URL + API_VERSION + PROJECT + UPDATE_PROJECT)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isBadRequest())
//				.andDo(print());
//	}
//
//	@Test
//	void createNewProject_TargetLocalesNull_Failure() throws Exception {
//		projectDTO = new ProjectDTO("name", "pl_PL",
//				null, projectDTO.getReplaceableLocaleToItsSubstitute());
//		String jsonRequest = mapper.writeValueAsString(projectDTO);
//		mockMvc.perform(post(BASE_URL + API_VERSION + PROJECT + CREATE)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isBadRequest())
//				.andDo(print());
//	}
//
//	@Test
//	void updateProject_TargetLocalesNull_Failure() throws Exception {
//		projectDTO = new ProjectDTO("name", "pl_PL",
//				null, projectDTO.getReplaceableLocaleToItsSubstitute());
//		String jsonRequest = mapper.writeValueAsString(projectDTO);
//		mockMvc.perform(post(BASE_URL + API_VERSION + PROJECT + UPDATE_PROJECT)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isBadRequest())
//				.andDo(print());
//	}
//
//	@Test
//	void getAllProjects_AllProjectsReturned() throws Exception {
//		when(projectService.getAllProjects()).thenReturn(Arrays.asList(expectedProject));
//		String jsonRequest = mapper.writeValueAsString(projectDTO);
//		LocaleWrapper[] targetLocalesArray = expectedProject.getTargetLocales().toArray(new LocaleWrapper[0]);
//		mockMvc.perform(get(BASE_URL + API_VERSION + PROJECT + GET_ALL)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isOk())
//				.andDo(print())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", Matchers.is(expectedProject.getName())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].sourceLocale", Matchers.is(
//						expectedProject.getSourceLocale().toString())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].targetLocales[0].locale", Matchers.is(targetLocalesArray[0].getLocale().toString())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].targetLocales[1].locale", Matchers.is(targetLocalesArray[1].getLocale().toString())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].targetLocales[2].locale", Matchers.is(targetLocalesArray[2].getLocale().toString())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].targetLocales[3].locale", Matchers.is(targetLocalesArray[3].getLocale().toString())));
//	}
}
