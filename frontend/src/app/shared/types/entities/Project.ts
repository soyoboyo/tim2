import { Message } from './Message';
import { LocaleWrapper } from './LocaleWrapper';

export class Project {
	id: number;

	name: string;
	sourceLocale: string;
	replaceableLocaleToItsSubstitute: any;

	messages: Message[];
	targetLocales: LocaleWrapper[];

}
