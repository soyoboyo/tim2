import { Translation } from '../../entities/Translation';

export class MessageForTranslator {
	id: number;
	content: string;
	description: string;
	key: string;
	updateDate: Date;

	translation: Translation;
	substitute: Translation;

	previousMessageContent: string;

	constructor(id: number, content: string, description: string, key: string, updateDate: Date, translation: Translation, substitute: Translation) {
		this.id = id;
		this.content = content;
		this.description = description;
		this.key = key;
		this.updateDate = updateDate;
		this.translation = translation;
		this.substitute = substitute;
	}
}
