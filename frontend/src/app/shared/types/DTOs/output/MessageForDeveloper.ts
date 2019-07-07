import { Translation } from '../../entities/Translation';

export class MessageForDeveloper {
	id: number;
	content: string;
	description: string;
	key: string;
	updateDate: Date;
	createdBy: string;

	projectId: number;

	translations: Translation[];

	missingLocales: string[];

	translationStatuses: Map<string, number>;

}
