import { Injectable } from '@angular/core';
import { MatPaginatorIntl } from "@angular/material/paginator";
import { TranslateService } from "@ngx-translate/core";

@Injectable({
	providedIn: 'root'
})
export class MatPaginatorIntlService extends MatPaginatorIntl {

	constructor(private translateService: TranslateService) {
		super();

		translateService.onLangChange.subscribe((e: Event) => {
			this.getAndInitTranslations();
		});

		this.getAndInitTranslations();
	}

	public getRangeLabel = (page: number, pageSize: number, length: number): string => {
		if (length === 0 || pageSize === 0) {
			return `0 ${this.translateService.instant('of')} ${length}`;
		}

		length = Math.max(length, 0);

		const startIndex: number = page * pageSize;
		const endIndex: number = startIndex < length
			? Math.min(startIndex + pageSize, length)
			: startIndex + pageSize;

		return `${startIndex + 1} - ${endIndex} ${this.translateService.instant('of')} ${length}`;
	};

	private getAndInitTranslations() {
		this.translateService.get('itemsPerPage')
		.subscribe(translation => {
			this.itemsPerPageLabel = translation;

			this.changes.next();
		});
	}
}
