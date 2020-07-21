import { ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslationCreateDTO } from '../../../../shared/types/DTOs/input/TranslationCreateDTO';
import { TranslationUpdateDTO } from '../../../../shared/types/DTOs/input/TranslationUpdateDTO';
import { RestService } from '../../../../shared/services/rest/rest.service';
import { SnackbarService } from '../../../../shared/services/snackbar-service/snackbar.service';
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

	translationForm: FormGroup;
	toUpdate: any = null;


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
	@Output() formSubmitted = new EventEmitter<boolean>();

	constructor(private formBuilder: FormBuilder,
				private cd: ChangeDetectorRef,
				private http: RestService,
				private snackbar: SnackbarService,
				private confirmService: ConfirmationDialogService,
				private formState: TranslatorFormStateService) {
	}

	ngOnInit(): void {
		this.initTranslationForm();
		console.log(this.formMode);
		if (this.formMode === 'Update') {
			this.translationForm.patchValue({
				content: this.formState.getMessage().translation.content,
			});
		}
	}

	initTranslationForm() {
		this.translationForm = this.formBuilder.group({
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

	async addTranslation(body) {
		this.http.save('translation/create' + '?messageId=' + this.formState.getMessage().id, body).subscribe((response) => {
			this.submitNormal(response);
		}, (error) => {
			this.submitException(error);
		});
	}

	async updateTranslation(body) {
		const url = 'translation/update/' + this.formState.getMessage().translation.id + '?messageId=' + this.formState.getMessage().id;
		this.http.update(url, body).subscribe((response) => {
			this.submitNormal(response);
		}, (error) => {
			this.submitException(error);
		});
	}

	submitNormal(response: any) {
		if (response !== null) {
			this.formSubmitted.emit(true);
			this.snackbar.snackSuccess('Success!', 'OK');
			this.clearForm();
			this.hideForm.emit(true);
		} else {
			this.formSubmitted.emit(false);
			this.hideForm.emit(false);
			this.snackbar.snackError('Error', 'OK');
		}
	}

	submitException(error: any) {
		this.formSubmitted.emit(false);
		this.hideForm.emit(false);
		this.snackbar.snackError(error.error.message, 'OK');
	}
	
	cancelForm() {
		this.clearForm();
	}

	clearForm() {
		this.hideForm.emit(false);
		this.formState.closeForm();
		this.translationForm.reset();
		this.translationForm.markAsPristine();
		this.translationForm.markAsUntouched();
		this.cd.markForCheck();
	}


}
