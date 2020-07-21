import { Injectable } from '@angular/core';

@Injectable({
	providedIn: 'root'
})
export class TranslatorFormStateService {

	private messageId = -1;
	private content = '';

	public closeForm() {
		this.messageId = -1;
		this.content = '';
	}

	public setValues(messageId: number, content: string) {
		this.messageId = messageId;
		this.content = content;
		console.log(this.messageId);
		console.log(this.content);
	}


	constructor() {
	}
}
