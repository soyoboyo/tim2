import { ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslationCreateDTO } from '../../../../shared/types/DTOs/input/TranslationCreateDTO';
import { TranslationUpdateDTO } from '../../../../shared/types/DTOs/input/TranslationUpdateDTO';
import { RestService } from '../../../../shared/services/rest/rest.service';
import { SnackbarService } from '../../../../shared/services/snackbar-service/snackbar.service';
import { TranslatorFormStateService } from '../translator-form-state-service/translator-form-state.service';

@Component({
	selector: 'app-translator-form',
	templateUrl: './translator-form.component.html',
	styleUrls: ['./translator-form.component.scss']
})
export class TranslatorFormComponent implements OnInit {

	translationForm: FormGroup;
	@Input() formMode: string;
	@Output() hideForm = new EventEmitter<boolean>();
	@Output() formSubmitted = new EventEmitter<boolean>();

	constructor(private formBuilder: FormBuilder,
				private cd: ChangeDetectorRef,
				private http: RestService,
				private snackbar: SnackbarService,
				private formState: TranslatorFormStateService) {
	}

	ngOnInit(): void {
		this.translationForm = this.formBuilder.group({
			content: ['', [Validators.required]]
		});
		console.log(this.formMode);
		if (this.formMode === 'update') {
			this.translationForm.patchValue({
				content: this.formState.getMessage().translation.content,
			});
		}
	}

	// form actions
	cancelForm() {
		this.clearForm();
		this.hideForm.emit(false);
		this.formState.closeForm();
	}

	clearForm() {
		this.translationForm.reset();
		this.translationForm.markAsPristine();
		this.translationForm.markAsUntouched();
		this.cd.markForCheck();
	}

	submitTranslationForm(params: any) {
		if (this.formMode === 'add') {
			this.addTranslation(new TranslationCreateDTO(params.value.content, this.formState.getLocale()));
		} else {
			this.updateTranslation(new TranslationUpdateDTO(params.value.content));
		}
	}

	// REST api actions
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
			this.hideForm.emit(true);
			this.formSubmitted.emit(true);
			this.clearForm();
			this.snackbar.snackSuccess('Success!', 'OK');
		} else {
			this.hideForm.emit(false);
			this.formSubmitted.emit(false);
			this.snackbar.snackError('Error', 'OK');
		}
	}

	submitException(error: any) {
		this.hideForm.emit(false);
		this.formSubmitted.emit(false);
		this.snackbar.snackError(error.error.message, 'OK');
	}
}
