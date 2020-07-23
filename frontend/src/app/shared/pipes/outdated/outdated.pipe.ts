import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
	name: 'outdated'
})
export class OutdatedPipe implements PipeTransform {

	transform(message: any): number {
		let outdated = 0;
		const translations = message.translations;
		const msgDate = new Date(message.updateDate);
		for (let i = 0; i < translations.length; i++) {
			const translDate = new Date(translations[i].updateDate);
			if (translDate < msgDate) {
				outdated++;
			}
		}
		return outdated;
	}

}
