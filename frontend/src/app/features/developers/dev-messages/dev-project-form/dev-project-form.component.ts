import { ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { Project } from '../../../../shared/types/entities/Project';
import { RestService } from '../../../../shared/services/rest/rest.service';
import { SnackbarService } from '../../../../shared/services/snackbar-service/snackbar.service';
import { ConfirmationDialogService } from '../../../../shared/services/confirmation-dialog/confirmation-dialog.service';
import { map, startWith } from 'rxjs/operators';
import { ProjectDTO } from '../../../../shared/types/DTOs/input/ProjectDTO';
import { ProjectsStoreService } from '../../../../stores/projects-store/projects-store.service';

@Component({
	selector: 'app-dev-project-form',
	templateUrl: './dev-project-form.component.html',
	styleUrls: ['./dev-project-form.component.scss']
})
export class DevProjectFormComponent implements OnInit {

	formMode = 'Add';
	toUpdate: any = null;
	isLoadingResults = true;

	projectParams: FormGroup;
	projectNameControl = new FormControl([''], [Validators.required]);
	sourceLanguageControl = new FormControl('', [Validators.required, Validators.pattern('[a-z]{2} .{0,}|[a-z]{2}')]);
	sourceCountryControl = new FormControl('', [Validators.required, Validators.pattern('[A-Z]{2} .{0,}|[A-Z]{2}')]);
	targetLanguageControl = new FormControl('');
	targetCountryControl = new FormControl('');
	filteredLanguages: Observable<any[]>;
	filteredCountries: Observable<any[]>;
	filteredTargetLanguages: Observable<any[]>;
	filteredTargetCountries: Observable<any[]>;

	sourceLocale: string = null;
	selectedTargetLocales: string[] = [];
	availableReplacements: string[] = [];

	allLanguages: string[] = [];
	allCountries: string[] = [];
	allTargetLanguages: string[] = [];
	allTargetCountries: string[] = [];

	@Output() hideForm = new EventEmitter<Project>();
	@Input() formModeInput: any;

	constructor(private formBuilder: FormBuilder,
				private cd: ChangeDetectorRef,
				private http: RestService,
				private snackbar: SnackbarService,
				private confirmService: ConfirmationDialogService,
				private projectStoreService: ProjectsStoreService) {
	}

	ngOnInit() {
		this.initProjectForm();
		if (this.formModeInput !== 'Add') {
			this.editProject(this.projectStoreService.getSelectedProject());
		}
		this.getLocales();

		this.filteredLanguages = this.sourceLanguageControl.valueChanges
		.pipe(
			startWith(''),
			map(lang => lang ? this.filterLocales(lang, this.allLanguages) : this.allLanguages.slice()));

		this.filteredCountries = this.sourceCountryControl.valueChanges
		.pipe(
			startWith(''),
			map(country => country ? this.filterLocales(country, this.allCountries) : this.allCountries.slice()));

		this.filteredTargetLanguages = this.targetLanguageControl.valueChanges
		.pipe(
			startWith(''),
			map(lang => lang ? this.filterLocales(lang, this.allTargetLanguages) : this.allTargetLanguages.slice()));

		this.filteredTargetCountries = this.targetCountryControl.valueChanges
		.pipe(
			startWith(''),
			map(country => country ? this.filterLocales(country, this.allTargetCountries) : this.allTargetCountries.slice()));
	}

	async getLocales() {
		this.allLanguages = await this.http.getAll('locales/languages/getAll');
		this.allCountries = await this.http.getAll('locales/countries/getAll');
		this.allTargetLanguages = this.allLanguages;
		this.allTargetCountries = this.allCountries;
	}

	private filterLocales(value: string, fullArray: string[]): any[] {
		const filterValue = value.toLowerCase();
		return fullArray.filter(locale => locale.toLowerCase().indexOf(filterValue) === 0);
	}

	initProjectForm() {
		this.projectParams = this.formBuilder.group({
			projectName: this.projectNameControl,
			sourceLanguage: this.sourceLanguageControl,
			sourceCountry: this.sourceCountryControl,
			targetLanguage: this.targetLanguageControl,
			targetCountry: this.targetCountryControl,
			replacableLocale: this.formBuilder.array([
				this.initReplacableLocale()
			])
		});
	}

	initReplacableLocale() {
		return this.formBuilder.group({
			target: [''],
			replacement: ['']
		});
	}

	addReplacableLocale() {
		const control = this.projectParams.get('replacableLocale') as FormArray;
		control.push(this.initReplacableLocale());
	}

	removeReplacableLocale(i: number) {
		const control = this.projectParams.get('replacableLocale') as FormArray;
		control.removeAt(i);
	}

	addExistingReplacableLocale(replaced: string, replacement: string) {
		const control = this.projectParams.get('replacableLocale') as FormArray;
		control.push(this.initExistingReplacableLocale(replaced, replacement));
	}

	initExistingReplacableLocale(replaced: string, replacement: string) {
		return this.formBuilder.group({
			target: [replaced],
			replacement: [replacement]
		});
	}

	createProject(params: any) {
		const replacements = new Map<string, string>();
		params.value.replacableLocale.forEach((group) => {
			if (group.target !== '' && group.replacement !== '') {
				replacements[group.target] = group.replacement;
			}
		});

		const sourceLocale = this.getCode(params.value.sourceLanguage) + '_' + this.getCode(params.value.sourceCountry);
		const targetLocales = [].concat(this.selectedTargetLocales);
		targetLocales.push(sourceLocale);
		this.removeLocaleFromArray(sourceLocale, targetLocales);
		const body = new ProjectDTO(
			params.value.projectName,
			sourceLocale,
			targetLocales,
			replacements);
		if (!this.toUpdate) {
			this.addProject(body);
		} else {
			this.updateProject(body);
		}
	}

	addProject(body) {
		this.http.save('project/create', body).subscribe((response) => {
			if (response !== null) {
				this.hideForm.emit(response);
				this.snackbar.snackSuccess('Success!', 'OK');
			} else {
				this.snackbar.snackError('Error', 'OK');
			}
		}, (error) => {
			this.snackbar.snackError(error.error.message, 'OK');
		});
	}

	editProject(project: Project) {
		this.toUpdate = project;
		this.formMode = 'Update';
		this.clearForm();

		const localeParts: string[] = project.sourceLocale.split('_');
		const sourceLanguage: string = localeParts[0];
		const sourceCountry = localeParts[1];
		this.projectParams.patchValue({
			projectName: project.name,
			sourceLanguage,
			sourceCountry,
			targetLanguage: '',
			targetCountry: ''
		});
		const stringTargetLocales: string[] = [];
		project.targetLocales.forEach((o) => {
			stringTargetLocales.push(o.locale);
		});
		this.selectedTargetLocales = [].concat(stringTargetLocales);
		this.availableReplacements = [].concat(stringTargetLocales);

		const replacements = project.replaceableLocaleToItsSubstitute;
		Object.keys(replacements).forEach((key) => {
			const replacedLocale = key.split('=')[2].substring(0, 5);
			const replacement = replacements[key];
			this.addExistingReplacableLocale(replacedLocale, replacement.locale);
		});
	}

	updateProject(body) {
		this.http.update('project/update/' + this.toUpdate.id, body).subscribe((response) => {
			if (response !== null) {
				this.hideForm.emit(response);
				this.snackbar.snackSuccess('Success!', 'OK');
			} else {
				this.snackbar.snackError('Error', 'OK');
			}
		}, (error) => {
			this.snackbar.snackError(error.error.message, 'OK');
		});
	}

	cancelUpdate() {
		this.hideForm.emit(this.projectStoreService.getSelectedProject());
	}

	addNewTargetLocale() {
		const lang = this.getCode(this.projectParams.value.targetLanguage);
		const co = this.getCode(this.projectParams.value.targetCountry);
		const targetLocale = lang + '_' + co;
		if (!this.selectedTargetLocales.includes(targetLocale)) {
			if (lang !== '' && co !== '') {
				this.selectedTargetLocales.push(targetLocale);
				this.availableReplacements.push(targetLocale);
				this.projectParams.patchValue({
					targetLanguage: '',
					targetCountry: ''
				});
			}
		}
	}

	removeTargetLocale(targetLocale: string): void {
		this.removeLocaleFromArray(targetLocale, this.selectedTargetLocales);
		this.removeLocaleFromArray(targetLocale, this.availableReplacements);
	}

	public getCode(value: string): string {
		return value.split(' ')[0];
	}

	public clearForm() {
		this.projectParams.markAsPristine();
		this.projectParams.markAsUntouched();
		this.projectParams.reset();
		const replacementControls = this.projectParams.get('replacableLocale') as FormArray;
		while (replacementControls.controls.length > 0) {
			replacementControls.removeAt(0);
		}
		this.availableReplacements = [];
		this.selectedTargetLocales = [];
		this.cd.markForCheck();
	}

	onSelectSourceLanguage(language: string) {
		if (this.sourceLocale !== null) {
			this.removeLocaleFromArray(this.sourceLocale, this.availableReplacements);
		}

		if (typeof this.projectParams.value.sourceCountry === 'string') {
			const coun = this.projectParams.value.sourceCountry.split(' ')[0];
			this.sourceLocale = this.getCode(language) + '_' + this.getCode(coun);
			if (!this.availableReplacements.includes(this.sourceLocale)) {
				this.availableReplacements.push(this.sourceLocale);
			}
		}
	}

	onSelectSourceCountry(country: string) {
		if (this.sourceLocale !== null) {
			this.removeLocaleFromArray(this.sourceLocale, this.availableReplacements);
		}

		if (typeof this.projectParams.value.sourceLanguage === 'string') {
			const lang = this.projectParams.value.sourceLanguage.split(' ')[0];
			this.sourceLocale = this.getCode(lang) + '_' + this.getCode(country);
			if (!this.availableReplacements.includes(this.sourceLocale)) {
				this.availableReplacements.push(this.sourceLocale);
			}
		}
	}

	removeLocaleFromArray(locale: string, array: string[]) {
		const index = array.indexOf(locale);
		array.splice(index, 1);
	}

	openAutocomplete($event: any) {
		const formFieldName = $event.target.attributes.formcontrolname.value;
		const formField = this.projectParams.controls[formFieldName];
		if (formField.value === '') {
			formField.patchValue('');
		}
	}
}
