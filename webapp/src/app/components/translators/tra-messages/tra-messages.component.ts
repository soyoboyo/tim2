import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RestService } from '../../../services/rest/rest.service';
import { SnackbarService } from '../../../services/snackbar/snackbar.service';
import { UtilsService } from '../../../services/utils/utils.service';
import { ProjectsStoreService } from '../../../services/projects-store/projects-store.service';
import { ConfirmationDialogService } from '../../../services/confirmation-dialog/confirmation-dialog.service';
import { TranslationDTO } from '../../../models/DTOs/TranslationDTO';
import { Message } from '../../../models/entities/Message';
import { Project } from '../../../models/entities/Project';
import { MessageForTranslator } from '../../../models/MessageForTranslator';
import { TranslationUpdateDTO } from '../../../models/DTOs/TranslationUpdateDTO';
import { TranslationCreateDTO } from '../../../models/DTOs/TranslationCreateDTO';

@Component({
	selector: 'app-tra-messages',
	templateUrl: './tra-messages.component.html',
	styleUrls: ['./tra-messages.component.scss']
})
export class TraMessagesComponent implements OnInit {

	messageParams: FormGroup;
	formMode: string = 'Add';
	toUpdate: any = null;
	showForm = false;

	isLoadingResults = true;
	selectedRowIndex: number = -1;

	projects: Project[] = [];
	messages: Message[] = [];

	selectedProject = null;
	selectedLocale: string = null;
	selectedMessage = null;
	availableLocales: any[] = [];
	private selectedTranslationId: number;

	constructor(private formBuilder: FormBuilder,
				private cd: ChangeDetectorRef,
				private http: RestService,
				private snackbar: SnackbarService,
				private utils: UtilsService,
				private projectsStore: ProjectsStoreService,
				private confirmService: ConfirmationDialogService,
				private projectStoreService: ProjectsStoreService) {
		this.selectedProject = this.projectStoreService.getSelectedProject();
	}

	ngOnInit() {
		this.initProjectForm();
		this.getProjects();
		this.getMessagesForTranslator();
	}

	initProjectForm() {
		this.messageParams = this.formBuilder.group({
			content: ['', [Validators.required]]
		});
	}

	createTranslation(params: any) {
		if (!this.toUpdate) {
			this.addTranslation(new TranslationCreateDTO(params.value.content, this.selectedLocale));
		} else {
			this.updateTranslation(new TranslationUpdateDTO(params.value.content));
		}
	}

	addNewTranslation(message: MessageForTranslator) {
		this.selectedMessage = message;
		if (this.showForm == false) {
			this.showForm = true;
		}
	}

	editTranslation(message: MessageForTranslator) {
		this.selectedTranslationId = message.translation.id;
		this.selectedMessage = message;
		this.selectedRowIndex = message.id;

		if (this.showForm === false) {
			this.showForm = true;
		}

		this.messageParams.patchValue({
			'content': message.translation.content,
		});

		this.toUpdate = message;
		this.formMode = 'Update';
	}

	async addTranslation(body) {
		await this.http.save('translation/create' + '?messageId=' + this.selectedMessage.id, body).subscribe((response) => {
			if (response !== null) {
				this.getMessagesForTranslator();
				this.snackbar.snackSuccess('Success!', 'OK');
				this.selectedRowIndex = -1;
				this.clearForm();
			} else {
				this.snackbar.snackError('Error', 'OK');
			}
		}, (error) => {
			this.snackbar.snackError(error.error.message, 'OK');
		});
	}

	async updateTranslation(body) {
		await this.http.update('translation/update/' + this.selectedTranslationId + '?messageId=' + this.selectedMessage.id, body).subscribe((response) => {
			if (response !== null) {
				this.toUpdate = null;
				this.getMessagesForTranslator();
				this.formMode = 'Add';
				this.snackbar.snackSuccess('Success!', 'OK');
				this.selectedRowIndex = -1;
				this.clearForm();
			} else {
				this.snackbar.snackError('Error', 'OK');
			}
		}, (error) => {
			this.snackbar.snackError(error.error.message, 'OK');
		});
		this.toUpdate = null;
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
	}

	downloadXLS() {
		this.http.downloadXLS(this.selectedProject);
	}

	async getMessagesForTranslator() {
		if (this.selectedProject && this.selectedLocale) {
			this.isLoadingResults = true;
			this.messages = await this.http.getAll('message/translator/getByLocale/' + this.selectedProject.id + '?locale=' + this.selectedLocale);
			this.messages = [].concat(this.messages);
			this.isLoadingResults = false;
		} else if (this.selectedProject) {
			this.isLoadingResults = true;
			this.messages = await this.http.getAll('message/translator/getByProject/' + this.selectedProject.id);
			this.messages = [].concat(this.messages);
			this.isLoadingResults = false;
		}
	}

	async invalidateTranslation(message: any) {
		await this.confirmService.openDialog('Are you sure you want to invalidate this translation?').subscribe((result) => {
			if (result) {
				this.http.update('translation/invalidate/' + message.translation.id + '?messageId=' + message.id, null).subscribe((response) => {
					if (response !== null) {
						this.getMessagesForTranslator();
						this.snackbar.snackSuccess('Translation invalidated successfully!', 'OK')
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

	cancelUpdate() {
		this.toUpdate = null;
		this.selectedRowIndex = -1;
		this.formMode = 'Add';
		this.showForm = false;
		this.clearForm();
	}

	clearForm() {
		this.messageParams.reset();
		this.messageParams.markAsPristine();
		this.messageParams.markAsUntouched();
		this.cd.markForCheck();
	}

}
