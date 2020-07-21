import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { Project } from '../../../../shared/types/entities/Project';
import { Message } from '../../../../shared/types/entities/Message';
import { MessageForTranslator } from '../../../../shared/types/DTOs/output/MessageForTranslator';
import { TranslatorFormStateService } from '../../translator-form-state-service/translator-form-state.service';

@Component({
	selector: 'app-tra-messages-table',
	templateUrl: './tra-messages-table.component.html',
	styleUrls: ['./tra-messages-table.component.scss', '../tra-messages.component.scss'],
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
	missing = false;
	outdated = false;
	invalid = false;

	// form
	showForm = false;
	formMode = 'Add';

	constructor(private formState: TranslatorFormStateService) {
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

	createDataSource(messages: any[]) {
		this.dataSource = new MatTableDataSource(messages);
		this.dataSource.paginator = this.paginator;
		this.dataSource.filterPredicate = (data, filter: string) => {
			return JSON.stringify(data).toLowerCase().includes(filter.toLowerCase())
				&& this.filterByMissing(data)
				&& this.filterByInvalid(data)
				&& this.filterByOutdated(data);
		};
		this.dataSource.sort = this.sort;
	}

	filterByMissing(row): boolean {
		if (this.missing === false) {
			return true;
		} else {
			if (row.translation === null) {
				return true;
			}
		}
		return false;
	}

	filterByInvalid(row): boolean {
		if (this.invalid === false) {
			return true;
		} else {
			if (row.translation !== null) {
				if (row.translation.isValid === false) {
					return true;
				}
			}
		}
		return false;
	}

	filterByOutdated(row): boolean {
		if (this.outdated === false) {
			return true;
		} else {
			if (row.translation !== null) {
				if (this.isTranslationOutdated(row)) {
					return true;
				}
			}
		}
		return false;
	}

	filterMessages() {
		this.dataSource.filter = '{';
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
			return 'Missing';
		}
		if (message.updateDate > translation.updateDate) {
			return 'Outdated';
		}
		if (translation.isValid === false) {
			return 'Invalid';
		}
		return 'Correct';
	}

	// interaction with form
	triggerTranslationForm(message: any, mode: string) {
		if (mode === 'Add') {
			this.formMode = 'Add';
		} else {
			this.formMode = 'Update';
		}
		this.formState.setValues(message, this.selectedLocale);
		this.selectedRowIndex = message.id;
		this.showForm = true;
	}

	hideForm(stateChange: boolean) {
		this.showForm = false;
		this.selectedRowIndex = -1;
	}

	async invalidateTranslation(message: any) {
		this.invalidateTranslationEvent.emit(message);
	}

	formSubmit(result: boolean) {
		console.log(result);
		this.formSubmitted.emit(result);
	}

}
