import { Injectable } from '@angular/core';
import { MessageForTranslator } from '../../../../shared/types/DTOs/output/MessageForTranslator';

@Injectable({
	providedIn: 'root'
})
export class TranslatorFormStateService {

	private message = null;
	private selectedLocale = '';

	constructor() {
	}

	public closeForm() {
		this.message = null;
	}

	public setValues(message: MessageForTranslator, locale: string) {
		this.selectedLocale = locale;
		this.message = message;
	}

	public getMessage() {
		return this.message;
	}

	public getLocale() {
		return this.selectedLocale;
	}


}
