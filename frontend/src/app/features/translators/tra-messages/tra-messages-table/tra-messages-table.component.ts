import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { Project } from '../../../../shared/types/entities/Project';
import { Message } from '../../../../shared/types/entities/Message';
import { MessageForTranslator } from '../../../../shared/types/DTOs/output/MessageForTranslator';
import { TranslatorFormStateService } from '../translator-form-state-service/translator-form-state.service';
import { RestService } from '../../../../shared/services/rest/rest.service';
import { SnackbarService } from '../../../../shared/services/snackbar-service/snackbar.service';
import { TranslationCreateDTO } from '../../../../shared/types/DTOs/input/TranslationCreateDTO';

@Component({
	selector: 'app-tra-messages-table',
	templateUrl: './tra-messages-table.component.html',
	styleUrls: ['./tra-messages-table.component.scss'],
	animations: [
		trigger('detailExpand', [
			state('collapsed', style({ height: '0px', minHeight: '0' })),
			state('expanded', style({ height: '*' })),
			transition('* => collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
			transition('expanded => *', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
			transition('* => expanded', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
			transition('collapsed => *', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
		])
	]
})

export class TraMessagesTableComponent implements OnInit, OnChanges {

	@Input() messages: Message[] = [];
	@Input() selectedLocale = '';
	@Input() selectedProject: Project;
	@Input() selectedRowIndex = -1;

	@Output() formSubmitted = new EventEmitter<boolean>();
	@Output() invalidateTranslationEvent = new EventEmitter<any>();
	@Output() sendSelectedLocale = new EventEmitter<any>();

	// table
	@ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
	@ViewChild(MatSort, { static: true }) sort: MatSort;
	dataSource = new MatTableDataSource<MessageForTranslator>();
	displayedColumns: string[] = ['key', 'message', 'translation', 'status'];
	isLoadingResults = false;
	expandedRow = null;

	// filters
	clearFilter = false;
	incorrect = false;
	missing = false;
	outdated = false;
	invalid = false;

	// form
	showForm = false;
	formMode = 'add';

	constructor(private http: RestService,
				private snackbar: SnackbarService,
				private formState: TranslatorFormStateService) {
	}

	ngOnInit() {
		this.getMessages();
	}

	ngOnChanges(changes: SimpleChanges): void {
		if (this.selectedLocale !== null) {
			this.getMessages();
			this.dataSource.filter = '{';
		}
	}

	async getMessages() {
		this.isLoadingResults = true;
		this.createDataSource(this.messages);
		this.isLoadingResults = false;
	}

	// data table
	applyFilter(filterValue: string) {
		this.dataSource.filter = filterValue.trim().toLowerCase();
	}

	filterMessages() {
		this.clearFilter = false;
		this.incorrect = false;
		if (!this.missing && !this.invalid && !this.outdated && !this.incorrect) {
			this.clearFilter = true;
		}
		this.dataSource.filter = '{';
	}

	filterAllIncorrect() {
		this.clearFilter = false;
		this.missing = true;
		this.invalid = true;
		this.outdated = true;
		this.dataSource.filter = '{';
	}

	clearFilters() {
		this.missing = false;
		this.invalid = false;
		this.outdated = false;
		this.incorrect = false;
		this.dataSource.filter = '{';
	}

	createDataSource(messages: any[]) {
		this.dataSource = new MatTableDataSource(messages);
		if (this.dataSource.data.length > 0) {
			this.clearFilter = true;
		}
		this.dataSource.paginator = this.paginator;
		this.dataSource.filterPredicate = (data, filter: string) => {
			console.log(data);
			console.log(filter);
			console.log(this.filterByMissing(data));
			console.log(this.filterByInvalid(data));
			console.log(this.filterByOutdated(data));
			return ((this.filterByMissing(data) || this.filterByInvalid(data) || this.filterByOutdated(data)) || this.clearFilter);
		};
		this.dataSource.sort = this.sort;
	}

	filterByMissing(row): boolean {
		return this.missing === true && row.translation === null;
	}

	filterByInvalid(row): boolean {
		if (row.translation !== null) {
			if (this.invalid === true && row.translation.isValid === false) {
				return true;
			}
		}
		return false;
	}

	filterByOutdated(row): boolean {
		if (row.translation !== null) {
			if (this.outdated === true && this.isTranslationOutdated(row)) {
				return true;
			}
		}
		return false;
	}


	isTranslationOutdated(message: MessageForTranslator): boolean {
		if (message.translation !== null) {
			return message.updateDate > message.translation.updateDate;
		} else {
			return false;
		}
	}

	getTranslationStatus(message: MessageForTranslator) {
		const translation = message.translation;
		if (translation === null) {
			return 'missing';
		}
		if (message.updateDate > translation.updateDate) {
			return 'outdated';
		}
		if (translation.isValid === false) {
			return 'invalid';
		}
		return 'correct';
	}

	// interaction with form
	triggerTranslationForm(message: any, mode: string) {
		if (mode === 'Add') {
			this.formMode = 'add';
		} else {
			this.formMode = 'update';
		}
		this.formState.setValues(message, this.selectedLocale);
		this.selectedRowIndex = message.id;
		this.showForm = true;
	}

	useSubstitute(message: any) {
		const body = new TranslationCreateDTO(message.substitute.content, this.selectedLocale);
		this.http.save('translation/create' + '?messageId=' + message.id, body).subscribe((response) => {
			this.submitNormal(response);
		}, (error) => {
			this.submitException(error);
		});
	}

	hideForm(stateChange: boolean) {
		this.showForm = false;
		this.selectedRowIndex = -1;

	}

	async invalidateTranslation(message: any) {
		this.invalidateTranslationEvent.emit(message);
	}

	formSubmit(result: boolean) {
		this.formSubmitted.emit(result);
	}


	submitNormal(response: any) {
		if (response !== null) {
			this.formSubmitted.emit(true);
			this.snackbar.snackSuccess('Success!', 'OK');
		} else {
			this.formSubmitted.emit(false);
			this.snackbar.snackError('Error', 'OK');
		}
	}

	submitException(error: any) {
		this.formSubmitted.emit(false);
		this.snackbar.snackError(error.error.message, 'OK');
	}

}
