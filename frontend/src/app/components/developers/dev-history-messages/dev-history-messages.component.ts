import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuditMessageService } from '../../../services/audit-message/audit-message.service';
import { RestService } from '../../../services/rest/rest.service';
import { MessageForDeveloper } from '../../../models/MessageForDeveloper';

@Component({
	selector: 'app-dev-history-messages',
	templateUrl: './dev-history-messages.component.html',
	styleUrls: ['./dev-history-messages.component.scss']
})
export class DevHistoryMessagesComponent implements OnInit {

	id;
	auditedMessage = null;
	messageVersions = [];
	loadingResults = false;

	constructor(private auditedMessageService: AuditMessageService,
				private activatedRoute: ActivatedRoute,
				private router: Router,
				private http: RestService) {
	}

	ngOnInit() {
		window.scrollTo(0, 64);
		this.auditedMessage = this.auditedMessageService.auditedMessage;
		this.activatedRoute.params.subscribe((params) => {
			this.id = params['id'];
		});
		this.getMessageVersions();
	}

	async getMessageVersions(){
		this.loadingResults = true;
		this.messageVersions = await this.http.getAll('message/version/' + this.id);
		this.loadingResults = false;
	}

	goBack() {
		this.router.navigateByUrl('developer');
	}

}
