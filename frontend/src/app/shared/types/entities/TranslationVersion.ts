export class TranslationVersion {
	id: number;
	content: string;
	locale: string;
	updateDate: Date;
	isValid: boolean;

	createdBy: string;

	originalTranslationId: number;

	constructor(id: number, content: string, locale: string, updateDate: Date, isValid: boolean, createdBy: string, originalTranslationId: number) {
		this.id = id;
		this.content = content;
		this.locale = locale;
		this.updateDate = updateDate;
		this.isValid = isValid;
		this.createdBy = createdBy;
		this.originalTranslationId = originalTranslationId;
	}
}
