export class TranslationCreateDTO {

	content: string;
	locale: string;

	constructor(content: string, locale: string) {
		this.content = content;
		this.locale = locale;
	}
}
