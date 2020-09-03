import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ProjectDTO } from '../../../shared/types/DTOs/input/ProjectDTO';
import { RestService } from '../../../shared/services/rest/rest.service';
import { SnackbarService } from '../../../shared/services/snackbar-service/snackbar.service';
import { ConfirmationDialogService } from '../../../shared/services/confirmation-dialog/confirmation-dialog.service';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { Project } from '../../../shared/types/entities/Project';
import { ProjectForDeveloper } from '../../../shared/types/DTOs/output/ProjectForDeveloper';

@Component({
	selector: 'app-dev-projects',
	templateUrl: './dev-projects.component.html',
	styleUrls: ['./dev-projects.component.scss']
})
export class DevProjectsComponent implements OnInit {

	projectParams: FormGroup;
	projectNameControl = new FormControl([''], [Validators.required]);
	sourceLanguageControl = new FormControl([''], [Validators.required, Validators.pattern('[a-z]{2} .{0,}|[a-z]{2}')]);
	sourceCountryControl = new FormControl([''], [Validators.required, Validators.pattern('[A-Z]{2} .{0,}|[A-Z]{2}')]);
	targetLanguageControl = new FormControl(null);
	targetCountryControl = new FormControl(null);
	filteredLanguages: Observable<any[]>;
	filteredCountries: Observable<any[]>;
	filteredTargetLanguages: Observable<any[]>;
	filteredTargetCountries: Observable<any[]>;

	formMode = 'add';
	toUpdate: any = null;
	showForm = false;

	isLoadingResults = true;
	selectedRowIndex = -1;

	projects: Project[] = [];

	sourceLocale: string = null;
	selectedTargetLocales: string[] = [];
	availableReplacements: string[] = [];

	allLanguages: string[] = [];
	allCountries: string[] = [];
	allTargetLanguages: string[] = [];
	allTargetCountries: string[] = [];

	constructor(private formBuilder: FormBuilder,
				private cd: ChangeDetectorRef,
				private http: RestService,
				private snackbar: SnackbarService,
				private confirmService: ConfirmationDialogService) {
	}

	ngOnInit() {
		this.initProjectForm();
		this.getProjects();
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

	async getProjects() {
		this.isLoadingResults = true;
		this.projects = await this.http.getAll('project/developer/getAll');
		this.projects = [].concat(this.projects);
		this.isLoadingResults = false;
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
		const control = <FormArray> this.projectParams.get('replacableLocale');
		control.push(this.initReplacableLocale());
	}

	removeReplacableLocale(i: number) {
		const control = <FormArray> this.projectParams.get('replacableLocale');
		control.removeAt(i);
	}

	addExistingReplacableLocale(replaced: string, replacement: string) {
		const control = <FormArray> this.projectParams.get('replacableLocale');
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

		console.log(body);

		if (!this.toUpdate) {
			this.addProject(body);
		} else {
			this.updateProject(body);
		}
	}

	addProject(body) {
		this.http.save('project/create', body).subscribe((response) => {
			if (response.i !== null) {
				this.clearForm();
				this.getProjects();
				this.snackbar.snackSuccess('Success!', 'OK');
			} else {
				this.snackbar.snackError('Error', 'OK');
			}
		}, (error) => {
			this.snackbar.snackError(error.error.message, 'OK');
		});
	}

	editProject(project: ProjectForDeveloper) {
		this.toUpdate = project;
		this.formMode = 'update';
		this.clearForm();
		this.selectedRowIndex = project.id;
		if (this.showForm === false) {
			this.showForm = true;
		}
		this.sourceLocale = project.sourceLanguage + '_' + project.sourceCountry;
		this.projectParams.patchValue({
			projectName: project.name,
			sourceLanguage: project.sourceLanguage,
			sourceCountry: project.sourceCountry,
			targetLanguage: '',
			targetCountry: ''
		});
		this.selectedTargetLocales = [].concat(project.targetLocales);
		this.availableReplacements = [].concat(project.availableReplacements);

		const replacements = project.substitutes;
		Object.keys(replacements).forEach((key) => {
			const replacedLocale = key;
			const replacement = replacements[key];
			this.addExistingReplacableLocale(replacedLocale, replacement);
		});
	}

	updateProject(body) {
		this.http.update('project/update/' + this.toUpdate.id, body).subscribe((response) => {
			if (response !== null) {
				this.getProjects();
				this.toUpdate = null;
				this.formMode = 'add';
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

	async removeProject(id: any) {
		await this.confirmService.openDialog().subscribe((result) => {
			if (result) {
				this.http.remove('message/remove/' + id).subscribe((response) => {
					if (response.success) {
						this.snackbar.snackSuccess(response.message, 'OK');
					} else {
						this.snackbar.snackError('Error', 'OK');
					}
					this.getProjects();
				}, (error) => {
					this.snackbar.snackError(error.error.message, 'OK');
				});
			}
		});
		this.selectedRowIndex = -1;
	}

	toggleForm() {
		this.showForm = !this.showForm;
	}

	cancelUpdate() {
		this.toUpdate = null;
		this.selectedRowIndex = -1;
		this.formMode = 'add';
		this.showForm = false;
		this.clearForm();
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
		const replacementControls = <FormArray> this.projectParams.get('replacableLocale');
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

}
