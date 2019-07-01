import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ProjectDTO } from '../../../models/DTOs/ProjectDTO';
import { RestService } from '../../../services/rest/rest.service';
import { SnackbarService } from '../../../services/snackbar/snackbar.service';
import { UtilsService } from '../../../services/utils/utils.service';
import { ConfirmationDialogService } from '../../../services/confirmation-dialog/confirmation-dialog.service';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { Project } from '../../../models/entities/Project';

@Component({
	selector: 'app-dev-projects',
	templateUrl: './dev-projects.component.html',
	styleUrls: ['./dev-projects.component.scss']
})
export class DevProjectsComponent implements OnInit {

	projectParams: FormGroup;
	projectNameControl = new FormControl([''], [Validators.required]);
	sourceLanguageControl = new FormControl([''], [Validators.required]);
	sourceCountryControl = new FormControl([''], [Validators.required]);
	targetLanguageControl = new FormControl(null);
	targetCountryControl = new FormControl(null);
	filteredLanguages: Observable<any[]>;
	filteredCountries: Observable<any[]>;
	filteredTargetLanguages: Observable<any[]>;
	filteredTargetCountries: Observable<any[]>;

	formMode: string = 'Add';
	toUpdate: any = null;
	showForm = false;

	isLoadingResults = true;
	selectedRowIndex: number = -1;

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
				public utils: UtilsService,
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
		this.allLanguages.sort();
		this.allCountries.sort();
		this.allTargetLanguages = this.allLanguages;
		this.allTargetCountries = this.allCountries;
	}

	async getProjects() {
		this.isLoadingResults = true;
		this.projects = await this.http.getAll('project/getAll');
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
		const control = <FormArray>this.projectParams.get('replacableLocale');
		control.push(this.initReplacableLocale());
	}

	removeReplacableLocale(i: number) {
		const control = <FormArray>this.projectParams.get('replacableLocale');
		control.removeAt(i);
	}

	addExistingReplacableLocale(replaced: string, replacement: string) {
		const control = <FormArray>this.projectParams.get('replacableLocale');
		control.push(this.initExistingReplacableLocale(replaced, replacement));
	}

	initExistingReplacableLocale(replaced: string, replacement: string) {
		return this.formBuilder.group({
			target: [replaced],
			replacement: [replacement]
		});
	}

	createProject(params: any) {
		let replacements = new Map<string, string>();
		params.value.replacableLocale.forEach((group) => {
			if (group.target !== '' && group.replacement !== '') {
				replacements[group.target] = group.replacement;
			}
		});

		const sourceLocale = this.getCode(params.value.sourceLanguage) + '_' + this.getCode(params.value.sourceCountry);
		const targetLocales = [].concat(this.selectedTargetLocales);
		targetLocales.push(sourceLocale);
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

	editProject(project: Project) {
		this.toUpdate = project;
		this.formMode = 'Update';
		this.clearForm();
		this.selectedRowIndex = project.id;
		if (this.showForm === false) {
			this.showForm = true;
		}
		this.sourceLocale = project.sourceLocale;
		this.projectParams.patchValue({
			'projectName': project.name,
			'sourceLanguage': project.sourceLocale.split('_')[0],
			'sourceCountry': project.sourceLocale.split('_')[1],
			'targetLanguage': '',
			'targetCountry': ''
		});
		project.targetLocales.forEach((t) => {
			this.selectedTargetLocales.push(t.locale);
			this.availableReplacements.push(t.locale);
		});
		this.removeLocaleFromArray(this.sourceLocale, this.selectedTargetLocales);


		const replacements = project.replaceableLocaleToItsSubstitute;
		Object.keys(replacements).forEach((key) => {
			let replacedLocale = key.split(',')[1].split('=')[1];
			replacedLocale = replacedLocale.slice(0, -1);
			const replacement = replacements[key].locale;
			this.addExistingReplacableLocale(replacedLocale, replacement);
		});
	}

	updateProject(body) {
		this.http.update('project/update/' + this.toUpdate.id, body).subscribe((response) => {
			if (response !== null) {
				this.getProjects();
				this.toUpdate = null;
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
		this.formMode = 'Add';
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
			}
		}
	}

	removeTargetLocale(targetLocale: string): void {
		const index = this.selectedTargetLocales.indexOf(targetLocale);
		if (index >= 0) {
			this.selectedTargetLocales.splice(index, 1);
		}
		const replacementIndex = this.availableReplacements.indexOf(targetLocale);
		if (replacementIndex >= 0) {
			this.availableReplacements.splice(replacementIndex, 1);
		}

	}

	public getCode(value: string): string {
		return value.split(' ')[0];
	}

	public clearForm() {
		this.projectParams.markAsPristine();
		this.projectParams.markAsUntouched();
		this.projectParams.reset();
		const replacementControls = <FormArray>this.projectParams.get('replacableLocale');
		let i = 0;
		while (replacementControls.controls.length > 0) {
			replacementControls.removeAt(i);
		}
		this.availableReplacements = [];
		this.selectedTargetLocales = [];
		this.cd.markForCheck();
	}

	onSelectSourceLanguage(language: string) {
		if (this.sourceLocale !== null) {
			this.removeLocaleFromArray(this.sourceLocale, this.availableReplacements);
		}

		const lang = language.split(' ')[0];
		if (typeof this.projectParams.value.sourceCountry === 'string') {
			const coun = this.projectParams.value.sourceCountry.split(' ')[0];
			this.sourceLocale = this.getCode(lang) + '_' + this.getCode(coun);
			if (!this.availableReplacements.includes(this.sourceLocale)) {
				this.availableReplacements.push(this.sourceLocale);
			}
		}
	}

	onSelectSourceCountry(country: any) {
		if (this.sourceLocale !== null) {
			this.removeLocaleFromArray(this.sourceLocale, this.availableReplacements);
		}

		const coun = country.split(' ')[0];
		if (typeof this.projectParams.value.sourceLanguage === 'string') {
			const lang = this.projectParams.value.sourceLanguage.split(' ')[0];
			this.sourceLocale = this.getCode(lang) + '_' + this.getCode(coun);
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
