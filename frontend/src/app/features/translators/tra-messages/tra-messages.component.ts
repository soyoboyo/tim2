import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { RestService } from '../../../shared/services/rest/rest.service';
import { SnackbarService } from '../../../shared/services/snackbar-service/snackbar.service';
import { UtilsService } from '../../../shared/services/utils-service/utils.service';
import { ProjectsStoreService } from '../../../stores/projects-store/projects-store.service';
import { ConfirmationDialogService } from '../../../shared/services/confirmation-dialog/confirmation-dialog.service';
import { Message } from '../../../shared/types/entities/Message';
import { Project } from '../../../shared/types/entities/Project';
import { TranslateService } from '@ngx-translate/core';

@Component({
	selector: 'app-tra-messages',
	templateUrl: './tra-messages.component.html',
	styleUrls: ['./tra-messages.component.scss']
})
export class TraMessagesComponent implements OnInit {

	isLoadingResults = true;
	projects: Project[] = [];
	messages: Message[] = [];

	selectedProject = null;
	selectedLocale: string = null;
	availableLocales: any[] = [];
	selectedLocales = new FormControl();

	constructor(private formBuilder: FormBuilder,
				private cd: ChangeDetectorRef,
				private http: RestService,
				private snackbar: SnackbarService,
				private utils: UtilsService,
				private projectsStore: ProjectsStoreService,
				private confirmService: ConfirmationDialogService,
				private projectStoreService: ProjectsStoreService,
				private translateService: TranslateService) {
		this.selectedProject = this.projectStoreService.getSelectedProject();
	}

	ngOnInit() {
		this.getProjects();
		this.getMessagesForTranslator();
	}

	async getProjects() {
		this.isLoadingResults = true;
		this.projects = await this.http.getAll('project/getAll');
		this.isLoadingResults = false;
	}

	changeProject(value) {
		this.selectedProject = value;
		this.projectStoreService.setSelectedProject(value);
		this.getMessagesForTranslator();
		this.availableLocales = this.selectedProject.targetLocales;
		this.utils.sortByProperty(this.availableLocales, 'locale');
	}

	downloadReport() {
		console.log(this.selectedLocales);
		this.http.downloadReport(this.selectedProject, this.selectedLocales.value);
	}

	async getMessagesForTranslator() {
		if (this.selectedProject && this.selectedLocale) {
			this.isLoadingResults = true;
			this.messages = await this.http.getAll('message/translator/getByLocale/' + this.selectedProject.id + '?locale=' + this.selectedLocale);
			this.messages = [].concat(this.messages);
			this.isLoadingResults = false;
		}
	}

	async invalidateTranslation(message: any) {
		await this.confirmService.openDialog(this.translateService.instant('invalidateTranslationDialog') + '?').subscribe((result) => {
			if (result) {
				this.http.update('translation/invalidate/' + message.translation.id + '?messageId=' + message.id, null).subscribe((response) => {
					if (response !== null) {
						this.getMessagesForTranslator();
						this.snackbar.snackSuccess(this.translateService.instant('invalidationSuccess') + '!', 'OK');
					}
				}, (error) => {
					this.snackbar.snackError(error.error.message, 'OK');
				});
			}
		});
	}

	selectLocale(value: any) {
		this.selectedLocale = value;
		this.getMessagesForTranslator();
	}

	compareProjects(o1: any, o2: any): boolean {
		if (o1 === null || o2 === null) {
			return false;
		} else {
			return o1.name === o2.name;
		}
	}

	formAction(result: boolean) {
		if (result) {
			this.getMessagesForTranslator();
		}
	}

	importCSV($event: any) {
		if ($event.target.files[0]) {
			const url = 'report/import/translator';
			this.http.importCSV(url, $event.target.files[0]).subscribe(response => {
					if (response !== null) {
						this.snackbar.snackSuccess(response, 'OK');
						this.getMessagesForTranslator();
					}
				},
				error => {
					this.snackbar.snackError(error.error, 'OK');
				});
		}
	}
}
