import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'outdatedTranslation'
})
export class OutdatedTranslationPipe implements PipeTransform {

    transform(message: any, translation: any): boolean {
        const msgDate = new Date(message.updateDate);
        const translationDate = new Date(translation.updateDate);
        return msgDate > translationDate;
    }

}
