import { ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslationCreateDTO } from '../../../../shared/types/DTOs/input/TranslationCreateDTO';
import { TranslationUpdateDTO } from '../../../../shared/types/DTOs/input/TranslationUpdateDTO';
import { MessageForTranslator } from '../../../../shared/types/DTOs/output/MessageForTranslator';
import { RestService } from '../../../../shared/services/rest/rest.service';
import { SnackbarService } from '../../../../shared/services/snackbar-service/snackbar.service';
import { UtilsService } from '../../../../shared/services/utils-service/utils.service';
import { ProjectsStoreService } from '../../../../stores/projects-store/projects-store.service';
import { ConfirmationDialogService } from '../../../../shared/services/confirmation-dialog/confirmation-dialog.service';
import { Project } from '../../../../shared/types/entities/Project';
import { Message } from '../../../../shared/types/entities/Message';
import { TranslatorFormStateService } from '../../translator-form-state-service/translator-form-state.service';

@Component({
	selector: 'app-translator-form',
	templateUrl: './translator-form.component.html',
	styleUrls: ['./translator-form.component.scss']
})
export class TranslatorFormComponent implements OnInit {

	messageParams: FormGroup;
	toUpdate: any = null;
	// showForm = false;

	isLoadingResults = true;
	selectedRowIndex = -1;

	projects: Project[] = [];
	messages: Message[] = [];

	selectedProject = null;
	selectedLocale: string = null;
	selectedMessage = null;
	private selectedTranslationId: number;
	@Input() formMode: string;
	@Output() hideForm = new EventEmitter<boolean>();

	constructor(private formBuilder: FormBuilder,
				private cd: ChangeDetectorRef,
				private http: RestService,
				private snackbar: SnackbarService,
				private utils: UtilsService,
				private projectsStore: ProjectsStoreService,
				private confirmService: ConfirmationDialogService,
				private projectStoreService: ProjectsStoreService,
				private formState: TranslatorFormStateService) {
	}

	ngOnInit(): void {
		this.initTranslationForm();
		this.getMessagesForTranslator();
	}

	initTranslationForm() {
		this.messageParams = this.formBuilder.group({
			content: ['', [Validators.required]]
		});
	}

	createTranslation(params: any) {
		if (this.formMode === 'Add') {
			this.addTranslation(new TranslationCreateDTO(params.value.content, this.formState.getLocale()));
		} else {
			this.updateTranslation(new TranslationUpdateDTO(params.value.content));
		}
	}

	addNewTranslation(message: MessageForTranslator) {
		this.selectedMessage = message;
		this.selectedRowIndex = message.id;
	}

	editTranslation(message: MessageForTranslator) {
		this.selectedTranslationId = message.translation.id;
		this.selectedMessage = message;
		this.selectedRowIndex = message.id;

		this.messageParams.patchValue({
			content: message.translation.content,
		});

		this.toUpdate = message;
		this.formMode = 'Update';
	}

	async addTranslation(body) {
		this.http.save('translation/create' + '?messageId=' + this.formState.getMessage().id, body).subscribe((response) => {
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
		const url = 'translation/update/' + this.formState.getMessage().translation.id + '?messageId=' + this.formState.getMessage().id;
		this.http.update(url, body).subscribe((response) => {
			if (response !== null) {
				this.toUpdate = null;
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
		this.toUpdate = null;
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
		await this.confirmService.openDialog('Are you sure you want to invalidate this translation?').subscribe((result) => {
			if (result) {
				this.http.update('translation/invalidate/' + message.translation.id + '?messageId=' + message.id, null).subscribe((response) => {
					if (response !== null) {
						this.getMessagesForTranslator();
						this.snackbar.snackSuccess('Translation invalidated successfully!', 'OK');
					}
				}, (error) => {
					this.snackbar.snackError(error.error.message, 'OK');
				});
			}
		});
	}


	cancelUpdate() {
		this.selectedMessage = null;
		this.toUpdate = null;
		this.selectedRowIndex = -1;
		this.formMode = 'Add';
		// this.showForm = false;
		this.clearForm();
	}

	clearForm() {
		this.hideForm.emit(false);
		this.formState.closeForm();
		// this.messageParams.reset();
		// this.messageParams.markAsPristine();
		// this.messageParams.markAsUntouched();
		// this.cd.markForCheck();
	}


}
