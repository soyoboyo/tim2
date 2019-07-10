export class ProjectForDeveloper {
	id: number;
	name: string;
	sourceLanguage: string;
	sourceCountry: string;
	targetLocales: string[];
	availableReplacements: string[];
	substitutes: Map<string, string>;
}
