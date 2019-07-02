import { Injectable, OnInit } from '@angular/core';

@Injectable({
	providedIn: 'root'
})
export class AuditTranslationService implements OnInit{

	auditedTranslation: any;

	constructor(private auditTranslationService: AuditTranslationService) {
	}

	ngOnInit(): void {
		this.auditedTranslation = this.auditTranslationService.auditedTranslation;
	}



}
