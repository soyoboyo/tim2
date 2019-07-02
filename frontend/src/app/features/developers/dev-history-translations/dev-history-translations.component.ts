import { Component, OnInit } from '@angular/core';
import { AuditTranslationService } from './audit-translation/audit-translation.service';
import { ActivatedRoute, Router } from '@angular/router';
import { RestService } from '../../../shared/services/rest/rest.service';

@Component({
	selector: 'app-dev-history-translations',
	templateUrl: './dev-history-translations.component.html',
	styleUrls: ['./dev-history-translations.component.scss']
})
export class DevHistoryTranslationsComponent implements OnInit {

	id;
	auditedTranslation = null;
	translationVersions = [];
	loadingResults = false;

	constructor(private auditTranslationService: AuditTranslationService,
				private activatedRoute: ActivatedRoute,
				private router: Router,
				private http: RestService) {
	}

	ngOnInit() {
		window.scrollTo(0, 64);
		this.auditedTranslation = this.auditTranslationService.auditedTranslation;
		this.activatedRoute.params.subscribe((params) => {
			this.id = params['id'];
		});
		this.gettranslationVersion();
	}

	async gettranslationVersion() {
		this.loadingResults = true;
		this.translationVersions = await this.http.getAll('translation/version/' + this.id);
		this.loadingResults = false;
	}

	goBack() {
		this.router.navigateByUrl('developer');
	}
}
