import { Translation } from './entities/Translation';

export class MessageForDeveloper {
	id: number;
	content: string;
	description: string;
	key: string;
	updateDate: Date;
	createdBy: string;

	translations: Translation[];

	missingLocales: string[];

	projectId: number;

}
