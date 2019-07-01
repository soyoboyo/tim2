export class TranslationDTO {

	content: string;
	locale: string;
	messageId: number;

	isValid = true;

	constructor(content: string, locale: string, messageId: number, isValid: boolean) {
		this.content = content;
		this.locale = locale;
		this.messageId = messageId;
		this.isValid = isValid;
	}
}
