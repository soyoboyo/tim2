import { Injectable } from '@angular/core';

@Injectable({
	providedIn: 'root'
})
export class AuditMessageService {

	auditedMessage = null;

	constructor() {
	}
}
